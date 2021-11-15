package cz.cvut.ear.flashcards.service;


import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.repository.ReviewRepository;
import cz.cvut.ear.flashcards.dto.SearchDto;
import cz.cvut.ear.flashcards.model.Review;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Review service
 */
@SuppressWarnings("ALL")
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    /**
     * get all reviews
     * @return set of reviews
     */
    @Transactional(readOnly = true)
    public HashSet<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * get review by id
     * @param reviewId review id
     * @return review entity
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "reviewsCache", key = "#reviewId", unless = "#result==null")
    public Review getReview(Integer reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (!review.isPresent()) {
            throw new NotFoundException("Reviw not found! ID: " + reviewId);
        }
        return review.get();
    }

    /**
     * create new review
     * @param review new review
     * @return created review
     */
    @Transactional
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * create review and add created review to the topic and user
     * @param review new review
     * @param topicId topic id
     * @param userId user id
     * @return created review
     */
    @Transactional
    public Review addReview(Review review, Integer topicId, Integer userId) {
        Topic topic = topicService.getTopic(topicId);
        User user = userService.getUser(userId);
        review.setUser(user);
        review.setTopic(topic);
        return reviewRepository.save(review);
    }

    /**
     * update review
     * @param reviewId review id
     * @param review new review
     * @return updated review
     */
    @Transactional
    @CachePut(key = "#reviewId", value="reviewsCache")
    public Review updateReview(Integer reviewId, Review review) {
        Review review1 = getReview(reviewId);
        if(!review1.getReview().equals(review.getReview()) && review.getReview() != null) {
            review1.setReview(review.getReview());
        }
        return reviewRepository.save(review1);
    }

    /**
     * delete review by id
     * @param reviewId review id
     */
    @Transactional
    @CacheEvict(value="reviewsCache", key = "#reviewId")
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    /**
     * find all topic's reviews
     * @param topicId topic id
     * @return set of reviews
     */
    @Transactional
    public HashSet<Review> findAllReviewsByTopicId(Integer topicId) {
        return reviewRepository.findAllByTopicId(topicId);
    }

    /**
     * parse search request from search dto
     * @param searchDto search dto
     * @return parsed request as map
     */
    public Map<String, Set<String>> parseSearchReqest(SearchDto searchDto) {
        String request = searchDto.getSearch();

        if(request == null || request.isEmpty()) {
            return null;
        }

        StringBuilder review = new StringBuilder("");
        StringBuilder author = new StringBuilder("");

        Set<String> reviews = new HashSet<>();
        Set<String> authors = new HashSet<>();

        char[] array = request.toCharArray();

        for(int i = 0; i < array.length; i++) {
            boolean add = false;
            switch (array[i]) {
                case '@':
                    for(++i; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            author.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        authors.add(author.toString());
                        author = new StringBuilder("");
                    }
                    break;
                default:
                    add = false;
                    for(; i < array.length; i++) {
                        char c = array[i];
                        if(Character.isLetter(c) || Character.isDigit(c)) {
                            review.append(c);
                            add = true;
                        } else {
                            if(c == ' ') {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        reviews.add(review.toString());
                        review = new StringBuilder("");
                    }
                    break;
                case ' ':
                    continue;
                case '#':
                    continue;
                case '\n':
                    continue;
            }
        }

        Map<String, Set<String>> map = new HashMap<>();

        map.put("reviews", reviews);
        map.put("authors", authors);

        return map;
    }

    /**
     * search by parsed search request
     * @param map parsed search request
     * @return set of reviews
     */
    public Set<Review> search(Map<String, Set<String>> map) {

        Set<String> reviews = map.get("reviews");
        Set<String> authors = map.get("authors");

        if(!reviews.isEmpty() && authors.isEmpty()) {
            return reviews
                    .stream()
                    .map(reviewRepository::findAllByReviewContainingIgnoreCase)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(reviews.isEmpty() && !authors.isEmpty()) {
            return authors
                    .stream()
                    .map(reviewRepository::findAllByAuthorContainingIgnoreCase)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        if(!reviews.isEmpty() && !authors.isEmpty()) {
            return authors
                    .stream()
                    .map( a -> reviews
                            .stream()
                            .map( r -> this.reviewRepository.findAllByReviewContainingIgnoreCaseAndAuthorContainingIgnoreCase(r,a))
                            .flatMap(Set::stream)
                            .collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        return new HashSet<>();
    }
}


