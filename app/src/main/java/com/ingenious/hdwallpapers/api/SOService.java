package com.ingenious.hdwallpapers.api;

import com.ingenious.Model.Activitytime;
import com.ingenious.Model.Authorization;
import com.ingenious.Model.GlobalResponse;
import com.ingenious.Model.Profile;
import com.ingenious.Model.Registration;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface SOService {

    @Multipart
    @POST("login")
    Call<Profile> login
            (
                    @Part("username") RequestBody username,
                    @Part("password") RequestBody password,
                    @Part("fcm") RequestBody fcm,
                    @Part("package_name") RequestBody package_name
            );

    @Multipart
    @POST("signup")
    Call<Registration> registration
            (
                    @Part("name") RequestBody name,
                    @Part("email") RequestBody email,
                    @Part("username") RequestBody username,
                    @Part("password") RequestBody password,
                    @Part("package_name") RequestBody package_name
            );


    @Multipart
    @POST("auth")
    Call<Authorization> authorization
            (
                    @Part("grant_type") RequestBody grant_type,
                    @Part("client_id") RequestBody client_id,
                    @Part("client_secret") RequestBody client_secret
            );

    @Multipart
    @POST("addActivity")
    Call<Activitytime> addActivity
            (
                    @Part("user_id") RequestBody user_id,
                    @Part("package_name") RequestBody package_name,
                    @Part("activity_time") RequestBody activity_time
            );

    @Multipart
    @POST("update_fcm_token")
    Call<GlobalResponse> update_fcm_token(@Part("user_id") RequestBody user_id,
                                          @Part("token") RequestBody token);
}

