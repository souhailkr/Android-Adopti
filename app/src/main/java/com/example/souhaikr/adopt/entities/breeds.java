package com.example.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by SouhaiKr on 10/12/2018.
 */

public class breeds {
    @SerializedName("breed")
    ArrayList<BreedName> breed ;

    public ArrayList<BreedName> getBreed() {
        return breed;
    }

    public void setBreed(ArrayList<BreedName> breed) {
        this.breed = breed;
    }
}
