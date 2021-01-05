package com.example.backendbeta;

import com.example.backendbeta.model.Review;
import com.example.backendbeta.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenReviews_whenGetReviewByUserIdAndAppId_thenReturnJsonReview() throws Exception {
        Review reviewUser1 = new Review(001,620,4);

        given(reviewRepository.findReviewByUserIdAndAppId(001,620)).willReturn(reviewUser1);

        mockMvc.perform(get("/reviews/user/{userId}/game/{appId}",001,620))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(001)))
                .andExpect(jsonPath("$.appId",is(620)))
                .andExpect(jsonPath("$.scoreNumber",is(4)));
    }

    @Test
    public void givenReview_whenGetReviewByAppId_thenReturnJsonReviews() throws Exception {
        Review review1 = new Review(001,620,4);
        Review review2 = new Review(002,620,3);
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        given(reviewRepository.findReviewsByAppId(620)).willReturn(reviewList);

        mockMvc.perform(get("/reviews/{appId}",620))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(001)))
                .andExpect(jsonPath("$[0].appId",is(620)))
                .andExpect(jsonPath("$[0].scoreNumber",is(4)))
                .andExpect(jsonPath("$[1].userId",is(002)))
                .andExpect(jsonPath("$[1].appId",is(620)))
                .andExpect(jsonPath("$[1].scoreNumber",is(3)));
    }

    @Test
    public void givenReview_whenGetReviewsByUserId_thenReturnJsonReviews() throws Exception {
        Review review1 = new Review(001,620,4);
        Review review2 = new Review(001,1145360,2);
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        given(reviewRepository.findReviewsByUserId(001)).willReturn(reviewList);

        mockMvc.perform(get("/reviews/user/{userId}",001))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId",is(001)))
                .andExpect(jsonPath("$[0].appId",is(620)))
                .andExpect(jsonPath("$[0].scoreNumber",is(4)))
                .andExpect(jsonPath("$[1].userId",is(001)))
                .andExpect(jsonPath("$[1].appId",is(1145360)))
                .andExpect(jsonPath("$[1].scoreNumber",is(2)));
    }

    @Test
    public void whenPostReview_thenReturnJsonReview() throws Exception {
        Review review3 = new Review(003,620,3);

        mockMvc.perform(post("/reviews")
                .content(mapper.writeValueAsString(review3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(003)))
                .andExpect(jsonPath("$.appId",is(620)))
                .andExpect(jsonPath("$.scoreNumber",is(3)));
    }

    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception {
        Review review1 = new Review(001,620,4);

        given(reviewRepository.findReviewByUserIdAndAppId(001,620)).willReturn(review1);

        Review updatedReview = new Review(001,620,2);

        mockMvc.perform(put("/reviews")
                .content(mapper.writeValueAsString(updatedReview))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId",is(001)))
                .andExpect(jsonPath("$.appId",is(620)))
                .andExpect(jsonPath("$.scoreNumber",is(2)));
    }

    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception{
        Review reviewToBeDeleted = new Review(999,620,5);

        given(reviewRepository.findReviewByUserIdAndAppId(999,620)).willReturn(reviewToBeDeleted);

        mockMvc.perform(delete("/reviews/user/{userId}/game/{appId}",999,620)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception{
        given(reviewRepository.findReviewByUserIdAndAppId(888,620)).willReturn(null);

        mockMvc.perform(delete("/reviews/user/{userId}/game/{appId}",888,620)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
