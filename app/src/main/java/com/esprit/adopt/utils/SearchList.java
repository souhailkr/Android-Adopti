package com.esprit.souhaikr.adopt.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.CustomerAdapter;
import com.esprit.souhaikr.adopt.entities.Pet;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchList extends AppCompatActivity {

    APIInterface apiInterface;

    private List<Pet> contacts;
    ListView mylistview;
    ImageView img;
    List<Pet> pets = new ArrayList<>();
    Context m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);


        Bundle extras = getIntent().getExtras();

        String type = extras.getString("type");
        String breed = extras.getString("breed");
        String size_variable = extras.getString("size");
        String gender = extras.getString("gender");


        mylistview = findViewById(R.id.list);
        img = findViewById(R.id.noresultsImg);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitleTextColor(android.graphics.Color.WHITE);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa :  "+type+breed+size_variable+gender);



        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Pet>> call = apiInterface.doGetListPets();
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {


                contacts = response.body();


                for (Pet size : contacts) {
                    System.out.println(size.toString());

                    if (type.equals(size.getType()) && size_variable.equals(size.getSize()) && breed.equals(size.getBreed()) && gender.equals(size.getGender()))

                        pets.add(size);

                    else if (type.equals(size.getType()) && size_variable.equals("") && breed.equals(size.getBreed()) && gender.equals("")) {
                        pets.add(size);
                    } else if (type.equals(size.getType()) && size_variable.equals("") && breed.equals(size.getBreed()) && gender.equals(size.getGender())) {
                        pets.add(size);
                    } else if (type.equals(size.getType()) && size_variable.equals(size.getSize()) && breed.equals(size.getBreed()) && gender.equals("")) {
                        pets.add(size);
                    }


                }
                int itemCount = pets.size();
                getSupportActionBar().setTitle(itemCount + " result(s) found");
                if (pets.isEmpty()) {
                    img.setVisibility(View.VISIBLE);
                }
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa :  "+pets);

                CustomerAdapter adapter = new CustomerAdapter(getApplicationContext(), pets);
                mylistview.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                call.cancel();
            }
        });





    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
