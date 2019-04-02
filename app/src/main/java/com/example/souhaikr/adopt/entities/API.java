package com.example.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SouhaiKr on 10/12/2018.
 */

public class API {
    @SerializedName("petfinder")
   petfinder petfiner ;

    public petfinder getPetfiner() {
        return petfiner;
    }

    public void setPetfiner(petfinder petfiner) {
        this.petfiner = petfiner;
    }
}
