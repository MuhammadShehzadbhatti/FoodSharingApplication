package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.foodsharingapplication.Adapters.MessageViewAdapter;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.ChatModel;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvMessageList;

    private Map<String, Boolean> otherUserMap = new HashMap<>();
    List<User> userList = new ArrayList<>();


    private String myId;
    private String ad_id = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        myId = FirebaseAuth.getInstance().getUid();



        rvMessageList = findViewById(R.id.rvMessageList);
        rvMessageList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference getfoodAdIdReference = FirebaseDatabase.getInstance().getReference("Messages");
        //Show Progress Dialog
        getfoodAdIdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //otherUserMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String conversationId = snapshot.getKey();
                    if (!TextUtils.isEmpty(conversationId)) {
                        ad_id = conversationId;
                        getUser();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void getUser(){
        DatabaseReference allChatReference = FirebaseDatabase.getInstance().getReference("Messages").child(ad_id);

        //Show Progress Dialog
        allChatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //otherUserMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    User otherUser = null;

                    if (myId.equals(chatModel.getSender().getUserId())) {
                        otherUser = chatModel.getReciever();
                    } else if (myId.equals(chatModel.getReciever().getUserId())) {
                        otherUser = chatModel.getSender();
                    }

                    if (otherUser != null) {
                        if(!otherUserMap.containsKey(otherUser.getUserId())) {
                            otherUserMap.put(otherUser.getUserId(), true);
                            userList.add(otherUser);
                        }
                    }
                }
                setRVAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setRVAdapter() {
        MessageViewAdapter messageViewAdapter = new MessageViewAdapter(this, userList);
        messageViewAdapter.setOnClickListener(this);
        rvMessageList.setAdapter(messageViewAdapter);
        //userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        User clickedUserModel = (User) v.getTag();
        Intent messageIntent = new Intent(MessageListActivity.this, MessageActivity.class);
        messageIntent.putExtra("foodPostedBy", clickedUserModel);
        messageIntent.putExtra("ad_id", ad_id);
        startActivity(messageIntent);

    }
}
