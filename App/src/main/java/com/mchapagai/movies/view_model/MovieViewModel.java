package com.mchapagai.movies.view_model;

import com.mchapagai.movies.model.binding.CreditResponse;
import com.mchapagai.movies.model.binding.MovieDetailsResponse;
import com.mchapagai.movies.model.binding.MovieResponse;
import com.mchapagai.movies.model.binding.PersonResponse;
import com.mchapagai.movies.model.binding.ReviewsResponse;
import com.mchapagai.movies.model.binding.VideoResponse;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface MovieViewModel {

    Observable<MovieResponse> discoverMovies(String sortBy);
    Observable<VideoResponse> getMovieVideosbyId(int movieId);
    Observable<ReviewsResponse> getMovieReviewsById(int movieId);
    Observable<MovieDetailsResponse> getMovieDetails(int movieId);
    Observable<CreditResponse> getMovieCreditDetails(int movieId);
    Single<PersonResponse> getPersonDetailsById(String movieId);

}