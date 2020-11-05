package com.ingenious.hdwallpapers.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ingenious.hdwallpapers.R;
import com.pixplicity.easyprefs.library.Prefs;

import com.ingenious.SharedPref.SharedPreferencesHelper;

public class UserData extends AppCompatActivity {

    private boolean isLogged = false;
    private TextView txtUserEmail, txtUserFullname;
    private Button LogOut;
    private FirebaseAuth mAuth;
    private String provider;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        isLogged = Prefs.getBoolean("isLogin",false);
        provider = SharedPreferencesHelper.getString("provider", "");
        mAuth = FirebaseAuth.getInstance();
        txtUserEmail=findViewById(R.id.txtUserEmail);
        txtUserFullname=findViewById(R.id.txtUserFullname);
        img=findViewById(R.id.imgUserPhoto);
        LogOut=findViewById(R.id.btnLogOut);

        txtUserFullname.setText(Prefs.getString("user_name", ""));
        txtUserEmail.setText(Prefs.getString("email", ""));
        Glide.with(this).load(R.drawable.logo).into(img);

//        LogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Prefs.remove("isLogin");
//                SharedPreferences myPrefs = getSharedPreferences("Activity",
//                        MODE_PRIVATE);
//                SharedPreferences.Editor editor = myPrefs.edit();
//                editor.clear();
//                editor.commit();
//                Intent intent = new Intent(UserData.this,
//                        Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//
//            }
//        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPrefs = getSharedPreferences("Activity",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
//                SharedPreferencesHelper.setBoolean("isLogged",false); //set logged false
                Prefs.remove("isLogin");
                Intent intent = new Intent(UserData.this,
                        Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}