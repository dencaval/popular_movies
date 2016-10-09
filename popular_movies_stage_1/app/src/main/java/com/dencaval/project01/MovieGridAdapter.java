package com.dencaval.project01;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.dencaval.project01.themoviedbclient.MovieInfo;
import com.dencaval.project01.themoviedbclient.RequestResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by denis on 06/09/2016.
 */
public class MovieGridAdapter extends BaseAdapter {
    private Context context;
    RequestResponse result;

    public MovieGridAdapter(Context c) {
        context = c;
        result = new RequestResponse();
    }

    public void clear_data(){
        result.getMovie_info_list().clear();
        result.setTotal_results(0);
        result.setTotal_pages(0);
    }

    public void append_data(RequestResponse new_r){
        if( new_r != null) {
            if (new_r.getCurrent_page() != result.getCurrent_page()) {
                ArrayList<MovieInfo> current = result.getMovie_info_list();
                current.addAll(new_r.getMovie_info_list());
                result.setMovie_info_list(current);
                result.setCurrent_page(new_r.getCurrent_page());
            }
        }
    }

    public int getCurrentPage(){
        return result.getCurrent_page();
    }

    @Override
    public int getCount() {
        if (result.getMovie_info_list().isEmpty()){
            return 0;
        } else {
            return result.getMovie_info_list().size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (result.getMovie_info_list().isEmpty()){return null;}
        else return result.getMovie_info_list().get(i);
    }

    @Override
    public long getItemId(int i) {
        if (result.getMovie_info_list().isEmpty()){return 0;}
        else return Long.parseLong(result.getMovie_info_list().get(i).id);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            Point size = new Point();
            ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;

            // It will show 3 movies
            int halfScreenWidth = (int)(screenWidth *0.33);
            int halfScreenHeight = (int) (screenHeight * 0.33);
            imageView.setLayoutParams(new GridView.LayoutParams(halfScreenWidth, halfScreenHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";
        String url = BASE_URL + result.getMovie_info_list().get(i).posterPath;
        Uri current_uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY).build();

        // Picasso already loads image in background
        Picasso.with(((Activity) context)).load(current_uri.toString()).error(R.mipmap.ic_no_image).into(imageView);
        return imageView;
    }
}
