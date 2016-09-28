package com.dencaval.project01.ThemoviedbClient;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.GridView;

import com.dencaval.project01.MovieGridAdapter;
import com.dencaval.project01.Utils;
import com.dencaval.project01.Utils.Criteria;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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


public class AsyncMoviePageRequest extends AsyncTask<RequestParameter, Void, RequestResponse> {
    Context context;
    GridView gridView;

    public AsyncMoviePageRequest(Context c, GridView gv){
        context = c;
        gridView = gv;
    }

    @Override
    protected RequestResponse doInBackground(RequestParameter... params) {
        Enum enum_criteria = (Criteria) params[0].getCriteria();
        String movie_list_page = String.valueOf(params[0].getPage_id());
        String criteria;
        if(enum_criteria == Criteria.popular){
            criteria = "popularity.desc";
        }else{
            criteria = "vote_average.desc";
        }

        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedReader reader = null;

        final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
        final String SORT_BY = "sort_by";
        final String PAGE = "page";
        Uri current_uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY, criteria)
                .appendQueryParameter(PAGE, movie_list_page)
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY)
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

        RequestResponse resquest_response = null;
        try {
            resquest_response = Utils.getMovieList(buffer.toString(), 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resquest_response;
    }

    protected void onPostExecute(RequestResponse response){
        MovieGridAdapter m = (MovieGridAdapter) gridView.getAdapter();
        m.append_data(response);
        m.notifyDataSetChanged();
    }
}