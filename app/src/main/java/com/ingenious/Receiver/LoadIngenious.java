package com.ingenious.Receiver;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.hdwallpapers.BuildConfig;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class LoadIngenious extends AsyncTask<String, String, String> {

    private Methods methods;
    private IngeniousListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadIngenious(Context context, IngeniousListener aboutListener) {
        this.aboutListener = aboutListener;
        methods = new Methods(context);
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(BuildConfig.APPLICATION_ID+ Setting.api, methods.getAPIRequest(Setting.TAG_ROOT, 0, Setting.nemosofts_key, "", "", "", "", "", "", "", "", "", ""));
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has(Setting.TAG_ROOT)) {
                JSONArray jsonArray = jsonObject.getJSONArray(Setting.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);

                    if (!objJson.has(Setting.TAG_SUCCESS)) {
                        String purchase_code = objJson.getString("purchase_code");
                        String product_name = objJson.getString("product_name");
                        String purchase_date = objJson.getString("purchase_date");
                        String buyer_name = objJson.getString("buyer_name");
                        String license_type = objJson.getString("license_type");
                        String nemosofts_key = objJson.getString("nemosofts_key");
                        String package_name = objJson.getString("package_name");

                        Setting.itemAbout = new ItemIngenious(purchase_code, product_name, purchase_date, buyer_name, license_type, nemosofts_key, package_name);
                    }else {
                        verifyStatus = objJson.getString("success");
                        message = objJson.getString("msg");
                    }
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
        aboutListener.onEnd(s, verifyStatus, message);
        super.onPostExecute(s);
    }
}