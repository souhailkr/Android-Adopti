package com.esprit.souhaikr.adopt.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.FollowingPostsAdapter;
import com.esprit.souhaikr.adopt.entities.Pet;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    APIInterface apiInterface;

    List<User> result;
    List<Pet> result2;
    ListView mylistview;
    List<User> users = new ArrayList<>();
    List<Pet> pets = new ArrayList<>();
    SwipeRefreshLayout swipeLayout;
    View view ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_following, container, false);


        swipeLayout =  view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.post(new Runnable() {
                             @Override
                             public void run() {
                                 swipeLayout.setRefreshing(true);

                                 fetchData();
                             }
                         }
        );




        return view;
    }

    @Override
    public void onRefresh() {
        fetchData();

    }

    private void fetchData() {
        pets.clear();
        users.clear();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<User>> call = apiInterface.showFollowings(BottomNavigationActivity.id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                result = response.body();


                for (User size : result) {
                    System.out.println(size.toString());


                    users.add(size);
                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
            }
        });


        Call<List<Pet>> call2 = apiInterface.doGetListPets();
        call2.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {


                result2 = response.body();
                //contacts.addAll(response.body());


                for (Pet p : result2) {
                    for (User u : users) {
                        if (p.getUser().getId() == u.getId()) {

                            pets.add(p);
                        }
                    }



                }


                Collections.reverse(pets);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                RecyclerView recyclerView = view.findViewById(R.id.posts_recycler_view);
                recyclerView.setLayoutManager(layoutManager);
                FollowingPostsAdapter adapter = new FollowingPostsAdapter(getActivity(), pets);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                call.cancel();
            }
        });

        swipeLayout.setRefreshing(false);

    }
}
