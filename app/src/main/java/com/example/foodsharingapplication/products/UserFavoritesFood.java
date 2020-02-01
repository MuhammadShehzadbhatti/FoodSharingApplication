package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodsharingapplication.Adapters.NearestAdapter;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFavoritesFood extends AppCompatActivity implements NearestAdapter.ItemClickListener {

    private FirebaseAuth mFirebaseAuth;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLL;
    private List<UserUploadFoodModel> orderList;
    NearestAdapter nearestAdapter;
    UserUploadFoodModel userUploadFoodModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorites_food);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.getCurrentUser();
        orderList = new ArrayList<>();
        nearestAdapter = new NearestAdapter(getApplicationContext(), orderList);
        nearestAdapter.setClickListener(this);

/*        if (!mFirebaseAuth.getCurrentUser().equals(null)) {

        }*/
        // ////////Action Bar////////////
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today's Menu");*/
        mRecyclerView = findViewById(R.id.favRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLL = new LinearLayoutManager(this);

        // ///// Set Latest First ////////////
        mLL.setReverseLayout(true);
        mLL.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLL);

        String userID = mFirebaseAuth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance().getReference("favorites").child(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserUploadFoodModel orders = ds.getValue(UserUploadFoodModel.class);

                    orderList.add(orders);

                }
                mRecyclerView.setAdapter(nearestAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNearestItemClick(View view, int position) {

        userUploadFoodModel = nearestAdapter.getItem(position);
        String ad_id = userUploadFoodModel.getAdId();
        String myTitle = userUploadFoodModel.getFoodTitle();
        String myDesc = userUploadFoodModel.getFoodDescription();
        String myImage = userUploadFoodModel.getmImageUri();
        String myPrice = userUploadFoodModel.getFoodPrice();
        String myTime = userUploadFoodModel.getFoodPickUpDetail();
        String myType = userUploadFoodModel.getFoodType();
        String myCuisineType = userUploadFoodModel.getFoodTypeCuisine();
        String pay = userUploadFoodModel.getPayment();
        User foodPostedBy = userUploadFoodModel.getFoodPostedBy();
        String available = userUploadFoodModel.getAvailabilityDays();
        String mImageUri = userUploadFoodModel.getmImageUri();
        ArrayList<String> imageArray = userUploadFoodModel.getmArrayString();



        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
        intent.putExtra("ad_id",ad_id);
        intent.putExtra("image", myImage);
        intent.putExtra("title", myTitle);
        intent.putExtra("description", myDesc);
        intent.putExtra("price", myPrice);
        intent.putExtra("time", myTime);
        intent.putExtra("type", myType);
        intent.putExtra("cuisineType", myCuisineType);
        intent.putExtra("pay", pay);
        intent.putExtra("foodPostedBy",foodPostedBy);
        intent.putExtra("availability", available);

        if(mImageUri!=null){
            intent.putExtra("imageUri", mImageUri);
        }
        else{
            intent.putStringArrayListExtra("imageArray",imageArray);
        }
        startActivity(intent);

    }

}
