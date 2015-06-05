package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gminspiration.mobileapi.DownloadAsyncImage;
import com.gminspiration.mobileapi.HTTPRequestCallback;
import com.gminspiration.mobileapi.ImageDownloadCallback;
import com.gminspiration.mobileapi.ImageViewHolder;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by tehcoconut on 6/4/15.
 */
public class MySearchListAdapter extends BaseAdapter implements ImageDownloadCallback{

    private String imageURLs[], nameTypeSubtype[], users[], game[];
    private double fun[], bal[];
    private int ids[], privacys[];

    private Context context;

    private Bitmap bitmaps[];
    private Bitmap default_bitmap;


    public MySearchListAdapter(Context context, int ids[], int[] privacys, String imageURLs[], String nameTypeSubtype[], double fun[], double bal[], String users[], String game[]){
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

        bitmaps = new Bitmap[ids.length];

        default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_search);
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_search, parent, false);
        TextView tv_name = (TextView) rowView.findViewById(R.id.tv_search_name);
        TextView tv_user = (TextView) rowView.findViewById(R.id.tv_search_submittedby);
        TextView tv_game = (TextView) rowView.findViewById(R.id.tv_search_game);
        ImageView iv_search = (ImageView) rowView.findViewById(R.id.iv_search_list_item);

        TextView tv_fun = (TextView) rowView.findViewById(R.id.tv_search_rating_fun);
        TextView tv_bal = (TextView) rowView.findViewById(R.id.tv_search_rating_bal);
        RatingBar rb_fun = (RatingBar) rowView.findViewById(R.id.rb_search_fun);
        RatingBar rb_bal = (RatingBar) rowView.findViewById(R.id.rb_search_bal);

        tv_name.setText(nameTypeSubtype[position]);
        tv_user.setText("submitted by " + users[position]);
        tv_game.setText(game[position]);

        if(fun[position] >= 1) {
            rb_fun.setRating((float) fun[position]);
            rb_bal.setRating((float) bal[position]);
        }else{
            tv_fun.setText("Not Yet Rated");
            tv_bal.setVisibility(View.INVISIBLE);
            rb_bal.setVisibility(View.INVISIBLE);
            rb_fun.setVisibility(View.INVISIBLE);
        }

        if(bitmaps[position] == null) {
            DownloadAsyncImage downloader = new DownloadAsyncImage(this);
            ImageViewHolder holder = new ImageViewHolder();
            holder.setImageView(iv_search);
            holder.setURL(imageURLs[position]);
            holder.setPosition(position);
            downloader.execute(holder);
        }else{
            iv_search.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], 120, 120, false));
        }

        return rowView;
    }

    @Override
    public void onRequestCompleted(ImageViewHolder result) {

        if (result.getBitmap() == null) {
            result.getImageView().setImageResource(R.drawable.ic_action_search);
            bitmaps[result.getPosition()] = default_bitmap;
        } else {
            result.getImageView().setImageBitmap(Bitmap.createScaledBitmap(result.getBitmap(), 120, 120, false));
            bitmaps[result.getPosition()] = result.getBitmap();
        }
    }
}

