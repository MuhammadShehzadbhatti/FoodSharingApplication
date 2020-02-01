package com.example.foodsharingapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foodsharingapplication.Maps.MapsActivity;
import com.example.foodsharingapplication.authentication.AuthemnticationFragments.ProfileHomeFragment;
import com.example.foodsharingapplication.authentication.Authentication_Firebase;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.MessageListActivity;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.example.foodsharingapplication.products.ProductsFragment.ProductListView;
import com.example.foodsharingapplication.products.ProductsFragment.UploadDataFragment;
import com.example.foodsharingapplication.products.UserOrderedFood;
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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = HomeActivity.class.getSimpleName();
    public static boolean mLocationPermissionGranted;
    public static LatLng curr;
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
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

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

        //Toolbar searchToolbar = findViewById(R.id.searchToolbar);
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
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("User").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);

                Log.i("user.getUserName()1",user.getUserName());
                    if (user.getUserName() != "") {
                        Log.i("user.getUserName()",user.getUserName());
                        txtHeaderEmail.setText(user.getUserName());
                    }

                    if (user.getUserEmail() != "") {
                        Log.i("user.getUserEmail()",user.getUserEmail());
                        txtHeaderName.setText(user.getUserEmail());
                    }
                    if (user.getUserProfilePicUrl() != null) {
                        Log.i("user.getUserPicUrl()",user.getUserProfilePicUrl());
                        Picasso.get().load(user.getUserProfilePicUrl()).centerCrop().fit().into(headerUserProfilePic);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // ////////// Functions for Searching ///////////

            @Override
            public boolean onQueryTextChange(String newText) {
                String srchTxt = newText.substring(0, 1).toUpperCase();
                final String querytext = srchTxt + newText.substring(1);
                ProductGridView.getInstance().firebaseSearch(querytext);
                firebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserUploadFoodModel userUploadFoodModel = ds.getValue(UserUploadFoodModel.class);
                            if (querytext.equals(userUploadFoodModel.getFoodTitle())) {
                                //ProductGridView.getInstance().firebaseSearch(querytext);
                                UserOrderedFood.getInstance().showSearch(userUploadFoodModel);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                /*String s1 = query.substring(0, 1).toUpperCase();
                String nameCapitalized = s1 + query.substring(1);
                ProductGridView.getInstance().firebaseSearch(nameCapitalized);*/

                return false;
            }

        });
        return true;
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

            case R.id.myOrders:
                startActivity(new Intent(HomeActivity.this, UserOrderedFood.class));

                break;

            case R.id.myAds:
                Intent intent = new Intent(HomeActivity.this, UserUploadedFood.class);
                startActivity(intent);
                //getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.fragment_container, new ProfileHomeFragment()).commit();
                //nav_bar.setVisibility(View.GONE);
                //startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;

            case R.id.messages:
                Intent viewMessage = new Intent(HomeActivity.this, MessageListActivity.class);
                startActivity(viewMessage);
                //nav_bar.setVisibility(View.GONE);
                //startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;


            case R.id.signOut:
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, HomeDefinition.class));

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
                            curr = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            Log.e(TAG, "current1" + curr);
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}
