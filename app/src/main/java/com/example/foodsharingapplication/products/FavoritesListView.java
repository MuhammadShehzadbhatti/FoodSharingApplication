package com.example.foodsharingapplication.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesListView extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    BottomNavigationView nav_bar;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    LinearLayoutManager mLL;
    FirebaseAuth mFirebaseAuth;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.getCurrentUser();
        if (!mFirebaseAuth.getCurrentUser().equals(null)) {

        }
        // ////////Action Bar////////////
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today's Menu");*/
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLL = new LinearLayoutManager(this);

        // ///// Set Latest First ////////////
        mLL.setReverseLayout(true);
        mLL.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLL);


    } // onCreate ends here


    @Override
    protected void onStart() {
        super.onStart();

        // ///////////Query to get Data from Firebase and Populate HomePage///////////

        Query query = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter;
        FirebaseRecyclerOptions<UserUploadFoodModel> options =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(query, UserUploadFoodModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserUploadFoodModel model) {
                holder.setDetails(getApplicationContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser().getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);

                // //////Handle click on the recycler view/////////////
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        String available = getItem(position).getAvailabilityDays();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        ArrayList<String> imageArray = getItem(position).getmArrayString();


                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);

                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("availability", available);
                        intent.putExtra("foodPostedBy",foodPostedBy);

                        // Image Setting
                        intent.putExtra("imageArray", imageArray);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }


    //navigation drawer start

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }
    }

}
