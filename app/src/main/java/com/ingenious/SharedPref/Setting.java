package com.ingenious.SharedPref;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.ingenious.hdwallpapers.Constant.Constant;
import com.ingenious.Receiver.ItemIngenious;
import com.ingenious.items.ItemCat;
import com.ingenious.items.ItemGIF;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class Setting implements Serializable {

    public static String api="speed_api.php";public static String SERVER_URL = Constant.Saver_Url + api;public static String company = "";public static String email = "";
    public static String website = "";public static String contact = "";public static Boolean in_app = true; public static Boolean in_code = false;
    public static  String SUBSCRIPTION_ID = "SUBSCRIPTION_ID";public static  String MERCHANT_KEY = "MERCHANT_KEY";public static int  SUBSCRIPTION_DURATION = 30;

    public static Boolean getPurchases = false; public static ItemIngenious itemAbout; public static final String METHOD_HOME = "home";public static final String METHOD_CAT = "cat_list";

    public static final String METHOD_LATEST_WALL = "Latest";public static final String METHOD_MOST_VIEWED = "most_viewed";public static final String METHOD_WALLPAPER_BY_CAT = "cat_well";public static final String METHOD_WALL_SINGLE = "single_wallpaper";

    public static final String METHOD_WALL_RATING = "wallpaper_rate";public static final String METHOD_WALL_GET_RATING = "get_wallpaper_rate";public static final String TAG_ROOT = "nemosofts";public static final String METHOD_APP_DETAILS = "app_details";

    public static final String TAG_MSG = "MSG";public static final String TAG_SUCCESS = "success";public static final String TAG_LATEST_WALL = "latest_wallpaper";public static final String TAG_POPULAR_WALL = "popular_wallpaper";public static final String TAG_WALL_CAT = "wallpaper_category";
    public static final String TAG_RESOLUTION = "resolution";public static final String TAG_SIZE = "size";public static String purchase_code = "";public static String nemosofts_key = "";public static final String METHOD_LATEST_GIF = "latest_gif";
    public static final String METHOD_MOST_VIEWED_GIF = "gif_wallpaper_most_viewed";public static final String METHOD_MOST_RATED_GIF = "gif_wallpaper_most_rated";public static final String METHOD_GIF_DOWNLOAD = "download_gif";public static final String METHOD_GIF_SINGLE = "single_gif";
    public static final String METHOD_GIF_RATING = "gif_rate";public static final String METHOD_GIF_GET_RATING = "get_gif_rate";public static final String TAG_GIF_ID = "id";public static final String TAG_GIF_IMAGE = "gif_image";public static final String TAG_GIF_VIEWS = "total_views";
    public static final String TAG_GIF_TOTAL_RATE = "total_rate";public static final String TAG_GIF_AVG_RATE = "rate_avg";public static final String TAG_CAT_ID = "cid";public static final String TAG_CAT_NAME = "category_name";public static final String TAG_CAT_IMAGE = "category_image";
    public static final String TAG_CAT_IMAGE_THUMB = "category_image_thumb";public static final String TAG_TOTAL_WALL = "category_total_wall";public static final String TAG_WALL_ID = "id";public static final String TAG_WALL_IMAGE = "wallpaper_image";
    public static final String TAG_WALL_IMAGE_THUMB = "wallpaper_image_thumb";public static final String TAG_WALL_VIEWS = "total_views";public static final String TAG_WALL_AVG_RATE = "rate_avg";public static final String TAG_WALL_TOTAL_RATE = "total_rate";
    public static final String TAG_WALL_DOWNLOADS = "total_download";public static final String METHOD_WALL_DOWNLOAD = "download_wallpaper";public static final String METHOD_WALL_SHARE = "share_wallpaper";public static final String METHOD_WALL_SET = "set_wallpaper";
    public static final String METHOD_GIF_SHARE = "share_gif";public static final String METHOD_GIF_SET = "set_gif";public static ArrayList<ItemWallpaper> arrayList = new ArrayList<>();public static ArrayList<ItemGIF> arrayListGIF = new ArrayList<>();
    public static Uri uri_setwall;public static File file;public static String giFName = "", gifPath = "";public static Boolean  Dark_Mode = false;
    public static Boolean   isAdmobBannerAd = true, isAdmobInterAd = true, isAdmobNativeAd = false, isFBBannerAd = true, isFBInterAd = true, isFBNativeAd = false;
    public static String ad_publisher_id = "";public static String ad_banner_id = "", ad_inter_id = "", ad_native_id = "", fb_ad_banner_id = "", fb_ad_inter_id = "", fb_ad_native_id = "";
    public static int adShow = 5;public static int adShowFB = 5;public static int adCount = 0;public static int admobNativeShow = 5, fbNativeShow = 5;public static ArrayList<ItemWallpaper> arrayList_Home_wall_1 = new ArrayList<>();
    public static ArrayList<ItemWallpaper> arrayList_Home_wall_2 = new ArrayList<>();public static ArrayList<ItemCat> arrayList_Home_cat = new ArrayList<>();
}