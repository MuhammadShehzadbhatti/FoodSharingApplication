package com.example.foodsharingapplication.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.Dashboard;
import com.example.foodsharingapplication.R;
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

public class ShowData extends AppCompatActivity implements View.OnClickListener{

    private TextView name,email,pass,phoneNumber,gender,listTextView,userDOB,userCountry;
    private String strName,strEmail,strPass,strPhoneNumber,strGender;
    private Button btnRegister, btnCancel,btnEdit;
    private ImageView profileImage;
    private String userID;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private ListView simpleListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        name= (TextView) findViewById(R.id.name);
        email= (TextView) findViewById(R.id.enterEmail);
        pass= (TextView) findViewById(R.id.enterPassword);
        phoneNumber= (TextView) findViewById(R.id.phoneNumber);
        userCountry= (TextView) findViewById(R.id.userCountry);
        gender= (TextView) findViewById(R.id.gender);
        listTextView= (TextView) findViewById(R.id.listTextView);
        Button cancelBtn= findViewById(R.id.btnCancel);
        /*btnEdit= findViewById(R.id.btnEdit);*/
        CheckBox showPassword= (CheckBox) findViewById(R.id.showPass);
        simpleListView= (ListView) findViewById(R.id.simpleListView);

        firebaseAuth =FirebaseAuth.getInstance();
        //firebaseStorageReference=firebasestorage.getReference("User");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("User");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //if (currentUser!=null)
            userID = currentUser.getUid();
            Log.i(" firebaseAuth Id :", firebaseAuth.getUid() +" Current User Id" +currentUser.getDisplayName());

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        //pass.setTransformationMethod(new PasswordTransformationMethod());
        /*cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnCancel:
                        Intent intent = new Intent(ShowData.this, Dashboard.class);
                        startActivity(intent);
                        break;
                }
            }
        });*/

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null){

                    firebaseAuth.signOut();
                    Intent intent=new Intent(ShowData.this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                //firebaseAuth.sendPasswordResetEmail(email);
            }
        });


        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.i(" AuthStateChanged login", firebaseUser.getUid());
                }
                else{
                    firebaseAuth.signOut();
                    Toast.makeText(ShowData.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    /*Intent intent=new Intent(ShowData.this, Dashboard.class);
                    startActivity(intent);*/
                 //   Log.i(" logOut", firebaseUser.getUid());
                }

            }
        };

        mDatabaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot ds : dataSnapshot.getChildren()){//snapshot is of overall data and we get its children
                    User userData= dataSnapshot.getValue(User.class);
                    name.setText(userData.getUserName());
                    email.setText(userData.getUserEmail());
                    pass.setText(userData.getUserPassword());
                    gender.setText(userData.getSex());
                    userCountry.setText(userData.getUserCountry());
                    userDOB.setText(userData.getUserDateOfBirth());
                    phoneNumber.setText(userData.getUserPhoneNumber());
                    String profilePicUrl= userData.getUserProfilePicUrl();
                    Picasso.get().load(profilePicUrl).fit().centerCrop().into(profileImage);
                   /* String uname= userData.getUserName();
                    String uemail= userData.getUserEmail();
                    String upassword= userData.getUserPassword();
                    String uphoneNumber= userData.getUserPhoneNumber();
                    String uImageUrl=userData.getUserProfilePicUrl();

                    name.setText(uname);
                    email.setText(uemail);
                    pass.setText(upassword);*/
                        //phoneNumber.setText(uphoneNumber);
                   /* Log.i(" User Password: ", ds.child(userID).getValue(User.class).getUserPassword());
                    Log.i(" User name: ", ds.child(userID).getValue(User.class).getUserName());
                    Log.i(" User Email: ", ds.child(userID).getValue(User.class).getUserEmail());
                    Log.i(" User PhoneNumber: ", ds.child(userID).getValue(User.class).getUserPhoneNumber());
                    Log.i(" User Gender: ", ds.child(userID).getValue(User.class).getSex());
                    Log.i(" User Country: ", ds.child(userID).getValue(User.class).getUserCountry());

                    userData.setUserName(ds.child(userID).getValue(User.class).getUserName());
                    userData.setUserPassword(ds.child(userID).getValue(User.class).getUserPassword());
                    userData.setUserEmail(ds.child(userID).getValue(User.class).getUserEmail());
                    userData.setUserPhoneNumber(ds.child(userID).getValue(User.class).getUserPhoneNumber());
                    userData.setSex(ds.child(userID).getValue(User.class).getSex());
                    userData.setUserCountry(ds.child(userID).getValue(User.class).getUserCountry());

                    ArrayList<String> arrayList=new ArrayList<>();
                    arrayList.add(userData.getUserName());
                    arrayList.add(userData.getUserPassword());
                    arrayList.add(userData.getUserEmail());
                    arrayList.add(userData.getUserPhoneNumber());
                    arrayList.add(userData.getSex());
                    arrayList.add(userData.getUserCountry());

                    ArrayAdapter arrayAdapter=new ArrayAdapter(ShowData.this, R.layout.activity_show_data,R.id.listTextView,arrayList);
                    simpleListView.setAdapter(arrayAdapter);*/

                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ShowData.this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                Intent intent = new Intent(ShowData.this, Dashboard.class);
                startActivity(intent);
                break;
        }
    }
}
