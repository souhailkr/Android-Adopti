package com.esprit.souhaikr.adopt.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SouhaiKr on 10/12/2018.
 */

public class petfinder {
    @SerializedName("breeds")
   breeds breeds ;

    public com.esprit.souhaikr.adopt.entities.breeds getBreeds() {
        return breeds;
    }

    public void setBreeds(com.esprit.souhaikr.adopt.entities.breeds breeds) {
        this.breeds = breeds;
    }
}
