package com.gminspiration.mobileapi;

import org.apache.http.params.HttpParams;

/**
 * Created by tehcoconut on 5/31/15.
 */
public class GMIConnection implements HTTPRequestCallback{
    public static final int SORT_RELEVANCE=0;
    public static final int SORT_RATING=1;
    public static final int SORT_SUBMITDATE=2;


    public static final String SEARCH_PATH="http://gminspiration.com/zac/mobile_api/search-results.php";


    String api_key = "";
    GMIQueryCallback listener;


    public GMIConnection(String api_key){
        this.api_key = api_key;
    }

    public void searchQuery(String keywords, int sort, GMIQueryCallback listener){
        this.listener = listener;

        BackgroundHTTPRequest searchRequest = new BackgroundHTTPRequest(SEARCH_PATH, this);
        searchRequest.addGETParam("keywords", keywords);
        switch (sort) {
            case SORT_RELEVANCE:
                searchRequest.addGETParam("csort", "relevance");
                break;
            case SORT_RATING:
                searchRequest.addGETParam("csort", "rating");
                break;
            case SORT_SUBMITDATE:
                searchRequest.addGETParam("csort", "submitdate");
                break;
            default:
                searchRequest.addGETParam("csort", "relevance");
        }
        searchRequest.addGETParam("api_key", api_key);

        searchRequest.execute();

    }

    @Override
    public void onRequestCompleted(String results) {
        listener.onRequestCompleted(results);
        listener = null;
    }

    @Override
    public void onProgressUpdate(Integer... progress) {
        listener.onProgressUpdate(progress);
    }
}
