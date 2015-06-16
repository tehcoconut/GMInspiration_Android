package com.gminspiration.mobileapi;

import android.util.Log;

import org.apache.http.params.HttpParams;

/**
 * Created by tehcoconut on 5/31/15.
 */
public class GMIConnection implements HTTPRequestCallback{

    public static final String TAG = "GMI-GMIConnection";

    public static final int SORT_RELEVANCE=0;
    public static final int SORT_RATING=1;
    public static final int SORT_SUBMITDATE=2;

    public static final int SEARCH_QUERY=0;
    public static final int VIEW_CONTRIBUTION_QUERY=1;
    public static final int HOT_CONTRIBUTIONS_QUERY=2;
    public static final int GAME_LIST_QUERY=3;


    public static final String SEARCH_PATH="http://gminspiration.com/zac/mobile_api/search-results.php";
    public static final String VIEW_CONTRIBUTION_PATH = "http://gminspiration.com/zac/mobile_api/view-contribution.php";
    public static final String HOT_CONTRIBUTION_PATH = "http://gminspiration.com/zac/mobile_api/hot-contributions.php";
    public static final String GAME_LIST_PATH = "http://gminspiration.com/zac/mobile_api/list-games.php";


    String api_key = "";
    GMIQueryCallback searchListener;
    GMIQueryCallback viewcontriListener;
    GMIQueryCallback hotcontriListener;
    GMIQueryCallback gamelistListener;


    public GMIConnection(String api_key){
        this.api_key = api_key;
    }

    public void searchQuery(String keywords, int sort, int offset, String type, String game, GMIQueryCallback listener){
        this.searchListener = listener;

        BackgroundHTTPRequest searchRequest = new BackgroundHTTPRequest(SEARCH_PATH, SEARCH_QUERY, this);
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
                Log.d(TAG, "Invalid sort identifier, defaulting to SORT_RELEVANCE");
                searchRequest.addGETParam("csort", "relevance");
        }
        if(!type.contentEquals("") && !type.contentEquals("All")){
            searchRequest.addGETParam("type", type);
        }
        if(!game.contentEquals("") && !game.contentEquals("All")){
            searchRequest.addGETParam("game", game);
        }
        searchRequest.addGETParam("offset", ""+offset);
        searchRequest.addGETParam("api_key", api_key);

        searchRequest.execute();

    }

    public void getViewContribution(long id, GMIQueryCallback listener){
        this.viewcontriListener = listener;

        BackgroundHTTPRequest viewcontriRequest = new BackgroundHTTPRequest(VIEW_CONTRIBUTION_PATH, VIEW_CONTRIBUTION_QUERY, this);
        viewcontriRequest.addGETParam("contid", ""+id);
        viewcontriRequest.addGETParam("api_key", api_key);

        viewcontriRequest.execute();

    }

    public void getHotContributions(GMIQueryCallback listener) {
        this.hotcontriListener = listener;
        BackgroundHTTPRequest hotcontriRequest = new BackgroundHTTPRequest(HOT_CONTRIBUTION_PATH, HOT_CONTRIBUTIONS_QUERY, this);
        hotcontriRequest.addGETParam("api_key", api_key);

        hotcontriRequest.execute();
    }

    public void getGameList(GMIQueryCallback listener){
        this.gamelistListener = listener;
        BackgroundHTTPRequest gamelistRequest = new BackgroundHTTPRequest(GAME_LIST_PATH, GAME_LIST_QUERY, this);
        gamelistRequest.addGETParam("api_key", api_key);

        gamelistRequest.execute();
    }

    @Override
    public void onRequestCompleted(String results, int requestID) {
        switch(requestID){
            case SEARCH_QUERY:
                if(searchListener != null) {
                    searchListener.onRequestCompleted(results, requestID);
                    searchListener = null;
                }else{
                    Log.d(TAG, "Unable to find search query callback, reference is null");
                }
                break;

            case VIEW_CONTRIBUTION_QUERY:
                if(viewcontriListener != null){
                    viewcontriListener.onRequestCompleted(results, requestID);
                    viewcontriListener = null;
                }else{
                    Log.d(TAG, "Unable to find view contribution callback, reference is null");
                }
                break;

            case HOT_CONTRIBUTIONS_QUERY:
                if(hotcontriListener != null){
                    hotcontriListener.onRequestCompleted(results, requestID);
                    hotcontriListener = null;
                }else{
                    Log.d(TAG, "Unable to find hot contribution callback, reference is null");
                }
                break;

            case GAME_LIST_QUERY:
                if(gamelistListener != null){
                    gamelistListener.onRequestCompleted(results, requestID);
                    gamelistListener = null;
                }else{
                    Log.d(TAG, "Unable to find game list callback, reference is null");
                }
                break;

            default:
                Log.d(TAG, "Unknown requestID");
        }
    }

    @Override
    public void onProgressUpdate( int requestID, Integer... progress) {
        switch(requestID){
            case SEARCH_QUERY:
                if(searchListener != null) {
                    searchListener.onProgressUpdate(requestID, progress);
                }else{
                    Log.d(TAG, "Unable to find search query callback, reference is null");
                }
                break;

            case VIEW_CONTRIBUTION_QUERY:
                if(viewcontriListener != null){
                    searchListener.onProgressUpdate(requestID, progress);
                }else{
                    Log.d(TAG, "Unable to find view contribution callback, reference is null");
                }
                break;

            case HOT_CONTRIBUTIONS_QUERY:
                if(hotcontriListener != null){
                    hotcontriListener.onProgressUpdate(requestID, progress);
                }else{
                    Log.d(TAG, "Unable to find hot contribution callback, reference is null");
                }
                break;

            case GAME_LIST_QUERY:
                if(gamelistListener != null){
                    gamelistListener.onProgressUpdate(requestID, progress);
                }else{
                    Log.d(TAG, "Unable to find game list callback, reference is null");
                }
                break;

            default:
                Log.d(TAG, "Unknown requestID");
        }
    }
}
