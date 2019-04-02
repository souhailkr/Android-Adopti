package com.example.souhaikr.adopt.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.controllers.APIClient;
import com.example.souhaikr.adopt.controllers.PostsAdapter;
import com.example.souhaikr.adopt.controllers.RecyclerViewAdapter;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.entities.User;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.android.gestures.Utils.dpToPx;

public class UserProfileActivity extends AppCompatActivity {

    APIInterface apiInterface;

    private List<Pet> contacts;
    ListView mylistview ;
    List<Pet> pets = new ArrayList<>();
    Context m ;
    TextView username ;
    TextView useremail ;
    ImageView profile_picture ;
    TextView total_posts ;
    TextView total_following ;
    TextView total_followers ;
    List<User> result ;
    Button followbtn ;
    int id ;
    private List<User> followers_result;
    private List<User> following_result;

    String followers_number;
    String following_number ;
    private List<User> followers = new ArrayList<>();
    private List<User> following = new ArrayList<>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        RecyclerView myrv =  findViewById(R.id.posts_view);
        username = findViewById(R.id.user_name) ;
        useremail = findViewById(R.id.user_email) ;
        profile_picture = findViewById(R.id.profile_image) ;
        total_following = findViewById(R.id.following_number) ;

        total_followers = findViewById(R.id.followers_number) ;

        total_posts = findViewById(R.id.posts_number) ;
        followbtn = findViewById(R.id.btnFollow);

        Handler mHandler = new Handler(Looper.getMainLooper());

        Toolbar toolbar = findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User's Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String idUser =(String) b.get("id");
        id = Integer.parseInt(idUser);


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Pet>> call = apiInterface.getPetsByUser(id);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {



                contacts = response.body();
                //contacts.addAll(response.body());
                Log.d("TAG", String.valueOf(contacts));


                for(Pet size: contacts) {
                    System.out.println(size.toString());



                    pets.add(size) ;





                }
                int total = contacts.size() ;
                total_posts.setText(String.valueOf(total));

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        PostsAdapter myAdapter = new PostsAdapter(getApplicationContext(),pets);
                        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                        myrv.addItemDecoration(new GridSpacingItemDecoration(3, (int) dpToPx(4), true));
                        myrv.setItemAnimator(new DefaultItemAnimator());
                        myrv.setNestedScrollingEnabled(false);
                        myrv.setAdapter(myAdapter);

                    }
                });







            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                call.cancel();
            }
        });



        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call2 = apiInterface.getUser(id);
        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();

                String userName= user.getFirstname() ;
                String userImg = user.getPhoto() ;
                String userEmail = user.getEmail() ;
                username.setText(userName);
                useremail.setText(userEmail);
                if (userImg!=null) {
                    Picasso.get().load(userImg).into(profile_picture);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }


        });



        Call<List<User>> call3 = apiInterface.showFollowings(BottomNavigationActivity.id);
        call3.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {



                        result = response.body() ;

                        for (User u : result)
                        {
                            if (id == u.getId())
                            {
                                if (followbtn.getText().toString().equalsIgnoreCase("Follow me")) {

                                    followbtn.setText("Following");
                                }

                            }

                        }

//                        if (!result.isEmpty()) {
//                            if (followbtn.getText().toString().equalsIgnoreCase("Follow me")) {
//
//                                followbtn.setText("Following");
//                            }
//                        }





                }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
            }
        });


        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followbtn.getText().toString().equalsIgnoreCase("Following")) {
                    followbtn.setText("Follow me");


                    APIInterface apiInterface;

                    apiInterface = APIClient.getClient().create(APIInterface.class);

                    Call<User> callApi = apiInterface.unfollowUser(BottomNavigationActivity.id,id);
                    callApi.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callApi, Response<User> response) {
                            if (response.isSuccessful()) {

                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }});}

                        else
                {
                    followbtn.setText("Following");
                    APIInterface apiInterface;

                    apiInterface = APIClient.getClient().create(APIInterface.class);

                    Call<User> callApi = apiInterface.followUser(BottomNavigationActivity.id,id);
                    callApi.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callApi, Response<User> response) {


                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }});}




            }




            }
        );


        Call<List<User>> call4 = apiInterface.showFollowers(id);
        call4.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call4, Response<List<User>> response) {

                followers_result = response.body();

                for(User size: followers_result) {
                    System.out.println(size.toString());

                    followers.add(size) ;

                }

                int total = followers.size() ;
                followers_number = String.valueOf(total);

                total_followers.setText(String.valueOf(total));

            }

            @Override
            public void onFailure(Call<List<User>> call4, Throwable t) {
                call4.cancel();
            }
        });


        Call<List<User>> call_following = apiInterface.showFollowings(id);
        call_following.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call3, Response<List<User>> response3) {

                following_result = response3.body();

                for(User size: following_result) {
                    System.out.println(size.toString());

                    following_result.add(size) ;

                }

                int total = following_result.size() ;
                following_number = String.valueOf(total);
                total_following.setText(String.valueOf(total));


            }

            @Override
            public void onFailure(Call<List<User>> call3, Throwable t) {
                call3.cancel();
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
