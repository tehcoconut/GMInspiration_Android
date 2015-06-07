package com.gminspiration.mobileapi;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by tehcoconut on 5/31/15.
 */
public class BackgroundHTTPRequest extends AsyncTask<Void, Integer, String> {

    private String path;
    private String postData;
    private boolean firstGET = true;
    private boolean firstPOST = true;

    private int requestID;

    private HTTPRequestCallback listener;

    public BackgroundHTTPRequest(String path, int requestID, HTTPRequestCallback listener){
        this.path = path;
        this.listener = listener;
        this.requestID = requestID;
    }

    public void addGETParam(String key, String value){
        if(firstGET){
            try {
                path += "?" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }else{
            try {
                path += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        firstGET = false;
    }

    public void addPOSTParam(String key, String value){
        if(firstPOST){
            try {
                postData = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }else{
            try {
                postData += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        firstPOST = false;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            if (!firstPOST){
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(postData);
                wr.flush();
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();

        }catch(Exception e){
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onRequestCompleted(s, requestID);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        listener.onProgressUpdate(requestID, values);
    }
}
