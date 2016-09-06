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
            MovieRequestTask movieTask = new MovieRequestTask();
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
            MovieRequestTask movieTask = new MovieRequestTask();
            if(v.matches("rating")){
                movieTask.execute(utils.Criteria.userRating);
            }else{
                movieTask.execute(utils.Criteria.popular);
            }
        }

        public class MovieListParser {
            private ArrayList<MovieInfo> movieList = null;

            public ArrayList<MovieInfo> getMovieList(String response, int count) throws JSONException {
                movieList = new ArrayList<MovieInfo>();

                JSONObject json_response = new JSONObject(response);
                JSONArray results = json_response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++){
                    JSONObject movie_json = results.getJSONObject(i);
                    MovieInfo movieInfo = new MovieInfo();
                    movieInfo.id = movie_json.getString("id");
                    movieInfo.posterPath = movie_json.getString("poster_path");
                    movieInfo.originalTitle = movie_json.getString("original_title");
                    movieInfo.overview = movie_json.getString("overview");
                    movieInfo.userRating = movie_json.getString("vote_average");
                    movieInfo.releaseDate = movie_json.getString("release_date");
                    movieList.add(movieInfo);
                }

                return movieList;
            }
        }

        // Get poster example
        // http://image.tmdb.org/t/p/w185/811DjJTon9gD6hZ8nCjSitaIXFQ.jpg?api_key=91aac8f2720c38e2a19de85f21271430
        // Get movie metadata example
        // https://api.themoviedb.org/3/movie/297761?api_key=91aac8f2720c38e2a19de85f21271430
        // Get more best popularity
        // https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=91aac8f2720c38e2a19de85f21271430&page=1
        // get best vote average
        // https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=91aac8f2720c38e2a19de85f21271430&page=1

        public class MovieRequestTask extends AsyncTask<Enum, Void, ArrayList<MovieInfo>> {
            final String LOG_TAG = "MovieRequestTask";

            @Override
            protected ArrayList<MovieInfo> doInBackground(Enum... params) {
                int result = 5;

                Enum enum_criteria = (utils.Criteria) params[0];
                String criteria;
                if(enum_criteria == utils.Criteria.popular){
                    criteria = "popularity.desc";
                }else{
                    criteria = "vote_average.desc";
                }

                HttpURLConnection urlConnection = null;
                URL url = null;
                BufferedReader reader = null;

                SharedPreferences pref = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());

                String sort_by = pref.getString(getString(R.string.pref_sorting_key), "");
                Log.d(LOG_TAG, sort_by);

                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String SORT_BY = "sort_by";
                final String PAGE = "page";
                Uri current_uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, criteria)
                        .appendQueryParameter(PAGE, "1")
                        .appendQueryParameter("api_key", utils.TMDB_API_KEY)
                        .build();
                try {
                    url = new URL(current_uri.toString());
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }

                InputStream inputStream = null;
                StringBuffer buffer = new StringBuffer();

                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();

                    if (inputStream == null) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    //TODO: why is buffer.length zero?
//                if (buffer.length() == 0) {
//                    return null;
//                }
                }catch (IOException e){
                    e.printStackTrace();
                }

                MovieListParser movieParser = new MovieListParser();
                ArrayList<MovieInfo> movieList = null;
                try {
                    movieList = movieParser.getMovieList(buffer.toString(), 10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return movieList;
            }

            protected void onPostExecute(ArrayList<MovieInfo> result){
                Log.d(LOG_TAG, "onPostExecute");

                MovieGridAdapter movieGridGridAdapter = new MovieGridAdapter(getActivity(), result);
                gridView.setAdapter(movieGridGridAdapter);

                Toast toast = Toast.makeText(getContext(), "teste",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
