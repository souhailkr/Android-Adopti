package com.esprit.souhaikr.adopt.utils;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIBreed;
import com.esprit.souhaikr.adopt.entities.API;
import com.esprit.souhaikr.adopt.entities.BreedName;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment  implements AdapterView.OnItemSelectedListener,RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    RadioButton rb;
    Spinner type ;
    Spinner breed ;
    String size ;
    String gender ;
    Button save ;
    String typeName ;
    ArrayAdapter<String> dataAdapter ;
    API p ;
    Handler mHandler ;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        mHandler = new Handler(Looper.getMainLooper());

        SegmentedGroup segmented2 =  view.findViewById(R.id.segmented2);
        SegmentedGroup segmented1 =  view.findViewById(R.id.segmented1);
        RadioButton b = view.findViewById(R.id.button1) ;
        b.toggle();
        RadioButton bt = view.findViewById(R.id.btn1) ;
        bt.toggle();
        gender="" ;
        size="" ;



        rb =  view.findViewById(R.id.button1);
        type = view.findViewById(R.id.spinner) ;
        breed = view.findViewById(R.id.spinner2) ;
        save = view.findViewById(R.id.btn) ;


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bro = (String) breed.getSelectedItem();
                String typo = (String) type.getSelectedItem();


                Intent intent = new Intent(getActivity(), SearchList.class);
                Bundle extras = new Bundle();
                extras.putString("size",size);
                extras.putString("gender",gender);
                extras.putString("type",typo);
                extras.putString("breed",bro);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });


        type.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Cat");
        categories.add("Dog");
        categories.add("Bird");
        categories.add("Reptile");
        categories.add("Small&Furry");

        segmented2.setOnCheckedChangeListener(this);
        segmented1.setOnCheckedChangeListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type.setAdapter(dataAdapter);




        return view ;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.button1:
                gender="" ;


                break;
            case R.id.button2:

                gender = "male";
                break;
            case R.id.button3:

                gender = "female";
                break;

            case R.id.btn1:


                size="" ;
                break;
            case R.id.btn2:

                size = "small";
                break;
            case R.id.btn3:

                size = "medium";
                break;
            case R.id.btn4:

                size = "large";
                break;

            default:
                // Nothing to do
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(Color.WHITE);




        if (type.getSelectedItem().equals("Bird")) {
                typeName = "bird";

            } else if (type.getSelectedItem().equals("Cat"))

            {
                typeName = "cat";
            } else if (type.getSelectedItem().equals("Dog"))

            {
                typeName = "dog";
            }else if (type.getSelectedItem().equals("Reptile"))

            {
                typeName = "reptile";
            }else if (type.getSelectedItem().equals("Small&Furry"))

            {
                typeName = "smallfurry";
            }



            List<String> categories = new ArrayList<String>();
            //breedList.setOnItemSelectedListener(this);


            // Showing selected spinner item

            APIInterface apiInterface = APIBreed.getClient().create(APIInterface.class);
            Call<API> call = apiInterface.doGetLista("/breed.list?key=1abbf326403e6d3d360d9b9a5ad90da1&animal="+typeName+"&format=json");
            call.enqueue(new Callback<API>() {
                @Override
                public void onResponse(Call<API> call, Response<API> response) {


                    p = response.body();
                    //contacts.addAll(response.body());


                    ArrayList<BreedName> bi = p.getPetfiner().getBreeds().getBreed();

                    for (BreedName a : bi) {

                        categories.add(a.getName());

                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(R.layout.spinner_item);


                            // attaching data adapter to spinner
                            breed.setAdapter(dataAdapter);



                        }
                    });


                }

                @Override
                public void onFailure(Call<API> call, Throwable t) {
                    call.cancel();
                }
            });



//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//
//            // Drop down layout style - list view with radio button
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            // attaching data adapter to spinner
//            breed.setAdapter(dataAdapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
