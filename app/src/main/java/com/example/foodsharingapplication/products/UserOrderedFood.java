package com.example.foodsharingapplication.products;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.Adapters.UserUploadedFoodAdapter;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.ProductsFragment.ProductGridView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserOrderedFood extends AppCompatActivity implements UserUploadedFoodAdapter.ItemClickListener {


    private RecyclerView recyclerView;
    private UserUploadedFoodAdapter userUploadedFoodAdapter;
    private List<UserUploadFoodModel> orderList;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mFirebaseCurrentUser;
    private String ad_id;
    private UserUploadFoodModel orders;
    private static UserOrderedFood userOrderedFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_food);
        userOrderedFood = this;
        recyclerView = findViewById(R.id.userAdsRecyclerView);
        ad_id = getIntent().getStringExtra("ad_id");

        orderList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userUploadedFoodAdapter = new UserUploadedFoodAdapter(UserOrderedFood.this, orderList);
        userUploadedFoodAdapter.setClickListener(this);

        mFirebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance()
                .getReference("Orders").child(mFirebaseCurrentUser.getUid()).child("Product")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            orders = ds.getValue(UserUploadFoodModel.class);

                            orderList.add(orders);
                            Log.i("current Order: ",userUploadedFoodAdapter.toString());
                            recyclerView.setAdapter(userUploadedFoodAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static UserOrderedFood getInstance(){
        return userOrderedFood;
    }
    public void showSearch(UserUploadFoodModel searchResult){
        orderList.add(searchResult);
        Log.i("current Order: ",userUploadedFoodAdapter.toString());
        recyclerView.setAdapter(userUploadedFoodAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        /*UserUploadFoodModel clickedUserFoodModel = (UserUploadFoodModel) view.getTag();
        Intent intent1 = new Intent(this, PostDetailActivity.class);
        intent1.putExtra("orders", clickedUserFoodModel);
        startActivity(intent1);*/


        Intent intent = new Intent(UserOrderedFood.this, UserOrderedFoodDetails.class);
        orders = userUploadedFoodAdapter.getItem(position);
        intent.putExtra("FoodAdID", orders.getAdId());
        intent.putExtra("FoodTitle", orders.getFoodTitle());
        intent.putExtra("FoodType", orders.getFoodType());
        intent.putExtra("FoodCuisineType", orders.getFoodTypeCuisine());
        intent.putExtra("FoodDescription", orders.getFoodDescription());
        intent.putExtra("FoodPrice", orders.getFoodPrice());
        intent.putExtra("FoodSingleImage", orders.getmImageUri());
        intent.putExtra("FoodMultipleImages", orders.getmArrayString());
        startActivity(intent);
    }

  /*  private void saveRating() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, float rating, boolean fromUser) {
                //barRating = rating;
                Toast.makeText(UserOrderAndUploads.this, "rating"+rating, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance()
                        .getReference("Orders").child(mFirebaseCurrentUser.getUid()).child("Product").child("Rating").setValue(rating)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UserOrderAndUploads.this, "Thanks for rating", Toast.LENGTH_SHORT).show();
                                //ratingBar.setIsIndicator(false);
                            }
                        });
            }
        });
    }*/
}
