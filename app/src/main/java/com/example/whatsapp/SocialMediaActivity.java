package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        FancyToast.makeText(SocialMediaActivity.this, "Welcome "+ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        listView=findViewById(R.id.listView);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(SocialMediaActivity.this,android.R.layout.simple_list_item_1,arrayList);

        listView.setOnItemClickListener(this);

         swipeRefreshLayout=findViewById(R.id.swipeRefreshLatout);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (parseUsers.size() > 0 && e == null) {
                        for (ParseUser parseUser : parseUsers) {
                            arrayList.add(parseUser.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try{
                    ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",arrayList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if(parseUsers.size()>0 && e==null){
                                for(ParseUser parseUser:parseUsers){
                                    arrayList.add(parseUser.getUsername());
                                }
                                arrayAdapter.notifyDataSetChanged();
                                if(swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }else{

                                if(swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
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
            FancyToast.makeText(SocialMediaActivity.this, ParseUser.getCurrentUser().getUsername()+" Logout Successfully!!",
                    Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Intent intent=new Intent(SocialMediaActivity.this,LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        FancyToast.makeText(SocialMediaActivity.this, "Unknown error: "+e.getMessage(),
                                Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(SocialMediaActivity.this,ChatActivity.class);
        intent.putExtra("username",arrayList.get(position));
        startActivity(intent);
    }
}