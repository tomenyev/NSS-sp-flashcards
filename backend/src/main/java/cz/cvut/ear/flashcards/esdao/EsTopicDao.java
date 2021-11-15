package cz.cvut.ear.flashcards.esdao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.ear.flashcards.model.Topic;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;


@Repository
public class EsTopicDao extends BaseDao<Topic> {

    private final String INDEX = "topicdata";
    private final String TYPE = "topics";

    public EsTopicDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        super(objectMapper, restHighLevelClient);
    }

    public Map<String, Object> find(String id) {
        return super.find(id, INDEX, TYPE);
    }

    public Topic insert(Topic entity, String id) {
        return super.insert(entity, id, INDEX, TYPE);
    }

    public Map<String, Object> update(String id, Topic entity) {
        return super.update(id, entity, INDEX, TYPE);
    }

    public void remove(String id) {
        super.remove(id, INDEX, TYPE);
    }

    public Collection<Map<String, Object>> search(String value, String... fields)  {
        return super.search(INDEX, TYPE, value, fields);
    }
}
