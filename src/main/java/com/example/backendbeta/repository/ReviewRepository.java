package com.example.backendbeta.repository;

import com.example.backendbeta.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findReviewsByUserId(Integer userId);
    List<Review> findReviewsByAppId(Integer appId);
    Review findReviewByUserIdAndAppId(Integer userId, Integer appId);
}
