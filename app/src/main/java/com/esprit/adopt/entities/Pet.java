package com.esprit.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;
import com.mapbox.mapboxsdk.annotations.Marker;

/**
 * Created by SouhaiKr on 01/12/2018.
 */

public class Pet {

    @SerializedName("id")
    int id ;
    @SerializedName("name")
    String name ;
    @SerializedName("photo")
    String image ;
    @SerializedName("description")
    String desc ;
    @SerializedName("breed")
    String breed ;
    @SerializedName("size")
    String size ;
    @SerializedName("type")
    String type ;
    @SerializedName("sexe")
    String gender ;
    @SerializedName("age")
    int age ;
    @SerializedName("User")
    User user ;
    @SerializedName("altitude")
    Double latitude ;
    @SerializedName("longitude")
    Double longitude ;

    @SerializedName("createdAt")
    String createdAt ;


    public Pet(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    Marker marker ;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



    public Pet() {
    }

    public Pet(int id, String name, String image, String desc, String breed, String size, String type, String gender, int age, User user) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.breed = breed;
        this.size = size;
        this.type = type;
        this.gender = gender;
        this.age = age;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
