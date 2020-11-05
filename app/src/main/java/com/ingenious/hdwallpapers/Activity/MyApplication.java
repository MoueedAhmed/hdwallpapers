package com.ingenious.hdwallpapers.Activity;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ingenious.hdwallpapers.R;
import com.onesignal.OneSignal;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ingenious.hdwallpapers.DBHelper.DBHelper;
import com.ingenious.Model.Activitytime;
import com.ingenious.hdwallpapers.api.ApiUtil;
import com.ingenious.hdwallpapers.api.SOService;
import com.ingenious.hdwallpapers.api.Utils;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */

public class MyApplication extends Application implements LifecycleObserver
{

    private static Date startTimeInstance;
    private static Date endTimeInstance;
    private String startTime;
    private String totalSpentTime;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static Context context;
    public static Context getContext() { return context; }

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        try {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            dbHelper.onCreate(dbHelper.getWritableDatabase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OneSignal.startInit(getApplicationContext())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        Fresco.initialize(this);
        FirebaseAnalytics.getInstance(getApplicationContext());
        MobileAds.initialize(getApplicationContext(), getApplicationContext().getString(R.string.admob_app_id));
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded()
    {
        endTimeInstance = new Date();
        Log.d("onAppBackgrounded", "App in background");
        long time  = calculateSpentTime(startTime,sdf.format(endTimeInstance.getTime()));
        Log.d("spendingTime",time+"");
        if(Prefs.getString("user_type","0").equals("1"))
        {
            if (time >= 300)
                activitytime(String.valueOf(time));
        }
    }

    private long calculateSpentTime(String startTime,String stopTime)
    {
        long diffSeconds = 0;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date d1 = format.parse(startTime);
            Date d2 = format.parse(stopTime);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            diffSeconds = diff / 1000 ;

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return diffSeconds;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded()
    {
        startTimeInstance = new Date();
        Log.d("onAppForegrounded", "App in foreground");
        startTime = sdf.format(startTimeInstance.getTime());
//        Toast.makeText(this,"onAppForegrounded"+startTime,Toast.LENGTH_SHORT).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onAppClosed()
    {
        Log.d("onAppClosed", "Closed");
    }


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//CODE FOR ACTIVITY TIME OR GLOBAL RESPONSE

    private  void activitytime(String time)
    {
        SOService soService = ApiUtil.getSOService();
        soService.addActivity(
                Utils.getSimpleTextBody(Prefs.getString("user_id","")),
                Utils.getSimpleTextBody(context.getPackageName()),
                Utils.getSimpleTextBody(time)).enqueue(new Callback<Activitytime>()
        {
            @Override
            public void onResponse(Call<Activitytime> call, retrofit2.Response<Activitytime> response)
            {
                Log.d("ServerResponse", response + "");
            }

            @Override
            public void onFailure(Call<Activitytime> call, Throwable t)
            {
                Log.d("ServerError", t.getMessage() + "");

            }
        });
    }

}