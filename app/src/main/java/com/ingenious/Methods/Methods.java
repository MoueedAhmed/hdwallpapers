package com.ingenious.Methods;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ingenious.hdwallpapers.R;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

import com.ingenious.Listener.InterAdListener;
import com.ingenious.SharedPref.MY_API;
import com.ingenious.SharedPref.Setting;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */public class Methods {

    private Context context;
    private InterAdListener interAdListener;
    private SecretKey key;

    private InterstitialAd interstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAdFB;
    // constructor
    public Methods(Context context) {
        this.context = context;

        Store store = new Store(context);
        if (!store.hasKey("hdwallpaperenc")) {
            key = store.generateSymmetricKey("hdwallpaperenc", null);
        } else {
            key = store.getSymmetricKey("hdwallpaperenc", null);
        }
    }

    public Methods(Context context, InterAdListener interAdListener) {
        this.context = context;
        this.interAdListener = interAdListener;

        loadad();

        Store store = new Store(context);
        if (!store.hasKey("hdwallpaperenc")) {
            key = store.generateSymmetricKey("hdwallpaperenc", null);
        } else {
            key = store.getSymmetricKey("hdwallpaperenc", null);
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        point.y = display.getHeight();

        return point.y;
    }

    public String format(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }


    public String getImageThumbSize(String imagePath) {
        imagePath = imagePath.replace("&size=300x300", "&size=350x500");
        return imagePath;
    }



    public int getColumnWidth(int column, int grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    private void loadad() {
        if (Setting.getPurchases){
        }else {
            if (Setting.isAdmobInterAd) {
                interstitialAd = new InterstitialAd(context);
                AdRequest adRequest;
                if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                    adRequest = new AdRequest.Builder()
                            .build();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("npa", "1");
                    adRequest = new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build();
                }
                interstitialAd.setAdUnitId(Setting.ad_inter_id);
                interstitialAd.loadAd(adRequest);
            } else if (Setting.isFBInterAd) {
                interstitialAdFB = new com.facebook.ads.InterstitialAd(context, Setting.fb_ad_inter_id);
                interstitialAdFB.loadAd();
            }
        }

    }


    public void showInter(final int pos, final String type) {
        if (Setting.getPurchases){
            interAdListener.onClick(pos, type);
        }else {
            if (Setting.isAdmobInterAd) {
                Setting.adCount = Setting.adCount + 1;
                if (Setting.adCount % Setting.adShow == 0) {
                    interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdClosed() {
                            interAdListener.onClick(pos, type);
                            super.onAdClosed();
                        }
                    });
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        interAdListener.onClick(pos, type);
                    }
                    loadad();
                } else {
                    interAdListener.onClick(pos, type);
                }
            } else if (Setting.isFBInterAd) {
                Setting.adCount = Setting.adCount + 1;
                if (Setting.adCount % Setting.adShowFB == 0) {
                    interstitialAdFB.loadAd(interstitialAdFB.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                        @Override
                        public void onError(com.facebook.ads.Ad ad, AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            interAdListener.onClick(pos, type);
                        }
                    }).build());

                    if (interstitialAdFB.isAdLoaded()) {
                        interstitialAdFB.show();
                    } else {
                        interAdListener.onClick(pos, type);
                    }
                    loadad();
                } else {
                    interAdListener.onClick(pos, type);
                }
            } else {
                interAdListener.onClick(pos, type);
            }
        }

    }

    public void showInterDownload() {
        if (!Setting.getPurchases){
            if (Setting.isAdmobInterAd) {
                interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                });
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
                loadad();
            } else if (Setting.isFBInterAd) {
                interstitialAdFB.loadAd(interstitialAdFB.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onError(com.facebook.ads.Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onAdClicked(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

                    }
                }).build());

                if (interstitialAdFB.isAdLoaded()) {
                    interstitialAdFB.show();
                }
                loadad();
            }
        }
    }

    private void showPersonalizedAds(LinearLayout linearLayout) {
        if (Setting.isAdmobBannerAd) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("0336997DCA346E1612B610471A578EEB").build();
            adView.setAdUnitId(Setting.ad_banner_id);
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Setting.isFBBannerAd) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, Setting.fb_ad_banner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

    private void showNonPersonalizedAds(LinearLayout linearLayout) {
        if (Setting.isAdmobBannerAd) {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(Setting.ad_banner_id);
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Setting.isFBBannerAd) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, Setting.fb_ad_banner_id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

    public void showBannerAd(LinearLayout linearLayout) {
        if (Setting.getPurchases){
        }else {
            if (isNetworkAvailable() && linearLayout != null) {
                if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                    showNonPersonalizedAds(linearLayout);
                } else {
                    showPersonalizedAds(linearLayout);
                }
            }
        }

    }

    public String encrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt(value, key);
        } catch (Exception e) {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt("null", key);
        }
    }

    public String decrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.decrypt(value, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }

//  public void showSnackBar(View linearLayout, String message) {
//      Snackbar snackbar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT);
//      snackbar.getView().setBackgroundResource(R.drawable.bg_grt_toolbar);
//      snackbar.show();
//  }

    public void showSnackBar(View linearLayout, String message) {
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public void showSnackBar(String message, String type) {
        if (type.equals("success")){
            Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        }else {
            Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        }
    }

    public RequestBody getAPIRequest(String method, int page, String Nemosofts_key, String cat_id, String searchText, String itemID, String rate, String name, String email, String password, String deviceID, String userID, String reportFor) {
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new MY_API());
        jsObj.addProperty("method_name", method);
        jsObj.addProperty("package_name", context.getPackageName());

        switch (method) {

            case Setting.METHOD_CAT:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_LATEST_WALL:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_MOST_VIEWED:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_WALLPAPER_BY_CAT:
                jsObj.addProperty("page", String.valueOf(page));
                jsObj.addProperty("cat_id", cat_id);
                break;

            case Setting.METHOD_WALL_DOWNLOAD:
                jsObj.addProperty("wallpaper_id", itemID);
                break;

            case Setting.METHOD_WALL_SET:
                jsObj.addProperty("wallpaper_id", itemID);
                break;

            case Setting.METHOD_WALL_SHARE:
                jsObj.addProperty("wallpaper_id", itemID);
                break;

            case Setting.METHOD_WALL_SINGLE:
                jsObj.addProperty("wallpaper_id", itemID);
                break;

            case Setting.METHOD_WALL_RATING:
                jsObj.addProperty("post_id", itemID);
                jsObj.addProperty("device_id", deviceID);
                jsObj.addProperty("rate", rate);
                break;

            case Setting.METHOD_WALL_GET_RATING:
                jsObj.addProperty("post_id", itemID);
                jsObj.addProperty("device_id", deviceID);
                break;

            case Setting.METHOD_MOST_VIEWED_GIF:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_MOST_RATED_GIF:
                jsObj.addProperty("page", String.valueOf(page));
                break;

            case Setting.METHOD_LATEST_GIF:
                jsObj.addProperty("page", String.valueOf(page));
                break;


            case Setting.METHOD_GIF_DOWNLOAD:
                jsObj.addProperty("gif_id", itemID);
                break;

            case Setting.METHOD_GIF_SET:
                jsObj.addProperty("gif_id", itemID);
                break;

            case Setting.METHOD_GIF_SHARE:
                jsObj.addProperty("gif_id", itemID);
                break;

            case Setting.METHOD_GIF_SINGLE:
                jsObj.addProperty("gif_id", itemID);
                break;

            case Setting.METHOD_GIF_RATING:
                jsObj.addProperty("post_id", itemID);
                jsObj.addProperty("device_id", deviceID);
                jsObj.addProperty("rate", rate);
                break;

            case Setting.METHOD_GIF_GET_RATING:
                jsObj.addProperty("post_id", itemID);
                jsObj.addProperty("device_id", deviceID);
                break;


            case Setting.TAG_ROOT:
                jsObj.addProperty("key_id", Nemosofts_key);
                break;

        }

        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", MY_API.toBase64(jsObj.toString()))
                .build();
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if(Setting.SERVER_URL.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getVerifyDialog(String string, String message) {
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public void ShowDialog_pay(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_subscribe, null);

        final BottomSheetDialog dialog_setas = new BottomSheetDialog(context);
        dialog_setas.setContentView(view);
        dialog_setas.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        final TextView text_view_go_pro=(TextView) dialog_setas.findViewById(R.id.text_view_go_pro);
        RelativeLayout relativeLayout_close_rate_gialog=(RelativeLayout) dialog_setas.findViewById(R.id.relativeLayout_close_rate_gialog);
        relativeLayout_close_rate_gialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setas.dismiss();
            }
        });
        text_view_go_pro.setText("Cancel");
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setas.cancel();
            }
        });
        dialog_setas.setOnKeyListener(new BottomSheetDialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog_setas.dismiss();
                }
                return true;
            }
        });
        dialog_setas.show();

    }
}