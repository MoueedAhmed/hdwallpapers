package com.ingenious.hdwallpapers.Activity;

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

import com.ingenious.hdwallpapers.R;
import com.pixplicity.easyprefs.library.Prefs;

import com.ingenious.Model.Registration;
import com.ingenious.hdwallpapers.api.ApiUtil;
import com.ingenious.hdwallpapers.api.SOService;
import com.ingenious.hdwallpapers.api.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    private EditText fullname,email,usrname,pass;
    private TextView signin;
    private Button signup;
    private ProgressBar progressBar;
    CustomProgressDialogue dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dialog = new CustomProgressDialogue(Signup.this);
        fullname = findViewById(R.id.etFullname);
        email = findViewById(R.id.etEmail);
        usrname = findViewById(R.id.etusername);
        pass = findViewById(R.id.etPassword);
        signup=findViewById(R.id.btnSignUp);
        signin=findViewById(R.id.txtSignIn);



        //SIGN IN button function
        signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);

            }
        });

        //SIGN UP button function
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userSignup();
            }
        });
    }

    private void userSignup()
    {
        String Fullname = fullname.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String name = usrname.getText().toString().trim();
        String passw = pass.getText().toString().trim();
        if (Fullname.isEmpty())
        {
            fullname.setError("Name is required");
            fullname.requestFocus();
            return;
        }
        if (Email.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (name.isEmpty())
        {
            usrname.setError("phone number is required");
            usrname.requestFocus();
            return;
        }
        if (passw.isEmpty())
        {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        registration();
    }

    private void registration()
    {
        //for progressbar visibility while registeration
        dialog.show();
        SOService soService = ApiUtil.getSOService();
        soService.registration(Utils.getSimpleTextBody(fullname.getText().toString()),
                Utils.getSimpleTextBody(email.getText().toString()),
                Utils.getSimpleTextBody(usrname.getText().toString()),
                Utils.getSimpleTextBody(pass.getText().toString()),
                Utils.getSimpleTextBody(getPackageName())).enqueue(new Callback<Registration>()
        {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response)
            {
                // progressbar become invisible after registeration
                dialog.cancel();
                Log.d("ServerResponse", response + "");
                if (response.isSuccessful()) {

                    if (response.body().getSuccess())
                    {
                        //Registration success show msg
                        Prefs.putBoolean("isLogin",true);
                        Prefs.putString("user_id",response.body().getUserId());
                        //same for username and other data
                        Prefs.putString("user_name",response.body().getName());
                        Prefs.putString("email",response.body().getEmail());
                        Prefs.putString("user_type",response.body().getUserType());

                        Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, MainActivity.class));
                        finish();
                    } else
                    {
                        //Error while registration
                        Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t)
            {
                // progressbar become invisible after registeration
               dialog.show();
                //Toast any error
                Log.d("ServerError", t.getMessage() + "");
                Toast.makeText(Signup.this, "Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
