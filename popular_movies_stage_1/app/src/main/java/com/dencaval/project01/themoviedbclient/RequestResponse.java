package com.dencaval.project01.themoviedbclient;

import java.util.ArrayList;

/**
 * Created by denis on 07/09/2016.
 */
public class RequestResponse {
    private int total_pages;
    private int total_results;
    private int current_page;
    private ArrayList<MovieInfo> movie_info_list;

    public RequestResponse(){
        int total_pages = 0;
        int total_results = 0;
        int current_page = 0;
        movie_info_list = new ArrayList<MovieInfo>();
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public ArrayList<MovieInfo> getMovie_info_list() {
        return movie_info_list;
    }

    public void setMovie_info_list(ArrayList<MovieInfo> movie_info_list) {
        this.movie_info_list = movie_info_list;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
}
