package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gminspiration.mobileapi.GMIConnection;
import com.gminspiration.mobileapi.GMIQueryCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tehcoconut on 5/30/15.
 */
public class HomeFragment extends Fragment implements GMIQueryCallback{

    String[] listItems = {"All","Armor","Classes","Feats","Items","Monsters","Races","Spells","Weapons"};

    Context context;

    private MySearchListAdapter adapter;
    private GMIConnection gmic;

    private ListView lv_hotcontri;
    private ListView lv_categories;

    private ArrayList<String> imgs, names, usernames, games, joined;
    private ArrayList<Double> avg_funs, avg_bals;
    private ArrayList<Integer> ids, privacys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        lv_hotcontri = (ListView) v.findViewById(R.id.lv_home_hotcontri);
        lv_categories = (ListView) v.findViewById(R.id.lv_home_categories);

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

            adapter = new MySearchListAdapter(context, ids, privacys, imgs, names, avg_funs, avg_bals, usernames, games, joined);
            adapter.setLoaderFlag(false);
            lv_hotcontri.setAdapter(adapter);

        }

        if(usernames != null){
            lv_hotcontri.setAdapter(adapter);

        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_item_categories, listItems);
        //lv_hotcontri.setAdapter(arrayAdapter);

        lv_categories.setAdapter(arrayAdapter);

        //setListViewHeightWrapItems(lv_hotcontri);
        setListViewHeightWrapItems(lv_categories, false);


        lv_hotcontri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < usernames.size()) {   // if the user doesnt click on the loading icon
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



    public void setContext(Context context){
        this.context = context;
    }

    public void setConnection(GMIConnection gmic){
        this.gmic = gmic;
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

    @Override
    public void onProgressUpdate(int requestID, Integer... progress) {

    }

    @Override
    public void onRequestCompleted(String results, int requestID) {


        String name = "Name not found";

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
                    adapter.setLoaderFlag(false);

                    lv_hotcontri.setAdapter(adapter);


                }
            }

        }catch(JSONException e){
            e.printStackTrace();
        }


    }

    private void collectResult(JSONObject jsonObj) throws JSONException{
        int id = jsonObj.optInt("id");
        ids.add(id);

        String img = jsonObj.optString("img");
        imgs.add(img);


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

        privacys.add(0);


    }

    public void setListViewHeightWrapItems(ListView lv, Boolean isSearchList) {
        ListAdapter listAdapter = lv.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lv);
            if(isSearchList) {
                Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                LinearLayout ll = (LinearLayout) listItem.findViewById(R.id.ll_search_list_item);
                int listViewWidth = size.x ;
                ll.measure(View.MeasureSpec.makeMeasureSpec((int) listViewWidth, View.MeasureSpec.AT_MOST), 0);
                totalHeight += ll.getMeasuredHeight();
            }else{
                listItem.measure(0,0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
        lv.setLayoutParams(params);
        lv.requestLayout();

    }


}
