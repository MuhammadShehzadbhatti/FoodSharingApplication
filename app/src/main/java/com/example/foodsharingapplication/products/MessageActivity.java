package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodsharingapplication.Adapters.MessageAdapter;
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

public class MessageActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ImageButton send_btn;
    EditText input_message;

    MessageAdapter messageAdapter;
    List<ChatModel> chatModels = new ArrayList<>();

    RecyclerView recyclerView;

    private User sender, receiver;
    private String ad_id = " ";
    String food_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            ad_id = bundle.getString("ad_id");
            food_name = bundle.getString("foodTitle");
            receiver = bundle.getParcelable("foodPostedBy");
        }

        firebaseAuth = FirebaseAuth.getInstance();

        send_btn = findViewById(R.id.btn_send);
        input_message = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.recyclerViewChat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = input_message.getText().toString();
                if (!message.equals("")) {
                    sendMessage(message);
                } else {
                    Toast.makeText(MessageActivity.this, "You cannot send empty message", Toast.LENGTH_LONG).show();
                }

                input_message.setText(" ");
            }
        });

        DatabaseReference recieverDatabaseReference;
        recieverDatabaseReference = FirebaseDatabase.getInstance().getReference("User");
        recieverDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user.getUserId().equals(firebaseAuth.getUid())) {
                        sender = user;
                        readMessage();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(String message) {

        ChatModel chatModel = new ChatModel();
        chatModel.setConversationID(ad_id);
        chatModel.setMessage(message);
        chatModel.setSender(sender);
        chatModel.setReciever(receiver);
        chatModel.setFoodName(food_name);

        DatabaseReference sendMessageReference = FirebaseDatabase.getInstance().getReference("Messages");
        sendMessageReference.child(ad_id).push().setValue(chatModel);
    }


    private void readMessage(){

        DatabaseReference readMessageReference;
        readMessageReference = FirebaseDatabase.getInstance().getReference("Messages").child(ad_id);
        readMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModels.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel != null) {
                        if ((sender.getUserId().equals(chatModel.getReciever().getUserId()) && receiver.getUserId().equals(chatModel.getSender().getUserId()))
                                || (receiver.getUserId().equals(chatModel.getReciever().getUserId()) && sender.getUserId().equals(chatModel.getSender().getUserId()))) {
                            chatModels.add(chatModel);
                        }


                        if (messageAdapter == null) {
                            messageAdapter = new MessageAdapter(MessageActivity.this, chatModels);
                            recyclerView.setAdapter(messageAdapter);
                        } else {
                            messageAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
