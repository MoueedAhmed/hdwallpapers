package com.ingenious.hdwallpapers.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ingenious.hdwallpapers.R;
import com.ingenious.library.Ingenious;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import com.ingenious.hdwallpapers.Adapter.AdapterCatHome;
import com.ingenious.hdwallpapers.Adapter.AdapterWallHome;
import com.ingenious.hdwallpapers.DBHelper.DBHelper;
import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.HomeListener;
import com.ingenious.Listener.InterAdListener;

import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Methods.Methods;
import com.ingenious.Methods.NavigationUtil;
import com.ingenious.SharedPref.Setting;
import com.ingenious.SharedPref.SharedPre;

import com.ingenious.hdwallpapers.asyncTask.LoadHome;
import com.ingenious.items.ItemCat;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Methods methods;
    private DBHelper dbHelper;
    private SharedPre sharedPref;
    private Ingenious ingenious;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private RecyclerView  rv_latest, rv_popular, rv_cat, rv_fav, rv_home_recent;
    private AdapterWallHome adapter_latest;
    private AdapterWallHome adapter_popular;
    private AdapterWallHome  adapter_fav;
    private AdapterWallHome  adapter_recent;
    private AdapterCatHome adapterCategories;
    private ArrayList<ItemWallpaper> arrayList_latest;
    private ArrayList<ItemWallpaper>  arrayList_popular;
    private ArrayList<ItemWallpaper>  arrayList_fav;
    private ArrayList<ItemWallpaper>  arrayList_recent;
    private ArrayList<ItemCat> arrayList_cat;
    private LinearLayout wall, category, lin_gif, fav, latest, most_view, recent_lin;

    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private Menu menu;
    private static final String MERCHANT_ID=null;
    IInAppBillingService mService;
    private  Boolean DialogOpened = false;
    private TextView text_view_go_pro;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";
    private RelativeLayout rel_fav1, rel_fav2;
    private TextView seeall_latest, seeall_most, seeall_fav,seeall_cat;
    private ProgressBar progressBar;
    private AdView adViewDK;
    private FirebaseAuth mAuth;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // Toast.makeText(MainActivity.this, "set null", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            //Toast.makeText(MainActivity.this, "set Stub", Toast.LENGTH_SHORT).show();


        }
    };

    private ReviewInfo reviewInfo ;
    private  ReviewManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        if (!Prefs.getBoolean("isLogin",false))
        {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }

        sharedPref = new SharedPre(this);
        if (sharedPref.getIsFirstPurchaseCode()) {
        } else {
            sharedPref.getPurchaseCode();
            sharedPref.getPurchase();
        }
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener( new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                } else {
                    reviewInfo = null;
                }
            }
        });

        initBuy();

        dbHelper = new DBHelper(this);
        methods = new Methods(this);
        ingenious = new Ingenious(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        adViewDK = findViewById(R.id.adViewDK);
        navigationView =  findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        if (Setting.Dark_Mode) {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu2);
        } else {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu1);
        }

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        navigationView.setNavigationItemSelectedListener(this);

        methods = new Methods(MainActivity.this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                switch (type) {
                    case "latest":
                        Setting.arrayList.clear();
                        Setting.arrayList.addAll(arrayList_latest);
                        NavigationUtil.Wallpaper_Details_Activity(MainActivity.this, position, 0);
                        break;
                    case "popular":
                        Setting.arrayList.clear();
                        Setting.arrayList.addAll(arrayList_popular);
                        NavigationUtil.Wallpaper_Details_Activity(MainActivity.this, position, 0);
                        break;
                    case "fav":
                        Setting.arrayList.clear();
                        Setting.arrayList.addAll(arrayList_fav);
                        NavigationUtil.Wallpaper_Details_Activity(MainActivity.this, position, 0);
                        break;
                    case "rec":
                        Setting.arrayList.clear();
                        Setting.arrayList.addAll(arrayList_recent);
                        NavigationUtil.Wallpaper_Details_Activity(MainActivity.this, position, 0);
                        break;
                    case "cat":
                        NavigationUtil.Wall_Cid_Activity(MainActivity.this, arrayList_cat.get(position).getName(), arrayList_cat.get(position).getId());
                        break;
                }
            }
        });

        LinearLayout adView = findViewById(R.id.adView_home);
        methods.showBannerAd(adView);
        recent_lin = findViewById(R.id.recent_lin);
        seeall_latest = findViewById(R.id.seeall_latest);
        seeall_most = findViewById(R.id.seeall_most);
        seeall_fav = findViewById(R.id.seeall_fav);
        seeall_cat = findViewById(R.id.seeall_cat);
        category = findViewById(R.id.category);
        lin_gif = findViewById(R.id.lin_gif);
        fav = findViewById(R.id.fav);
        latest = findViewById(R.id.latest);
        most_view = findViewById(R.id.most_view);
        rel_fav1 = findViewById(R.id.rel_fav1);
        rel_fav2 = findViewById(R.id.rel_fav2);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        wall = findViewById(R.id.wall);
        wall.setVisibility(View.GONE);

        arrayList_latest = new ArrayList<>();
        arrayList_popular = new ArrayList<>();
        arrayList_fav = new ArrayList<>();
        arrayList_cat = new ArrayList<>();
        arrayList_fav.clear();
        arrayList_recent = new ArrayList<>();
        arrayList_recent.clear();

        rv_home_recent = findViewById(R.id.rv_home_recent);
        rv_home_recent.setHasFixedSize(true);
        LinearLayoutManager llm5 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_home_recent.setLayoutManager(llm5);


        rv_latest = findViewById(R.id.rv_home_latest);
        rv_latest.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_latest.setLayoutManager(llm2);

        rv_popular = findViewById(R.id.rv_home_popular);
        rv_popular.setHasFixedSize(true);
        LinearLayoutManager llm3 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_popular.setLayoutManager(llm3);

        rv_fav = findViewById(R.id.rv_home_fav);
        rv_fav.setHasFixedSize(true);
        LinearLayoutManager fav_l = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_fav.setLayoutManager(fav_l);

        rv_cat = findViewById(R.id.rv_home_cat);
        rv_cat.setHasFixedSize(true);
        LinearLayoutManager llm_cat = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_cat.setLayoutManager(llm_cat);

        if (Setting.arrayList_Home_wall_1.size() == 0) {
            if (JSONParser.isNetworkCheck()){
                if (JSONParser.isDataCheck()){
                    getWallpapers();
                }
            }
        }else {
            arrayList_latest.addAll( Setting.arrayList_Home_wall_1);
            arrayList_popular.addAll(Setting.arrayList_Home_wall_2);
            arrayList_cat.addAll(Setting.arrayList_Home_cat);
            try {
                arrayList_fav.addAll(dbHelper.getWallpapers(DBHelper.TABLE_WALL_BY_FAV,"10"));
                arrayList_recent.addAll(dbHelper.getWallpapers(DBHelper.TABLE_WALL_BY_RECENT,"10"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SetAdapter();
        }

        seeall_latest.setOnClickListener(this);
        seeall_most.setOnClickListener(this);
        seeall_fav.setOnClickListener(this);
        seeall_cat.setOnClickListener(this);
        category.setOnClickListener(this);
        lin_gif.setOnClickListener(this);
        fav.setOnClickListener(this);
        latest.setOnClickListener(this);
        most_view.setOnClickListener(this);


        AdRequest adRequest = new AdRequest.Builder().build();
        adViewDK.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Prefs.getBoolean("isLogin",false))
        {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            Toast.makeText(MainActivity.this, "Thanks for the feedback!", Toast.LENGTH_LONG).show();
                        }
                    });
                    flow.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this, "feedback Failure !", Toast.LENGTH_LONG).show();
                        }
                    });
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Completed!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            },3000);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all:
                NavigationUtil.LatestActivity(this);
                break;
            case R.id.nav_most_view:
                NavigationUtil.MostActivity(this);
                break;
            case R.id.nav_cat:
                NavigationUtil.CategoryActivity(this);
                break;
            case R.id.nav_gif:
                NavigationUtil.GifActivity(this);
                break;
            case R.id.nav_favourite:
                NavigationUtil.FavouriteActivity(this);
                break;
            case R.id.nav_set:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(), UserData.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seeall_latest:
                NavigationUtil.LatestActivity(this);
                break;
            case R.id.seeall_most:
                NavigationUtil.MostActivity(this);
                break;
            case R.id.seeall_fav:
                NavigationUtil.FavouriteActivity(this);
                break;
            case R.id.seeall_cat:
                NavigationUtil.CategoryActivity(this);
                break;
            case R.id.category:
                NavigationUtil.CategoryActivity(this);
                break;
            case R.id.lin_gif:
                NavigationUtil.GifActivity(this);
                break;
            case R.id.fav:
                NavigationUtil.FavouriteActivity(this);
                break;
            case R.id.latest:
                NavigationUtil.LatestActivity(this);
                break;
            case R.id.most_view:
                NavigationUtil.MostActivity(this);
                break;
        }
    }



    private void getWallpapers() {
        if (methods.isNetworkAvailable()) {
            LoadHome loadHome = new LoadHome(MainActivity.this, new HomeListener() {
                @Override
                public void onStart() {
                    arrayList_latest.clear();
                    arrayList_popular.clear();
                    arrayList_cat.clear();
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListLatest, ArrayList<ItemWallpaper> arrayListpopular, ArrayList<ItemCat> arrayListcat) {
                    if (MainActivity.this != null) {
                        arrayList_latest.addAll(arrayListLatest);
                        arrayList_popular.addAll(arrayListpopular);
                        arrayList_cat.addAll(arrayListcat);
                        Setting.arrayList_Home_wall_1.addAll(arrayListLatest);
                        Setting.arrayList_Home_wall_2.addAll(arrayListpopular);
                        Setting.arrayList_Home_cat.addAll(arrayListcat);
                        try {
                            arrayList_fav.addAll(dbHelper.getWallpapers(DBHelper.TABLE_WALL_BY_FAV,"10"));
                            arrayList_recent.addAll(dbHelper.getWallpapers(DBHelper.TABLE_WALL_BY_RECENT,"10"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SetAdapter();

                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }

            }, methods.getAPIRequest(Setting.METHOD_HOME, 0, "", "", "", "", "", "", "", "", "", "", ""));
            loadHome.execute();

        }
    }

    private void SetAdapter() {
        adapter_latest = new AdapterWallHome(MainActivity.this,  arrayList_latest, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "latest");
            }
        });
        AnimationAdapter adapterAnim_land = new SlideInRightAnimationAdapter(adapter_latest);
        adapterAnim_land.setFirstOnly(true);
        adapterAnim_land.setDuration(500);
        adapterAnim_land.setInterpolator(new OvershootInterpolator(.9f));
        rv_latest.setAdapter(adapterAnim_land);

        adapter_popular = new AdapterWallHome(MainActivity.this,  arrayList_popular, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "popular");
            }
        });
        AnimationAdapter adapterAnim_square = new SlideInRightAnimationAdapter(adapter_popular);
        adapterAnim_square.setFirstOnly(true);
        adapterAnim_square.setDuration(500);
        adapterAnim_square.setInterpolator(new OvershootInterpolator(.9f));
        rv_popular.setAdapter(adapterAnim_square);

        if (arrayList_fav.size() == 0) {
            rel_fav1.setVisibility(View.GONE);
            rel_fav2.setVisibility(View.GONE);
        }else {
            adapter_fav = new AdapterWallHome(MainActivity.this,  arrayList_fav, new RecyclerViewClickListener() {
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "fav");
                }
            });
            AnimationAdapter adapterAnim_fav = new SlideInRightAnimationAdapter(adapter_fav);
            adapterAnim_fav.setFirstOnly(true);
            adapterAnim_fav.setDuration(500);
            adapterAnim_fav.setInterpolator(new OvershootInterpolator(.9f));
            rv_fav.setAdapter(adapterAnim_fav);
        }

        adapterCategories = new AdapterCatHome(MainActivity.this, arrayList_cat, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInter(position, "cat");
            }
        });
        AnimationAdapter adapterAnim4 = new SlideInRightAnimationAdapter(adapterCategories);
        adapterAnim4.setFirstOnly(true);
        adapterAnim4.setDuration(500);
        adapterAnim4.setInterpolator(new OvershootInterpolator(.9f));
        rv_cat.setAdapter(adapterAnim4);


        if (arrayList_recent.size() == 0) {
            recent_lin.setVisibility(View.GONE);
        }else {
            adapter_recent = new AdapterWallHome(MainActivity.this,  arrayList_recent, new RecyclerViewClickListener() {
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "rec");
                }
            });
            AnimationAdapter adapterAnim_fav = new SlideInRightAnimationAdapter(adapter_recent);
            adapterAnim_fav.setFirstOnly(true);
            adapterAnim_fav.setDuration(500);
            adapterAnim_fav.setInterpolator(new OvershootInterpolator(.9f));
            rv_home_recent.setAdapter(adapterAnim_fav);
        }

        progressBar.setVisibility(View.GONE);
        wall.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MainActivity.this.menu = menu;
        if(Setting.in_app){
            if(Setting.getPurchases){
                menu.clear();
            }
        }else {
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_pro :
                showDialog_pay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if(!BillingProcessor.isIabServiceAvailable(MainActivity.this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(MainActivity.this, Setting.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                //  showToast("onProductPurchased: " + productId);
                Intent intent= new Intent(MainActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                // showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
                //  showToast("onBillingInitialized");
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                // showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    private void updateTextViews() {
        SharedPre prf= new SharedPre(getApplicationContext());
        bp.loadOwnedPurchasesFromGoogle();
    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            //  Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return null;
        }
        try{
            // Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            //  Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }


    public void showDialog_pay(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_subscribe, null);

        final BottomSheetDialog dialog_setas = new BottomSheetDialog(MainActivity.this);
        dialog_setas.setContentView(view);
        dialog_setas.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        this.text_view_go_pro=(TextView) dialog_setas.findViewById(R.id.text_view_go_pro);
        RelativeLayout relativeLayout_close_rate_gialog=(RelativeLayout) dialog_setas.findViewById(R.id.relativeLayout_close_rate_gialog);
        relativeLayout_close_rate_gialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setas.dismiss();
            }
        });
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(MainActivity.this, Setting.SUBSCRIPTION_ID);
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
        DialogOpened=true;



    }

}
