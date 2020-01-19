package com.example.foodsharingapplication.products;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.Products;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.userOrdersAndUploadedAds.UserOrderAndUploads;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Products.PAYPAL_CLIENT_ID);
    String payment;
    private TextView pTitle, pDescription, pPrice, pTime, pType, pCuisineType, pAvailable, pPayment;
    private ViewFlipper pFlip;
    //Get Data from Card
    private String title, desc, imageString, price, time, type, availablity, cuisineType, paymentimage2;
    private ArrayList<String> imageArray;
    private LinearLayout linLayout;
    //private ArrayList<String> imageArray;
    //Shehzad Paypal declarations start
    private Button btnPayNow;
    private int PAYPAL_REQ_CODE = 5;
    //Shehzad Paypal declarations end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

       /* //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/

        //Flipper View
        pFlip = findViewById(R.id.viewFlipper);
        pTitle = findViewById(R.id.titlePost);
        pDescription = findViewById(R.id.descPost);
        pPrice = findViewById(R.id.pricePost);
        pTime = findViewById(R.id.timePost);
        pType = findViewById(R.id.typePost);
        pAvailable = findViewById(R.id.availability);
        pCuisineType = findViewById(R.id.cuisineTypePost);
        pPayment = findViewById(R.id.paymentPost);
        linLayout = findViewById(R.id.linLayout_id);

        //Shehzad Paypal button Casting start
        btnPayNow = (Button) findViewById(R.id.btnPay);
        //Shehzad Paypal button Casting end

        //Get Data from Card
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("description");
        imageString = intent.getStringExtra("imageUri");
        price = intent.getStringExtra("price");
        time = intent.getStringExtra("time");
        type = intent.getStringExtra("type");
        availablity = getIntent().getStringExtra("availability");
        cuisineType = getIntent().getStringExtra("cuisineType");
        payment = getIntent().getStringExtra("pay");
        imageArray = getIntent().getStringArrayListExtra("imageArray");



        //Set Data in Views
        pTitle.setText(title);
        pDescription.setText(desc);
        pPrice.setText(price);
        pTime.setText(time);
        pType.setText(type);
        pAvailable.setText(availablity);
        pCuisineType.setText(cuisineType);
        pPayment.setText(payment);

        if(imageString!=null){
            ImageView imageV = new ImageView(PostDetailActivity.this);
            Picasso.get().load(imageString).into(imageV);
            pFlip.addView(imageV);
        }
        else{
            for (int i = 0; i < imageArray.size(); i++) {

                String uri = imageArray.get(i);
                ImageView imageV = new ImageView(PostDetailActivity.this);
                Picasso.get().load(uri).into(imageV);
                pFlip.addView(imageV);

            }
            Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

            //pFlip.setAutoStart(true);
            pFlip.setInAnimation(in);
            pFlip.setOutAnimation(out);
            pFlip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pFlip.showNext();
                }
            });
        }


        //pFlip.showNext();
        //pFlip.setFlipInterval(3000);

        //Shehzad Paypal Acting functionality called on click start
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithPaypal();
            }
        });
        //Shehzad Paypal Acting functionality called on click end

    }

    private void payWithPaypal() {
        /*int integerPrice = 0;
        try {
            integerPrice = Integer.parseInt(price);
        }catch (NumberFormatException e){
            System.out.println("not a number");
        }*/
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(2),
                "EUR", pTitle.getText().toString(), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Toast.makeText(this, "Successfully...", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
        if (requestCode == PAYPAL_REQ_CODE) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();

                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
                        Log.i("PAYMENTdETAILS: ", paymentDetails);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("Orders").child("UserID").child("Product");
                        UserUploadFoodModel uploadModel = new UserUploadFoodModel();
                        uploadModel.setFoodTitle(title);
                        uploadModel.setFoodDescription(desc);
                        uploadModel.setFoodPrice(price);
                        uploadModel.setFoodType(type);
                        uploadModel.setFoodTypeCuisine(cuisineType);
                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        databaseReference.setValue(uploadModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PostDetailActivity.this, "Successful Order", Toast.LENGTH_SHORT).show();
                                Intent userOrder = new Intent(PostDetailActivity.this, UserOrderAndUploads.class);
                                startActivity(userOrder);
                            }
                        });

                        //startActivity(new Intent(PostDetailActivity.this, UserOrderAndUploads.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {

                Toast.makeText(this, "Something went wrong WITH RESULT_OK", Toast.LENGTH_SHORT).show();

            }
        } else {

            Toast.makeText(this, "Something went wrong WITH PAYPAL_REQ_CODE", Toast.LENGTH_SHORT).show();

        }


    }

   /* @Override
    public void onActivityResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Toast.makeText(this, "Successfully Paid", Toast.LENGTH_SHORT).show();
        if (requestCode == PAYPAL_REQ_CODE) {

            if (requestCode == Activity.RESULT_OK) {


                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Orders").child("UserID"+" - "+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Product");
                UploadModel uploadModel= new UploadModel();
                uploadModel.setFoodTitle(title);
                uploadModel.setFoodDescription(desc);
                uploadModel.setFoodPrice(price);
                uploadModel.setFoodType(type);
                uploadModel.setFoodTypeCuisine(cuisineType);
                databaseReference.setValue(uploadModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostDetailActivity.this, "Successful Order", Toast.LENGTH_SHORT).show();
                        Intent userOrder = new Intent(PostDetailActivity.this, UserOrderAndUploads.class);
                        startActivity(userOrder);
                    }
                });


            } else {

                Toast.makeText(this, "Something went wrong during Payment", Toast.LENGTH_SHORT).show();

            }


        }
    }*/

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    // Handle Back press

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
