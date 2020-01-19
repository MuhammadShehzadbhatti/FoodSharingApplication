package com.example.foodsharingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foodsharingapplication.Maps.MapsActivity;
import com.example.foodsharingapplication.authentication.AuthemnticationFragments.ProfileHomeFragment;
import com.example.foodsharingapplication.authentication.Authentication_Firebase;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.example.foodsharingapplication.products.ProductsFragment.ProductListView;
import com.example.foodsharingapplication.products.ProductsFragment.UploadDataFragment;
import com.example.foodsharingapplication.products.UserUploadedFood;
import com.example.foodsharingapplication.userOrdersAndUploadedAds.UserOrderAndUploads;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView nav_bar;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DrawerLayout drawerLayout;
    private ImageView headerUserProfilePic;
    private Intent intent;
    private Authentication_Firebase authentication_firebase;
    private User userData;
    private TextView txtHeaderEmail;
    private TextView txtHeaderName;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference firebaseDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductListView()).commit();

        userData = new User();
        authentication_firebase = new Authentication_Firebase(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtHeaderEmail = headerView.findViewById(R.id.headerUserEmail);
        txtHeaderName = headerView.findViewById(R.id.headerUserName);
        headerUserProfilePic = headerView.findViewById(R.id.headerUserProfilePic);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseRef = mFirebaseDatabase.getReference("User");
        /*firebaseAuth.getCurrentUser().getEmail();
        firebaseAuth.getCurrentUser().getDisplayName();*/
        /*Log.i("auth.getUserName: ", firebaseAuth.getCurrentUser().getDisplayName());
        Log.i("auth.getUserEmail: ", firebaseAuth.getCurrentUser().getEmail());*/
        //firebaseAuth.getCurrentUser().getPhotoUrl();
        if (firebaseAuth.getCurrentUser()!=null){
            txtHeaderEmail.setText(firebaseAuth.getCurrentUser().getEmail().toString());
            firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userData = dataSnapshot.getValue(User.class);
                            txtHeaderEmail.setText(firebaseAuth.getCurrentUser().getEmail());
                            //txtHeaderName.setText(userData.getUserName());
                            //String profilePicUrl = userData.getUserProfilePicUrl();
                           /* Log.i("auth.getUserName: ", userData.getUserName());
                            Log.i("auth.getUserEmail: ", firebaseAuth.getCurrentUser().getEmail());*/
                            //Picasso.get().load(profilePicUrl).centerCrop().fit().into(headerUserProfilePic);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


        // Navigation Bar Grid View
        nav_bar = findViewById(R.id.bottom_nav_bar);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.grid:
                        //Fragment Animation add where calling new fragment using fallowing function after transaction
                        //.setCustomAnimations(R.anim.slide_in,R.anim.slide_out)
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductGridView()).commit();
                        return true;
                    case R.id.add:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new UploadDataFragment()).commit();
                        return true;
                    case R.id.map:
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.fragment_container,new MapsFragment()).commit();

                        // ///// ADD more cases for different navigation bar options////////
                    default:
                        return false;
                }
            }
        });

        //Default for all fragments

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductGridView()).commit();
            navigationView.setCheckedItem(R.id.homeFragment);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(HomeActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                            .replace(R.id.fragment_container, new ProductGridView()).commit();

                } else {

                    intent = new Intent(getApplicationContext(), SignIn.class);
                }
                startActivity(intent);
            }
        };*/

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeFragment:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductGridView()).commit();
                break;

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProfileHomeFragment()).commit();
                //nav_bar.setVisibility(View.GONE);
                //startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;

            case R.id.myAds:
                Intent intent = new Intent(HomeActivity.this, UserUploadedFood.class);
                startActivity(intent);
                //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProfileHomeFragment()).commit();
                //nav_bar.setVisibility(View.GONE);
                //startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;



            case R.id.signOut:
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, HomeDefinition.class));

                break;
            case R.id.myOrders:
                startActivity(new Intent(HomeActivity.this, UserOrderAndUploads.class));

                break;
            /*case R.id.updateProfile:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProfileHomeFragment()).commit();

                break;*/
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
