package com.esprit.souhaikr.adopt.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.FollowAdapter;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {

    APIInterface apiInterface;

    List<User> result ;
    ListView mylistview ;
    List<User> users = new ArrayList<>();
    Context m ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        mylistview =  findViewById(R.id.list);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitleTextColor(android.graphics.Color.WHITE) ;

        Handler mHandler = new Handler(Looper.getMainLooper());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<User>> call = apiInterface.showFollowers(BottomNavigationActivity.id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {



                result = response.body();




                for(User size: result) {
                    System.out.println(size.toString());


                 users.add(size) ;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FollowAdapter adapter = new FollowAdapter(getApplicationContext(), users);
                        mylistview.setAdapter(adapter);

                    }
                });






            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
            }
        });











    }
}
