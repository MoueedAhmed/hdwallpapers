package com.ingenious.hdwallpapers.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.HomeListener;
import com.ingenious.SharedPref.Setting;
import com.ingenious.items.ItemCat;
import com.ingenious.items.ItemWallpaper;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadHome extends AsyncTask<String, String, Boolean> {

    private RequestBody requestBody;
    private HomeListener homeListener;
    private ArrayList<ItemCat> arrayListCat;
    private ArrayList<ItemWallpaper>  arrayListLatest, arrayListPopular;

    public LoadHome(Context context, HomeListener homeListener, RequestBody requestBody) {
        this.homeListener = homeListener;
        this.requestBody = requestBody;
        arrayListPopular = new ArrayList<>();
        arrayListLatest = new ArrayList<>();
        arrayListCat = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONObject jsonObj = jOb.getJSONObject(Setting.TAG_ROOT);

            JSONArray jsonArray_potrait = jsonObj.getJSONArray(Setting.TAG_LATEST_WALL);
            Log.d("DK_HOME" ,jsonArray_potrait.toString());

            for (int i = 0; i < jsonArray_potrait.length(); i++) {
                JSONObject objJson = jsonArray_potrait.getJSONObject(i);

                String id = objJson.getString(Setting.TAG_WALL_ID);
                String cid = objJson.getString(Setting.TAG_CAT_ID);
                String cat_name = objJson.getString(Setting.TAG_CAT_NAME);
                String img = objJson.getString(Setting.TAG_WALL_IMAGE).replace(" ", "%20");
                String img_thumb = objJson.getString(Setting.TAG_WALL_IMAGE_THUMB).replace(" ", "%20");
                String totalviews = objJson.getString(Setting.TAG_WALL_VIEWS);
                String totalrate = objJson.getString(Setting.TAG_WALL_TOTAL_RATE);
                String averagerate = objJson.getString(Setting.TAG_WALL_AVG_RATE);
                String pay = objJson.getString("pay");


                ItemWallpaper itemWallpaper = new ItemWallpaper(id, cid, cat_name, img, img_thumb, totalviews, totalrate, averagerate, pay,"");
                arrayListLatest.add(itemWallpaper);
            }

            JSONArray jsonArray_pop = jsonObj.getJSONArray(Setting.TAG_POPULAR_WALL);
            for (int i = 0; i < jsonArray_pop.length(); i++) {
                JSONObject objJson = jsonArray_pop.getJSONObject(i);

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
                arrayListPopular.add(itemWallpaper);
            }

            JSONArray jsonArray_cat = jsonObj.getJSONArray(Setting.TAG_WALL_CAT);
            for (int i = 0; i < jsonArray_cat.length(); i++) {
                JSONObject objJson = jsonArray_cat.getJSONObject(i);

                String id = objJson.getString(Setting.TAG_CAT_ID);
                String name = objJson.getString(Setting.TAG_CAT_NAME);
                String image = objJson.getString(Setting.TAG_CAT_IMAGE).replace(" ", "%20");
                String image_thumb = objJson.getString(Setting.TAG_CAT_IMAGE_THUMB).replace(" ", "%20");
                String tot_wall = objJson.getString(Setting.TAG_TOTAL_WALL);

                ItemCat itemCat = new ItemCat(id, name, image, image_thumb, tot_wall);
                arrayListCat.add(itemCat);
            }


            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        homeListener.onEnd(String.valueOf(s), arrayListLatest, arrayListPopular, arrayListCat);
        super.onPostExecute(s);
    }
}