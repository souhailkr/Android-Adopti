package com.esprit.souhaikr.adopt.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.esprit.souhaikr.adopt.controllers.PreferencesUtility.LOGGED_IN_PREF;

/**
 * Created by SouhaiKr on 25/12/2018.
 */

public class SaveSharedPreferences {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }


    public static void setToken(Context context, String loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("a", loggedIn);
        editor.apply();
    }


    public static String getToken(Context context) {
        return getPreferences(context).getString("a","a");
    }





}
