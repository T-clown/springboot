//package com.springboot.util;
//
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//
//import com.alibaba.fastjson.JSONObject;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.springboot.common.exception.ServiceRuntimeException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.MatchQueryBuilder;
//import org.elasticsearch.index.query.RangeQueryBuilder;
//import org.elasticsearch.index.query.TermQueryBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import static com.springboot.common.enums.ResultCode.ES_ERROR;
//import static com.springboot.util.ParserDataUtil.OBJECT_MAPPER;
//
//@Slf4j
//@Component
//public class ElasticsearchUtil {
//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//
//    private static RestHighLevelClient client;
//
//    @PostConstruct
//    public void init() {
//        client = restHighLevelClient;
//    }
//
//    /**
//     * 创建索引
//     *
//     * @param indexName
//     * @param settings
//     * @param mapping
//     * @return
//     */
//    public static boolean createIndex(String indexName, String settings, String mapping) {
//        //index名必须全小写，否则报错
//        CreateIndexRequest request = new CreateIndexRequest(indexName);
//        if (StringUtils.isNotBlank(settings)) {
//            request.settings(settings, XContentType.JSON);
//        }
//        if (StringUtils.isNotBlank(mapping)) {
//            request.mapping(mapping, XContentType.JSON);
//        }
//        try {
//            CreateIndexResponse indexResponse = client.indices().create(request, RequestOptions.DEFAULT);
//            return indexResponse.isAcknowledged();
//        } catch (IOException e) {
//            log.error("索引创建失败,indexName:{},settings:{},mapping:{},e:{}", indexName, settings, mapping, e);
//            throw new ServiceRuntimeException(ES_ERROR, String.format("索引创建失败:%s", indexName));
//        }
//    }
//
//    /**
//     * 判断 index 是否存在
//     */
//    public static boolean indexExist(String indexName) {
//        GetIndexRequest request = new GetIndexRequest(indexName);
//        try {
//            return client.indices().exists(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("Elasticsearch indexExist error:{}", e.getMessage());
//            throw new ServiceRuntimeException(ES_ERROR);
//        }
//    }
//
//    public static String addData(String index, JSONObject object) {
//        IndexRequest indexRequest = new IndexRequest(index);
//        try {
//            indexRequest.source(OBJECT_MAPPER.writeValueAsString(object), XContentType.JSON);
//            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//            return indexResponse.getId();
//        } catch (Exception e) {
//            log.error("Elasticsearch addData error :{}", e.getMessage());
//            throw new ServiceRuntimeException(ES_ERROR);
//        }
//    }
//
//    /**
//     * 搜索
//     */
//    public static SearchResponse search(String field, String key, String rangeField, String
//            from, String to, String termField, String termVal,
//                                        String... indexNames) {
//        SearchRequest request = new SearchRequest(indexNames);
//
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(new MatchQueryBuilder(field, key)).must(new RangeQueryBuilder(rangeField).from(from)
//                .to(to)).must(new TermQueryBuilder(termField, termVal));
//        builder.query(boolQueryBuilder);
//        request.source(builder);
//        log.info("[搜索语句为:{}]", request.source().toString());
//        try {
//            return client.search(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("Elasticsearch search error:{}", e.getMessage());
//            throw new ServiceRuntimeException(ES_ERROR);
//        }
//    }
//
//    /**
//     * 批量导入
//     *
//     * @param indexName
//     * @param isAutoId  使用自动id 还是使用传入对象的id
//     * @param data
//     * @return
//     * @throws IOException
//     */
//    public static BulkResponse batchImport(String indexName, boolean isAutoId, String data) throws IOException {
//        if (StringUtils.isBlank(data)) {
//            throw new ServiceRuntimeException(ES_ERROR, "数据为空");
//        }
//        BulkRequest request = new BulkRequest();
//        JsonNode jsonNode = OBJECT_MAPPER.readTree(data);
//
//        if (jsonNode.isArray()) {
//            for (JsonNode node : jsonNode) {
//                if (isAutoId) {
//                    request.add(new IndexRequest(indexName).source(node.asText(), XContentType.JSON));
//                } else {
//                    request.add(new IndexRequest(indexName).id(node.get("id").asText())
//                            .source(node.asText(), XContentType.JSON));
//                }
//            }
//        }
//        return client.bulk(request, RequestOptions.DEFAULT);
//    }
//
//}
