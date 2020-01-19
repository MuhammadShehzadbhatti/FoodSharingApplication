package com.example.foodsharingapplication.userOrdersAndUploadedAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.ProductsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserOrderAndUploads extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductsAdapter allProductsAdapter;
    private List<UserUploadFoodModel> productsList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private String userId, productTitle, productPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productslist);
        recyclerView=findViewById(R.id.productsListRecyclerView);
        recyclerView.setHasFixedSize(true);
        //txtAddProduct=(Button) findViewById(R.id.txtAddProduct);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        /*User user= new User();
        user = getIntent().getParcelableExtra("userOrder");
        UploadModel uploadModel = new UploadModel();
        uploadModel = user.getUploadModel();*/
        productsList=new ArrayList<>();
        mFirebaseAuth= FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Orders").child(mFirebaseAuth.getCurrentUser().getUid()).child("Product");
        /*mDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Orders").child(mFirebaseAuth.getCurrentUser().getUid()).child("Product");*/
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    UserUploadFoodModel orders=ds.getValue(UserUploadFoodModel.class);
                    if (orders != null){
                        productsList.add(orders);
                        allProductsAdapter = new ProductsAdapter(UserOrderAndUploads.this,productsList);
                        recyclerView.setAdapter(allProductsAdapter);
                    }
                    else {
                        Toast.makeText(UserOrderAndUploads.this, "Order Products", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserOrderAndUploads.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
