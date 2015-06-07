package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gminspiration.mobileapi.DownloadAsyncImage;
import com.gminspiration.mobileapi.GMIConnection;
import com.gminspiration.mobileapi.GMIQueryCallback;
import com.gminspiration.mobileapi.ImageDownloadCallback;
import com.gminspiration.mobileapi.ImageViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tehcoconut on 6/5/15.
 */
public class ViewContributionFragment extends Fragment implements GMIQueryCallback, ImageDownloadCallback{

    private static final String TAG = "GMI-ViewContribution";

    private TextView tv_name, tv_type_subtype, tv_submittedby, tv_game;
    private TextView tv_fun, tv_bal;
    private RatingBar rb_fun, rb_bal;
    private ImageView iv_image;
    private ProgressBar pb_image, pb_page;

    private WebView wv_desc;

    private Context context;

    private GMIConnection gmic;

    private String username, name, type, sub_type, imgURL, game;
    private String desc;
    private double avg_fun, avg_bal;
    private int privacy;

    private JSONArray json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewcontribution, container, false);

        tv_name = (TextView) v.findViewById(R.id.tv_viewcontri_name);
        tv_type_subtype = (TextView) v.findViewById(R.id.tv_viewcontri_type_subtype);
        tv_submittedby = (TextView) v.findViewById(R.id.tv_viewcontri_submittedby);
        tv_game = (TextView) v.findViewById(R.id.tv_viewcontri_game);

        tv_fun = (TextView) v.findViewById(R.id.tv_viewcontri_rating_fun);
        tv_bal = (TextView) v.findViewById(R.id.tv_viewcontri_rating_bal);
        rb_fun = (RatingBar) v.findViewById(R.id.rb_viewcontri_fun);
        rb_bal = (RatingBar) v.findViewById(R.id.rb_viewcontri_bal);

        iv_image = (ImageView) v.findViewById(R.id.iv_viewcontri_image);
        pb_image = (ProgressBar) v.findViewById(R.id.pb_viewcontri_image_loading);

        pb_page = (ProgressBar) v.findViewById(R.id.pb_viewcontri_page_loading);

        wv_desc = (WebView) v.findViewById(R.id.wv_viewcontri_description_text);


        return v;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setConnection(GMIConnection gmic){
        this.gmic = gmic;
    }

    @Override
    public void onProgressUpdate(int requestID, Integer... progress) {

    }

    @Override
    public void onRequestCompleted(String results, int requestID) {
        if(requestID == GMIConnection.VIEW_CONTRIBUTION_QUERY){

            try{
                JSONObject jsonObj = new JSONObject(results);

                username = jsonObj.getString("username");
                imgURL = jsonObj.getString("img");
                name = jsonObj.getString("name");
                type = jsonObj.getString("type");
                sub_type = jsonObj.getString("sub_type");
                game = jsonObj.getString("game");
                desc = jsonObj.getString("desc");

                String temp = jsonObj.getString("json");
                json = new JSONArray(temp);

                avg_bal = jsonObj.getDouble("avg_balance");
                avg_fun = jsonObj.getDouble("avg_fun");
                privacy = jsonObj.getInt("privacy");

                tv_name.setText(name);
                tv_type_subtype.setText(type.trim()+" ("+sub_type+")");
                tv_submittedby.setText("submitted by " + username.trim());
                tv_game.setText(game.trim());

                if(avg_bal >= 0) {
                    rb_fun.setRating((float) avg_fun);
                    rb_bal.setRating((float) avg_bal);
                }else{
                    rb_bal.setVisibility(View.GONE);
                    rb_fun.setVisibility(View.GONE);
                    tv_bal.setVisibility(View.GONE);
                    tv_fun.setText("Not yet rated");
                }

                String htmlString = "<h2>Description</h2>"+desc;

                for(int i =0; i<json.length(); i++){
                    JSONObject labelTextPair = json.getJSONObject(i);
                    htmlString += "<h2>"+labelTextPair.getString("label")+"</h2>";
                    htmlString += labelTextPair.getString("text");
                }

                wv_desc.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", "");

                pb_page.setVisibility(View.GONE);

                DownloadAsyncImage downloader = new DownloadAsyncImage(this, 0);
                ImageViewHolder holder = new ImageViewHolder();
                holder.setImageView(iv_image);
                holder.setURL(imgURL);
                holder.setProgBar(pb_image);
                downloader.execute(holder);



            }catch (Exception e){
                Log.d(TAG, "Error Loading Page");
            }




        }else{
            Log.d(TAG, "Invalid callback with requestID #" + requestID);
        }

    }

    @Override
    public void onImageDownloaded(ImageViewHolder result, int originalPosition) {
        result.setProgressBarVisibility(View.GONE);

        if (result.getBitmap() == null) {
            if(originalPosition == result.getPosition())
                result.getImageView().setImageResource(R.drawable.ic_action_search);
        } else {
            if(originalPosition == result.getPosition())
                result.getImageView().setImageBitmap(Bitmap.createScaledBitmap(result.getBitmap(), 200, 200, false));
        }
    }
}
