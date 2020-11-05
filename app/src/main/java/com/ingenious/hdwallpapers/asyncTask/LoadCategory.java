package com.ingenious.hdwallpapers.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.CategoryListener;
import com.ingenious.SharedPref.Setting;
import com.ingenious.items.ItemCat;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadCategory extends AsyncTask<String, String, String> {

    private CategoryListener categoryListener;
    private ArrayList<ItemCat> arrayList;
    private RequestBody requestBody;

    public LoadCategory(CategoryListener categoryListener, RequestBody requestBody) {
        this.categoryListener = categoryListener;
        arrayList = new ArrayList<>();
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        categoryListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected  String doInBackground(String... strings)  {
        String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);

        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Setting.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                String id = objJson.getString(Setting.TAG_CAT_ID);
                String name = objJson.getString(Setting.TAG_CAT_NAME);
                String image = objJson.getString(Setting.TAG_CAT_IMAGE).replace(" ", "%20");
                String image_thumb = objJson.getString(Setting.TAG_CAT_IMAGE_THUMB).replace(" ", "%20");
                String tot_wall = objJson.getString(Setting.TAG_TOTAL_WALL);

                ItemCat itemCat = new ItemCat(id, name, image, image_thumb, tot_wall);
                arrayList.add(itemCat);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }


    }

    @Override
    protected void onPostExecute(String s) {
        categoryListener.onEnd(String.valueOf(s),arrayList);
        super.onPostExecute(s);
    }

}

