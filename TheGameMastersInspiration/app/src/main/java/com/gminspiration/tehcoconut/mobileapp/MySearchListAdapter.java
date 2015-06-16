package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gminspiration.mobileapi.DownloadAsyncImage;
import com.gminspiration.mobileapi.ImageDownloadCallback;
import com.gminspiration.mobileapi.ImageViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by tehcoconut on 6/4/15.
 */
public class MySearchListAdapter extends BaseAdapter implements ImageDownloadCallback{

/*    private String imageURLs[], nameTypeSubtype[], users[], game[], joined[];
    private double fun[], bal[];
    private int ids[], privacys[];
  */

    public static final String TAG = "GMI-SearchListAdapter";

    private ArrayList<String> imageURLs, nameTypeSubtype, users, game, joined;
    private ArrayList<Double> fun, bal;
    private ArrayList<Integer> ids, privacys;
    private ArrayList<Bitmap> bitmaps;

    private Context context;

    private Bitmap default_bitmap;

    private LayoutInflater inflater;
    private View rowView = null;

    private Boolean loadingMore;
    private Boolean loaderFlag;

    private SharedPreferences sharedPref;


    public MySearchListAdapter(Context context, ArrayList<Integer> ids, ArrayList<Integer> privacys, ArrayList<String> imageURLs, ArrayList<String> nameTypeSubtype,
                               ArrayList<Double> fun, ArrayList<Double> bal, ArrayList<String> users, ArrayList<String> game, ArrayList<String> joined){
        this.context = context;
        this.ids = ids;
        this.privacys = privacys;
        this.imageURLs = imageURLs;
        this.nameTypeSubtype = nameTypeSubtype;
        this.fun = fun;
        this.bal = bal;
        this.users = users;
        this.game = game;

        this.ids = ids;
        this.joined = joined;

        bitmaps = new ArrayList<Bitmap>();

        default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_search);

        loadingMore = true;
        loaderFlag = true;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        System.gc();
    }

    public void setLoaderFlag(boolean flag){
        this.loaderFlag = flag;
    }

    public void setLoadingMore(Boolean loadingMore){
        this.loadingMore = loadingMore;
    }

    @Override
    public int getCount() {
        if(loaderFlag)
            return nameTypeSubtype.size()+1;
        else
            return nameTypeSubtype.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(ids.size() > position)
            return ids.get(position);
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int asdf = position;

        Boolean imagesDisabled = sharedPref.getBoolean("pref_search_images", false);

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(game.size() == position){
            rowView = inflater.inflate(R.layout.list_item_loadmore, parent, false);
            ProgressBar pb = (ProgressBar) rowView.findViewById(R.id.pb_search_loadmore);
            if(!loadingMore)
                pb.setVisibility(View.INVISIBLE);
            else
                pb.setVisibility(View.VISIBLE);
            rowView.setTag(R.id.TAG_SEARCH_ITEM_TYPE, "LOADING");
            rowView.setTag(R.id.TAG_VIEW_HOLDER, "NULL ADDRESS");
            return rowView;
        }


        if(game.get(position) == null || game.get(position).contentEquals("")){ // This result must be a user then
            if(convertView == null) {
                rowView = inflater.inflate(R.layout.list_item_search_user, parent, false);
                rowView.setTag(R.id.TAG_SEARCH_ITEM_TYPE, "USER");
            }else if(convertView.getTag(R.id.TAG_SEARCH_ITEM_TYPE).equals("USER")){
                rowView = convertView;
            }else{
                rowView = inflater.inflate(R.layout.list_item_search_user, parent, false);
                rowView.setTag(R.id.TAG_SEARCH_ITEM_TYPE, "USER");
            }

            ImageView iv_profile = (ImageView) rowView.findViewById(R.id.iv_search_user_list_item);
            ProgressBar pb_loading = (ProgressBar) rowView.findViewById(R.id.pb_search_user_image_loading);
            TextView tv_username = (TextView) rowView.findViewById(R.id.tv_search_username);
            TextView tv_joined = (TextView) rowView.findViewById(R.id.tv_search_user_joined);


            tv_username.setText(users.get(position));
            tv_joined.setText("user since " + joined.get(position).trim());

            if (!imagesDisabled) {
                if (bitmaps.size() <= position) {
                    pb_loading.setVisibility(View.VISIBLE);
                    iv_profile.setImageResource(R.drawable.ic_action_search);
                    DownloadAsyncImage downloader = new DownloadAsyncImage(this, position);
                    ImageViewHolder holder = new ImageViewHolder();
                    holder.setImageView(iv_profile);
                    holder.setURL(imageURLs.get(position));
                    holder.setPosition(position);
                    holder.setProgBar((ProgressBar) rowView.findViewById(R.id.pb_search_user_image_loading));
                    rowView.setTag(R.id.TAG_VIEW_HOLDER, imageURLs.get(position));
                    holder.setRowView(rowView);
                    downloader.execute(holder);
                } else {
                    iv_profile.setImageBitmap(Bitmap.createScaledBitmap(bitmaps.get(position), 120, 120, false));
                    rowView.setTag(R.id.TAG_VIEW_HOLDER, imageURLs.get(position));
                    pb_loading.setVisibility(View.GONE);
                }
            }else{
                pb_loading.setVisibility(View.GONE);
                iv_profile.setVisibility(View.GONE);
            }

        }else if (users.get(position) != null || !users.get(position).contentEquals("")){
            if(convertView == null) {
                rowView = inflater.inflate(R.layout.list_item_search, parent, false);
                rowView.setTag(R.id.TAG_SEARCH_ITEM_TYPE, "CONTRIBUTION");
            }else if(convertView.getTag(R.id.TAG_SEARCH_ITEM_TYPE).equals("CONTRIBUTION")){
                rowView = convertView;
                //Log.d(TAG, (String) convertView.getTag(R.id.TAG_SEARCH_ITEM_TYPE));
            }else{
                rowView = inflater.inflate(R.layout.list_item_search, parent, false);
                rowView.setTag(R.id.TAG_SEARCH_ITEM_TYPE, "CONTRIBUTION");
            }

            TextView tv_name = (TextView) rowView.findViewById(R.id.tv_search_name);
            TextView tv_user = (TextView) rowView.findViewById(R.id.tv_search_submittedby);
            TextView tv_game = (TextView) rowView.findViewById(R.id.tv_search_game);
            ImageView iv_search = (ImageView) rowView.findViewById(R.id.iv_search_list_item);

            ProgressBar pb_image = (ProgressBar) rowView.findViewById(R.id.pb_search_image_loading);

            TextView tv_fun = (TextView) rowView.findViewById(R.id.tv_search_rating_fun);
            TextView tv_bal = (TextView) rowView.findViewById(R.id.tv_search_rating_bal);
            RatingBar rb_fun = (RatingBar) rowView.findViewById(R.id.rb_search_fun);
            RatingBar rb_bal = (RatingBar) rowView.findViewById(R.id.rb_search_bal);

            tv_name.setText(nameTypeSubtype.get(position));
            tv_user.setText("submitted by " + users.get(position));
            tv_game.setText(game.get(position));

            if (fun.get(position) >= 1) {
                tv_fun.setText("Fun");
                tv_bal.setVisibility(View.VISIBLE);
                rb_bal.setVisibility(View.VISIBLE);
                rb_fun.setVisibility(View.VISIBLE);
                rb_fun.setRating((float)(double) fun.get(position));
                rb_bal.setRating((float)(double) bal.get(position));
            } else {
                tv_fun.setText("Not Yet Rated");
                tv_bal.setVisibility(View.GONE);
                rb_bal.setVisibility(View.GONE);
                rb_fun.setVisibility(View.GONE);
            }

            if (!imagesDisabled) {
                if (bitmaps.size() <= position) {
                    pb_image.setVisibility(View.VISIBLE);
                    iv_search.setImageResource(R.drawable.ic_action_search);
                    DownloadAsyncImage downloader = new DownloadAsyncImage(this, position);
                    ImageViewHolder holder = new ImageViewHolder();
                    holder.setImageView(iv_search);
                    holder.setURL(imageURLs.get(position));
                    holder.setPosition(position);
                    holder.setProgBar((ProgressBar) rowView.findViewById(R.id.pb_search_image_loading));
                    rowView.setTag(R.id.TAG_VIEW_HOLDER, imageURLs.get(position));
                    holder.setRowView(rowView);
                    downloader.execute(holder);
                } else {
                    iv_search.setImageBitmap(Bitmap.createScaledBitmap(bitmaps.get(position), 120, 120, false));
                    rowView.setTag(R.id.TAG_VIEW_HOLDER, imageURLs.get(position));
                    pb_image.setVisibility(View.GONE);
                }
            }else{
                pb_image.setVisibility(View.GONE);
                iv_search.setVisibility(View.GONE);
            }

        }

        return rowView;
    }

    @Override
    public void onImageDownloaded(ImageViewHolder result, int originalPosition) {

        result.setProgressBarVisibility(View.GONE);

        if (result.getBitmap() == null) {
            Log.d(TAG, (String) result.getRowView().getTag(R.id.TAG_VIEW_HOLDER));
            if(result.getURL().contentEquals((String) result.getRowView().getTag(R.id.TAG_VIEW_HOLDER))) {
                result.getImageView().setImageResource(R.drawable.ic_action_search);
                //result.getRowView().setTag(R.id.TAG_VIEW_HOLDER, null);
                //result.setRowView(null);
            }
            addBitmapAtIndex(result.getPosition(), default_bitmap);
        } else {
            Log.d(TAG, (String) result.getRowView().getTag(R.id.TAG_VIEW_HOLDER));
            if(result.getURL().contentEquals((String) result.getRowView().getTag(R.id.TAG_VIEW_HOLDER))) {
                result.getImageView().setImageBitmap(Bitmap.createScaledBitmap(result.getBitmap(), 120, 120, false));
                //result.getRowView().setTag(R.id.TAG_VIEW_HOLDER, null);
                                //result.setRowView(null);
            }
            addBitmapAtIndex(result.getPosition(), result.getBitmap());
        }
    }

    public void addBitmapAtIndex(int index, Bitmap bitmap){
        if(bitmaps.size() <= index){
            int diff = (index-bitmaps.size());
            for(int i =0; i<diff; i++){
                bitmaps.add(default_bitmap);
            }
            bitmaps.add(bitmap);
        }else{
            bitmaps.set(index, bitmap);
        }
    }
}

