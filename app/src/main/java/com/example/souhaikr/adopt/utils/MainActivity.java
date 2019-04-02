package com.example.souhaikr.adopt.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.controllers.APIClient;
import com.example.souhaikr.adopt.controllers.SaveSharedPreferences;
import com.example.souhaikr.adopt.entities.User;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;



public class MainActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText;
    EditText _passwordText;
    APIInterface apiInterface;
    private ArrayList<User> usersList = new ArrayList<>() ;


    ProgressDialog progressDialog;

    Button _loginButto;
    TextView _signupLink;
    LinearLayout loginForm ;
    private List<User> users;
    private static final String EMAIL = "email";
    LoginButton loginButton ;
    private AccessToken mAccessToken;
    private CallbackManager callbackManager;
    String first_name ;
    String last_name ;
    String email ;
    String photo ;
    Call<User> loginApi ;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);



        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        //loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                mAccessToken = loginResult.getAccessToken();
                loginResult.getAccessToken().getUserId();

                getUserProfile(mAccessToken);



            }



            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });







        _loginButto = findViewById(R.id.btn_login) ;
        _signupLink = findViewById(R.id.link_signup) ;
        _emailText = findViewById(R.id.input_email) ;
        _passwordText = findViewById(R.id.input_password) ;
        loginForm = findViewById(R.id.loginForm) ;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        if(SaveSharedPreferences.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
            startActivity(intent);
        } else {
            loginForm.setVisibility(View.VISIBLE);
        }


        _loginButto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login(_emailText.getText().toString(), _passwordText.getText().toString());


            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void getUserProfile(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //You can fetch user info like this…
                            ;
                            object.getJSONObject("picture").
                            getJSONObject("data").getString("url");
                            first_name = object.getString("first_name");
                            last_name = object.getString("last_name");

                            email = object.getString("email");
                            object.getString("id");
                            Log.i(TAG, String.valueOf(object));
                            String password = "";

                            String num = "";


                            apiInterface = APIClient.getClient().create(APIInterface.class);
                            Call<List<User>> callApi = apiInterface.doGetListUsers();
                            callApi.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> callApi, Response<List<User>> response) {




                                    users = response.body();
                                    //contacts.addAll(response.body());
                                    Log.d("TAG", String.valueOf(users));


                                    for (User user : users) {
                                        if (email.equals(user.getEmail())) {
                                            usersList.add(user);
                                        }
                                    }


                                    if (usersList.isEmpty()) {
                                        Call<User> callApi2 = apiInterface.signupUser(first_name, last_name, email, num, password);
                                        callApi2.enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> callApi, Response<User> response) {

                                                if (response.isSuccessful() && response.body() != null) {
                                                    User user = response.body();


                                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("id", String.valueOf(user.getId()));
                                                    editor.apply();
                                                    SaveSharedPreferences.setLoggedIn(getApplicationContext(), true);

                                                    Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    LoginManager.getInstance().logOut();




                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {

                                            }
                                        });


                                    }
                                    else
                                    {

                                      loginUser(email,password);

                                    }








                                }

                                @Override
                                public void onFailure(Call<List<User>> callApi, Throwable t) {
                                    callApi.cancel();
                                }
                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200),first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void login(String email,String password) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButto.setEnabled(false);
        progressDialog = new ProgressDialog(MainActivity.this
        );
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {



        loginUser(email,password);



                    }
                }, 3000);





    }

    private void loginUser(String email, String password) {

        APIInterface apiInterface;

        apiInterface = APIClient.getClient().create(APIInterface.class);

        loginApi = apiInterface.loginUser(email,password);
        loginApi.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> callApi, Response<User> response) {

                if (response.isSuccessful() && response.body() !=null) {
                    // Set Logged In statue to 'true'

                    User user = response.body();


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", String.valueOf(user.getId()));
                    editor.apply();

                    SaveSharedPreferences.setLoggedIn(getApplicationContext(), true);
                    Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);




                }

                else {

                    //progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Login failed, Check your Email or Password!", Toast.LENGTH_LONG).show();
                    _loginButto.setEnabled(true);



                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }});
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButto.setEnabled(true);
        //finish();
        Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
        startActivity(intent);
        this.finish();

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButto.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }




}



