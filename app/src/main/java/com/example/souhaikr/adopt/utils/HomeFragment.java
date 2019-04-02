package com.example.souhaikr.adopt.utils;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.controllers.APIClient;
import com.example.souhaikr.adopt.controllers.ImageServer;
import com.example.souhaikr.adopt.controllers.RecentPostsAdapter;
import com.example.souhaikr.adopt.controllers.RecyclerViewAdapter;
import com.example.souhaikr.adopt.controllers.UsersViewAdapter;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.entities.User;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.souhaikr.adopt.utils.BottomNavigationActivity.id;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{




    APIInterface apiInterface;
    TextView responseText;
    private List<Pet> contacts;
    private List<User> users;
    private List<User> following;

    private ArrayList<Pet> pets = new ArrayList<>() ;

    private ArrayList<Pet> cats = new ArrayList<>() ;
    private ArrayList<Pet> dogs = new ArrayList<>() ;
    private ArrayList<Pet> birds = new ArrayList<>() ;
    private ArrayList<Pet> aquatic_anmails = new ArrayList<>() ;
    private ArrayList<Pet> small_mammals = new ArrayList<>() ;
    SwipeRefreshLayout swipeLayout;
    View view ;



    private ArrayList<User> usersList = new ArrayList<>() ;
    private ArrayList<User> followingList = new ArrayList<>() ;
    Handler mHandler ;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
       mHandler = new Handler(Looper.getMainLooper());


        TextView more = view.findViewById(R.id.more);
        TextView more_dogs = view.findViewById(R.id.more_dogs);
        TextView more_aquatic = view.findViewById(R.id.more_aquatic_animals);
        TextView more_birds = view.findViewById(R.id.more_birds);
        TextView more_small = view.findViewById(R.id.more_small_mammals);
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


                       more.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String type = "Cat" ;

                        onCli(type) ;



                    }

        });
        more_dogs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String type = "Dog" ;

                onCli(type) ;

            }

        });
        more_aquatic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String type = "Reptile" ;

                onCli(type) ;

            }

        });

        more_birds.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String type = "Bird" ;

                onCli(type) ;



            }

        });
        more_small.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String type = "SmallFurry" ;

                onCli(type) ;

            }

        });











        return view ;

    }

    private void fetchData() {

        usersList.clear();
        followingList.clear();


        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<User>> callFollowing = apiInterface.showFollowings(id);
        callFollowing.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                following = response.body();

                for(User size: following) {
                    System.out.println(size.toString());
                     followingList.add(size) ;
                }

                Call<List<User>> callApi = apiInterface.doGetListUsers();
                callApi.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> callApi, Response<List<User>> response) {

                        users = response.body();



                        for (User u : users)
                        {
                            if (u.getId()!=id)
                            {
                                usersList.add(u) ;
                            }
                        }



                        usersList.removeAll(followingList) ;

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(layoutManager);
                        UsersViewAdapter adapter = new UsersViewAdapter(getActivity(),usersList );
                        recyclerView.setAdapter(adapter);




                    }

                    @Override
                    public void onFailure(Call<List<User>> callApi, Throwable t) {
                        callApi.cancel();
                    }
                });





            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
            }
        });










        Call<List<Pet>> call = apiInterface.doGetList();
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {



                contacts = response.body();
                //contacts.addAll(response.body());
                Log.d("TAG", String.valueOf(contacts));

                dogs.clear();
                cats.clear();
                birds.clear();
                pets.clear();

                aquatic_anmails.clear();
                small_mammals.clear(); ;



                for(Pet size: contacts) {
                    System.out.println(size.toString());
                    String displayResponse = "";
                    String text = size.getType();
                    String total = size.getImage();
                    pets.add(size);

                    if (text.equals("Cat"))
                    {
                        cats.add(size);
                    }
                    else if (text.equals("Dog"))
                    {
                        dogs.add(size);
                    }
                    else if (text.equals("Reptile"))

                    {
                        aquatic_anmails.add(size);
                    }
                    else if  (text.equals("Bird"))

                    {
                        birds.add(size) ;

                    }
                    else
                    {
                        small_mammals.add(size) ;
                    }


                }

                Collections.reverse(pets);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView recyclerView = view.findViewById(R.id.recent_recyclerView);
                recyclerView.setLayoutManager(layoutManager);
                RecentPostsAdapter adapter = new RecentPostsAdapter(getActivity(),pets);
                recyclerView.setAdapter(adapter);






                Collections.reverse(cats);
                RecyclerView MyRecyclerView =  view.findViewById(R.id.cardView);
                MyRecyclerView.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
                MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (contacts.size() > 0 & MyRecyclerView != null) {
                    MyRecyclerView.setAdapter(new HomeFragment.MyAdapter(cats));
                }
                MyRecyclerView.setLayoutManager(MyLayoutManager);


                Collections.reverse(dogs);
                RecyclerView MyRecyclerView1 =  view.findViewById(R.id.second_recycler_view);
                MyRecyclerView.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager1 = new LinearLayoutManager(getActivity());
                MyLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (contacts.size() > 0 & MyRecyclerView1 != null) {
                    MyRecyclerView1.setAdapter(new HomeFragment.MyAdapter(dogs));
                }
                MyRecyclerView1.setLayoutManager(MyLayoutManager1);

                Collections.reverse(aquatic_anmails);
                RecyclerView MyRecyclerView2 =  view.findViewById(R.id.third_recycler_view);
                MyRecyclerView.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getActivity());
                MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (contacts.size() > 0 & MyRecyclerView2 != null) {
                    MyRecyclerView2.setAdapter(new HomeFragment.MyAdapter(aquatic_anmails));
                }
                MyRecyclerView2.setLayoutManager(MyLayoutManager2);

                Collections.reverse(birds);
                RecyclerView MyRecyclerView3 =  view.findViewById(R.id.fourth_recycler_view);
                MyRecyclerView.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager3 = new LinearLayoutManager(getActivity());
                MyLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (contacts.size() > 0 & MyRecyclerView3 != null) {
                    MyRecyclerView3.setAdapter(new HomeFragment.MyAdapter(birds));
                }
                MyRecyclerView3.setLayoutManager(MyLayoutManager3);

                Collections.reverse(small_mammals);
                RecyclerView MyRecyclerView4 =  view.findViewById(R.id.fifth_recycler_view);
                MyRecyclerView.setHasFixedSize(true);
                LinearLayoutManager MyLayoutManager4 = new LinearLayoutManager(getActivity());
                MyLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (contacts.size() > 0 & MyRecyclerView4 != null) {
                    MyRecyclerView4.setAdapter(new HomeFragment.MyAdapter(small_mammals));
                }
                MyRecyclerView4.setLayoutManager(MyLayoutManager4);






            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                call.cancel();
            }
        });


        // stopping swipe refresh
        swipeLayout.setRefreshing(false);




    }

    private void onCli(String type) {

        Intent intent = new Intent(getActivity(), MorePostsActivity.class);

        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void setFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onRefresh() {
        fetchData();
    }


    public class MyAdapter extends RecyclerView.Adapter<HomeFragment.MyViewHolder> {
        private ArrayList<Pet> list;


        public MyAdapter(ArrayList<Pet> Data) {
            list = Data;

        }




        @Override
        public HomeFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            HomeFragment.MyViewHolder holder = new HomeFragment.MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(HomeFragment.MyViewHolder holder, final int position) {

            holder.titleTextView.setText(list.get(position).getName());
            String url = list.get(position).getImage() ;
            Picasso.get().load(ImageServer.chemin+url).into(holder.coverImageView);
            final int x = list.get(position).getId() ;



            holder.coverImageView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {

                    int id_ = list.get(position).getId() ;

                    String id = String.valueOf(id_);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public View container;


        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);










        }
    }


}
