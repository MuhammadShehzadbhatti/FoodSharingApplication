package com.example.foodsharingapplication.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodsharingapplication.Adapters.UserUploadedFoodAdapter;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserUploadedFood extends AppCompatActivity implements UserUploadedFoodAdapter.ItemClickListener {


    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private UserUploadedFoodAdapter adapter;

    UserUploadFoodModel userUploadFoodModel;
    List<UserUploadFoodModel> userUploadFoodModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_food);

        firebaseAuth = FirebaseAuth.getInstance();
        userUploadFoodModel = new UserUploadFoodModel();

        DatabaseReference viewDatabaseReference;
        viewDatabaseReference = FirebaseDatabase.getInstance().getReference("Food").child("FoodByUser")
                .child(firebaseAuth.getCurrentUser().getUid());
        userUploadFoodModelList = new ArrayList<>();

        recyclerView = findViewById(R.id.userAdsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserUploadedFoodAdapter(this, userUploadFoodModelList);
        adapter.setClickListener(this);

        Toast.makeText(this,"hay hay", Toast.LENGTH_SHORT).show();

        viewDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userUploadFoodModelList.clear();
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    UserUploadFoodModel userUploadFoodModel = usersSnapshot.getValue(UserUploadFoodModel.class);
                    userUploadFoodModelList.add(userUploadFoodModel);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(UserUploadedFood.this, UserUploadedFoodDetails.class);
        userUploadFoodModel = adapter.getItem(position);
        String FoodAdID = userUploadFoodModel.getAdId();
        String myCurrentDateTime = userUploadFoodModel.getFoodUploadDateAndTime();
        String FoodTitle = userUploadFoodModel.getFoodTitle();
        String FoodType = userUploadFoodModel.getFoodType();
        String FoodCuisineType = userUploadFoodModel.getFoodTypeCuisine();
        String FoodDescription = userUploadFoodModel.getFoodDescription();
        String FoodPrice = userUploadFoodModel.getFoodPrice();
        String FoodPickUpDetail = userUploadFoodModel.getFoodPickUpDetail();
        String FoodAvailability = userUploadFoodModel.getAvailabilityDays();
        String FoodPayment = userUploadFoodModel.getPayment();
        String FoodSingleImage = userUploadFoodModel.getmImageUri();
        ArrayList<String> FoodMultipleImages = userUploadFoodModel.getmArrayString();

        intent.putExtra("myCurrentDateTime", myCurrentDateTime);
        intent.putExtra("FoodAdID", FoodAdID);
        intent.putExtra("FoodTitle", FoodTitle);
        intent.putExtra("FoodType", FoodType);
        intent.putExtra("FoodCuisineType", FoodCuisineType);
        intent.putExtra("FoodDescription", FoodDescription);
        intent.putExtra("FoodPrice", FoodPrice);
        intent.putExtra("FoodPickUpDetail", FoodPickUpDetail);
        intent.putExtra("FoodAvailability", FoodAvailability);
        intent.putExtra("FoodPayment", FoodPayment);
        intent.putExtra("FoodSingleImage", FoodSingleImage);
        intent.putExtra("FoodMultipleImages", FoodMultipleImages);
        startActivity(intent);

    }
}
