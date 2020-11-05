package com.ingenious.Receiver;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public interface IngeniousListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message);
}