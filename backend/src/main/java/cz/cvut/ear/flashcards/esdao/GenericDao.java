package cz.cvut.ear.flashcards.esdao;


import org.elasticsearch.action.search.SearchRequest;

import java.util.Collection;
import java.util.Map;

/**
 * Base interface for data access objects.
 *
 * @param <T>
 */
public interface GenericDao<T> {

    /**
     * Finds entity instance with the specified identifier.
     *
     * @param id Identifier
     * @return Entity instance or {@code null} if no such instance exists
     */
    Map<String,Object> find(String id, String INDEX, String TYPE);


    /**
     * Persists the specified entity.
     *
     * @param entity Entity to persist
     */
    T insert(T entity, String id,  String INDEX, String TYPE);


    /**
     * Updates the specified entity.
     *
     * @param entity Entity to update
     * @return The updated instance
     */
    Map<String,Object> update(String id, T entity,  String INDEX, String TYPE);

    /**
     * Removes the specified entity.
     *
     * @param id Entity to remove
     */
    void remove(String id,  String INDEX, String TYPE);


    /**
     * Search value by fields
     * @param index
     * @param type
     * @param value
     * @param fields
     * @return Collection<Map<String, Object>>
     */
    Collection<Map<String, Object>> search(String index, String type, String value, String... fields);

    /**
     * elastic search request generator
     * @param index index
     * @param type type
     * @param value search value
     * @param fields search in fields
     * @return search request
     */
    SearchRequest searchRequestGenerator(String index, String type, String value, String... fields);

}


