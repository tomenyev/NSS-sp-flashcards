package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.esdao.EsTopicDao;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Review;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.service.DeckService;
import cz.cvut.ear.flashcards.service.ReviewService;
import cz.cvut.ear.flashcards.service.SearchFacade;
import cz.cvut.ear.flashcards.service.TopicService;
import cz.cvut.ear.flashcards.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SearchFacade searchFacade;

    @Autowired
    private EsTopicDao topicDao;

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * Get all topics
     *
     * @return HashSet<Topic>
     */
    @GetMapping
    public HashSet<Topic> getAllTopics() {
        return this.topicService.getAllTopics();
    }

    /**
     * Get single topic with topicId
     *
     * @param topicId
     * @return Topic
     */
    @GetMapping("/{topicId}")
    public Topic getTopic(@PathVariable Integer topicId) {
        return this.topicService.getTopic(topicId);
    }

    /**
     * Get all topic's reviews
     *
     * @param topicId
     * @return Topic
     */
    @GetMapping("/{topicId}/reviews")
    public Set<Review> getTopicReviews(@PathVariable Integer topicId) {
        return reviewService.findAllReviewsByTopicId(topicId);
    }

    /**
     * Get single topic's decks
     *
     * @param topicId
     * @return HashSet<Deck>
     */
    @GetMapping("/{topicId}/decks")
    public HashSet<Deck> getTopicDecks(@PathVariable Integer topicId) {
        return this.deckService.getAllTopicDecks(topicId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * Get topic's by search request
     *
     * @param searchDto
     * @return Set<Topic>
     */
    @PostMapping(value = "/public")
    public Set<Topic> searchPublicTopics(@RequestBody SearchDto searchDto) {
        return searchFacade.search(searchDto, false);
    }

    /**
     * Create topic
     *
     * @param topic
     * @return Topic
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Topic addTopic(@RequestBody Topic topic) {
//        this.esearchInsert(topic, topic.getId().toString()); //ELASTICSEARCH
        return this.topicService.addTopic(topic);
    }

    /**
     * Search topics
     * @return Set<Topic>
     */
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<Topic> search(@RequestBody SearchDto searchDto) {
        return searchFacade.search(searchDto, true);
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * Update topic
     *
     * @param topic
     * @param topicId
     * @return Topic
     */
    @PutMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public Topic updateTopic(@RequestBody Topic topic, @PathVariable Integer topicId) {
//        this.esearchUpdate(topic, topicId.toString()); //ELASTICSEARCH
        return this.topicService.updateTopic(topicId, topic);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * Delete topic
     *
     * @param topicId
     */
    @DeleteMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTopic(@PathVariable Integer topicId) {
        //this.esearchDelete(topicId.toString());//ELASTICSEARCH
        this.topicService.deleteTopic(topicId);
    }


    /*-----------ELASTICSEARCH--------------*/

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * search topic by value in fields
     * @param value
     * @param fields
     * @return Collection<Map<String, Object>>
     */
    @GetMapping("/esearch")
    public Collection<Map<String, Object>> search(@RequestParam String value, @RequestParam String[] fields) {
        return topicDao.search(value, fields);
    }

    /**
     * find topic by id
     * @param topicId
     * @return Map<String, Object>
     */
    @GetMapping("/esearch/{topicId}")
    public Map<String, Object> esearchById(@PathVariable String topicId) {
        return topicDao.find(topicId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * insert topic
     * @param topic
     * @param topicId
     * @return Topic
     */
    @PostMapping("/esearch/{topicId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Topic esearchInsert(@RequestBody Topic topic, @PathVariable String topicId) {
        return topicDao.insert(topic, topicId); //ELASTICSEARCH
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update topic
     * @param topic
     * @param topicId
     * @return Map<String, Object>
     */
    @PutMapping("/esearch/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> esearchUpdate(@RequestBody Topic topic, @PathVariable String topicId) {
        return topicDao.update(topicId, topic);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * delete topic by id
     * @param topicId
     */
    @DeleteMapping("/esearch/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public void esearchDelete(@PathVariable String topicId) {
        topicDao.remove(topicId.toString());
    }
}