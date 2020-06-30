package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        FancyToast.makeText(SocialMediaActivity.this, "Welcome"+ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        listView=findViewById(R.id.listView);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(SocialMediaActivity.this,android.R.layout.simple_list_item_1,arrayList);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(parseUsers.size()>0 && e==null){
                    for(ParseUser parseUser:parseUsers){
                        arrayList.add(parseUser.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
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