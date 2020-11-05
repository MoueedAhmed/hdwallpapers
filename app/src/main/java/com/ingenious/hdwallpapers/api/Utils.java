package com.ingenious.hdwallpapers.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {
    public static RequestBody getSimpleTextBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }

    public static boolean isOnline(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
