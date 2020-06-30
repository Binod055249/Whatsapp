package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SocialMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        FancyToast.makeText(SocialMediaActivity.this, "Welcome"+ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        FancyToast.makeText(SocialMediaActivity.this, "Logout Successfully!!",
                                Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }else{
                        FancyToast.makeText(SocialMediaActivity.this, "Unknown error: "+e.getMessage(),
                                Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                }
            });
            finish();
            Intent intent=new Intent(SocialMediaActivity.this,LogInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}