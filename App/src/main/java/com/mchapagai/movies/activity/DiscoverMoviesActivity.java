package com.mchapagai.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mchapagai.library.utils.MaterialDialogUtils;
import com.mchapagai.library.views.PageLoader;
import com.mchapagai.movies.R;
import com.mchapagai.movies.adapter.MoviesGridAdapter;
import com.mchapagai.movies.common.BaseActivity;
import com.mchapagai.movies.common.Constants;
import com.mchapagai.movies.model.Movies;
import com.mchapagai.movies.model.Sort;
import com.mchapagai.movies.model.binding.MovieResponse;
import com.mchapagai.movies.utils.PreferencesHelper;
import com.mchapagai.movies.view_model.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DiscoverMoviesActivity extends BaseActivity implements MoviesGridAdapter.OnItemClickListener {

    private static final String TAG = DiscoverMoviesActivity.class.getSimpleName();

    private static final int COLUMN_COUNT = 2;
    private List<Movies> movieItems = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private PageLoader pageLoader;
    private Sort sort = Sort.MOST_POPULAR;
    private PreferencesHelper preferencesHelper;

    @Inject
    MovieViewModel movieViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_movies_activity_container);

        preferencesHelper = new PreferencesHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.movies_recycler_view);
        pageLoader = findViewById(R.id.movies_page_loader);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_popularity:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.MOST_POPULAR);
                loadMovies();
                break;
            case R.id.menu_sort_vote_count:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.MOST_RATED);
                loadMovies();
                break;
            case R.id.menu_sort_vote_average:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.TOP_RATED);
                loadMovies();
                break;

            case R.id.menu_logout:
                preferencesHelper.setSignedOut();
                Intent i = new Intent(this, LandingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                ActivityCompat.finishAffinity(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSortChanged(Sort sort) {
        this.sort = sort;
    }

    private void movieResponseItems(MovieResponse response) {
        movieItems = response.getMovies();
        recyclerView.setAdapter(new MoviesGridAdapter(movieItems, this));
    }

    private void loadMovies() {
        pageLoader.setVisibility(View.VISIBLE);
        compositeDisposable.add(movieViewModel.discoverMovies(sort.toString())
                .doFinally(() -> pageLoader.setVisibility(View.GONE))
                .subscribe(
                        response -> {
                            movieResponseItems(response);
                            Log.d(TAG, response.toString());
                        }, throwable -> MaterialDialogUtils.showDialog(this,
                                getResources().getString(R.string.service_error_title),
                                throwable.getMessage(),
                                getResources().getString(R.string.material_dialog_ok))
                ));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onClickItem(Movies movies, int position) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        startActivity(intent.putExtra(Constants.MOVIE_DETAILS, movies));
    }
}