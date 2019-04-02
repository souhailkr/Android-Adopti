package com.example.souhaikr.adopt.interfaces;

import com.example.souhaikr.adopt.entities.API;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.entities.User;
import com.google.gson.JsonElement;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by SouhaiKr on 25/11/2018.
 */

public interface APIInterface {

    @GET
    public Call<API> doGetLista(@Url String url) ;



    @GET("/getPetsByUser/{id}")
    public Call<List<Pet>> getPetsByUser(@Path("id") int id);


    @GET("/getpets")
    public Call<List<Pet>> doGetList() ;

    @GET("/showallpets")
    public Call<List<Pet>> doGetListPets() ;


    @GET("/showallusers")
    public Call<List<User>> doGetListUsers() ;

    @GET("/getpet/{id}")
    public Call<Pet> getPet(@Path("id") int id);

    @GET("/getpets/{type}")
    public Call<List<Pet>> getPetsByType(@Path("type") String type);

    @FormUrlEncoded
    @POST("/follow/{id}")
    Call<User> followUser(@Path("id") int id,@Field("id") int id1);

    @FormUrlEncoded
    @POST("/unfollow/{id}")
    Call<User> unfollowUser(@Path("id") int id,@Field("id") int id1);

    @FormUrlEncoded
    @POST("/showFollowing")
    Call<List<User>> showFollowings(@Field("id") int id);

    @FormUrlEncoded
    @POST("/showFollowers")
    Call<List<User>> showFollowers(@Field("id") int id);

    @Multipart
    @POST("/addpet")
    Call<Pet> getDetails(
                                  @Part MultipartBody.Part photo,
                                  @Part("name") RequestBody name,
                                  @Part("description") RequestBody desc,
                                  @Part("type") RequestBody type,
                                  @Part("breed") RequestBody breed,
                                  @Part("sexe") RequestBody gender,
                                  @Part("size") RequestBody size,
                                  @Part("age") RequestBody age,
                                  @Part("image") RequestBody image,
                                  @Part("altitude") RequestBody latitude  ,
                                  @Part("longitude") RequestBody longitude,
                                  @Part("user") RequestBody user


    );

    @FormUrlEncoded
    @POST("/deletepet")
    Call<Pet> deletePet(@Field("id") int id);

    @FormUrlEncoded
    @POST("/login")
    Call<User> loginUser(@Field("email") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("/signup")
    Call<User> signupUser(@Field("firstName") String firstname,
                          @Field("lastName") String lastname,
                          @Field("email") String email,
                          @Field("num_tel") String num_tel,
                          @Field("password") String password);


    @GET("/getuser/{id}")
    Call<User> getUser(@Path("id") int id);

    @Multipart
    @POST("/updateuser")
    Call<User> updateUser(
            @Part MultipartBody.Part photo,
            @Part("firstName") RequestBody firstName,

            @Part("lastName") RequestBody lastName,
            @Part("email") RequestBody email,

            @Part("image") RequestBody image,
            @Part("num_tel") RequestBody num_tel,
            @Part("password") RequestBody password,
            @Part("id") RequestBody id





    );


}
