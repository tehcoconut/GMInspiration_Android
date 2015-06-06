package com.gminspiration.mobileapi;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.gminspiration.tehcoconut.mobileapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tehcoconut on 6/4/15.
 */
public class DownloadAsyncImage extends AsyncTask<ImageViewHolder, Void, ImageViewHolder>{

    ImageDownloadCallback callback;
    int position;

    public DownloadAsyncImage(ImageDownloadCallback listener, int position){
        this.position = position;
        callback = listener;
    }


    @Override
    protected ImageViewHolder doInBackground(ImageViewHolder... params) {
        ImageViewHolder holder = params[0];

        try{
            if(!holder.imageURL.contains(".gif")) {
                URL imageURL = new URL(holder.imageURL);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(imageURL.openStream(), null, options);
                final int REQUIRED_SIZE = 100;

                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2;
                }

                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inSampleSize = scale;

                holder.bitmap = BitmapFactory.decodeStream(imageURL.openStream(), null, options2);

                if(holder.bitmap != null)
                    Log.d("DownLoadAsyncImage", "Image Size: "+(holder.bitmap.getByteCount()/1024)+"kb");
                else
                    Log.d("DownLoadAsyncImage", "Could not download image, decodeStream gave null");
            }
        }catch(IOException e){
            Log.e("Error", "Downloading Image Failed");
            holder.bitmap = null;

        }

        return holder;
    }

    @Override
    protected void onPostExecute(ImageViewHolder result) {
        callback.onRequestCompleted(result, position);
    }
}
