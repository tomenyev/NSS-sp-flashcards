package cz.cvut.ear.flashcards.esdao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings("ALL")
public abstract class BaseDao<T> implements GenericDao<T> {

    protected RestHighLevelClient restHighLevelClient;
    protected ObjectMapper objectMapper;

    public BaseDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Map<String, Object> find(String id, String INDEX, String TYPE) {
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return getResponse.getSourceAsMap();
    }

    @Override
    public T insert(T entity, String id,  String INDEX, String TYPE) {
        Map dataMap = this.objectMapper.convertValue(entity, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, id)
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return entity;
    }

    @Override
    public Map<String,Object> update(String id, T entity,  String INDEX, String TYPE) {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                .fetchSource(true);    // Fetch Object after its update
        Map<String, Object> error = new HashMap<>();
        error.put("Error", "Unable to update");
        try {
            String bookJson = objectMapper.writeValueAsString(entity);
            updateRequest.doc(bookJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            return updateResponse.getGetResult().sourceAsMap();
        }catch (JsonProcessingException e){
            e.getMessage();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return error;
    }

    @Override
    public void remove(String id, String INDEX, String TYPE) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }

    @Override
    public Collection<Map<String, Object>> search(String index, String type, String value, String... fields) {
        SearchResponse response = new SearchResponse();
        try {
            response = restHighLevelClient.search(searchRequestGenerator(index, type, value, fields));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHit[] hits = response.getHits().getHits();
        Collection<Map<String, Object>> res = new LinkedList<>();
        for (SearchHit hit : hits) {
            res.add(hit.getSourceAsMap());
        }
        return res;
    }

    @Override
    public SearchRequest searchRequestGenerator(String index, String type, String value, String... fields) {
        QueryBuilder query = QueryBuilders.multiMatchQuery(value, fields);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

}
