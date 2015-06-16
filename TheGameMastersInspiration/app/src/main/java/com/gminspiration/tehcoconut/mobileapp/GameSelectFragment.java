package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gminspiration.mobileapi.GMIConnection;
import com.gminspiration.mobileapi.GMIQueryCallback;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by tehcoconut on 6/16/15.
 */
public class GameSelectFragment extends Fragment implements GMIQueryCallback, AdapterView.OnItemClickListener{

    public static final String TAG = "GMI-GameSelectFragment";

    Context context;
    GMIConnection gmic;
    String category;

    ListView lv_games;

    ArrayAdapter<String> adapter;
    String[] games;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameselect, container, false);

        lv_games = (ListView) view.findViewById(R.id.lv_gameselect);
;

        // This is in case android kills the fragment before we go back to it
        if(savedInstanceState != null){
            games = savedInstanceState.getStringArray("games");
            adapter = new ArrayAdapter<String>(context, R.layout.list_item_categories, games);
            lv_games.setAdapter(adapter);

            lv_games.setOnItemClickListener(this);
        }

        if(games != null){
            adapter = new ArrayAdapter<String>(context, R.layout.list_item_categories, games);
            lv_games.setAdapter(adapter);

            lv_games.setOnItemClickListener(this);
        }

        return view;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setConnection(GMIConnection gmic){
        this.gmic = gmic;
    }

    public void setCategory(String category){
        this.category = category;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray("games", games);
    }

    @Override
    public void onProgressUpdate(int requestID, Integer... progress) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String game = games[position];
        SearchFragment frag = new SearchFragment();
        frag.setConnection(gmic);
        frag.setContext(context);
        frag.setQuerySortOffset("", GMIConnection.SORT_RATING, 0);
        frag.setCategoryGame(category, game);
        gmic.searchQuery("", GMIConnection.SORT_RATING, 0, category, game, frag);
        ((MainActivity) context).openFragment(frag);
    }

    @Override
    public void onRequestCompleted(String results, int requestID) {
        try{
            Log.d(TAG, "RESULTS:"+results);
            JSONArray arr = new JSONArray(results);

            games = new String[arr.length()+1];
            games[0] = "All";

            for(int i =0; i<arr.length(); i++){
                games[i+1] = arr.getString(i);
            }

            adapter = new ArrayAdapter<String>(context, R.layout.list_item_categories, games);
            lv_games.setAdapter(adapter);

            lv_games.setOnItemClickListener(this);



        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
