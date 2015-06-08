package com.gminspiration.mobileapi;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by tehcoconut on 6/4/15.
 */
public class ImageViewHolder {
    String imageURL;
    Bitmap bitmap;
    ImageView imageView;
    int position;
    ProgressBar imageLoading;
    View rowView;


    public int getPosition(){
        return position;
    }
    public void setPosition(int id){
        this.position = id;
    }

    public void setURL(String url){
        imageURL = url;
    }
    public String getURL(){ return imageURL; }
    public void setImageView(ImageView iv){
        imageView = iv;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public ImageView getImageView(){
        return imageView;
    }

    public void setProgressBarVisibility(int visibility){
        imageLoading.setVisibility(visibility);
    }

    public void setProgBar(ProgressBar imageLoading){
        this.imageLoading = imageLoading;
    }
    public void setRowView(View rowView){
        this.rowView = rowView;
    }
    public View getRowView(){
        return rowView;
    }
}
