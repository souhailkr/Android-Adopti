package com.esprit.souhaikr.adopt.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.GpsTracker;
import com.esprit.souhaikr.adopt.controllers.SaveSharedPreferences;
import com.facebook.login.LoginManager;

public class BottomNavigationActivity extends AppCompatActivity {
    private android.support.v4.app.Fragment homeFragment = new HomeFragment();
    private android.support.v4.app.Fragment mapFragment = new MapFragment();
    private android.support.v4.app.Fragment searchFragment = new SearchFragment();
    private android.support.v4.app.Fragment profileFragment = new ProfileFragment();
    private android.support.v4.app.Fragment followingFragment = new FollowingFragment();


    final FragmentManager fm = getSupportFragmentManager();
    android.support.v4.app.Fragment active = homeFragment;
    TextView toolbar_text;

    public static int id;
    public static String token;

    SharedPreferences sharedPreferences;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle("Home");
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;


                    return true;
                case R.id.navigation_dashboard:
                    getSupportActionBar().setTitle("Map");
                    fm.beginTransaction().hide(active).show(mapFragment).commit();
                    active = mapFragment;




                    return true;
                case R.id.navigation_search:
                    getSupportActionBar().setTitle("Search");
                    fm.beginTransaction().hide(active).show(searchFragment).commit();
                    active = searchFragment;



                    return true;

                case R.id.following:
                    getSupportActionBar().setTitle("Following");
                    fm.beginTransaction().hide(active).show(followingFragment).commit();
                    active = followingFragment;




                    return true;
                case R.id.navigation_notifications:
                    getSupportActionBar().setTitle("Profile");




                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;


                    return true;


            }




            return false ;


        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setIcon(R.drawable.add);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = sharedPreferences.getString("id", null);

        if (value.equals("")) {
            // not having user id
        } else {
            // user id is available
            id = Integer.parseInt(value);
        }

        token = SaveSharedPreferences.getToken(getApplicationContext()) ;

        setContentView(R.layout.activity_bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            getSupportActionBar().setTitle("Home");
        }


        GpsTracker gpsTracker = new GpsTracker(BottomNavigationActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String l = String.valueOf(latitude);
            String lo = String.valueOf(longitude);


        } else {
            gpsTracker.showSettingsAlert();
        }

        fm.beginTransaction().add(R.id.frame, followingFragment, "5").hide(followingFragment).commit();
        fm.beginTransaction().add(R.id.frame, profileFragment, "4").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.frame, searchFragment, "3").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.frame, mapFragment, "2").hide(mapFragment).commit();
        fm.beginTransaction().add(R.id.frame, homeFragment, "1").commit();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {

                SaveSharedPreferences.setLoggedIn(getApplicationContext(), false);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                LoginManager.getInstance().logOut();


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.add: {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.action_edit_profile: {

                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_favourites: {

                Intent intent = new Intent(getApplicationContext(), FavouritesActivity.class);
                startActivity(intent);
                break;
            }


        }
        return true;
    }


    @Override
    public void onBackPressed() {
//        moveTaskToBack(false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
