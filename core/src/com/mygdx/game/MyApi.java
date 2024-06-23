package com.mygdx.game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyApi {
    @FormUrlEncoded
    @POST("/arcade.php")
    Call<List<RecordFromDB>> sendData(@Field("name") String name, @Field("score") int score);

    @FormUrlEncoded
    @POST("/arcade.php")
    Call<List<RecordFromDB>> readData();

}
