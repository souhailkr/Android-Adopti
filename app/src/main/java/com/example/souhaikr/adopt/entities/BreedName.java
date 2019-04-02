package com.example.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SouhaiKr on 10/12/2018.
 */

public class BreedName {
    @SerializedName("$t")

    String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
