package com.example.pbb_kamera;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers("Accept: application/json")
    @Multipart
    @POST("ppb-predict/7/predict")
    Call<String> predict(@Part MultipartBody.Part image);
}

