package com.mchapagai.movies.service;

import com.mchapagai.movies.model.movies.binding.CombinedPersonResponse;
import com.mchapagai.movies.model.movies.binding.CreditResponse;
import com.mchapagai.movies.model.movies.binding.MovieDetailsResponse;
import com.mchapagai.movies.model.movies.binding.MovieResponse;
import com.mchapagai.movies.model.movies.binding.PersonResponse;
import com.mchapagai.movies.model.movies.binding.ReviewsResponse;
import com.mchapagai.movies.model.movies.binding.VideoResponse;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("discover/movie")
    Observable<MovieResponse> discoverMovies(@Query("sort_by") String sortBy);

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

}
