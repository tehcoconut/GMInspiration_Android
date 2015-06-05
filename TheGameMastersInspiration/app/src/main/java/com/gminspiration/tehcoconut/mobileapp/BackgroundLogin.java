package com.gminspiration.tehcoconut.mobileapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by tehcoconut on 5/28/15.
 */
public class BackgroundLogin extends AsyncTask<String, Void, String> {

    Context context;

    public BackgroundLogin(Context context){
        this.context = context;
    }

    protected String doInBackground(String... arg0){
        try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];

            String link="http://gminspiration.com/login.php";
            String data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

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

    protected void onPostExecute(String result){
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}
