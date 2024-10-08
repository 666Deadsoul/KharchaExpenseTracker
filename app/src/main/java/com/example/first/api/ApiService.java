package com.example.first.api;

import retrofit2.Call;
import retrofit2.http.POST;


import retrofit2.http.Body;

public interface ApiService {
    @POST("registerApi.php")
    Call<ApiResponse> registerUser(@Body User user);
}



//    @FormUrlEncoded
//    @POST("putDataTest.php")
//    Call<String> sendData(
//            @Field("param-1") String param1,
//            @Field("param-2") String param2
//    );