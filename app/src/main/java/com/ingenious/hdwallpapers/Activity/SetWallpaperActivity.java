package com.ingenious.hdwallpapers.Activity;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenious.hdwallpapers.R;
import com.theartofdev.edmodo.cropper.CropImageView;


import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */

public class SetWallpaperActivity extends AppCompatActivity {

    private Methods methods;
    private CropImageView imageView;
    private FloatingActionButton button;
    private Bitmap bmImg;
    private Dialog dialog;
    private BottomSheetDialog dialog_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode ) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwallpaper);

        methods = new Methods(this);

        dialog = new Dialog(SetWallpaperActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_download);
        final TextView textView = dialog.findViewById(R.id.download);
        textView.setText(getResources().getString(R.string.loading));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView imageView_back = findViewById(R.id.iv_back_crop);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView = findViewById(R.id.iv_crop);
        button = findViewById(R.id.button_setwallpaper);

        imageView.setImageUriAsync(Setting.uri_setwall);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SetWall extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            bmImg = imageView.getCroppedImage();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setWallpaperOffsetSteps(0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String locktype = strings[0];
                    switch (locktype) {
                        case "home":
                            myWallpaperManager.setBitmap(bmImg, null, true, WallpaperManager.FLAG_SYSTEM);
                            break;
                        case "lock":
                            myWallpaperManager.setBitmap(bmImg, null, true, WallpaperManager.FLAG_LOCK);
                            break;
                        case "all":
                            myWallpaperManager.setBitmap(bmImg);
                            break;
                    }
                } else {
                    myWallpaperManager.setBitmap(bmImg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("1")) {
                Toast.makeText(SetWallpaperActivity.this, getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SetWallpaperActivity.this, getString(R.string.err_set_wall), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            super.onPostExecute(s);
        }
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.layout_set_wall_options, null);

        dialog_desc = new BottomSheetDialog(this);
        dialog_desc.setContentView(view);
        dialog_desc.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog_desc.show();

        TextView textView_home = dialog_desc.findViewById(R.id.tv_set_home);
        TextView textView_lock = dialog_desc.findViewById(R.id.tv_set_lock);
        TextView textView_all = dialog_desc.findViewById(R.id.tv_set_all);

        textView_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWall().execute("home");
            }
        });

        textView_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWall().execute("lock");
            }
        });

        textView_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWall().execute("all");
            }
        });
    }
}