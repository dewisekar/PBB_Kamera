package com.example.pbb_kamera;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers("Accept: application/json")
    @POST("dataset/4/{label}")
    @FormUrlEncoded
    Call<FileUpload> uploadFile(
            @Path("label") String label,
            @Field("image") String fileBase
    );
}

