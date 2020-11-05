package com.ingenious.Listener;

import java.util.ArrayList;

import com.ingenious.items.ItemGIF;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public interface GIFListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemGIF> arrayListCat, int totalNumber);
}