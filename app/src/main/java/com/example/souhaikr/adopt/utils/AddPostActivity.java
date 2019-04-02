package com.example.souhaikr.adopt.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.controllers.APIBreed;
import com.example.souhaikr.adopt.controllers.APIClient;
import com.example.souhaikr.adopt.controllers.GpsTracker;
import com.example.souhaikr.adopt.controllers.RecyclerViewAdapter;
import com.example.souhaikr.adopt.entities.API;
import com.example.souhaikr.adopt.entities.BreedName;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView iv;
    private static final int PICK_IMAGE_REQUEST = 100;
    String mediaPath ;
    EditText name ;
    EditText desc ;
    EditText age ;
    Spinner breedList ;
    Spinner typeList ;
    CheckBox small ;
    CheckBox medium ;
    CheckBox large ;
    CheckBox male ;
    CheckBox female ;
    Button savebtn ;
    File f ;
    double longitude;
    double latitude ;
    APIInterface apiInterface;
    API p ;
    ArrayAdapter<String> dataAdapter ;
    private GpsTracker gpsTracker;
    String typeName = null;
    ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_post);

        progressBar = findViewById(R.id.spin_kit);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(toolbar!=null){
            getSupportActionBar().setTitle("Add a pet");
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        name = findViewById(R.id.titleEditText) ;
        desc = findViewById(R.id.descriptionEditText) ;
        age = findViewById(R.id.editTextAge) ;
        small =findViewById(R.id.cbSmall) ;
        medium = findViewById(R.id.cbMedium) ;
        large = findViewById(R.id.cbLarge) ;
        male = findViewById(R.id.cbMale) ;
        female = findViewById(R.id.cbFemale) ;
        breedList = findViewById(R.id.breedSpinner) ;
        typeList = findViewById(R.id.typeSpinner) ;
        savebtn = findViewById(R.id.savebtn) ;

        age.addTextChangedListener(new TextWatcher() {
                                       @Override
                                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                       }

                                       @Override
                                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                                           if (age.getText().toString().trim().equals("")) {
                                               savebtn.setEnabled(false);
                                           } else {
                                               savebtn.setEnabled(true);
                                           }
                                       }

                                       @Override
                                       public void afterTextChanged(Editable s) {

                                       }
                                   }) ;




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = (String) typeList.getSelectedItem();
                String breed = (String) breedList.getSelectedItem() ;
                String postName = name.getText().toString().trim() ;
                String postDesc = desc.getText().toString().trim() ;
                String postAge = age.getText().toString().trim() ;
                String gender = null;
                String size = null ;
                String id = String.valueOf(BottomNavigationActivity.id);



                if(female.isChecked()){
                gender = "female" ;
                 
                }
                else if (male.isChecked())
                {
                    gender = "male" ;
                }

                if(small.isChecked()){
                   size = "small" ;

                }
                else if (medium.isChecked())
                {
                     size = "medium" ;
                }
                else if (large.isChecked())
                {
                    size = "large" ;
                }

                gpsTracker = new GpsTracker(AddPostActivity.this);
                if(gpsTracker.canGetLocation()){
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    String l = String.valueOf(latitude);
                    String lo = String.valueOf(longitude);
                    if (!postName.equals("") && !postDesc.equals("") && !postAge.equals("") && f!=null) {
                        progressBar.setVisibility(View.VISIBLE);

                        Sprite doubleBounce = new DoubleBounce();
                        progressBar.setIndeterminateDrawable(doubleBounce);

                        uploadImg(f, postName, postDesc, type, breed, gender, size, postAge, l, lo, id);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "insert all Data!",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    gpsTracker.showSettingsAlert();
                }

                
                    
            }
        });

        typeList.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Cat");
        categories.add("Dog");

        categories.add("Bird");
        categories.add("Reptile");
        categories.add("Small&Furry");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        typeList.setAdapter(dataAdapter);


        breedList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK); //Change selected text color
                ((TextView) view).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);

            return;
        }


        iv = findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });




    }

    public void onCheckboxClicked(View view) {

        switch(view.getId()) {

            case R.id.cbMale:

                female.setChecked(false);

                break;

            case R.id.cbFemale:

                male.setChecked(false);


                break;


        }
    }

    public void onCheckboxClicked2(View view) {

        switch(view.getId()) {

            case R.id.cbSmall:

                medium.setChecked(false);
                large.setChecked(false);

                break;

            case R.id.cbMedium:

                small.setChecked(false);
                large.setChecked(false);


                break;

            case R.id.cbLarge:

                small.setChecked(false);
                medium.setChecked(false);


                break;


        }
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case PICK_IMAGE_REQUEST:

                if (resultCode == RESULT_OK) {
                    try {


                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        iv.setImageBitmap(selectedImage);

                        String filePath = getRealPathFromURI(imageUri);
                        File file = new File(filePath);

                        f = new File(filePath) ;

//



                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(AddPostActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(AddPostActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void uploadImg(File f,String name,String description,String type,String breed,String gender,String size,String age
    ,String lat , String lon , String id) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), f);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("test", f.getName(), requestFile);
// add another part within the multipart request
        RequestBody image = RequestBody.create( okhttp3.MultipartBody.FORM, f.getName());
        RequestBody post_lat = RequestBody.create(MediaType.parse("multipart/form-data"),lat);
        RequestBody post_long = RequestBody.create(MediaType.parse("multipart/form-data"),lon);
        RequestBody post_age = RequestBody.create(MediaType.parse("multipart/form-data"),age);
        RequestBody post_name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
        RequestBody post_desc = RequestBody.create(MediaType.parse("multipart/form-data"),description);
        RequestBody  post_type= RequestBody.create(MediaType.parse("multipart/form-data"),type);
        RequestBody post_breed = RequestBody.create(MediaType.parse("multipart/form-data"),breed);
        RequestBody post_gender = RequestBody.create(MediaType.parse("multipart/form-data"),gender);
        RequestBody post_size = RequestBody.create(MediaType.parse("multipart/form-data"),size);
        RequestBody UserId = RequestBody.create(MediaType.parse("multipart/form-data"),id);



        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Pet> call = apiInterface.getDetails(body,post_name,post_desc,post_type,post_breed,post_gender,post_size,post_age,image,post_lat,post_long,UserId);
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                //if(response.isSuccessful()){
                    // HERE!!!!





                Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);

                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Log.e("TAG", "ERRORD: "+ t.getMessage());
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // User refused to grant permission.
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI)
    {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null)
        { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        }
        else
        {
            if(cursor.moveToFirst())
            {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextColor(R.color.black);




        String item = parent.getItemAtPosition(position).toString();

        Handler mHandler = new Handler(Looper.getMainLooper());




        if (typeList.getSelectedItem().equals("Bird")) {
            typeName = "bird";

        } else if (typeList.getSelectedItem().equals("Cat"))

        {
            typeName = "cat";
        } else if (typeList.getSelectedItem().equals("Dog"))

        {
            typeName = "dog";
        }else if (typeList.getSelectedItem().equals("Reptile"))

        {
            typeName = "reptile";
        }else if (typeList.getSelectedItem().equals("Small&Furry"))

        {
            typeName = "smallfurry";
        }



            List<String> categories = new ArrayList<String>();
             //breedList.setOnItemSelectedListener(this);


            // Showing selected spinner item
            apiInterface = APIBreed.getClient().create(APIInterface.class);
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
                            breedList.setAdapter(dataAdapter);









                        }
                    });


                }

                @Override
                public void onFailure(Call<API> call, Throwable t) {
                    call.cancel();
                }
            });



    }






    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getLocation(){
        gpsTracker = new GpsTracker(AddPostActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String l = String.valueOf(latitude);
            String lo = String.valueOf(longitude);
            Toast.makeText(AddPostActivity.this, l+lo, Toast.LENGTH_LONG).show();

        }else{
            gpsTracker.showSettingsAlert();
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
