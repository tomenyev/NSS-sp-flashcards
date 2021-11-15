package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.esdao.EsReviewDao;
import cz.cvut.ear.flashcards.model.Review;
import cz.cvut.ear.flashcards.service.ReviewService;
import cz.cvut.ear.flashcards.service.SearchFacade;
import cz.cvut.ear.flashcards.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@SuppressWarnings("ALL")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SearchFacade searchFacade;

    @Autowired
    private EsReviewDao reviewDao;

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * Get all reviews
     *
     * @return HashSet<Review>
     */
    @GetMapping
    public HashSet<Review> getAllReviews() {
        return this.reviewService.getAllReviews();
    }

    /**
     * Get single review with reviewId
     *
     * @param reviewId
     * @return Review
     */
    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable Integer reviewId) {
        return this.reviewService.getReview(reviewId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * Search reviews
     * @return Set<Review>
     */
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<Review> search(@RequestBody SearchDto searchDto) {
        return searchFacade.searchReview(searchDto);
    }

    /**
     * Create review
     *
     * @param review
     * @return Review
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody Review review) {
        review = this.reviewService.addReview(review);
//        this.esearchInsert(review, review.getId().toString());//ELASTICSEARCH
        return review;
    }

    /**
     * Create review
     *
     * @param review
     * @return  Review
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReviewByTopicAndUser(@RequestBody Review review, @RequestParam Integer topicId, @RequestParam Integer userId) {
        review = this.reviewService.addReview(review, topicId, userId);
//        this.esearchInsert(review, review.getId().toString());//ELASTICSEARCH
        return review;
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * Update review
     *
     * @param review
     * @param reviewId
     * @return Review
     */
    @PutMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@RequestBody Review review, @PathVariable Integer reviewId) {
        review = this.reviewService.updateReview(reviewId, review);
//        this.updateReview(review, reviewId); //ELASTICSEARCH
        return review;
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * Delete review
     *
     * @param reviewId
     */
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable Integer reviewId) {
//        this.esearchDelete(reviewId.toString()); //ELASTICSEARCH
        this.reviewService.deleteReview(reviewId);
    }


    /*-----------ELASTICSEARCH--------------*/

    /* ============ */
    /* GET REQUESTS */
    /* ============ */

    /**
     * search reviews by value in fields
     * @param value
     * @param fields
     * @return
     */
    @GetMapping("/esearch")
    public Collection<Map<String, Object>> search(@RequestParam String value, @RequestParam String[] fields) {
        return reviewDao.search(value, fields);
    }

    /**
     * find review by id
     * @param reviewId
     * @return Review
     */
    @GetMapping("/esearch/{reviewId}")
    public Map<String, Object> esearchById(@PathVariable String reviewId) {
        return reviewDao.find(reviewId);
    }

    /* ============= */
    /* POST REQUESTS */
    /* ============= */

    /**
     * insert review by id
     * @param review
     * @param reviewId
     * @return Review
     */
    @PostMapping("/esearch/{reviewId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Review esearchInsert(@RequestBody Review  review, @PathVariable String reviewId) {
        return reviewDao.insert(review, reviewId); //ELASTICSEARCH
    }

    /* ============ */
    /* PUT REQUESTS */
    /* ============ */

    /**
     * update review by id
     * @param review
     * @param reviewId
     * @return Review
     */
    @PutMapping("/esearch/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> esearchUpdate(@RequestBody Review review, @PathVariable String reviewId) {
        return reviewDao.update(reviewId, review);
    }

    /* =============== */
    /* DELETE REQUESTS */
    /* =============== */

    /**
     * delete review by id
     * @param reviewId
     */
    @DeleteMapping("/esearch/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void esearchDelete(@PathVariable String reviewId) {
        reviewDao.remove(reviewId);
    }

}