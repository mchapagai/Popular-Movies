package com.example.mchapagai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.library.utils.MaterialDialogUtils;
import com.example.library.views.PageLoader;
import com.example.mchapagai.R;
import com.example.mchapagai.adapter.MoviesGridAdapter;
import com.example.mchapagai.common.BaseActivity;
import com.example.mchapagai.model.Movies;
import com.example.mchapagai.model.binding.MovieResponse;
import com.example.mchapagai.view_model.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class LandingActivity extends BaseActivity {

    private static final String TAG = LandingActivity.class.getSimpleName();

    private static final int COLUMN_COUNT = 2;
    private List<Movies> movieItems = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private PageLoader pageLoader;

    @Inject
    MovieViewModel movieViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(navigateToAbout);

        recyclerView = findViewById(R.id.movies_recycler_view);
        pageLoader = findViewById(R.id.movies_page_loader);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.landing_fragment_container, new LandingFragment()).commit();*/
    }

    View.OnClickListener navigateToAbout = view -> {
        Intent intent = new Intent(view.getContext(), AboutActivity.class);
        startActivity(intent);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void movieResponseItems(MovieResponse response) {
        movieItems = response.getMovies();
        recyclerView.setAdapter(new MoviesGridAdapter(movieItems));
    }

    private void loadMovies() {
        pageLoader.setVisibility(View.VISIBLE);
        compositeDisposable.add(movieViewModel.discoverMovies()
                .doFinally(() -> pageLoader.setVisibility(View.GONE))
                .subscribe(
                        new Consumer<MovieResponse>() {
                            @Override
                            public void accept(MovieResponse response) {
                                movieResponseItems(response);
                                Log.d(TAG, response.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                MaterialDialogUtils.showDialog(LandingActivity.this,
                                        getResources().getString(R.string.service_error_title),
                                        throwable.getMessage(),
                                        getResources().getString(R.string.material_dialog_ok));
                            }
                        }
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
}
