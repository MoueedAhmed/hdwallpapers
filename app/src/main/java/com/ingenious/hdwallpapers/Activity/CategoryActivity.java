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

import java.util.ArrayList;

import com.ingenious.hdwallpapers.Adapter.AdapterCategories;
import com.ingenious.hdwallpapers.EndlessRecycler.EndlessRecyclerViewScrollListener;
import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.CategoryListener;
import com.ingenious.Listener.InterAdListener;
import com.ingenious.Listener.RecyclerViewClickListener;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.hdwallpapers.R;
import com.ingenious.hdwallpapers.asyncTask.LoadCategory;
import com.ingenious.items.ItemCat;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */



public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterCategories adapter;
    private ArrayList<ItemCat> arrayList;
    private Methods methods;
    private Boolean isOver = false, isScroll = false;
    private int page = 1;
    private AdView adViewDK;
    private GridLayoutManager grid;
    private FrameLayout progressBar;

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

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.category));
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


        methods = new Methods(CategoryActivity.this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Intent intent_view = new Intent(CategoryActivity.this, WallCIDActivity.class);
                intent_view.putExtra("name", arrayList.get(position).getName());
                intent_view.putExtra("cid", arrayList.get(position).getId());
                startActivity(intent_view);
            }
        });

        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.load_video);
        adViewDK = findViewById(R.id.adViewDK);
        recyclerView = findViewById(R.id.tv);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(CategoryActivity.this, 3);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if(!isOver) {
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
        LoadCategory loadCategory = new LoadCategory(new CategoryListener(){
            @Override
            public void onStart() {
                if(arrayList.size() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(String success, ArrayList<ItemCat> arrayListWall) {
                if(arrayListWall.size() == 0) {
                    isOver = true;
                    try {
                        adapter.hideHeader();
                    }catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    page = page + 1;
                    arrayList.addAll(arrayListWall);
                    progressBar.setVisibility(View.INVISIBLE);
                    setAdapter();
                }
            }
        },methods.getAPIRequest(Setting.METHOD_CAT, page, "", "", "", "", "", "", "", "", "", "", ""));
        loadCategory.execute();

    }

    public void setAdapter() {
        if(!isScroll) {
            adapter = new AdapterCategories(CategoryActivity.this, arrayList , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {
                    methods.showInter(position,"");
                }
            });
            recyclerView.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
