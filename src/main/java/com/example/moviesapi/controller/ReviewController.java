package com.example.moviesapi.controller;

import com.example.moviesapi.dto.CreateReviewRequest;
import com.example.moviesapi.dto.ReviewDTO;
import com.example.moviesapi.entity.Review;
import com.example.moviesapi.mapper.ReviewMapper;
import com.example.moviesapi.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies/{movieId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long movieId,
            @RequestParam String userName,
            @Valid @RequestBody CreateReviewRequest request) {
        Review review = reviewService.createReview(movieId, userName,
                request.getRating(), request.getComment());
        return ResponseEntity.status(201).body(reviewMapper.toDTO(review));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getMovieReviews(
            @PathVariable Long movieId,
            Pageable pageable) {
        Page<Review> reviews = reviewService.getMovieReviews(movieId, pageable);
        return ResponseEntity.ok(reviews.map(reviewMapper::toDTO));
    }

    @GetMapping("/average")
    public ResponseEntity<Double> getMovieAverageRating(@PathVariable Long movieId) {
        Double averageRating = reviewService.getMovieAverageRating(movieId);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long movieId,
            @PathVariable Long reviewId,
            @RequestParam String userName) {
        reviewService.deleteReview(reviewId, userName);
        return ResponseEntity.noContent().build();
    }
}