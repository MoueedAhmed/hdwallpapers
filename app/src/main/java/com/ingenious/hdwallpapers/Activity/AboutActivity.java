package com.ingenious.hdwallpapers.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ingenious.Methods.Methods;
import com.ingenious.SharedPref.Setting;
import com.ingenious.hdwallpapers.R;

/**
 * Company : Ingenious
 * Detailed : Software Development Company in Pakistan
 * Developer : Ingenious
 * Contact : Ingenious.mcc@gmail.com
 * Website : https://www.ingenious.pk/
 */


public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_company, tv_email, tv_website, tv_contact;
    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Setting.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        methods = new Methods(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.about));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_company = (TextView) findViewById(R.id.company);
        tv_email = (TextView) findViewById(R.id.email);
        tv_website = (TextView) findViewById(R.id.website);
        tv_contact = (TextView) findViewById(R.id.contact);

        tv_company.setText(Setting.company);
        tv_email.setText(Setting.email);
        tv_website.setText(Setting.website);
        tv_contact.setText(Setting.contact);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}