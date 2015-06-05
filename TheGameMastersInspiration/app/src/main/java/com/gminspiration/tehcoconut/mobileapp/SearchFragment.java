package com.gminspiration.tehcoconut.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gminspiration.mobileapi.GMIQueryCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tehcoconut on 5/30/15.
 */
public class SearchFragment extends Fragment implements GMIQueryCallback{

    TextView tv_search;
    //LinearLayout ll_search;
    ListView lv_search;
    Context context;

    String[] imgs, names, games, usernames;
    double[] avg_funs, avg_bals;
    int[] ids, privacys;

    private int resultcount = 0;

    public void setContext(Context context){
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        tv_search = (TextView) v.findViewById(R.id.tv_search);
        //ll_search = (LinearLayout) v.findViewById(R.id.ll_search);
        lv_search = (ListView) v.findViewById(R.id.lv_search);

        return v;
    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onRequestCompleted(String results) {

        String name = "Name not found";

        try {
            if(!results.contentEquals("null")) {
                JSONArray jsonArr = new JSONArray(results);
                imgs = new String[jsonArr.length()];
                names = new String[jsonArr.length()];
                games = new String[jsonArr.length()];
                usernames = new String[jsonArr.length()];
                avg_funs = new double[jsonArr.length()];
                avg_bals = new double[jsonArr.length()];
                ids = new int[jsonArr.length()];
                privacys = new int[jsonArr.length()];

                if (ids.length > 0) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        collectResult(jsonObj);
                    }

                    MySearchListAdapter adapter = new MySearchListAdapter(context, ids, privacys, imgs, names, avg_funs, avg_bals, usernames, games);
                    lv_search.setAdapter(adapter);
                }
            }

        }catch(JSONException e){
            e.printStackTrace();
            tv_search.setText("Error: unable to parse JSON");
        }



    }

    private void collectResult(JSONObject jsonObj) throws JSONException{
        int id = jsonObj.getInt("id");
        ids[resultcount] = id;

        String img = jsonObj.getString("img");
        imgs[resultcount] = img;

        String name = jsonObj.getString("name").trim();
        String type = jsonObj.getString("type").trim();
        String sub_type = jsonObj.getString("sub_type").trim();
        String ntst;
        if(!sub_type.contentEquals("null") && !sub_type.isEmpty())
            ntst = name + " - " + type + " (" + sub_type + ")";
        else
            ntst = name + " - " + type;
        names[resultcount] = ntst;

        String game = jsonObj.getString("game");
        games[resultcount] = game;

        String username = jsonObj.getString("username");
        usernames[resultcount] = username;

        double avg_fun = jsonObj.getDouble("avg_fun");
        avg_funs[resultcount] = avg_fun;

        double avg_balance = jsonObj.getDouble("avg_balance");
        avg_bals[resultcount] = avg_balance;

        int privacy = jsonObj.getInt("privacy");
        privacys[resultcount] = privacy;

        resultcount++;

    }
}
