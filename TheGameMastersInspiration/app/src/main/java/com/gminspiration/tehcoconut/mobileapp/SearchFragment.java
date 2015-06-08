package com.gminspiration.tehcoconut.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gminspiration.mobileapi.GMIConnection;
import com.gminspiration.mobileapi.GMIQueryCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tehcoconut on 5/30/15.
 */
public class SearchFragment extends Fragment implements GMIQueryCallback, AbsListView.OnScrollListener{

    public static final int PAGE_SIZE = 10;

    TextView tv_search;
    LinearLayout ll_search;
    ListView lv_search;
    ProgressBar pb_loadMore;
    Context context;

    String query;
    int sort, offset;

    private GMIConnection gmic;

    private MySearchListAdapter adapter;

    private ArrayList<String> imgs, names, usernames, games, joined;
    private ArrayList<Double> avg_funs, avg_bals;
    private ArrayList<Integer> ids, privacys;

    private boolean flag_loading;
    private boolean triedAndFailed;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        tv_search = (TextView) v.findViewById(R.id.tv_search);
        ll_search = (LinearLayout) v.findViewById(R.id.ll_searchContainer);
        lv_search = (ListView) v.findViewById(R.id.lv_search);

        flag_loading = true;

        triedAndFailed = false;

        // This is in case android kills the fragment before we go back to it
        if(savedInstanceState != null){

            names = savedInstanceState.getStringArrayList("names");
            games = savedInstanceState.getStringArrayList("games");
            imgs = savedInstanceState.getStringArrayList("imgs");
            usernames = savedInstanceState.getStringArrayList("usernames");
            joined = savedInstanceState.getStringArrayList("joined");
            avg_funs = (ArrayList<Double>) savedInstanceState.get("avg_funs");
            avg_bals = (ArrayList<Double>) savedInstanceState.get("avg_bals");
            ids = savedInstanceState.getIntegerArrayList("ids");
            privacys = savedInstanceState.getIntegerArrayList("privacys");

            ll_search.findViewById(R.id.pb_search_list_loading).setVisibility(View.GONE);

            adapter = new MySearchListAdapter(context, ids, privacys, imgs, names, avg_funs, avg_bals, usernames, games, joined);
            lv_search.setAdapter(adapter);
            lv_search.setOnScrollListener(this);

        }

        // When the fragment becomes visible from the backstack,
        // android keeps the object, but will call this function again.
        // So when that happens we need to set the adapter again, since
        // the GMI callback wont be called again.
        if(usernames != null){
            ll_search.findViewById(R.id.pb_search_list_loading).setVisibility(View.GONE);

            lv_search.setAdapter(adapter);
            lv_search.setOnScrollListener(this);

        }


        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < usernames.size()) {   // if the user doesnt click on the loading icon
                    ViewContributionFragment frag = new ViewContributionFragment();
                    frag.setContext(context);
                    frag.setConnection(gmic);
                    gmic.getViewContribution(id, frag);
                    ((MainActivity) context).openFragment(frag);
                }
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("names", names);
        outState.putStringArrayList("games", games);
        outState.putStringArrayList("imgs", imgs);
        outState.putStringArrayList("usernames", usernames);
        outState.putStringArrayList("joined", joined);
        outState.putSerializable("avg_funs", avg_funs);
        outState.putSerializable("avg_bals", avg_bals);
        outState.putIntegerArrayList("ids", ids);
        outState.putIntegerArrayList("privacys", privacys);
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setConnection(GMIConnection gmic){
        this.gmic = gmic;
    }

    public void setQuerySortOffset(String query, int sort, int offset){
        this.query = query;
        this.sort = sort;
        this.offset = offset;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount != 0){
            if(!flag_loading && !triedAndFailed){
                flag_loading = true;
                adapter.setLoadingMore(true);
                offset+=PAGE_SIZE;
                pb_loadMore = new ProgressBar(context);
                pb_loadMore.setIndeterminate(true);
                ll_search.addView(pb_loadMore);
                gmic.searchQuery(query, sort, offset, new LoadMoreCallback());
            }
            if(triedAndFailed){
                adapter.setLoadingMore(false);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onProgressUpdate(int requestID, Integer... progress) {

    }

    @Override
    public void onRequestCompleted(String results, int requestID) {


        String name = "Name not found";

        ll_search.findViewById(R.id.pb_search_list_loading).setVisibility(View.GONE);

        try {
            if(!results.contentEquals("null")) {
                JSONArray jsonArr = new JSONArray(results);
                imgs = new ArrayList<String>();
                names = new ArrayList<String>();
                usernames = new ArrayList<String>();
                games = new ArrayList<String>();
                joined = new ArrayList<String>();

                avg_funs = new ArrayList<Double>();
                avg_bals = new ArrayList<Double>();

                ids = new ArrayList<Integer>();
                privacys = new ArrayList<Integer>();

                if (jsonArr.length() > 0) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        collectResult(jsonObj);
                    }

                    adapter = new MySearchListAdapter(context, ids, privacys, imgs, names, avg_funs, avg_bals, usernames, games, joined);
                    lv_search.setAdapter(adapter);
                    lv_search.setOnScrollListener(this);

                }
            }

        }catch(JSONException e){
            e.printStackTrace();
            tv_search.setText("Error: unable to parse JSON");
        }

        flag_loading = false;



    }

    private void collectResult(JSONObject jsonObj) throws JSONException{
        int id = jsonObj.optInt("id");
        ids.add(id);

        String img = jsonObj.optString("img");
        imgs.add(img);

        String join = jsonObj.optString("joined");
        joined.add(join);

        String username = jsonObj.optString("username");
        usernames.add(username);

        String name = jsonObj.optString("name").trim();
        String type = jsonObj.optString("type").trim();
        String sub_type = jsonObj.optString("sub_type").trim();
        String ntst;
        if(!sub_type.contentEquals("null") && !sub_type.isEmpty())
            ntst = name + " - " + type + " (" + sub_type + ")";
        else
            ntst = name + " - " + type;
        names.add(ntst);

        String game = jsonObj.optString("game");
        games.add(game);



        double avg_fun = jsonObj.optDouble("avg_fun");
        avg_funs.add(avg_fun);

        double avg_balance = jsonObj.optDouble("avg_balance");
        avg_bals.add(avg_balance);

        int privacy = jsonObj.optInt("privacy");
        privacys.add(privacy);


    }

    private class LoadMoreCallback implements GMIQueryCallback{
        @Override
        public void onProgressUpdate(int requestID, Integer... progress) {

        }

        @Override
        public void onRequestCompleted(String results, int requestID) {
            try {

                if(!results.contentEquals("null")) {
                    JSONArray jsonArr = new JSONArray(results);
                    int sizebefore = usernames.size();

                    if (jsonArr.length() > 0) {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            collectResult(jsonObj);
                        }

                        int sizeafter = usernames.size();
                        if(sizeafter - sizebefore <= 0){
                            triedAndFailed = true;
                            adapter.setLoadingMore(false);
                        }
                        //adapter = new MySearchListAdapter(context, ids, privacys, imgs, names, avg_funs, avg_bals, usernames, games, joined);
                        //lv_search.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }else{
                    triedAndFailed = true;
                    adapter.setLoadingMore(false);
                    adapter.notifyDataSetChanged();
                }

            }catch(JSONException e){
                e.printStackTrace();
                tv_search.setText("Error: unable to parse JSON");
            }

            flag_loading = false;

        }
    }

}
