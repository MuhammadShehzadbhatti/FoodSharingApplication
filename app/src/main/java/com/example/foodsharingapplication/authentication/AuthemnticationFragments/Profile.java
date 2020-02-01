package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class Profile extends Fragment {

    private TextView userName, userPassword, userEmail;
    private Button btnEditProfile;
    private ImageView userProfilePic, logoImg;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private User userData;

    /*public Profile() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile, container, false);
        Toast.makeText(getContext(), "Profile", Toast.LENGTH_SHORT).show();
        userName = view.findViewById(R.id.showUserName);
        userEmail = view.findViewById(R.id.showUserEmail);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        userProfilePic = view.findViewById(R.id.userProfilePic);
        logoImg = view.findViewById(R.id.logoImg);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


        userData = new User();
        if (!firebaseUser.getUid().isEmpty()) {
            getCurrentUserProfile();
        } else {
            startActivity(new Intent(getContext(), SignIn.class));
        }
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getContext(), UpdateProfile.class);
                startActivity(intent);*/
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                        .replace(R.id.fragment_container, new UpdateProfile()).commit();
            }
        });
        return view;
    }


    public void getCurrentUserProfile() {
        databaseReference.child(Objects.requireNonNull(firebaseUser.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class);
/*                Log.i(" Name ", userData.getUserName());
                Log.i(" getUserEmail ", userData.getUserEmail());
                Log.i(" getUserProfilePicUrl ", userData.getUserProfilePicUrl());*/
                userName.setText(userData.getUserName());
                userEmail.setText(userData.getUserEmail());
                if (userData.getUserProfilePicUrl() != null) {
                    Picasso.get().load(userData.getUserProfilePicUrl()).centerCrop().fit().into(logoImg);
                } else {
                    Picasso.get().load(R.drawable.user_circle).centerCrop().fit().into(logoImg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser.getUid();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseUser != null) {
            //firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                Intent intent = new Intent(getContext(), Dashboard.class);
                startActivity(intent);
                break;
        }
    }*/
}
