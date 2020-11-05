package com.ingenious.Listener;

import java.util.ArrayList;

import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public interface WallListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemWallpaper> arrayListWell);
}