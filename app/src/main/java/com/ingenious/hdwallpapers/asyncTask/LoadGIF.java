package com.ingenious.hdwallpapers.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.GIFListener;
import com.ingenious.SharedPref.Setting;
import com.ingenious.items.ItemGIF;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadGIF extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private GIFListener gifListener;
    private ArrayList<ItemGIF> arrayList;
    private String verifyStatus = "0", message = "";
    private int num = -1;

    public LoadGIF(GIFListener gifListener, RequestBody requestBody) {
        this.gifListener = gifListener;
        this.requestBody = requestBody;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        gifListener.onStart();
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
                    if(objJson.has("num")) {
                        num = Integer.parseInt(objJson.getString("num"));
                    }

                    String id = objJson.getString(Setting.TAG_GIF_ID);
                    String img = objJson.getString(Setting.TAG_GIF_IMAGE).replace(" ", "%20");
                    String totalviews = objJson.getString(Setting.TAG_GIF_VIEWS);
                    String totalRate = objJson.getString(Setting.TAG_GIF_TOTAL_RATE);
                    String avj_rate = objJson.getString(Setting.TAG_GIF_AVG_RATE);
                    String pay = objJson.getString("pay");

                    ItemGIF itemGIF = new ItemGIF(id, img, totalviews, totalRate, avj_rate, pay,"");
                    arrayList.add(itemGIF);
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
        gifListener.onEnd(s, verifyStatus, message, arrayList, num);
        super.onPostExecute(s);
    }
}