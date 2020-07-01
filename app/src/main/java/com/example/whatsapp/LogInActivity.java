package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLogInEmail,edtLogInPassword;
    private Button btnlogInActivity,btnLogInActivitySignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("Log In");

        edtLogInEmail=findViewById(R.id.edtLogInEmail);
        edtLogInPassword=findViewById(R.id.edtLogInPassword);
        btnlogInActivity=findViewById(R.id.btnlogInActivity);
        btnLogInActivitySignUp=findViewById(R.id.btnLogInActivitySignUp);

        btnlogInActivity.setOnClickListener(this);
        btnLogInActivitySignUp.setOnClickListener(this);

        edtLogInPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnlogInActivity);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser()!=null){
            transitionToSocialMediaActivity();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent=new Intent(LogInActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

    public void rootLayoutIsTapped(View view) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    @Override
    public void onClick(View btnView) {

        switch (btnView.getId()){

            case R.id.btnlogInActivity:
                ParseUser.logInInBackground(edtLogInEmail.getText().toString(),
                        edtLogInPassword.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user!=null && e== null){
                                    FancyToast.makeText(LogInActivity.this, user.getUsername() + " Logged in Succesfully.",
                                            Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    transitionToSocialMediaActivity();
                                }else {
                                    FancyToast.makeText(LogInActivity.this, "Unknown error: "+e.getMessage(),
                                            Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            }
                        });

                break;

            case R.id.btnLogInActivitySignUp:
                Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}