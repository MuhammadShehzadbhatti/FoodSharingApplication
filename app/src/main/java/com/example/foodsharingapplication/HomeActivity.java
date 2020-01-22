package com.example.foodsharingapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foodsharingapplication.Maps.MapsActivity;
import com.example.foodsharingapplication.authentication.AuthemnticationFragments.ProfileHomeFragment;
import com.example.foodsharingapplication.authentication.Authentication_Firebase;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.products.MessageListActivity;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.example.foodsharingapplication.products.ProductsFragment.ProductListView;
import com.example.foodsharingapplication.products.ProductsFragment.UploadDataFragment;
import com.example.foodsharingapplication.userOrdersAndUploadedAds.UserOrderAndUploads;
import com.example.foodsharingapplication.products.UserUploadedFood;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView nav_bar;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private DrawerLayout drawerLayout;
    private ImageView headerUserProfilePic;
    private Intent intent;
    private Authentication_Firebase authentication_firebase;
    private User userData;
    private TextView txtHeaderEmail;
    private TextView txtHeaderName;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference firebaseDatabaseRef;
    public static boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    public static LatLng curr;
    private static final String TAG = HomeActivity.class.getSimpleName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductListView()).commit();
        getLocationPermission();
        getDeviceLocation();
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
            txtHeaderEmail.setText(firebaseAuth.getCurrentUser().getEmail());
            txtHeaderName.setText(firebaseAuth.getCurrentUser().getDisplayName());
//            firebaseDatabaseRef.child(firebaseAuth.getCurrentUser().getUid())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            userData = dataSnapshot.getValue(User.class);
//                            if (!userData.getUserEmail().isEmpty()){
//                                txtHeaderEmail.setText(userData.getUserEmail());
//                            }
//
//                            if (!userData.getUserName().isEmpty()){
//                                txtHeaderName.setText(userData.getUserName());
//                            }
//                            //String profilePicUrl = userData.getUserProfilePicUrl();
//                           /* Log.i("auth.getUserName: ", userData.getUserName());
//                            Log.i("auth.getUserEmail: ", firebaseAuth.getCurrentUser().getEmail());*/
//                            //Picasso.get().load(profilePicUrl).centerCrop().fit().into(headerUserProfilePic);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
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

                    case R.id.list:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProductListView()).commit();
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

            case R.id.messages:
                Intent viewMessage  = new Intent(HomeActivity.this, MessageListActivity.class);
                startActivity(viewMessage);
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
    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            curr=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            Log.e(TAG,"current1"+curr);





                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}
