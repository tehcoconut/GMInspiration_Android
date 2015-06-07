package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import java.util.zip.Inflater;

/**
 * Created by tehcoconut on 6/4/15.
 */
public class MySearchListAdapter extends BaseAdapter implements ImageDownloadCallback{

    private String imageURLs[], nameTypeSubtype[], users[], game[], joined[];
    private double fun[], bal[];
    private int ids[], privacys[];

    private Context context;

    private Bitmap bitmaps[];

    private Bitmap default_bitmap;

    private ViewGroup parent;

    private LayoutInflater inflater;
    private View rowView = null;


    public MySearchListAdapter(Context context, int ids[], int[] privacys, String imageURLs[], String nameTypeSubtype[], double fun[], double bal[], String users[], String game[], String joined[]){
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

        bitmaps = new Bitmap[ids.length];

        default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_search);

        System.gc();
    }

    @Override
    public int getCount() {
        return nameTypeSubtype.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return ids[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(game[position] == null || game[position].contentEquals("")){ // This result must be a user then
            if(convertView == null) {
                rowView = inflater.inflate(R.layout.list_item_search_user, parent, false);
                rowView.setTag("USER");
            }else if(convertView.getTag().equals("USER")){
                rowView = convertView;
            }else{
                rowView = inflater.inflate(R.layout.list_item_search_user, parent, false);
                rowView.setTag("USER");
            }

            ImageView iv_profile = (ImageView) rowView.findViewById(R.id.iv_search_user_list_item);
            ProgressBar pb_loading = (ProgressBar) rowView.findViewById(R.id.pb_search_user_image_loading);
            TextView tv_username = (TextView) rowView.findViewById(R.id.tv_search_username);
            TextView tv_joined = (TextView) rowView.findViewById(R.id.tv_search_user_joined);


            tv_username.setText(users[position]);
            tv_joined.setText("user since " + joined[position].trim());

            if (bitmaps[position] == null) {
                iv_profile.setImageResource(R.drawable.ic_action_search);
                DownloadAsyncImage downloader = new DownloadAsyncImage(this, position);
                ImageViewHolder holder = new ImageViewHolder();
                holder.setImageView(iv_profile);
                holder.setURL(imageURLs[position]);
                holder.setPosition(position);
                holder.setProgBar((ProgressBar) rowView.findViewById(R.id.pb_search_user_image_loading));
                downloader.execute(holder);
            } else {
                iv_profile.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], 120, 120, false));
                pb_loading.setVisibility(View.GONE);
            }

        }else {
            if(convertView == null) {
                rowView = inflater.inflate(R.layout.list_item_search, parent, false);
                rowView.setTag("CONTRIBUTION");
            }else if(convertView.getTag().equals("CONTRIBUTION")){
                rowView = convertView;
                Log.d("SearchListAdapter", (String) convertView.getTag());
            }else{
                rowView = inflater.inflate(R.layout.list_item_search, parent, false);
                rowView.setTag("CONTRIBUTION");
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

            tv_name.setText(nameTypeSubtype[position]);
            tv_user.setText("submitted by " + users[position]);
            tv_game.setText(game[position]);

            if (fun[position] >= 1) {
                rb_fun.setRating((float) fun[position]);
                rb_bal.setRating((float) bal[position]);
            } else {
                tv_fun.setText("Not Yet Rated");
                tv_bal.setVisibility(View.INVISIBLE);
                rb_bal.setVisibility(View.INVISIBLE);
                rb_fun.setVisibility(View.INVISIBLE);
            }

            if (bitmaps[position] == null) {
                iv_search.setImageResource(R.drawable.ic_action_search);
                DownloadAsyncImage downloader = new DownloadAsyncImage(this, position);
                ImageViewHolder holder = new ImageViewHolder();
                holder.setImageView(iv_search);
                holder.setURL(imageURLs[position]);
                holder.setPosition(position);
                holder.setProgBar((ProgressBar) rowView.findViewById(R.id.pb_search_image_loading));
                downloader.execute(holder);
            } else {
                iv_search.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], 120, 120, false));
                pb_image.setVisibility(View.GONE);
            }
        }

        return rowView;
    }

    @Override
    public void onImageDownloaded(ImageViewHolder result, int originalPosition) {

        result.setProgressBarVisibility(View.GONE);

        if (result.getBitmap() == null) {
            if(originalPosition == result.getPosition())
                result.getImageView().setImageResource(R.drawable.ic_action_search);
            bitmaps[result.getPosition()] = default_bitmap;
        } else {
            if(originalPosition == result.getPosition())
                result.getImageView().setImageBitmap(Bitmap.createScaledBitmap(result.getBitmap(), 120, 120, false));
            bitmaps[result.getPosition()] = result.getBitmap();
        }
    }

}

