package com.dencaval.project01.themoviedbclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.dencaval.project01.MovieGridAdapter;
import com.dencaval.project01.R;
import com.dencaval.project01.Utils;

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
public class AsyncMoviePageRequest extends AsyncTask<RequestParameter, Void, RequestResponse> {
    Context context;
    GridView gridView;
    private ProgressDialog dialog;

    public AsyncMoviePageRequest(Context c, GridView gv){
        context = c;
        gridView = gv;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Loading data");
        this.dialog.show();
    }

    @Override
    protected RequestResponse doInBackground(RequestParameter... params) {
        String criteria = (String) params[0].getCriteria();
        String movie_list_page = String.valueOf(params[0].getPage_id());

        String use_criteria = "";
        if(criteria.equals(Utils.CRITERIA_POPULAR)){
            use_criteria = "popular";
        }else{
            use_criteria = "top_rated";
        }

        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedReader reader = null;

        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String SORT_BY = "sort_by";
        final String PAGE = "page";
        Uri current_uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(use_criteria)
                .appendQueryParameter(PAGE, movie_list_page)
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY)
                .build();
        try {
            url = new URL(current_uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.d("AsyncMoviePageRequest",url.toString());

        InputStream inputStream = null;
        StringBuffer buffer = new StringBuffer();

        RequestResponse resquest_response = null;

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
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }


        try {
            resquest_response = Utils.getMovieList(buffer.toString(), 10);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return resquest_response;
    }

    protected void onPostExecute(RequestResponse response){
        if( response == null){
            Toast.makeText(context,
                    context.getResources().getText(R.string.no_server_response),
                    Toast.LENGTH_LONG).show();
            return ;
        }else{
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.cancel();
            }

            MovieGridAdapter m = (MovieGridAdapter) gridView.getAdapter();
            m.append_data(response);
            m.notifyDataSetChanged();
        }
    }
}