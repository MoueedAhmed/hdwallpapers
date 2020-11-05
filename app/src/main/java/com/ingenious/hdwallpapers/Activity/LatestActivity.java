package com.ingenious.hdwallpapers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import com.ingenious.hdwallpapers.Adapter.AdapterWallpaper;
import com.ingenious.hdwallpapers.EndlessRecycler.EndlessRecyclerViewScrollListener;
import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.InterAdListener;
import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Listener.WallListener;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.hdwallpapers.R;
import com.ingenious.hdwallpapers.asyncTask.LoadWallpaper;
import com.ingenious.items.ItemWallpaper;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://ingenious.pk/   */public class LatestActivity extends AppCompatActivity {

    private Methods methods;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterWallpaper adapter;
    private ArrayList<ItemWallpaper> arrayList, arrayListTemp;
    private Boolean isOver = false, isScroll = false;
    private int page = 1;
    private GridLayoutManager grid;
    private LoadWallpaper load;
    private FloatingActionButton fab;
    private AdView adViewDK;
    private FrameLayout progressBar;
    private int nativeAdPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        methods = new Methods(this);
        adViewDK = findViewById(R.id.adViewDK);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.latest));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Setting.Dark_Mode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp2);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        methods = new Methods(LatestActivity.this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                int real_pos = adapter.getRealPos(position, arrayListTemp);

                Intent intent = new Intent(LatestActivity.this, WallpaperDetailsActivity.class);
                intent.putExtra("pos", real_pos);
                intent.putExtra("pos_type", 3);
                intent.putExtra("page", page);
                Setting.arrayList.clear();
                Setting.arrayList.addAll(arrayListTemp);
                startActivity(intent);
            }
        });


        if(Setting.isAdmobNativeAd) {
            nativeAdPos = Setting.admobNativeShow;
        } else if(Setting.isFBNativeAd) {
            nativeAdPos = Setting.fbNativeShow;
        }


        arrayList = new ArrayList<>();
        arrayListTemp = new ArrayList<>();

        progressBar = findViewById(R.id.load_video);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.tv);

        grid = new GridLayoutManager(LatestActivity.this, 3);
        grid.setSpanCount(3);

        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.getItemViewType(position) >= 1000 || adapter.isHeader(position)) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            getData();
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = grid.findFirstVisibleItemPosition();

                if (firstVisibleItem > 6) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        if (JSONParser.isNetworkCheck()){
            if (JSONParser.isDataCheck()){
                getData();
            }
        }

//        LinearLayout adView = findViewById(R.id.adView);
//        methods.showBannerAd(adView);
        /* DK Code to load Ads */
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewDK.loadAd(adRequest);
    }

    private void getData() {
        load = new LoadWallpaper(new WallListener() {
            @Override
            public void onStart() {
                if(arrayList.size() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemWallpaper> arrayListWell) {
                if (success.equals("1")) {
                    if (!verifyStatus.equals("-1")) {
                        if (arrayListWell.size() == 0) {
                            isOver = true;
                            try {
                                adapter.hideHeader();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            arrayListTemp.addAll(arrayListWell);
                            for (int i = 0; i < arrayListWell.size(); i++) {
                                arrayList.add(arrayListWell.get(i));

                                if(Setting.isAdmobNativeAd || Setting.isFBNativeAd) {
                                    int abc = arrayList.lastIndexOf(null);
                                    if (((arrayList.size() - (abc + 1)) % nativeAdPos == 0) && (arrayListWell.size()-1 != i)) {
                                        arrayList.add(null);
                                    }
                                }
                            }

                            page = page + 1;
                            progressBar.setVisibility(View.INVISIBLE);
                            setAdapter();
                        }
                    } else {
                        methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                    }
                } else {

                }
                progressBar.setVisibility(View.GONE);
            }


        },methods.getAPIRequest(Setting.METHOD_LATEST_WALL, page, "", "", "", "", "", "", "", "", "", "", ""));
        load.execute();

    }

    public void setAdapter() {
        if (!isScroll) {
            adapter = new AdapterWallpaper(LatestActivity.this,  arrayList,  new RecyclerViewClickListener() {
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "");
                }

            });

            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        if(adapter != null) {
            adapter.destroyNativeAds();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
