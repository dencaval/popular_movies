package com.dencaval.project01.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.dencaval.project01.MovieGridAdapter;
import com.dencaval.project01.R;
import com.dencaval.project01.model.MovieInfo;
import com.dencaval.project01.utils;

import org.json.JSONException;

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
    Context context;
    GridView gridView;

    public MovieRequestTask(Context c, GridView gv){
        context = c;
        gridView = gv;
    }

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

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//
//        String sort_by = pref.getString(getString(R.string.pref_sorting_key), "");

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

        ArrayList<MovieInfo> movieList = null;
        try {
            movieList = utils.getMovieList(buffer.toString(), 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    protected void onPostExecute(ArrayList<MovieInfo> result){
        MovieGridAdapter movieGridGridAdapter = new MovieGridAdapter((Activity)context, result);
        gridView.setAdapter(movieGridGridAdapter);

        Toast toast = Toast.makeText(context, "teste",
                Toast.LENGTH_LONG);
        toast.show();
    }
}