package com.example.souhaikr.adopt.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.controllers.APIClient;
import com.example.souhaikr.adopt.controllers.DatabaseHelper;
import com.example.souhaikr.adopt.controllers.GpsTracker;
import com.example.souhaikr.adopt.controllers.ImageServer;
import com.example.souhaikr.adopt.controllers.TimeAgo;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    APIInterface apiInterface;
    ImageView iv;
    TextView tv;
    TextView desc;
    TextView breed;
    TextView age;
    TextView gender;
    TextView siz;
    TextView locationText;
    TextView timeText;
    private Pet pet;
    ImageView userimage;
    TextView username;
    ImageView callImage;
    ImageView locationImage;
    ImageView message_icon;
    String text;
    int petId;
    private CollapsingToolbarLayout collapsingToolbar = null;

    double latitude;
    double longitude;
    double latA;
    double lngA;
    boolean flag  ;
    DatabaseHelper mDatabaseHelper ;
    String email ;
    int user_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDatabaseHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        GpsTracker gpsTracker = new GpsTracker(DetailsActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            String l = String.valueOf(latitude);
            String lo = String.valueOf(longitude);


        }


        locationText = findViewById(R.id.locationText);
        timeText = findViewById(R.id.timeText);
        locationImage = findViewById(R.id.location_icon);
        message_icon = findViewById(R.id.message_icon);
        iv = findViewById(R.id.imageView2);
        //tv = findViewById(R.id.textView) ;
        userimage = findViewById(R.id.userimage);
        callImage = findViewById(R.id.call_icon);
        username = findViewById(R.id.username);
        desc = findViewById(R.id.desc);

        breed = findViewById(R.id.breed);
        age = findViewById(R.id.age);
        siz = findViewById(R.id.size);
        gender = findViewById(R.id.gender);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j = (String) b.get("id");
        int id = Integer.parseInt(j);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ShelterLocationActivity.class);
                 myIntent.putExtra("id", String.valueOf(latA));
                 startActivity(myIntent);

            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent myIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    myIntent.putExtra("id", String.valueOf(user_id));
                    startActivity(myIntent);


            }
        });



        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Pet> call = apiInterface.getPet(id);

        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {

                pet = response.body();


                petId = pet.getId();
                text = pet.getName();
                String desc1 = pet.getDesc();
                String breed1 = pet.getBreed();
                String gende1 = pet.getGender();
                String size1 = pet.getSize();
                email = pet.getUser().getEmail() ;
                String userName = pet.getUser().getFirstname();
                String userImage = pet.getUser().getPhoto();
                 user_id = pet.getUser().getId();
                String createdAt = pet.getCreatedAt();
                int ag = pet.getAge();
                latA = pet.getLatitude();
                lngA = pet.getLongitude();

                Location locationA = new Location("A");
                locationA.setLatitude(latA);
                locationA.setLongitude(lngA);

                Location locationB = new Location("B");
                locationB.setLatitude(latitude);
                locationB.setLongitude(longitude);

                int distance = (int) (locationA.distanceTo(locationB) / 1000);   // in km

                locationText.setText(String.valueOf(distance) + " Km");


                TimeAgo timeAgo2 = new TimeAgo();
                String MyFinalValue = timeAgo2.covertTimeToText(createdAt);

                timeText.setText(MyFinalValue);


                String pet_image = pet.getImage();
                //tv.setText(text);
                desc.setText(desc1);
                breed.setText(breed1);
                gender.setText(gende1);
                siz.setText(size1);
                age.setText((String.valueOf(ag) + " Months"));
                username.setText(userName);





                Picasso.get().load(ImageServer.chemin + pet_image).into(iv);

                if (userImage != null) {
                    Picasso.get().load(ImageServer.chemin+userImage).into(userimage);
                }
//
                String usernumber = pet.getUser().getNum_tel();
                collapsingToolbar =
                        findViewById(R.id.toolbar_layout);
                collapsingToolbar.setTitle(text);


                callImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL class
                        callIntent.setData(Uri.parse("tel:" + usernumber));


                        startActivity(callIntent);


                    }
                });


            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                call.cancel();

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        Cursor data = mDatabaseHelper.getItemID(id); //get the id associated with that name
        Log.d("aaaa", String.valueOf(data));

        if (data.getCount()>0)
        {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.like1));

            flag = false ;
        }
        else
        {
            flag = true ;
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (flag) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.like1));
                    boolean insertData = mDatabaseHelper.addData(id);

                    if (insertData) {
                        toastMessage("Pet added to Favourites!");
                    } else {
                        toastMessage("Something went wrong");
                    }

                    flag = false;

                } else if (!flag) {

                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.like2));
                    mDatabaseHelper.deleteName(id);
                    toastMessage("removed from Favourites");
                    flag = true;

                }


            }
        });


        message_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Adopti Application");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });

        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(getApplicationContext(), ShelterLocationActivity.class);
                Bundle extras = new Bundle();
                extras.putString("lat", String.valueOf(latA));
                extras.putString("lng", String.valueOf(lngA));
                myIntent.putExtras(extras);
                startActivity(myIntent);


            }
        });


    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (BottomNavigationActivity.id == user_id) {
            getMenuInflater().inflate(R.menu.post_menu, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home: // Press Back Icon
            {
                finish();
            }



                case R.id.delete: {


                    AlertDialog.Builder adb = new AlertDialog.Builder(this);


                    //adb.setView(alertDialogView);


                    adb.setTitle("Delete Post?");


                    adb.setIcon(android.R.drawable.ic_dialog_alert);


                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            apiInterface = APIClient.getClient().create(APIInterface.class);
                            Call<Pet> call = apiInterface.deletePet(petId);

                            call.enqueue(new Callback<Pet>() {
                                @Override
                                public void onResponse(Call<Pet> call, Response<Pet> response) {


                                }

                                @Override
                                public void onFailure(Call<Pet> call, Throwable t) {
                                    call.cancel();

                                }
                            });

                            finish();
                        }
                    });


                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    adb.show();


                    break;


                }

            }

        return true;
    }





}

