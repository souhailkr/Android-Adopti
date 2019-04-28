package com.esprit.souhaikr.adopt.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.controllers.APIClient;
import com.esprit.souhaikr.adopt.controllers.SaveSharedPreferences;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _emailText;
    EditText _lastnameText ;
    EditText _phonenumber ;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        _nameText = findViewById(R.id.input_name) ;
        _lastnameText = findViewById(R.id.input_lastname) ;
        _phonenumber = findViewById(R.id.input_phonenumber) ;
        _emailText = findViewById(R.id.input_email) ;
        _passwordText = findViewById(R.id.input_password) ;
        _signupButton = findViewById(R.id.btn_signup) ;
        _loginLink = findViewById(R.id.link_login) ;

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this
                );
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String lastname = _lastnameText.getText().toString();
        String phonenumber = _phonenumber.getText().toString();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        APIInterface apiInterface;

                        apiInterface = APIClient.getClient().create(APIInterface.class);

                        Call<User> callApi = apiInterface.signupUser(name,lastname,email,phonenumber,password);
                        callApi.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> callApi, Response<User> response) {

                                if (response.isSuccessful() && response.body() !=null) {
                                    // Set Logged In statue to 'true'
                                    progressDialog.dismiss();
                                    User user = response.body();


                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", String.valueOf(user.getId()));
                                    editor.apply();

                                    SaveSharedPreferences.setLoggedIn(getApplicationContext(), true);
                                    Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    onSignupSuccess();
                                    // onSignupFailed();
                                    progressDialog.dismiss();



                                }



                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }});
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success

                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String lastname = _lastnameText.getText().toString();
        String number = _phonenumber.getText().toString();


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3 || name.matches("[0-9]+")
                ) {
            _nameText.setError("enter a valid first name");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (lastname.isEmpty() || lastname.length() < 3 || lastname.matches("[0-9]+")) {
            _lastnameText.setError("enter a valid last name");
            valid = false;
        } else {
            _lastnameText.setError(null);
        }

        if (number.isEmpty() || number.length() < 8 || !number.matches("[0-9]+")) {
            _phonenumber.setError("enter a valid phone number");
            valid = false;
        } else {
            _phonenumber.setError(null);
        }

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


