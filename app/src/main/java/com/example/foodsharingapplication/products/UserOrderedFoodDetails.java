package com.example.foodsharingapplication.products;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserOrderedFoodDetails extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView text_food_title;
    TextView text_food_desc;
    TextView text_food_type;
    TextView text_food_price;
    TextView text_food_cuisine_type;
    TextView text_food_pickUpDetails;
    TextView text_food_availablity;

    Button btnUpdate, btnDel;

    ImageView imageView;
    ViewPager viewPager;
    ViewPageAdapter adapter = null;

    RelativeLayout myRelativeLayout;
    UploadData uploadData;
    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private ArrayList<String> multipleImagesList = new ArrayList<String>();
    private String user_id = " ";
    private String all_products_del_key = " ";
    private User foodPostedBy;
    private String FoodAdID = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_food_details);

        firebaseAuth = FirebaseAuth.getInstance();
        uploadData = new UploadData();
        //updateProductFragment = new UpdateProductFragment();

        text_food_title = findViewById(R.id.textview_ad_name);
        text_food_desc = findViewById(R.id.textview_ad_desc);
        text_food_availablity = findViewById(R.id.textview_ad_availability);
        text_food_price = findViewById(R.id.textview_ad_estimated);
        text_food_type = findViewById(R.id.textview_ad_type);
        text_food_cuisine_type = findViewById(R.id.textview_ad_cuisine);
        text_food_pickUpDetails = findViewById(R.id.textview_ad_food_pickup_detail);
        myRelativeLayout = findViewById(R.id.myAdsLayout);

        RelativeLayout buttonsLayout = findViewById(R.id.buttonsLayout);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDel = findViewById(R.id.btnDelete);

        btnUpdate.setVisibility(View.GONE);

        imageView = findViewById(R.id.myAdsDetailsImageView);
        /*ViewPager*/
        viewPager = findViewById(R.id.myAdsDetailsViewPager);
        adapter = new ViewPageAdapter(this, mArrayUri);
        viewPager.getOffscreenPageLimit();
        viewPager.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();

        FoodAdID = getIntent().getStringExtra("FoodAdID");
        String FoodTitle = getIntent().getStringExtra("FoodTitle");
        String FoodType = getIntent().getStringExtra("FoodType");
        String FoodCuisineType = getIntent().getStringExtra("FoodCuisineType");
        String FoodDescription = getIntent().getStringExtra("FoodDescription");
        String FoodPrice = getIntent().getStringExtra("FoodPrice");
        String FoodPayment = getIntent().getStringExtra("FoodPayment");
        String FoodPostedBy = getIntent().getStringExtra("FoodPostedBy");
        String FoodSingleImage = getIntent().getStringExtra("FoodSingleImage");
        multipleImagesList = getIntent().getStringArrayListExtra("FoodMultipleImages");

        if (bundle != null) {
            if (FoodSingleImage != null) {
                Picasso.get().load(FoodSingleImage)
                        .fit()
                        .centerCrop()
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else if (!multipleImagesList.isEmpty()) {
                mArrayUri.clear();
                for (int i = 0; i < multipleImagesList.size(); i++) {
                    Uri tem_uri = Uri.parse(multipleImagesList.get(i));
                    mArrayUri.add(tem_uri);
                }
                viewPager.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
            text_food_title.setText(FoodTitle);
            text_food_desc.setText(FoodDescription);
            text_food_price.setText("Food Price: " + FoodPrice);
            text_food_type.setText(FoodType);
            text_food_cuisine_type.setText("Cuisine Type :" + FoodCuisineType);
        }

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userFoodDelReference;
                userFoodDelReference = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(firebaseAuth.getCurrentUser().getUid()).child("Product");
                userFoodDelReference.child(FoodAdID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Order Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }


}
