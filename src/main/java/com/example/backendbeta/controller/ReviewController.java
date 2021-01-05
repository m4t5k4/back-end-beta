package com.example.backendbeta.controller;

import com.example.backendbeta.model.Review;
import com.example.backendbeta.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @PostConstruct
    public void fillDB() {
        if (reviewRepository.count()==0){
            reviewRepository.save(new Review(001,620, 5));
            reviewRepository.save(new Review(002, 620, 3));
            reviewRepository.save(new Review(003, 620, 4));
        }
    }

    @GetMapping("reviews/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable Integer userId){
        return reviewRepository.findReviewsByUserId(userId);
    }

    @GetMapping("/reviews/{appId}")
    public List<Review> getReviewsByAppId(@PathVariable Integer appId) {
        return reviewRepository.findReviewsByAppId(appId);
    }

    @GetMapping("/reviews/user/{userId}/game/{appId}")
    public Review getReviewByUserIdAndAppId(@PathVariable Integer userId, @PathVariable Integer appId){
        return reviewRepository.findReviewByUserIdAndAppId(userId, appId);
    }

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review){
        reviewRepository.save(review);
        return review;
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review updatedReview){
        Review retrievedReview = reviewRepository.findReviewByUserIdAndAppId(updatedReview.getUserId(),updatedReview.getAppId());
        retrievedReview.setAppId(updatedReview.getAppId());
        retrievedReview.setScoreNumber(updatedReview.getScoreNumber());
        reviewRepository.save(retrievedReview);
        return retrievedReview;
    }

    @DeleteMapping("/reviews/user/{userId}/game/{appId}")
    public ResponseEntity deleteReview(@PathVariable Integer userId, @PathVariable Integer appId){
        Review review = reviewRepository.findReviewByUserIdAndAppId(userId,appId);
        if(review!=null){
            reviewRepository.delete(review);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
