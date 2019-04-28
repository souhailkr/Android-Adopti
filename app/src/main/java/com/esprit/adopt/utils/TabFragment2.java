package com.esprit.souhaikr.adopt.utils;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {


    APIInterface apiInterface;

    List<User> result ;
    ListView mylistview ;
    List<User> users = new ArrayList<>();
    Context m ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        mylistview =  view.findViewById(R.id.list);



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
                        FollowAdapter adapter = new FollowAdapter(getActivity(), users);
                        mylistview.setAdapter(adapter);

                    }
                });






            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
            }
        });







        return view ;
    }

}
