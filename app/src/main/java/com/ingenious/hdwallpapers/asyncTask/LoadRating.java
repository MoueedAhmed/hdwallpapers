package com.ingenious.hdwallpapers.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.RatingListener;
import com.ingenious.SharedPref.Setting;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadRating extends AsyncTask<String, String, Boolean> {

    private RequestBody requestBody;
    private String msg = "";
    private String rate = "0";
    private RatingListener ratingListener;

    public LoadRating(RatingListener ratingListener, RequestBody requestBody) {
        this.ratingListener = ratingListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        ratingListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Setting.TAG_ROOT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                msg = c.getString(Setting.TAG_MSG);
                if (!msg.contains("already rated")) {
                    rate = c.getString("rate_avg");
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        ratingListener.onEnd(String.valueOf(s), msg, Float.parseFloat(rate));
        super.onPostExecute(s);
    }
}