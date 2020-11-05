package com.ingenious.hdwallpapers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ingenious.hdwallpapers.R;
import com.pixplicity.easyprefs.library.Prefs;

import com.ingenious.Model.Profile;
import com.ingenious.hdwallpapers.api.ApiUtil;
import com.ingenious.hdwallpapers.api.SOService;
import com.ingenious.hdwallpapers.api.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity
{
    private Button btnsignup,btnsignin;
    private EditText username, pass;
    private TextView txtForgotPassword;
    private ProgressBar progressBar;
    private String FCM_Token;
    CustomProgressDialogue dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.etuid);
        pass = findViewById(R.id.etpass);
        btnsignup=findViewById(R.id.btnSign_Up);
        btnsignin=findViewById(R.id.btnSignIn);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        dialog = new CustomProgressDialogue(Login.this);

        //FCM function
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
        {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task)
            {
                if (!task.isSuccessful())
                {
                    Log.d("FCM_Token_Failed", "getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                Log.d("FCM_TOKEN", task.getResult().getToken());
                FCM_Token = task.getResult().getToken();
            }
        });

        //SIGN IN button function
        btnsignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userSignin();
            }
        });

        //SIGN UP button function
        btnsignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Uri uri=Uri.parse("https://www.learnbuildlead.com/lbl/LeadUpSignup");
//                Intent i=new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(i);
                Intent intent = new Intent(Login.this,Signup.class);
                startActivity(intent);
            }
        });

    }

    //sign in alert function
    private void userSignin() {
        String Username = username.getText().toString().trim();
        String password = pass.getText().toString().trim();
        if (Username.isEmpty()) {
            username.setError("User name is required");
            username.requestFocus();
        } else if (password.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
        } else {
            signin();
        }
    }

    //sending request to server
    private void signin()
    {
        dialog.show();
        SOService soService = ApiUtil.getSOService();
        soService.login(
                Utils.getSimpleTextBody(username.getText().toString()),
                Utils.getSimpleTextBody(pass.getText().toString()),
                Utils.getSimpleTextBody(FCM_Token),
                Utils.getSimpleTextBody(getPackageName())).enqueue(new Callback<Profile>()
        {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response)
            {
                dialog.cancel();
                Log.d("ServerResponse", response + "");
                if (response.isSuccessful())
                {
                    // progressbar become invisible after registeration
//                    progressBar.setVisibility(View.GONE);
                    if (response.body().getSuccess())
                    {
                        //Registration success show msg
                        Prefs.putBoolean("isLogin", true);
                        Prefs.putString("user_id", response.body().getUserId());
                        //same for username and other data
                        Prefs.putString("user_name", response.body().getName());
                        Prefs.putString("email", response.body().getEmail());
                        Prefs.putString("user_type", response.body().getUserType());

                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else
                    {
                        //Error while registration
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t)
            {
                dialog.cancel();
                Log.d("ServerError", t.getMessage() + "");
                Toast.makeText(Login.this, "Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}
