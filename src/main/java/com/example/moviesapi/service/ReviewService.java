package com.example.moviesapi.service;

import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.entity.Review;
import com.example.moviesapi.exception.ApiException;
import com.example.moviesapi.repository.MovieRepository;
import com.example.moviesapi.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Review createReview(Long movieId, String userName, Integer rating, String comment) {
        // Check if user already reviewed this movie
        reviewRepository.findByMovieIdAndUserName(movieId, userName).ifPresent(review -> {
            throw ApiException.duplicate("You have already reviewed this movie");
        });

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> ApiException.notFound("Movie not found with id: " + movieId));

        Review review = new Review();
        review.setMovie(movie);
        review.setUserName(userName);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public Page<Review> getMovieReviews(Long movieId, Pageable pageable) {
        // Verify movie exists
        if (!movieRepository.existsById(movieId)) {
            throw ApiException.notFound("Movie not found with id: " + movieId);
        }
        return reviewRepository.findByMovieId(movieId, pageable);
    }

    public Double getMovieAverageRating(Long movieId) {
        return reviewRepository.findAverageRatingByMovieId(movieId)
                .orElse(0.0);
    }

    @Transactional
    public void deleteReview(Long reviewId, String userName) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ApiException.notFound("Review not found with id: " + reviewId));

        if (!review.getUserName().equals(userName)) {
            throw ApiException.invalidRequest("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }
}