package com.mchapagai.klutter.service;

import com.mchapagai.klutter.model.binding.CombinedPersonResponse;
import com.mchapagai.klutter.model.binding.CreditResponse;
import com.mchapagai.klutter.model.binding.MovieDetailsResponse;
import com.mchapagai.klutter.model.binding.MovieResponse;
import com.mchapagai.klutter.model.binding.PersonResponse;
import com.mchapagai.klutter.model.binding.ReviewsResponse;
import com.mchapagai.klutter.model.binding.VideoResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("discover/movie")
    Flowable<MovieResponse> discoverMovies(@Query("page") int page,
            @Query("sort_by") String sortBy);

    @GET("movie/{id}/videos")
    Observable<VideoResponse> getMovieVideosbyId(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getMovieReviewsById(@Path("id") int movieId);

    @GET("movie/{id}")
    Observable<MovieDetailsResponse> getMovieDetails(@Path("id") int movieId);

    @GET("movie/{id}/credits")
    Observable<CreditResponse> getMovieCreditDetails(@Path("id") int movieId);

    @GET("person/{personId}")
    Single<PersonResponse> getPersonDetailsById(@Path("personId") int id);

    @GET("person/{personId}/combined_credits")
    Observable<CombinedPersonResponse> getPersonCombinedDetailsById(@Path("personId") int personId);

    @GET("search/movie")
    Observable<MovieResponse> searchMovies(@Query("query") String q);

}