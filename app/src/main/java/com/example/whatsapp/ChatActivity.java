package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView chatListview;
    private ArrayList<String> chatsList;
    private ArrayAdapter arrayAdapter;
    private String selectedUser;

    private EditText edtSendMessage;
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        selectedUser=getIntent().getStringExtra("username");
        setTitle(selectedUser);

        FancyToast.makeText(this, "chat with: "+selectedUser+" now!!", Toast.LENGTH_SHORT,
                FancyToast.SUCCESS,false).show();

        chatListview=findViewById(R.id.chatListview);
        edtSendMessage=findViewById(R.id.edtSendMessage);
        btnSend=findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);
        chatsList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatsList);
        chatListview.setAdapter(arrayAdapter);
   try {
       ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
       ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

       firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
       firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

       secondUserChatQuery.whereEqualTo("waSender", selectedUser);
       secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

       ArrayList<ParseQuery<ParseObject>> allqueries = new ArrayList<>();
       allqueries.add(firstUserChatQuery);
       allqueries.add(secondUserChatQuery);

       ParseQuery<ParseObject> myQuery = ParseQuery.or(allqueries);
       myQuery.orderByAscending("createdAt");
       myQuery.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
               if (objects.size() > 0 && e == null) {
                   for (ParseObject chatObject : objects) {

                       String waMessage = chatObject.get("waMessage") + "";
                       if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                           waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                       }
                       if (chatObject.get("waSender").equals(selectedUser)) {
                           waMessage = selectedUser + ": " + waMessage;
                       }
                       chatsList.add(waMessage);
                   }
                   arrayAdapter.notifyDataSetChanged();
               }
           }
       });
   }catch (Exception e){
       e.printStackTrace();
   }


    }

    @Override
    public void onClick(View v) {
        ParseObject chat=new ParseObject("Chat");
        chat.put("waSender",ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient",selectedUser);
        chat.put("waMessage",edtSendMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(ChatActivity.this,
                            "Message from "+ParseUser.getCurrentUser().getUsername()+" sent to "+selectedUser,
                            Toast.LENGTH_SHORT, FancyToast.SUCCESS,false).show();
                    chatsList.add(ParseUser.getCurrentUser().getUsername()+": "+edtSendMessage.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    edtSendMessage.setText("" );
                }
            }
        });

    }
}