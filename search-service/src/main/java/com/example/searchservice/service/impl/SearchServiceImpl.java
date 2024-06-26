package com.example.searchservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.dto.es.SkuEsModel;
import com.example.searchservice.constant.EsConstant;
import com.example.searchservice.service.SearchService;
import com.example.searchservice.vo.SearchParam;
import com.example.searchservice.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CJW
 * @since 2024/5/13
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient esClient;


    @Override
    public SearchResult searchProduct(SearchParam searchParam) {
        //1.动态构建出查询需要的DSL语句
        SearchResult result = null;
        //准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            //执行检索请求
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);

            //分析封装响应数据
            result = buildSearchResult(response, searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 准备检索请求:
     * 模糊匹配，过滤(按照属性，分类，品牌，价格区间，库存),排序，分类，高亮，聚合分析
     *
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam paramVo) {
        //构建DSL语句
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //查询:模糊匹配，过滤(按照属性，分类，品牌，价格区间，库存)
        //1.构建bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1 must - 模糊匹配
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", paramVo.getKeyword()));
        }
        //1.2 bool - fitler 按照三级分类id查询
        if (paramVo.getCatalogId() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", paramVo.getCatalogId()));
        }
        //1.2 bool - filter 按照品牌id查询
        if (paramVo.getBrandId() != null && paramVo.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", paramVo.getBrandId()));
        }
        //1.2 bool - filter 按照指定属性进行查询，嵌入式查询,ScoreMode相关性得分
        if (paramVo.getAttrs() != null && paramVo.getAttrs().size() > 0) {
            for (String attrStr : paramVo.getAttrs()) {
                //attrs=1_5寸:8寸&attrs=2_16G:8G
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];//属性id
                String[] attrValues = s[1].split(":");//检索的属性值
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                //每一个必须得生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        //1.2 bool - filter 按照库存进行查询
        builder.query(QueryBuilders.termsQuery("hasStock", paramVo.getHasStock() == 1));
        //1.2 bool - filter 按照价格区间
        if (!StringUtils.isEmpty(paramVo.getSkuPrice())) {
            //1_500/_500/500_
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = paramVo.getSkuPrice().split("_");
            BigDecimal bigDecimal1 = new BigDecimal(s[0]);
            if (s.length == 2) {
                //gte大于等于，lte小于等于，gt大于，lt小于
                //区间
                BigDecimal bigDecimal2 = new BigDecimal(s[1]);
                rangeQuery.gte(bigDecimal1).lte(bigDecimal2);
            } else if (s.length == 1) {
                //大于
                if (paramVo.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(bigDecimal1);
                }
                //小于
                if (paramVo.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(bigDecimal1);
                }
            }
            boolQuery.filter(rangeQuery);
        }
        builder.query(boolQuery);
        //排序，分类，高亮
        //2.1 排序
        if (!StringUtils.isEmpty(paramVo.getSort())) {
            //sort = skuPrice_asc/desc
            String sort = paramVo.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            builder.sort(s[0], order);
        }
        //2.2 分页
        //from = (pageNum-1)*pageSize
        builder.from((paramVo.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        builder.size(EsConstant.PRODUCT_PAGE_SIZE);
        //2.3 高亮
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }

        //聚合分析
        //1.品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        builder.aggregation(brand_agg);
        //2.分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        builder.aggregation(catalog_agg);
        //3.属性聚合(嵌入式聚合)
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        //聚合出当前所有的attr_id
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析当前attr_id对应的所有可能的属性值attrValue
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        builder.aggregation(attr_agg);
        String s = builder.toString();
        System.out.println("DSL:" + s);
        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);
    }

    /**
     * 分析封装检索结果
     *
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam paramVo) {
        SearchResult result = new SearchResult();
        //1.返回所有查询到的商品
        SearchHits hits = response.getHits();
        List<SkuEsModel> esModelList = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(paramVo.getKeyword())) {
                    //设置高亮内容
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(string);
                }
                esModelList.add(skuEsModel);
            }
        }
        result.setProducts(esModelList);

        //2.当前所有商品涉及到的所有属性信息
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");//根据返回值确定数据类ParsedLongTerms，ParsedNested
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1.得到属性的id
            long attrId = bucket.getKeyAsNumber().longValue();
            //2.得到属性的名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            //3.得到属性的所有值
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets()
                    .stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);
        //3.当前所有商品涉及到的所有品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //1.品牌id
            long brandId = bucket.getKeyAsNumber().longValue();
            //2.品牌名字
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //3.品牌图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //4.当前所有商品涉及到的所有分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //得到分类id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            //得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
        //5.当前所有商品涉及到的所有分页信息
        //页码
        result.setPageNum(paramVo.getPageNum());
//        result.setPageNum();
        //总计录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //总页码
        Long totalPages = total % EsConstant.PRODUCT_PAGE_SIZE == 0 ? total / EsConstant.PRODUCT_PAGE_SIZE : (total / EsConstant.PRODUCT_PAGE_SIZE) + 1;
        result.setTotalPage(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
       /* //6.构建面包屑导航
        if (paramVo.getAttrs() != null && paramVo.getAttrs().size() > 0) {
            List<SearchResult.NavVo> collect = paramVo.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                //分析每个attr的参数值
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    AttrResponseVo attrs = r.getData2("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(attrs.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }
                String replace = replaceQueryString(paramVo, attr, "attrs");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(collect);
        }
        //品牌、分类面包屑导航
        if (paramVo.getBrandId() != null && paramVo.getBrandId().size() > 0) {
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("品牌");
            //远程查询所有品牌
            R r = productFeignService.brandsInfo(paramVo.getBrandId());
            if (r.getCode() == 0) {
                List<BrandVo> brands = r.getData2("brands", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brands) {
                    buffer.append(brandVo.getName() + ";");
                    replace = replaceQueryString(paramVo, brandVo.getBrandId() + "", "brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
            }
            navs.add(navVo);
        }*/
        return result;
    }

}
