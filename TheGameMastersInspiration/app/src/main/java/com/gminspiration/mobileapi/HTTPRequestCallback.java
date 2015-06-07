package com.gminspiration.mobileapi;

/**
 * Created by tehcoconut on 5/31/15.
 */
public interface HTTPRequestCallback {
    void onRequestCompleted(String results, int requestID);
    void onProgressUpdate(int requestID, Integer... progress);
}
