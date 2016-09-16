package com.dencaval.project01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.dencaval.project01.ThemoviedbClient.MoviePageRequest;
import com.dencaval.project01.ThemoviedbClient.RequestParameter;

import com.dencaval.project01.Utils.Criteria;

/**
 * Created by denis on 06/09/2016.
 */
public class MoviePosterGridFragment {
    public static class MovieFragment extends Fragment {

        ImageView imageView;
        GridView gridView;
        MoviePageRequest movieTask;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
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

//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    MovieInfo movie = (MovieInfo) gridView.getAdapter().getItem(i);
//                    Intent movieDetailsIntent = new Intent(getActivity(), DetailsActivity.class);
//                    movieDetailsIntent.putExtra("originalTitle", movie.originalTitle);
//                    movieDetailsIntent.putExtra("posterPath", movie.posterPath);
//                    movieDetailsIntent.putExtra("overview", movie.overview);
//                    movieDetailsIntent.putExtra("userRating", movie.userRating);
//                    movieDetailsIntent.putExtra("releaseDate", movie.releaseDate);
//
//                    startActivity(movieDetailsIntent);
//                }
//            });
//
            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int i) {
                    Log.d("MovieFragment", "onScrollStateChanged");
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    String msg = String.valueOf(firstVisibleItem);
                    msg = msg.concat("#");
                    msg = msg.concat(String.valueOf(visibleItemCount));
                    msg = msg.concat("$");
                    msg = msg.concat(String.valueOf(totalItemCount));

                    if(firstVisibleItem + visibleItemCount >= totalItemCount){
                        movieTask = new MoviePageRequest(getContext(), gridView);
                        RequestParameter parameter = new RequestParameter();
                        parameter.setCriteria(Criteria.popular);
                        MovieGridAdapter m = (MovieGridAdapter) gridView.getAdapter();
                        parameter.setPage_id(m.getCurrentPage() + 1);
                        movieTask.execute(parameter);
                    }

                    Log.d("MovieFragment", "onScroll");
//                    if(view.getAdapter() == null){
//                        return;
//                    }else if(view.getAdapter().getCount() == 0){
//                        return;
//                    }//else if(firstVisibleItem + visibleItemCount >= totalItemCount){

                    Log.d("MovieFragment", msg);
                    //}
                }
            });
//
//            // TODO(Denis): Request data according to default setting
            movieTask = new MoviePageRequest(getContext(), gridView);
            MovieGridAdapter m = (MovieGridAdapter) gridView.getAdapter();
            RequestParameter parameter = new RequestParameter();
            parameter.setCriteria(Criteria.popular);
            parameter.setPage_id(m.getCurrentPage() + 1);
            movieTask.execute(parameter);

            return rootView;
        }

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//
//            int id = item.getItemId();
//
//            if (R.id.action_settings == id){
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(intent);
//            }
//
//            return super.onOptionsItemSelected(item);
//        }

//        @Override
//        public void onResume() {
//            super.onResume();
//            Toast.makeText(getContext(), "onResume", Toast.LENGTH_SHORT);
//
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String v = prefs.getString(getString(R.string.pref_sorting_key), "");
//
//            gridView.invalidateViews();
//            RequestParameter parameter = new RequestParameter();
//            parameter.setPage_id(1); // Wrong decision to hard code in 1
//            if(v.matches("rating")){
//                parameter.setCriteria(Criteria.userRating);
//                movieTask.execute(parameter);
//            }else{
//                parameter.setCriteria(Criteria.popular);
//                movieTask.execute(parameter);
//            }
//        }
    }
}
