package com.cd.statussaver.api;

import com.cd.statussaver.model.TiktokModel;
import com.cd.statussaver.model.TwitterResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIServices {
    @GET
    Observable<JsonObject> callResult( @Url String Value);

    @FormUrlEncoded
    @POST
    Observable<TwitterResponse> callTwitter(@Url String Url, @Field("id") String id);

    @POST
    Observable<TiktokModel> getTiktokData(@Url String Url, @Body HashMap<String, String> hashMap);

}