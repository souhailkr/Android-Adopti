package com.esprit.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SouhaiKr on 25/11/2018.
 */

public class User {

    @SerializedName("id")
    int id ;
    @SerializedName("firstName")
    public String firstname;
    @SerializedName("lastName")
    public String lastname;
    @SerializedName("email")
    public String email;
    @SerializedName("photo")
    public String photo;
    @SerializedName("num_tel")
    public String num_tel;
    @SerializedName("password")
    public String password;
    @SerializedName("user")
    User user ;
    @SerializedName("token")
    public String token ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
