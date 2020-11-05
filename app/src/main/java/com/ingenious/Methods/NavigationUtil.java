package com.ingenious.Methods;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ingenious.hdwallpapers.Activity.AboutActivity;
import com.ingenious.hdwallpapers.Activity.CategoryActivity;
import com.ingenious.hdwallpapers.Activity.FavouriteActivity;
import com.ingenious.hdwallpapers.Activity.GIFActivity;
import com.ingenious.hdwallpapers.Activity.LatestActivity;
import com.ingenious.hdwallpapers.Activity.MostActivity;
import com.ingenious.hdwallpapers.Activity.WallCIDActivity;
import com.ingenious.hdwallpapers.Activity.WallpaperDetailsActivity;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class NavigationUtil {

    public static void LatestActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, LatestActivity.class), null);
    }

    public static void MostActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, MostActivity.class), null);
    }

    public static void CategoryActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, CategoryActivity.class), null);
    }

    public static void GifActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, GIFActivity.class), null);
    }

    public static void FavouriteActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, FavouriteActivity.class), null);
    }

    public static void AboutActivity(@NonNull Activity activity) {
        ActivityCompat.startActivity(activity, new Intent(activity, AboutActivity.class), null);
    }

    public static void Wallpaper_Details_Activity(@NonNull Activity activity, int position, int in) {
        Intent intent = new Intent(activity, WallpaperDetailsActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("page", in);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void Wall_Cid_Activity(@NonNull Activity activity, String name, String id) {
        Intent intent = new Intent(activity, WallCIDActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("cid", id);
        ActivityCompat.startActivity(activity, intent, null);
    }

}
