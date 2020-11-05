package com.ingenious.hdwallpapers.asyncTask;

import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.WallListener;
import com.ingenious.SharedPref.Setting;
import com.ingenious.items.ItemWallpaper;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadWallpaper extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private WallListener wallListener;
    private ArrayList<ItemWallpaper> arrayList;
    private String verifyStatus = "0", message = "";

    public LoadWallpaper(WallListener wallListener, RequestBody requestBody) {
        this.wallListener = wallListener;
        this.requestBody = requestBody;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        wallListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Setting.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(Setting.TAG_SUCCESS)) {
                    String id = objJson.getString(Setting.TAG_WALL_ID);
                    String cid = objJson.getString(Setting.TAG_CAT_ID);
                    String cat_name = objJson.getString(Setting.TAG_CAT_NAME);
                    String img = objJson.getString(Setting.TAG_WALL_IMAGE).replace(" ", "%20");
                    String img_thumb = objJson.getString(Setting.TAG_WALL_IMAGE_THUMB).replace(" ", "%20");
                    String totalviews = objJson.getString(Setting.TAG_WALL_VIEWS);
                    String totalrate = objJson.getString(Setting.TAG_WALL_TOTAL_RATE);
                    String averagerate = objJson.getString(Setting.TAG_WALL_AVG_RATE);
                    String pay = objJson.getString("pay");


                    ItemWallpaper itemWallpaper = new ItemWallpaper(id, cid, cat_name, img, img_thumb, totalviews, totalrate, averagerate, pay, "");
                    arrayList.add(itemWallpaper);
                } else {
                    verifyStatus = objJson.getString(Setting.TAG_SUCCESS);
                    message = objJson.getString(Setting.TAG_MSG);
                }
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        wallListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}