package com.ingenious.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ingenious.Receiver.ItemIngenious;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class SharedPre {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPre(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean getNightMode() {
        return sharedPreferences.getBoolean("night_mode", false);
    }

    public void setNightMode(Boolean state) {
        editor.putBoolean("night_mode", state);
        editor.apply();
    }

    public void setIsFirstPurchaseCode(Boolean flag) {
        editor.putBoolean("firstopenpurchasecode", flag);
        editor.apply();
    }

    public Boolean getIsFirstPurchaseCode() {
        return sharedPreferences.getBoolean("firstopenpurchasecode", true);
    }

    public void setPurchaseCode(ItemIngenious itemAbout) {
        editor.putString("purchase_code", itemAbout.getPurchase_code());
        editor.putString("product_name", itemAbout.getProduct_name());
        editor.putString("purchase_date", itemAbout.getPurchase_date());
        editor.putString("buyer_name", itemAbout.getBuyer_name());
        editor.putString("license_type", itemAbout.getLicense_type());
        editor.putString("nemosofts_key", itemAbout.getNemosofts_key());
        editor.putString("package_name", itemAbout.getPackage_name());
        editor.apply();
    }

    public void getPurchaseCode() {
        Setting.itemAbout = new ItemIngenious(
                sharedPreferences.getString("purchase_code",""),
                sharedPreferences.getString("product_name",""),
                sharedPreferences.getString("purchase_date",""),
                sharedPreferences.getString("buyer_name",""),
                sharedPreferences.getString("license_type",""),
                sharedPreferences.getString("nemosofts_key",""),
                sharedPreferences.getString("package_name","")
        );
    }

    public void setPurchase() {
        editor.putBoolean("in_app", Setting.in_app);
        editor.putString("subscription_id", Setting.SUBSCRIPTION_ID);
        editor.putString("merchant_key", Setting.MERCHANT_KEY);
        editor.putInt("sub_dur", Setting.SUBSCRIPTION_DURATION);
        editor.apply();
    }

    public void getPurchase() {
        Setting.in_app = sharedPreferences.getBoolean("in_app", true);
        Setting.SUBSCRIPTION_ID = sharedPreferences.getString("subscription_id","SUBSCRIPTION_ID");
        Setting.MERCHANT_KEY = sharedPreferences.getString("merchant_key","MERCHANT_KEY");
        Setting.SUBSCRIPTION_DURATION = sharedPreferences.getInt("sub_dur",30);
    }

    public void saveToPref(String str) {
        editor.putBoolean("in_code", true);
        editor.putString("code", str);
        editor.apply();
    }

    public String getCode() {
        return sharedPreferences.getString("code","");
    }

    public Boolean getIn_Code() {
        return sharedPreferences.getBoolean("in_code", false);
    }


    public void setDown_Arrow(Boolean flag) {
        editor.putBoolean("down_arrow_he", flag);
        editor.apply();
    }

    public Boolean getDown_Arrow() {
        return sharedPreferences.getBoolean("down_arrow_he", true);
    }
}
