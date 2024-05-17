package com.example.searchservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.common.dto.es.SkuEsModel;
import com.example.searchservice.constant.EsConstant;
import com.example.searchservice.service.SaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author CJW
 * @since 2024/5/13
 */
@Slf4j
@Service
public class SaveServiceImpl implements SaveService {
    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String string = JSON.toJSONString(skuEsModel);
            indexRequest.source(string, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        //批量错误处理
        return bulkResponse.hasFailures();
    }
}
