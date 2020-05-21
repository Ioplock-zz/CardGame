package com.example.cardgame.auxiliaryClasses.forRetrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyServer {
    @POST("/")
    Call<UniverseResponse> register(@Body UniverseRequest user);
    @POST("/")
    Call<UniverseResponse> fetchCards(@Body UniverseRequest fetchCards);
    @POST("/")
    Call<UniverseResponse> getRooms(@Body UniverseRequest getRooms);
    @POST("/")
    Call<UniverseResponse> GetWhoMove(@Body UniverseRequest getWhoMove);
}
