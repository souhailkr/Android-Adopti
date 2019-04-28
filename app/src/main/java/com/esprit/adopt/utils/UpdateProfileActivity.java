package com.esprit.souhaikr.adopt.utils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.ImageServer;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    APIInterface apiInterface;
    EditText firstName ;
    EditText lastName ;
    EditText email ;
    EditText phonenumber ;
    EditText password ;
    EditText confirm_password ;
    ImageView fab ;
    ImageView profileImage ;
    private static final int PICK_IMAGE_REQUEST = 100;
    File f ;
    String userImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Toolbar toolbar = findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.firstname) ;

        lastName = findViewById(R.id.lastname) ;
        email = findViewById(R.id.email) ;
        phonenumber = findViewById(R.id.phone_number) ;
        password = findViewById(R.id.password) ;
        confirm_password = findViewById(R.id.confirm_password) ;
        fab = findViewById(R.id.fab) ;
        profileImage = findViewById(R.id.profile_image) ;

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call = apiInterface.getUser(BottomNavigationActivity.id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();


                String user_name = user.getFirstname();
                String user_lastname = user.getLastname();
                String number = user.getNum_tel();

                userImg = user.getPhoto();
                String userEmail = user.getEmail();
                firstName.setText(user_name);
                firstName.setSelection(firstName.getText().length());

                lastName.setText(user_lastname);
                phonenumber.setText(number);
                email.setText(userEmail);
                if (userImg!=null) {
                    Picasso.get().load(ImageServer.chemin+userImg).into(profileImage);
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }


        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                {
                    finish();
                    break ;
                }





            case R.id.save :
            if (!validate()) {

                Toast.makeText(getBaseContext(), "Update informations failed", Toast.LENGTH_LONG).show();


            }
            else
            {
                String user_email = email.getText().toString();
                String first_name = firstName.getText().toString();
                String last_name = lastName.getText().toString();
                String num_tel = phonenumber.getText().toString();


                String user_password = password.getText().toString();
                uploadImg(f,first_name,last_name,user_email,user_password,num_tel);






            }


            }
        return false;
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
                        profileImage.setImageBitmap(selectedImage);

                        String filePath = getRealPathFromURI(imageUri);
                        File file = new File(filePath);

                        f = new File(filePath) ;

//



                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(UpdateProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
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



    public boolean validate() {
        boolean valid = true;

        String user_email = email.getText().toString();
        String first_name = firstName.getText().toString();
        String last_name = lastName.getText().toString();
        String num_tel = phonenumber.getText().toString();


        String user_password = password.getText().toString();
        String copy_password = confirm_password.getText().toString();


        if (user_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (user_password.isEmpty() || user_password.length() < 4 || user_password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (!copy_password.equals(user_password) && copy_password.equals("")) {
            confirm_password.setError("not the same as the password");
            valid = false;
        } else {
            confirm_password.setError(null);
        }

        if (first_name.equals("")) {
            firstName.setError("enter a first name");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (last_name.equals("")) {
            lastName.setError("enter a last name");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (num_tel.equals("")) {
            phonenumber.setError("enter a phone number");
            valid = false;
        } else {
            phonenumber.setError(null);
        }

        return valid;
    }


    private void uploadImg(File f,String firstname,String lastname,String email,String password,String phone) {


                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), f);
                RequestBody image = RequestBody.create(okhttp3.MultipartBody.FORM, f.getName());




// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("test", f.getName(), requestFile);
// add another part within the multipart request
        RequestBody user_name = RequestBody.create(MediaType.parse("multipart/form-data"),firstname);
        RequestBody user_last = RequestBody.create(MediaType.parse("multipart/form-data"),lastname);
        RequestBody user_email = RequestBody.create(MediaType.parse("multipart/form-data"),email);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"),password);
        RequestBody user_phone = RequestBody.create(MediaType.parse("multipart/form-data"),phone);

        RequestBody UserId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(BottomNavigationActivity.id));

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call = apiInterface.updateUser(body,user_name,user_last,user_email,image,user_phone,user_password,UserId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    // HERE!!!!



                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "ERRORD: "+ t.getMessage());
            }
        });

        finish();
    }


}
