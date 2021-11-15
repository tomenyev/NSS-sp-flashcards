package cz.cvut.ear.flashcards.dao;

import cz.cvut.ear.flashcards.model.Topic;
import org.springframework.stereotype.Repository;

import java.util.List;


/** Topic DAO
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Repository
public class TopicDao extends BaseDao<Topic> {
    public TopicDao() {
        super(Topic.class);
    }

    /** Search all public topic with name contains..
     * @param name Public topic's name
     * @return topics List of publi c topics, contains name
    */
    public List<Topic> searchPublicTopicsByName(String name) {
        String query = "SELECT * FROM Topic t WHERE t.name LIKE '%" + name.trim() + "%' AND t.shared = true";
        List<Topic> topics = em.createQuery(query)
                .setMaxResults(10)
                .getResultList();
        return topics;
    }

}
