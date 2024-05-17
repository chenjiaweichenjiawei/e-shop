package com.example.searchservice;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SearchServiceApplicationTests {
    @NoArgsConstructor
    @Data
    static class Account {
        private Integer accountNumber;
        private Integer balance;
        private String firstname;
        private String lastname;
        private Integer age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    @Autowired
    private RestHighLevelClient esClient;

    @Test
    void contextLoads() {

    }

    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("bank");
        //构造检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("address", "mill");
        searchSourceBuilder.query(matchQuery);
        //构造聚合条件
        TermsAggregationBuilder ageTerms = AggregationBuilders.terms("ageTerms").field("age").size(10);
        searchSourceBuilder.aggregation(ageTerms);
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);
        //查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        //分析数据
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Account account = JSON.parseObject(hit.getSourceAsString(), Account.class);
            System.out.println(account);
        }
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageTerms1 = aggregations.get("ageTerms");
        ageTerms1.getBuckets().forEach(bucket -> {
            System.out.println("年龄：" + bucket.getKey() + " 数量：" + bucket.getDocCount());
        });

        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("存款平均值" + balanceAvg1.getValue());
    }

    @Test
    void testEsAdd() throws IOException {
        IndexRequest indexRequest = new IndexRequest("user");
        User user = new User();
        user.setName("陈佳炜");
        user.setAge(21);
        user.setAddress("广东省汕头市");
        //数据id
        indexRequest.id("1");
        //数据内容，使用json传入
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse indexResponse = esClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }

    @Data
    static class User {
        String name;
        Integer age;
        String address;
    }
}
