package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
     private EditText edtSignUpEmail,edtSignUpUsername,edtSignUpPassword;
     private Button btnSignUp, btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        setTitle("Sign UP");

        edtSignUpEmail=findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername=findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword=findViewById(R.id.edtSignUpPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnLogIn=findViewById(R.id.btnLogIn);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return false;
            }
        });

      if(ParseUser.getCurrentUser()!=null){
          transitionToSocialMediaActivity();
      }


    }

    private void transitionToSocialMediaActivity() {
        Intent intent=new Intent(SignUpActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

    public void rootLayoutTapped(View view) {
        try{
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View btnView) {
        switch (btnView.getId()){

            case R.id.btnSignUp:
                ParseUser parseUser=new ParseUser();
                parseUser.setEmail(edtSignUpEmail.getText().toString());
                parseUser.setUsername(edtSignUpUsername.getText().toString());
                parseUser.setPassword(edtSignUpPassword.getText().toString());
                final ProgressDialog progressDialog=new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Signing UP "+edtSignUpUsername.getText().toString());
                progressDialog.show();
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            FancyToast.makeText(SignUpActivity.this, ParseUser.getCurrentUser().getUsername() + " Signned Up Succesfully.",
                                    Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            transitionToSocialMediaActivity();
                        }else {
                            FancyToast.makeText(SignUpActivity.this, "Unknown error: "+e.getMessage(),
                                    Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                        progressDialog.dismiss();
                    }
                });

                 break;

            case R.id.btnLogIn:
                Intent intent =new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
                finish();
               break;
        }

    }
}