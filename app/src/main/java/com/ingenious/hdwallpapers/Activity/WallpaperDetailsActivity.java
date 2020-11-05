package com.ingenious.hdwallpapers.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ingenious.hdwallpapers.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import com.ingenious.library.frescozoomablelib.zoomable.DoubleTapGestureListener;
import com.ingenious.library.frescozoomablelib.zoomable.ZoomableController;
import com.ingenious.library.frescozoomablelib.zoomable.ZoomableDraweeView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


import com.ingenious.hdwallpapers.DBHelper.DBHelper;
import com.ingenious.JSONParser.JSONParser;
import com.ingenious.Listener.GetRatingListener;
import com.ingenious.Listener.InterAdListener;
import com.ingenious.Listener.RatingListener;
import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.SharedPref.SharedPre;
import com.ingenious.hdwallpapers.asyncTask.GetRating;
import com.ingenious.hdwallpapers.asyncTask.LoadRating;
import okhttp3.RequestBody;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */

public class WallpaperDetailsActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    private Toolbar toolbar;
    private DBHelper dbHelper;
    private Methods methods;
    private SharedPre sharedPre;
    private ViewPager viewpager;
    private ImagePagerAdapter pagerAdapter;
    private int position;
    private int height = 0, pos_type = 0, page = 2;
    private String  cid = "1";
    private ImageView option_toggle, option_toggle2;
    private TextView details_cat_name, details_views, details_resolution,
             details_size, details_downloads, details_tipe, details_set, details_share;
    private LinearLayout ll_download, ll_share, ll_rate, ll_setas, ll_option_toggle, down_arrow_he;
    private LikeButton button_fav;
    private RelativeLayout coordinatorLayout;
    private Animation anim_top,anim_top2;
    private Dialog dialog_rate;
    private RatingBar ratingBar;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode ) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_details);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        methods = new Methods(this);
        dbHelper = new DBHelper(this);
        sharedPre = new SharedPre(this);

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Save_pre(type);

            }
        });

        toolbar = findViewById(R.id.toolbar_wall_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 55;
            toolbar.setLayoutParams(params);
        }

        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        coordinatorLayout = findViewById(R.id.rl);

        ll_download = findViewById(R.id.ll_download);
        ll_share = findViewById(R.id.ll_share);
        ll_rate = findViewById(R.id.ll_rate);
        ll_setas = findViewById(R.id.ll_setas);
        ll_option_toggle = findViewById(R.id.ll_option_toggle);
        down_arrow_he= findViewById(R.id.down_arrow_he);


        if (sharedPre.getDown_Arrow()){
            down_arrow_he.setVisibility(View.VISIBLE);
        }else {
            down_arrow_he.setVisibility(View.GONE);
        }


        button_fav = findViewById(R.id.button_wall_fav);

        details_cat_name = findViewById(R.id.details_cat_name);
        details_views = findViewById(R.id.details_views);
        details_resolution = findViewById(R.id.details_resolution);
        details_size = findViewById(R.id.details_size);
        details_downloads = findViewById(R.id.details_downloads);
        details_tipe = findViewById(R.id.details_tipe);
        ratingBar = findViewById(R.id.details_rating);

        details_set = findViewById(R.id.details_set);
        details_share = findViewById(R.id.details_share);


        position = getIntent().getIntExtra("pos", 0);
        pos_type = getIntent().getIntExtra("pos_type", 0);
        page = getIntent().getIntExtra("page", 0);
        if(pos_type == 3){
            cid = getIntent().getStringExtra("cid");
        }

        height = methods.getScreenHeight();

        load_well_Viewed(position);
        Details_tipe(position);

        setTotalView(Setting.arrayList.get(position).getTotalViews());
        details_cat_name.setText(Setting.arrayList.get(position).getCName());
        ratingBar.setRating(Float.parseFloat(Setting.arrayList.get(position).getAverageRate()));

        pagerAdapter = new ImagePagerAdapter();
        viewpager = findViewById(R.id.vp_wall_details);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(position);


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                position = viewpager.getCurrentItem();
                checkFav();
                ratingBar.setRating(Float.parseFloat(Setting.arrayList.get(position).getAverageRate()));
                setTotalView(Setting.arrayList.get(position).getTotalViews());
                details_cat_name.setText(Setting.arrayList.get(position).getCName());
                details_resolution.setText(Setting.arrayList.get(position).getResolution());
                details_size.setText(Setting.arrayList.get(position).getSize());

                Details_tipe(position);
                load_well_Viewed(position);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int position) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });

        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    methods.showInter(0, "download");
                }
            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    methods.showInter(0, "share");
                }
            }
        });

        ll_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRateDialog();
            }
        });


        ll_setas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    methods.showInter(0, "set");
                }
            }
        });

        option_toggle = findViewById(R.id.iv_option_toggle);
        option_toggle2 = findViewById(R.id.iv_option_toggle2);
        anim_top = AnimationUtils.loadAnimation(WallpaperDetailsActivity.this, R.anim.fade);
        anim_top.setFillAfter(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                option_toggle.startAnimation(anim_top);
            }
        }, 500);

        anim_top2 = AnimationUtils.loadAnimation(WallpaperDetailsActivity.this, R.anim.fade);
        anim_top2.setFillAfter(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                option_toggle2.startAnimation(anim_top2);
            }
        }, 1000);

        button_fav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHelper.addtoFavorite(Setting.arrayList.get(viewpager.getCurrentItem()));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHelper.removeFav(Setting.arrayList.get(viewpager.getCurrentItem()).getId());
            }
        });

        checkFav();
        showOptionDialog();
    }

    private void Save_pre(String type) {
        String pay_name = Setting.arrayList.get(viewpager.getCurrentItem()).getPay();
        if (pay_name.equals("premium")){
            if (Setting.getPurchases){
                switch (type) {
                    case "download":
                        new SaveTask("save").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                    case "share":
                        new MyTask("share", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SHARE, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                        new SaveTask("share").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                    case "set":
                        new MyTask("set", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SET, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                        new SaveTask("set").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                }
            }else {
                if (Setting.in_app){
                    methods.ShowDialog_pay();
                }else {
                    switch (type) {
                        case "download":
                            new SaveTask("save").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                            break;
                        case "share":
                            new MyTask("share", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SHARE, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                            new SaveTask("share").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                            break;
                        case "set":
                            new MyTask("set", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SET, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                            new SaveTask("set").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                            break;
                    }
                }
            }
        }else {
            switch (type) {
                case "download":
                    new SaveTask("save").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                    break;
                case "share":
                    new MyTask("share", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SHARE, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                    new SaveTask("share").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                    break;
                case "set":
                    new MyTask("set", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_SET, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                    new SaveTask("set").execute(Setting.arrayList.get(viewpager.getCurrentItem()).getImage());
                    break;
            }
        }
    }

    private void showOptionDialog() {
        LinearLayout llBottomSheet = findViewById(R.id.ll_hideshow);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                ll_option_toggle.setRotation(v * 180);

                if (sharedPre.getDown_Arrow()){
                    sharedPre.setDown_Arrow(false);
                }

                option_toggle.startAnimation(anim_top);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        option_toggle2.startAnimation(anim_top2);
                    }
                }, 500);

            }
        });
    }

    private void Details_tipe(int pos) {
        final String finalUrl = Setting.arrayList.get(pos).getImage();

        if (finalUrl.endsWith("_Other")) {
            finalUrl.replace("_Other", "");
        }

        if (finalUrl.endsWith(".jpg") || finalUrl.endsWith(".JPG")) {
            details_tipe.setText("JPEG");
        }
        if (finalUrl.endsWith(".png") || finalUrl.endsWith(".PNG")) {
            details_tipe.setText("PNG");
        }
        if (finalUrl.endsWith(".bmp") || finalUrl.endsWith(".BMP")) {
            details_tipe.setText("BMP");
        }

    }

    public void checkFav() {
        button_fav.setLiked(dbHelper.isFav(Setting.arrayList.get(viewpager.getCurrentItem()).getId()));
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return Setting.arrayList.size();

        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.layout_zoom_wall, container, false);
            assert imageLayout != null;

            final ZoomableDraweeView draweeView = imageLayout.findViewById(R.id.my_image_view);
            draweeView.setTapListener(new DoubleTapGestureListener(draweeView));

            final ProgressBar progressBar = imageLayout.findViewById(R.id.pb_wall_details);

            Uri uri = Uri.parse(Setting.arrayList.get(position).getImage());

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setCallerContext("ZoomableApp-MyPagerAdapter")
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                            progressBar.setVisibility(View.GONE);
                            super.onFinalImageSet(id, imageInfo, animatable);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            progressBar.setVisibility(View.GONE);
                            super.onFailure(id, throwable);
                        }
                    })
                    .build();
            draweeView.setController(controller);

            draweeView.setSwipeDownListener(new ZoomableController.OnSwipeDownListener() {
                @Override
                public void onSwipeDown(float translateY) {

                }

                @Override
                public void onSwipeRelease(float translateY) {
                    int a = (int) ((translateY / height) * 100);
                    if (a > 25) {
                        onBackPressed();
                        overridePendingTransition(0, android.R.anim.fade_out);
                    }
                }
            });

            container.addView(imageLayout, 0);

            return imageLayout;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public Boolean checkPer() {
        if ((ContextCompat.checkSelfPermission(WallpaperDetailsActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }


    public class SaveTask extends AsyncTask<String, String, String> {
        private Dialog dialog;
        URL myFileUrl;
        String option;
        File file;

        SaveTask(String option) {
            this.option = option;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(WallpaperDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_download);

            final TextView textView = dialog.findViewById(R.id.download);

            if (option.equals("save")) {
                textView.setText(getResources().getString(R.string.downloading_wallpaper));
            } else {
                textView.setText(getResources().getString(R.string.please_wait));
            }

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                String url = args[0];
                myFileUrl = new URL(url);
                String path = myFileUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Wallpapers");
                dir.mkdirs();
                file = new File(dir, fileName);

                if (!file.exists()) {

                    InputStream is;
                    if (Setting.SERVER_URL.contains("https://")) {
                        HttpsURLConnection conn = (HttpsURLConnection) myFileUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        is = conn.getInputStream();
                    } else {
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        is = conn.getInputStream();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    int count;
                    while ((count = is.read(data)) != -1) {
                        if (isCancelled()) {
                            is.close();
                            return null;
                        }
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                    fos.close();

                    if (option.equals("save")) {
                        MediaScannerConnection.scanFile(WallpaperDetailsActivity.this, new String[]{file.getAbsolutePath()},
                                null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                    }
                                });
                    }
                    return "1";
                } else {
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String args) {
            if (args.equals("1") || args.equals("2")) {
                switch (option) {
                    case "save":
                        if (args.equals("2")) {
                            methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.wallpaper_already_saved));
                        } else {
                            new MyTask("download", viewpager.getCurrentItem(), methods.getAPIRequest(Setting.METHOD_WALL_DOWNLOAD, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), "", "", "", "", "", "", "")).execute();
                            methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.wallpaper_saved));
                            methods.showInterDownload();
                        }
                        break;
                    case "set":
                        Setting.file = file;
                        Setting.uri_setwall = Uri.fromFile(file);
                        Intent intent = new Intent(WallpaperDetailsActivity.this, SetWallpaperActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.get_more_wall) + "\n" + getString(R.string.app_name) + " - " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_wallpaper)));
                        dialog.dismiss();
                        break;
                }
            } else {
                methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.please_try_again));
            }
            dialog.dismiss();
        }
    }

    private void load_well_Viewed(int pos) {
        try {
            dbHelper.addToRecent(Setting.arrayList.get(pos));
            if (methods.isNetworkAvailable()) {
                new MyTask("", pos, methods.getAPIRequest(Setting.METHOD_WALL_SINGLE, 0, "", "", "", Setting.arrayList.get(pos).getId(), "", "", "", "", "", "", "")).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        RequestBody requestBody;
        int pos;
        String downloads = "", type = "", res = "", size = "", total_set = "", total_share = "";

        MyTask(String type, int pos, RequestBody requestBody) {
            this.type = type;
            this.pos = pos;
            this.requestBody = requestBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = JSONParser.okhttpPost(Setting.SERVER_URL, requestBody);
            try {
                JSONObject jOb = new JSONObject(json);
                JSONArray jsonArray = jOb.getJSONArray(Setting.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);
                    downloads = objJson.getString(Setting.TAG_WALL_DOWNLOADS);
                    res = objJson.getString(Setting.TAG_RESOLUTION);
                    size = objJson.getString(Setting.TAG_SIZE);

                    total_set = objJson.getString("total_set");
                    total_share = objJson.getString("total_share");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(pos);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int p = Integer.parseInt(result);
            Setting.arrayList.get(p).setTotalDownloads(downloads);
            setTotalDownloads(downloads);
            setTotalSet(total_set);
            setTotalShare(total_share);
            details_downloads.setText(downloads);
            if (!type.equals("download")) {
                int tot = Integer.parseInt(Setting.arrayList.get(p).getTotalViews());
                Setting.arrayList.get(p).setTotalViews("" + (tot + 1));
            }

            if (pos == viewpager.getCurrentItem()) {
                details_resolution.setText(res);
                details_size.setText(size);
            }
            Setting.arrayList.get(p).setResolution(res);
            Setting.arrayList.get(p).setSize(size);

            try {
                dbHelper.updateView(Setting.arrayList.get(p).getId(), Setting.arrayList.get(p).getTotalViews(), Setting.arrayList.get(p).getTotalDownloads(), Setting.arrayList.get(p).getResolution(), Setting.arrayList.get(p).getSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setTotalShare(String total_share) {
        try {
            details_share.setText(methods.format(Double.parseDouble(total_share)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTotalSet(String total_set) {
        try {
            details_set.setText(methods.format(Double.parseDouble(total_set)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTotalView(String views) {
        try {
            details_views.setText(methods.format(Double.parseDouble(views)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTotalDownloads(String downloads) {
        try {
            details_downloads.setText(methods.format(Double.parseDouble(downloads)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openRateDialog() {
        dialog_rate = new Dialog(WallpaperDetailsActivity.this);
        dialog_rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_rate.setContentView(R.layout.layout_rating);

        final ImageView iv_close = dialog_rate.findViewById(R.id.iv_rate_close);
        final RatingBar ratingBar = dialog_rate.findViewById(R.id.rating_add);
        ratingBar.setRating(1);
        final Button button = dialog_rate.findViewById(R.id.button_submit_rating);
        final TextView textView = dialog_rate.findViewById(R.id.tv_rate_dialog);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Setting.arrayList.get(viewpager.getCurrentItem()).getUserRating().equals("0")) {
            new GetRating(new GetRatingListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(String success, String message, float rating) {
                    if (rating != 0 && success.equals("true")) {
                        ratingBar.setRating(rating);
                        textView.setText(getString(R.string.thanks_for_rating));
                    } else {
                        textView.setText(getString(R.string.rate_this_wall));
                    }
                }
            }, methods.getAPIRequest(Setting.METHOD_WALL_GET_RATING, 0, "", "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(),"", "", "", "", deviceId, "", "")).execute();
        } else {
            textView.setText(getString(R.string.thanks_for_rating));
            ratingBar.setRating(Float.parseFloat(Setting.arrayList.get(viewpager.getCurrentItem()).getUserRating()));
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() != 0) {
                    if (methods.isNetworkAvailable()) {
                       loadRatingApi(String.valueOf(ratingBar.getRating()));
                    } else {
                        methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.internet_not_connected));
                    }
                } else {
                    Toast.makeText(WallpaperDetailsActivity.this, getString(R.string.enter_rating), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog_rate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_rate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_rate.show();
        Window window = dialog_rate.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void loadRatingApi(final String rate) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(WallpaperDetailsActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));

        LoadRating loadRating = new LoadRating(new RatingListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message, float rating) {
                if (success.equals("true")) {
                    methods.showSnackBar(coordinatorLayout, message);
                    if (!message.contains("already")) {
                        Setting.arrayList.get(viewpager.getCurrentItem()).setAverageRate(String.valueOf(rating));
                        Setting.arrayList.get(viewpager.getCurrentItem()).setTotalRate(String.valueOf(Integer.parseInt(Setting.arrayList.get(viewpager.getCurrentItem()).getTotalRate()) + 1));
                        Setting.arrayList.get(viewpager.getCurrentItem()).setUserRating(String.valueOf(rate));
                        ratingBar.setRating(rating);
                    }
                } else {
                    methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.server_no_conn));
                }
                dialog_rate.dismiss();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, methods.getAPIRequest(Setting.METHOD_WALL_RATING, 0, "",  "", "", Setting.arrayList.get(viewpager.getCurrentItem()).getId(), rate, "", "", "", deviceId, "",""));
        loadRating.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}