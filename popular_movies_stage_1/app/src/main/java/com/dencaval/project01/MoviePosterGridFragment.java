package com.dencaval.project01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.dencaval.project01.ThemoviedbClient.MovieInfo;
import com.dencaval.project01.ThemoviedbClient.AsyncMoviePageRequest;
import com.dencaval.project01.ThemoviedbClient.RequestParameter;

import com.dencaval.project01.Utils.Criteria;

/**
 * Created by denis on 06/09/2016.
 */
public class MoviePosterGridFragment {
    public static class MovieFragment extends Fragment {

        ImageView imageView;
        GridView gridView;
        String previousCriteria;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            previousCriteria = prefs.getString(getString(R.string.pref_sorting_key), "");
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.activity_main, menu);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);

            gridView = (GridView) rootView.findViewById(R.id.gridView);

            MovieGridAdapter movieGridAdapter = new MovieGridAdapter(getContext());
            gridView.setAdapter(movieGridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieInfo movie = (MovieInfo) gridView.getAdapter().getItem(i);
                    Intent movieDetailsIntent = new Intent(getActivity(), DetailsActivity.class);

                    movieDetailsIntent.putExtra("originalTitle", movie.originalTitle);
                    movieDetailsIntent.putExtra("posterPath", movie.posterPath);
                    movieDetailsIntent.putExtra("overview", movie.overview);
                    movieDetailsIntent.putExtra("userRating", movie.userRating);
                    movieDetailsIntent.putExtra("releaseDate", movie.releaseDate);

                    startActivity(movieDetailsIntent);
                }
            });

            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int i) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if(firstVisibleItem + visibleItemCount >= totalItemCount){
                        MovieGridAdapter movieGridAdapter = (MovieGridAdapter) gridView.getAdapter();
                        AsyncMoviePageRequest movieTask = new AsyncMoviePageRequest(getContext(), gridView);

                        Criteria criteria;
                        if(previousCriteria.matches("rating")){
                            criteria = Criteria.userRating;
                        }else{
                            criteria = Criteria.popular;
                        }
                        RequestParameter parameter = new RequestParameter();
                        parameter.setCriteria(criteria);
                        parameter.setPage_id(movieGridAdapter.getCurrentPage() + 1);
                        movieTask.execute(parameter);
                    }
                }
            });

            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (R.id.action_settings == id){
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            super.onResume();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String currentSortingCriteria =
                    prefs.getString(getString(R.string.pref_sorting_key), "");

            if(previousCriteria != currentSortingCriteria){
                previousCriteria = currentSortingCriteria;
                MovieGridAdapter movieGridAdapter = (MovieGridAdapter) gridView.getAdapter();
                movieGridAdapter.clear_data();

                AsyncMoviePageRequest movieTask = new AsyncMoviePageRequest(getContext(), gridView);

                RequestParameter parameter = new RequestParameter();
                parameter.setPage_id(1); // Should it restart to page 01?
                if(currentSortingCriteria.matches("rating")){
                    parameter.setCriteria(Criteria.userRating);
                    movieTask.execute(parameter);
                }else{
                    parameter.setCriteria(Criteria.popular);
                    movieTask.execute(parameter);
                }
            }
        }
    }
}
