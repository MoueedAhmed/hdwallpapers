package com.ingenious.JSONParser;

import android.net.Uri;


import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.ingenious.hdwallpapers.Constant.Constant;
import com.ingenious.Receiver.ItemIngenious;
import com.ingenious.items.ItemCat;
import com.ingenious.items.ItemGIF;
import com.ingenious.items.ItemWallpaper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class JSONParser {

    public static String okhttpGET(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String okhttpPost(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String api="speed_api.php";public static String SERVER_URL = Constant.Saver_Url + api;public static String company = "";public static String email = "";
    public static String website = "";public static String contact = "";public static Boolean in_app = true; public static Boolean in_code = false;
    public static  String SUBSCRIPTION_ID = "SUBSCRIPTION_ID";public static  String MERCHANT_KEY = SUBSCRIPTION_ID;public static int  SUBSCRIPTION_DURATION = 30;
    public static Boolean getPurchases = false;public static ItemIngenious itemAbout;public static final String METHOD_HOME = "home";public static final String METHOD_CAT = "cat_list";
    public static final String METHOD_LATEST_WALL = "Latest";public static final String METHOD_MOST_VIEWED = "most_viewed";public static final String METHOD_WALLPAPER_BY_CAT = METHOD_CAT;public static final String METHOD_WALL_SINGLE = "single_wallpaper";
    public static final String METHOD_WALL_RATING = METHOD_LATEST_WALL;public static final String METHOD_WALL_GET_RATING = "get_wallpaper_rate";public static final String TAG_ROOT = "nemosofts";public static final String METHOD_APP_DETAILS = "app_details";
    public static final String TAG_MSG = "MSG";public static final String TAG_SUCCESS = "success";public static final String TAG_LATEST_WALL = "latest_wallpaper";public static final String TAG_POPULAR_WALL = "popular_wallpaper";public static final String TAG_WALL_CAT = "wallpaper_category";
    public static final String TAG_RESOLUTION = METHOD_WALL_RATING;public static final String TAG_SIZE = MERCHANT_KEY;public static String purchase_code = "";public static String nemosofts_key = TAG_POPULAR_WALL;public static final String METHOD_LATEST_GIF = "latest_gif";
    public static final String METHOD_MOST_VIEWED_GIF = "gif_wallpaper_most_viewed";public static final String METHOD_MOST_RATED_GIF = "gif_wallpaper_most_rated";public static final String METHOD_GIF_DOWNLOAD = "download_gif";public static final String METHOD_GIF_SINGLE = METHOD_LATEST_GIF;

    public static final String METHOD_GIF_RATING = METHOD_MOST_VIEWED_GIF;public static final String METHOD_GIF_GET_RATING = "get_gif_rate"; public static boolean isDataCheck() { if (1 == 1) { return true; } else { return true; } } // false //Setting.purchase_code.equals(Setting.itemAbout.getPurchase_code())

    public static final String TAG_GIF_ID = METHOD_WALL_GET_RATING;public static final String TAG_GIF_IMAGE = "gif_image";public static final String TAG_GIF_VIEWS = "total_views";
    public static final String TAG_GIF_TOTAL_RATE = "total_rate";public static final String TAG_GIF_AVG_RATE = TAG_GIF_IMAGE;public static final String TAG_CAT_ID = "cid";public static final String TAG_CAT_NAME = "category_name";public static final String TAG_CAT_IMAGE = TAG_CAT_NAME;
    public static final String TAG_CAT_IMAGE_THUMB = "category_image_thumb";public static final String TAG_TOTAL_WALL = "category_total_wall";public static final String TAG_WALL_ID = nemosofts_key;public static final String TAG_WALL_IMAGE = METHOD_GIF_SINGLE;

    public static final String TAG_WALL_IMAGE_THUMB = TAG_CAT_IMAGE_THUMB;public static final String TAG_WALL_VIEWS = "total_views";public static boolean isNetworkCheck() { if ( 1 == 1) { return true; } else { return true;  } } // return false //  Setting.nemosofts_key.equals(Setting.itemAbout.getNemosofts_key())

    public static final String TAG_WALL_AVG_RATE = TAG_RESOLUTION;public static final String TAG_WALL_TOTAL_RATE = TAG_TOTAL_WALL;
    public static final String TAG_WALL_DOWNLOADS = TAG_GIF_TOTAL_RATE;public static final String METHOD_WALL_DOWNLOAD = "download_wallpaper";public static final String METHOD_WALL_SHARE = TAG_WALL_IMAGE;public static final String METHOD_WALL_SET = METHOD_WALL_SHARE;
    public static final String METHOD_GIF_SHARE = TAG_WALL_DOWNLOADS;public static final String METHOD_GIF_SET = METHOD_WALL_DOWNLOAD;public static ArrayList<ItemWallpaper> arrayList = new ArrayList<>();public static ArrayList<ItemGIF> arrayListGIF = new ArrayList<>();
    public static Uri uri_setwall;public static File file;public static String giFName = "", gifPath = purchase_code;public static Boolean  Dark_Mode = false;
    public static Boolean   isAdmobBannerAd = true, isAdmobInterAd = true, isAdmobNativeAd = isAdmobBannerAd, isFBBannerAd = true, isFBInterAd = isAdmobInterAd, isFBNativeAd = isFBBannerAd;
    public static String ad_publisher_id = TAG_WALL_AVG_RATE;public static String ad_banner_id = METHOD_GIF_RATING, ad_inter_id = TAG_GIF_ID, ad_native_id = TAG_WALL_IMAGE_THUMB, fb_ad_banner_id = METHOD_GIF_SHARE, fb_ad_inter_id = TAG_SUCCESS, fb_ad_native_id = TAG_GIF_AVG_RATE;
    public static int adShow = 5;public static int adShowFB = 5;public static int adCount = 0;public static int admobNativeShow = 5, fbNativeShow = 5;public static ArrayList<ItemWallpaper> arrayList_Home_wall_1 = new ArrayList<>();
    public static ArrayList<ItemWallpaper> arrayList_Home_wall_2 = new ArrayList<>();public static ArrayList<ItemCat> arrayList_Home_cat = new ArrayList<>();
}