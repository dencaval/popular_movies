package com.dencaval.project01;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView originalTitle = (TextView) findViewById(R.id.originalTitle);
        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        ImageView imageViewPosterPath = (ImageView) findViewById(R.id.posterPath);
        TextView userRating = (TextView) findViewById(R.id.userRating);
        TextView overview = (TextView) findViewById(R.id.overview);

        Intent movieDetailsIntent = getIntent();
        originalTitle.setText(movieDetailsIntent.getStringExtra("originalTitle"));
        releaseDate.setText(movieDetailsIntent.getStringExtra("releaseDate"));
        userRating.setText(movieDetailsIntent.getStringExtra("userRating"));
        overview.setText(movieDetailsIntent.getStringExtra("overview"));

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";
        String url = BASE_URL + movieDetailsIntent.getStringExtra("posterPath");
        Uri current_uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", Utils.TMDB_API_KEY).build();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenWidth = (int)(screenWidth *0.33);
        int halfScreenHeight = (int) (screenHeight * 0.33);

        Picasso.with(this).load(current_uri.toString()).into(imageViewPosterPath);
    }
}
