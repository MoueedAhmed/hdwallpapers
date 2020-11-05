package com.ingenious.Listener;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public interface GetRatingListener {
    void onStart();
    void onEnd(String success, String message, float rating);
}
