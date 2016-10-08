package com.dencaval.project01;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dencaval.project01.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_details);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent movieDetailsIntent = getIntent();
        binding.originalTitle.setText(movieDetailsIntent.getStringExtra("originalTitle"));
        binding.releaseDate.setText(movieDetailsIntent.getStringExtra("releaseDate"));
        binding.userRating.setText(movieDetailsIntent.getStringExtra("userRating"));
        binding.overview.setText(movieDetailsIntent.getStringExtra("overview"));

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

        Picasso.with(this).load(current_uri.toString()).into(binding.posterPath);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
