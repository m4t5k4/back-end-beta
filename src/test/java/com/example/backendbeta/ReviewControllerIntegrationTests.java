package com.example.backendbeta;

import com.example.backendbeta.model.Review;
import com.example.backendbeta.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    private Review reviewUser1Game1 = new Review(001, 620, 4);
    private Review reviewUser1Game2 = new Review(001, 1145360, 3);
    private Review reviewUser2Game1 = new Review(002, 620, 2);
    private Review reviewToBeDeleted = new Review(999, 621, 1);

    @BeforeEach
    public void beforeAllTests() {
        reviewRepository.deleteAll();
        reviewRepository.save(reviewUser1Game1);
        reviewRepository.save(reviewUser1Game2);
        reviewRepository.save(reviewUser2Game1);
        reviewRepository.save(reviewToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
        reviewRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenReview_whenGetReviewByUserIdAndAppId_thenReturnJsonReview() throws Exception {

        mockMvc.perform(get("/reviews/user/{userId}/game/{appId}", 001, 620))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(001)))
                .andExpect(jsonPath("$.appId", is(620)))
                .andExpect(jsonPath("$.scoreNumber", is(4)));
    }

    @Test
    public void givenReview_whenGetReviewsByAppId_thenReturnJsonReviews() throws Exception {

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewUser1Game1);
        reviewList.add(reviewUser2Game1);

        mockMvc.perform(get("/reviews/{appId}", 620))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(001)))
                .andExpect(jsonPath("$[0].appId", is(620)))
                .andExpect(jsonPath("$[0].scoreNumber", is(4)))
                .andExpect(jsonPath("$[1].userId", is(002)))
                .andExpect(jsonPath("$[1].appId", is(620)))
                .andExpect(jsonPath("$[1].scoreNumber", is(2)));
    }

    @Test
    public void givenReview_whenGetReviewsByUserId_thenReturnJsonReviews() throws Exception {

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewUser1Game1);
        reviewList.add(reviewUser1Game2);

        mockMvc.perform(get("/reviews/user/{userId}", 001))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(001)))
                .andExpect(jsonPath("$[0].appId", is(620)))
                .andExpect(jsonPath("$[0].scoreNumber", is(4)))
                .andExpect(jsonPath("$[1].userId", is(001)))
                .andExpect(jsonPath("$[1].appId", is(1145360)))
                .andExpect(jsonPath("$[1].scoreNumber", is(3)));
    }

    @Test
    public void whenPostReview_thenReturnJsonReview() throws Exception {
        Review reviewUser3Game1 = new Review(003, 620, 5);

        mockMvc.perform(post("/reviews")
                .content(mapper.writeValueAsString(reviewUser3Game1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(003)))
                .andExpect(jsonPath("$.appId", is(620)))
                .andExpect(jsonPath("$.scoreNumber", is(5)));
    }

    @Test
    public void givenReview_whenPutReview_thenReturnJsonReview() throws Exception {

        Review updatedReview = new Review(001, 620, 2);

        mockMvc.perform(put("/reviews")
                .content(mapper.writeValueAsString(updatedReview))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(001)))
                .andExpect(jsonPath("$.appId", is(620)))
                .andExpect(jsonPath("$.scoreNumber", is(2)));
    }

    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/reviews/user/{userId}/game/{appId}", 999, 621)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/reviews/user/{userId}/game/{appId}", 888, 621)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
