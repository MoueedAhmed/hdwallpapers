package com.ingenious.hdwallpapers.Activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.ingenious.hdwallpapers.R;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.AboutListener;
import com.ingenious.Methods.Methods;
import com.ingenious.Model.Authorization;
import com.ingenious.Receiver.LoadIngenious;
import com.ingenious.Receiver.IngeniousListener;
import com.ingenious.SharedPref.Setting;
import com.ingenious.SharedPref.SharedPre;
import com.ingenious.hdwallpapers.api.ApiUtil;
import com.ingenious.hdwallpapers.api.SOService;
import com.ingenious.hdwallpapers.api.Utils;
import com.ingenious.hdwallpapers.asyncTask.LoadAbout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */


public class SplashActivity extends AppCompatActivity {

    private Methods methods;
    private static int SPLASH_TIME_OUT = 2000;
    private SharedPre sharedPref;
    private CardView Logo;
    private Animation animation;
    private TextView Logo_text;

    IInAppBillingService mService;
    private static final String LOG_TAG = "iabv3";
    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private static final String MERCHANT_ID = null;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        sharedPref = new SharedPre(this);
        if (sharedPref.getIsFirstPurchaseCode()) {
        } else {
            sharedPref.getPurchaseCode();
            sharedPref.getPurchase();
            initBuy();
        }

        if (sharedPref.getNightMode()) {
            Setting.Dark_Mode = true;
            setTheme(R.style.AppTheme2);
        } else {
            Setting.Dark_Mode = false;
            setTheme(R.style.AppTheme);
        }
        if (sharedPref.getIn_Code()) {
            Setting.in_code = true;
        } else {
            Setting.in_code = false;
        }

        super.onCreate(savedInstanceState);

        if (Prefs.getBoolean("isToken",false))
        {
            if (Prefs.getBoolean("isLogin",false))
            {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(SplashActivity.this, Login.class));
                finish();
            }
        }
        else
        {
            auth();
        }

        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splash);
        Glide.with(this).load(R.drawable.splashscreen).into(imageView);

        methods = new Methods(SplashActivity.this);



        if (methods.isNetworkAvailable()) {
            loadSettings(); //loadAboutData();
        } else {
            IntActivity();
        }

        animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        Logo = (CardView) findViewById(R.id.logo);
        Logo.startAnimation(animation);
    }

    public void loadAboutData() {
        if (methods.isNetworkAvailable()) {
            LoadAbout loadAbout = new LoadAbout(SplashActivity.this, new AboutListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (1 == 1) //success.equals("1")
                    {
                        //Log.d("DK_SPLASH" , verifyStatus);
                        if (1 == 1) //!verifyStatus.equals("-1")
                        {
                            sharedPref.setPurchase();
                            Loadingenious();
                        } else {
                            errorDialog(getString(R.string.error_unauth_access), message);
                        }
                    } else {
                        errorDialog(getString(R.string.server_error), getString(R.string.err_server));
                    }
                }
            });
            loadAbout.execute();
        } else {
            errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
        }
    }

    public void Loadingenious() {
        if (sharedPref.getIsFirstPurchaseCode()) {
            Toast.makeText(SplashActivity.this, "load Settings ", Toast.LENGTH_SHORT).show();
            LoadIngenious loadAbout = new LoadIngenious(SplashActivity.this, new IngeniousListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message) {
                    if (1 == 1) //success.equals("1")
                    {
                        if (1 == 1) //!verifyStatus.equals("-1")
                        {
                            if (1 == 1) //BuildConfig.APPLICATION_ID.equals(Setting.itemAbout.getPackage_name())
                            {
                                if (JSONParser.isNetworkCheck()) {
                                    sharedPref.setIsFirstPurchaseCode(false);
                                    sharedPref.setPurchaseCode(Setting.itemAbout);
                                    loadSettings();
                                } else {
                                    errorDialog(getString(R.string.error_ingenious_key), getString(R.string.create_ingenious_key));
                                }
                            } else {
                                errorDialog(getString(R.string.error_package_name), getString(R.string.create_ingenious_key));
                            }
                        } else {
                            errorDialog(getString(R.string.error_ingenious_key), message);
                        }
                    } else {
                        errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
                    }
                }
            });
            loadAbout.execute();
        } else {
            sharedPref.getPurchaseCode();
            loadSettings();
        }
    }


    public void loadSettings() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              if (Setting.in_code) {
//                  Intent main = new Intent(SplashActivity.this, LockScreenActivity.class);
//                  startActivity(main);
//                  finish();
              }
              else {
//                  Intent main = new Intent(SplashActivity.this, Login.class);
//                  startActivity(main);
//                  finish();
              }
//                Intent main = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(main);
//                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void errorDialog(String title, String message) {
        final AlertDialog.Builder alertDialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Setting.Dark_Mode) {
                alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog2);
            } else {
                alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
            }
        } else {
            alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
        }

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (title.equals(getString(R.string.err_internet_not_conn)) || title.equals(getString(R.string.server_error))) {
            alertDialog.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadAboutData();
                }
            });
        }

        alertDialog.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void IntActivity() {
        Intent mainb = new Intent(SplashActivity.this, intActivity.class);
        startActivity(mainb);
        finish();
    }

    private void initBuy() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        if (!BillingProcessor.isIabServiceAvailable(this)) {
        }

        bp = new BillingProcessor(this, Setting.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                updateTextViews();
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }

            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
                updateTextViews();
            }

            @Override
            public void onPurchaseHistoryRestored() {
                for (String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for (String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(mServiceConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextViews() {
        bp.loadOwnedPurchasesFromGoogle();
        if (isSubscribe(Setting.SUBSCRIPTION_ID)) {
            Setting.getPurchases = true;
        } else {
            Setting.getPurchases = false;
        }
    }

    public Bundle getPurchases() {
        if (!bp.isInitialized()) {
            return null;
        }
        try {
            return mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isSubscribe(String SUBSCRIPTION_ID_CHECK) {

        if (!bp.isSubscribed(Setting.SUBSCRIPTION_ID))
            return false;

        Bundle b = getPurchases();
        if (b == null)
            return false;
        if (b.getInt("RESPONSE_CODE") == 0) {
            ArrayList<String> ownedSkus =
                    b.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> purchaseDataList =
                    b.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String> signatureList =
                    b.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken =
                    b.getString("INAPP_CONTINUATION_TOKEN");

            if (purchaseDataList == null) {
                return false;
            }
            if (purchaseDataList.size() == 0) {
                return false;
            }
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku_1 = ownedSkus.get(i);

                try {
                    JSONObject rowOne = new JSONObject(purchaseData);
                    String productId = rowOne.getString("productId");

                    if (productId.equals(SUBSCRIPTION_ID_CHECK)) {

                        Boolean autoRenewing = rowOne.getBoolean("autoRenewing");
                        if (autoRenewing) {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            Long purchaseTime = rowOne.getLong("purchaseTime") / 1000;
                            return true;
                        } else {
                            // Toast.makeText(this, "is not autoRenewing ", Toast.LENGTH_SHORT).show();
                            Long tsLong = System.currentTimeMillis() / 1000;
                            Long purchaseTime = rowOne.getLong("purchaseTime") / 1000;
                            if (tsLong > (purchaseTime + (Setting.SUBSCRIPTION_DURATION * 86400))) {
                                //   Toast.makeText(this, "is Expired ", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                return true;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    private void auth() {
        //for progressbar visibility while login
        // progressBar.setVisibility(View.VISIBLE);
        SOService soService = ApiUtil.getSOService();
        soService.authorization(
                Utils.getSimpleTextBody("client_credentials"),
                Utils.getSimpleTextBody("lbl_android_client"),
                Utils.getSimpleTextBody("ED87A6CB466FED70A833433D98F1ACFBC1F5D2CFE388D38F23EBD7348B7BFC812397CCE141DA7FDF79113F8038D647C13B14DE1CD33106876AAEE8C587CE8D36")).enqueue(new Callback<Authorization>() {
        @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                if (response.isSuccessful())
                {
                    Prefs.putString("access_token", response.body().getAccessToken());
                    Prefs.putBoolean("isToken",true);
                    if (Prefs.getBoolean("isLogin",false))
                    {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this, Login.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t)
            {
                // progressbar become invisible after registeration
                // progressBar.setVisibility(View.GONE);
                //Toast any error
                Log.d("ServerError", t.getMessage() + "");
                Toast.makeText(SplashActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    }
