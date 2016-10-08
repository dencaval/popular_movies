package com.dencaval.project01.ThemoviedbClient;

import com.dencaval.project01.MovieGridAdapter;

/**
 * Created by denis on 06/09/2016.
 */
public class RequestParameter {
    private String criteria;
    private int page_id;
    private MovieGridAdapter movieGridAdapter;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public MovieGridAdapter getMovieGridAdapter() {
        return movieGridAdapter;
    }

    public void setMovieGridAdapter(MovieGridAdapter movieGridAdapter) {
        this.movieGridAdapter = movieGridAdapter;
    }
}
