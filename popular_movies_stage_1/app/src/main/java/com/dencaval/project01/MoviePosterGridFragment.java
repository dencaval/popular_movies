package com.dencaval.project01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.dencaval.project01.model.MovieInfo;
import com.dencaval.project01.network.MovieRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by denis on 06/09/2016.
 */
public class MoviePosterGridFragment {
    public static class MovieFragment extends Fragment {

        ImageView imageView;
        GridView gridView;

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

//            // Request data according to default setting
            MovieRequestTask movieTask = new MovieRequestTask(getContext(), gridView);
            movieTask.execute(utils.Criteria.popular);

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
            Toast.makeText(getContext(), "onResume", Toast.LENGTH_SHORT);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String v = prefs.getString(getString(R.string.pref_sorting_key), "");

            gridView.invalidateViews();
            MovieRequestTask movieTask = new MovieRequestTask(getContext(), gridView);
            if(v.matches("rating")){
                movieTask.execute(utils.Criteria.userRating);
            }else{
                movieTask.execute(utils.Criteria.popular);
            }
        }
    }
}
