package com.esprit.souhaikr.adopt.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.ImageServer;
import com.esprit.souhaikr.adopt.controllers.PostsAdapter;
import com.esprit.souhaikr.adopt.entities.Pet;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.android.gestures.Utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private List<Pet> pets = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    ListView mylistview ;

    private List<Pet> result;

    Context m ;
    TextView usernameText ;
    TextView useremailText ;
    ImageView profile_picture ;
    TextView total_posts ;
    APIInterface apiInterface;
    private List<User> result2;
    private List<User> result3;

    String username ;
    TextView total_following ;
    TextView total_followers ;
    String following_number ;
    String followers_number;
    SwipeRefreshLayout swipeLayout;
    RecyclerView myrv ;

    Handler mHandler ;
    RecyclerView.ItemDecoration itemDecoration;



    int id ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        myrv =  view.findViewById(R.id.posts_view);
        myrv.addItemDecoration(new GridSpacingItemDecoration(3, (int) dpToPx(4), true));

        usernameText = view.findViewById(R.id.user_name) ;
        useremailText = view.findViewById(R.id.user_email) ;
        profile_picture = view.findViewById(R.id.profile_image) ;
        total_posts = view.findViewById(R.id.posts_number) ;
        total_followers = view.findViewById(R.id.followers_number) ;
        total_following = view.findViewById(R.id.following_number);


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.post(new Runnable() {
                             @Override
                             public void run() {
                                 swipeLayout.setRefreshing(true);

                                 fetchData();
                             }
                         }
        );

        total_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), TabbedActivity.class);

                myIntent.putExtra("username", username);
                myIntent.putExtra("following_number", following_number);
                myIntent.putExtra("followers_number", followers_number);





                startActivity(myIntent);

            }
        });

        total_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), TabbedActivity.class);

                myIntent.putExtra("username", username);
                myIntent.putExtra("followers_number", followers_number);
                myIntent.putExtra("following_number", following_number);



                startActivity(myIntent);

            }
        });




        mHandler = new Handler(Looper.getMainLooper());


        id = BottomNavigationActivity.id ;


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call2 = apiInterface.getUser(id);
        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();
                username= user.getFirstname() ;
                String userImg = user.getPhoto() ;
                String userEmail = user.getEmail() ;
                usernameText.setText(username);
                useremailText.setText(userEmail);
                if (userImg!=null) {
                    Picasso.get().load(ImageServer.chemin+userImg).into(profile_picture);
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }


        });

        return view ;


    }

    private void fetchData() {

        pets.clear();
        users.clear();
        followers.clear();

        myrv.setAdapter(null);








        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Pet>> call = apiInterface.getPetsByUser(id);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {

                result = response.body();
                //contacts.addAll(response.body());
                Log.d("TAG", String.valueOf(pets));


                for(Pet size: result) {
                    System.out.println(size.toString());

                    pets.add(size) ;


                }
                int total = pets.size() ;
                total_posts.setText(String.valueOf(total));

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myrv.removeItemDecoration(new GridSpacingItemDecoration(3, (int) dpToPx(4), true));

                        PostsAdapter myAdapter = new PostsAdapter(getActivity(),pets);
                        myrv.setLayoutManager(new GridLayoutManager(getActivity(),3));


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


        Call<List<User>> call3 = apiInterface.showFollowings(id);
        call3.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call3, Response<List<User>> response3) {

                result2 = response3.body();

                for(User size: result2) {
                    System.out.println(size.toString());

                    users.add(size) ;

                }

                int total = users.size() ;
                following_number = String.valueOf(total);
                total_following.setText(String.valueOf(total));


            }

            @Override
            public void onFailure(Call<List<User>> call3, Throwable t) {
                call3.cancel();
            }
        });


        Call<List<User>> call4 = apiInterface.showFollowers(id);
        call4.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call4, Response<List<User>> response) {

                result3 = response.body();

                for(User size: result3) {
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


        swipeLayout.setRefreshing(false);

    }


    @Override
    public void onRefresh() {
        fetchData();

    }
}
