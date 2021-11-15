package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.model.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotSame;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReviewServiceTest {

//    findAllReviewsByUserId

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private ReviewService reviewService;


    @Test
    public void addReviewTest() {
        Review review = Generator.generateReview();
        review.setId(null);
        review = reviewService.addReview(review);
        assertNotNull(review);
    }

//    @Test
//    public void getReviewTest() {
//        Review review = Generator.generateReview();
//        review.setId(null);
//        review = reviewService.addReview(review);
//        assertNotNull(review);
//        review = reviewService.getReview(review.getId());
//        assertNotNull(review);
//    }

//    @Test
//    public void getAllReviewsTest() {
//        Set<Review> expected = new HashSet<>();
//        Set<Review> get = new HashSet<>();
//
//        for (int i = 0; i < 10; i++) {
//            Review review = Generator.generateReview();
//            review.setId(null);
//            review = reviewService.addReview(review);
//            assertNotNull(review);
//            expected.add(review);
//        }
//
//        get = reviewService.getAllReviews();
//
//        assertEquals(expected, get);
//
//    }
//
//    @Test
//    public void updateReviewTest() {
//        Review review = Generator.generateReview();
//        review.setId(null);
//        review = reviewService.addReview(review);
//        assertNotNull(review);
//
//        String expected = review.getReview();
//
//        review.setReview("newreview");
//
//        reviewService.updateReview(review.getId(), review);
//
//        review = reviewService.getReview(review.getId());
//
//        assertNotNull(review);
//        assertNotSame(review.getReview(), expected);
//        assertEquals(review.getReview(), "newreview");
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void deleteReviewTest() {
//        Review review = Generator.generateReview();
//        review.setId(null);
//        review = reviewService.addReview(review);
//        assertNotNull(review);
//        reviewService.deleteReview(review.getId());
//        reviewService.getReview(review.getId());
//    }
//
//    @Test
//    public void findAllReviewsByUserIdTest() {
//        Role role = Role.USER;
//
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        for (int i = 0; i < 10; i++) {
//            Review review = Generator.generateReview();
//            review.setId(null);
//            review.setAuthor(null);
//            review.setUser(user);
//            review = reviewService.addReview(review);
//            assertNotNull(review);
//        }
//
//        Set<Review> userReviews = reviewService.findAllReviewsByUserId(user.getId());
//        Set<Review> reviews = userServiceImpl.getReviews(user.getId());
//        assertEquals(reviews, userReviews);
//        assertEquals(reviews.size(), 10);
//    }
//
//    @Test
//    public void findAllReviewsByTopicIdTest() {
//        Role role = Role.USER;
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        Topic topic = Generator.generateTopic();
//        topic.setAuthor(user.getUsername());
//        topic.addUser(user);
//
//        topic = topicService.addTopic(topic);
//
//        assertNotNull(topic);
//
//        for (int i = 0; i < 10; i++) {
//            Review review = Generator.generateReview();
//            review.setId(null);
//            review.setAuthor(null);
//            review.setTopic(topic);
//            review.setUser(user);
//            review = reviewService.addReview(review);
//            assertNotNull(review);
//        }
//
//        Set<Review> topicReviews = reviewService.findAllReviewsByTopicId(topic.getId());
//
//        Set<Review> userReviews = reviewService.findAllReviewsByUserId(user.getId());
//
//        Set<Review> reviews = reviewService.getAllReviews();
//
//        assertEquals(reviews, userReviews);
//
//        assertEquals(userReviews, topicReviews);
//
//        assertEquals(reviews, topicReviews);
//
//        assertEquals(reviews.size(), 10);
//
//    }


}
