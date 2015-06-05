package com.gminspiration.mobileapi;

/**
 * Created by tehcoconut on 5/31/15.
 */
public interface GMIQueryCallback {
    void onRequestCompleted(String results);
    void onProgressUpdate(Integer... progress);
}
