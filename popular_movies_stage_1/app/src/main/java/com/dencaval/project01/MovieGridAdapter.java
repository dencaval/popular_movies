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

import com.dencaval.project01.model.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by denis on 06/09/2016.
 */
public class MovieGridAdapter extends BaseAdapter {
    private Context context;
    ArrayList<MovieInfo> result;

    public MovieGridAdapter(Context c, ArrayList<MovieInfo> r) {
        context = c;
        result = r;
    }
    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(result.get(i).id);
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
        String url = BASE_URL + result.get(i).posterPath;
        Uri current_uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", utils.TMDB_API_KEY).build();

        // Picasso already loads image in background
        Picasso.with(((Activity) context)).load(current_uri.toString()).error(R.mipmap.ic_no_image).into(imageView);
        return imageView;
    }
}
