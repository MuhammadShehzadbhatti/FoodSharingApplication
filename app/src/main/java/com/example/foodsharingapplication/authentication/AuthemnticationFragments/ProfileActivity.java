package com.example.foodsharingapplication.authentication.AuthemnticationFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private TextView userName, userPassword, userEmail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private User userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        userName= (TextView) findViewById(R.id.showUserName);
        userEmail= (TextView) findViewById(R.id.showUserEmail);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        userData = new User();
        if (!firebaseUser.getUid().isEmpty()){
            getCurrentUserProfile();
        }
        else{

            startActivity(new Intent(ProfileActivity.this, SignIn.class));

        }
    }
    public void getCurrentUserProfile() {
        databaseReference.child(Objects.requireNonNull(firebaseUser.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot ds : dataSnapshot.getChildren()){//snapshot is of overall data and we get its children
                User userData = dataSnapshot.getValue(User.class);
                Log.i(" Name ", userData.getUserName());
                Log.i(" getUserEmail ", userData.getUserEmail());
                Log.i(" getUserPassword ", userData.getUserPassword());
                if (userData.getUserProfilePicUrl()!=null){
                    Log.i(" getUserProfilePicUrl ", userData.getUserProfilePicUrl());
                    userName.setText(userData.getUserName().toString());
                }
               userName.setText(userData.getUserName());
               userEmail.setText(userData.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
