package com.esprit.souhaikr.adopt.utils;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.CartListAdapter;
import com.esprit.souhaikr.adopt.controllers.DatabaseHelper;
import com.esprit.souhaikr.adopt.controllers.RecyclerItemTouchHelper;
import com.esprit.souhaikr.adopt.entities.Pet;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    DatabaseHelper mDatabaseHelper;
    ArrayList<Pet> favourites = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Pet> cartList;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    List<Pet> result2;
    List<Pet> pets = new ArrayList<>();
    APIInterface apiInterface;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        mDatabaseHelper = new DatabaseHelper(this);


        Cursor data = mDatabaseHelper.getData(); //get the id associated with that name
        while (data.moveToNext()) {
            favourites.add(new Pet(data.getInt(data.getColumnIndex("pet_id"))));

        }

        recyclerView = findViewById(R.id.recycler_view);
        cartList = new ArrayList<>();
        mAdapter = new CartListAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



        // making http call and fetching menu json
        prepareCart();





    }

    private void prepareCart() {

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Pet>> call2 = apiInterface.doGetListPets();
        call2.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {


                result2 = response.body();

                for (Pet p : result2)
                {
                    for (Pet fav : favourites) {
                        if (p.getId() == fav.getId())
                        {
                            cartList.add(p) ;
                        }

                    }
                }

                //cartList.addAll(result2) ;





                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                call.cancel();
            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            final Pet item = cartList.get(position);

            mDatabaseHelper.deleteName(item.getId()) ;



            // get the removed item name to display it in snack bar
            String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Pet deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from favourites!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mDatabaseHelper.addData(item.getId());
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
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
