package com.ingenious.hdwallpapers.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ingenious.hdwallpapers.BuildConfig;
import com.ingenious.hdwallpapers.R;
import com.ingenious.library.SwitchButton.SwitchButton;

import com.ingenious.hdwallpapers.Constant.Constant;
import com.ingenious.Methods.Methods;
import com.ingenious.Methods.NavigationUtil;
import com.ingenious.SharedPref.Setting;
import com.ingenious.SharedPref.SharedPre;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */


public class SettingActivity extends AppCompatActivity {

    private SharedPre sharedPref;
    private SwitchButton switch_dark;
    private LinearLayout about, share,privacy, feedback, lockScreen;
    private TextView version;
    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPre(this);
        if (sharedPref.getNightMode()) {
            setTheme(R.style.AppTheme2);
            Setting.Dark_Mode = true;
        } else {
            setTheme(R.style.AppTheme);
            Setting.Dark_Mode = false;
        }

        if (sharedPref.getIn_Code()) {
            Setting.in_code = true;
        } else {
            Setting.in_code = false;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        methods = new Methods(this);


        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dark_mode();

        version = (TextView) findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);

        about= findViewById(R.id.nav_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.AboutActivity(SettingActivity.this);
            }
        });

        lockScreen= findViewById(R.id.lockScreen);
        lockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(SettingActivity.this, LockScreenActivity.class);
                startActivity(main);
            }
        });

        share  = findViewById(R.id.nav_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });

        privacy  = findViewById(R.id.nav_privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOpnsDialog();
            }
        });

        feedback  = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOpnsDialog2();
            }
        });

    }

    public void openOpnsDialog2() {
        Dialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(SettingActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(SettingActivity.this);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_privacy);

        WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl("file:///android_asset/index.html");

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public void openOpnsDialog() {
        Dialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(SettingActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(SettingActivity.this);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_privacy);

        WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl(Constant.privacy_policy_url);

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void Dark_mode() {
        switch_dark = findViewById(R.id.switch_dark);
        if (sharedPref.getNightMode()) {
            switch_dark.setChecked(true);
        } else {
            switch_dark.setChecked(false);
        }
        switch_dark.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPref.setNightMode(isChecked);
                Apps_recreate();
            }
        });
    }

    private void Apps_recreate() {
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}