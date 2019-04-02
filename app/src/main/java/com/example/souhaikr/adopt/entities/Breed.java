package com.example.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SouhaiKr on 10/12/2018.
 */

public class Breed {

    @SerializedName("breed")
    ArrayList<BreedName> breed_name ;

    public ArrayList<BreedName> getBreed_name() {
        return breed_name;
    }

    public void setBreed_name(ArrayList<BreedName> breed_name) {
        this.breed_name = breed_name;
    }
}
