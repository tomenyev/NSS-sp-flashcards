package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.model.Review;
import cz.cvut.ear.flashcards.service.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewControllerTest extends BaseControllerTestRunner {

    @Mock
    private ReviewService reviewServiceMock;

    @InjectMocks
    private ReviewController reviewControllerMock;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(reviewControllerMock);
    }


    @Test
    public void addReview() throws Exception {
        final Review review = Generator.generateReview();

        Mockito.when(reviewServiceMock.addReview(Mockito.any(Review.class))).thenReturn(review);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/reviews/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(review)).accept(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        final Review result = readValue(mvcResult, Review.class);

        assertNotNull(result);
        assertEquals(result.getId(), result.getId());
        assertEquals(review.getAuthor(), result.getAuthor());
    }

    @Test
    public void deleteReview() throws Exception {

        mockMvc.perform(delete("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
