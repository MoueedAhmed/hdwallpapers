package com.ingenious.hdwallpapers.api;

public class ApiUtil {

    public  static  String  BASE_URL = "https://admob.leadup.app/index.php/admob/v1/";

    public static SOService getSOService()
    {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
