package com.example.foodsharingapplication.extras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class productdetails extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private TextView txtFoodTitle;
    private TextView txtFoodDescription;
    private TextView txtfoodLocation;
    private TextView txtFoodPrice;
    private TextView txtFoodRegionReview;
    private ImageView foodImagesView;

    private Button btnPayNow;
    private static final int PAYPAL_REQ_CODE=5;
    private static PayPalConfiguration payPalConfiguration=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Products.PAYPAL_CLIENT_ID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        txtFoodTitle = (TextView) findViewById(R.id.foodNameReview);
        txtFoodPrice = (TextView) findViewById(R.id.foodPriceReview);
        txtFoodDescription = (TextView) findViewById(R.id.foodDescriptionReview);
        txtfoodLocation = (TextView) findViewById(R.id.foodLocationReview);
        foodImagesView = (ImageView) findViewById(R.id.viewProductImageReview);
        txtFoodRegionReview=(TextView) findViewById(R.id.foodRegionReview);
        btnPayNow=(Button) findViewById(R.id.btnPay);

        Intent intentPaypal=new Intent(this, PayPalService.class);
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(intentPaypal);

        Intent intent=getIntent();
        if (intent!=null){
            String title=intent.getStringExtra("pTitle");
            String desc=intent.getStringExtra("pDesc");
            String price=intent.getStringExtra("pPrice");
            String imgUrl=intent.getStringExtra("pImgUrl");
            txtFoodTitle.setText(title);
            txtFoodDescription.setText(desc);
            txtFoodPrice.setText(price);
            Picasso.get().load(imgUrl).centerCrop().fit().into(foodImagesView);
        }


        /*mDatabaseReference= FirebaseDatabase.getInstance().getReference("Products");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mDataSnapshot:dataSnapshot.getChildren()){

                    Products products=mDataSnapshot.getValue(Products.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithPaypal();
            }
        });
    }

    private void payWithPaypal() {

        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(100), "EUR", "Sasta Food Application",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent= new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent,PAYPAL_REQ_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PAYPAL_REQ_CODE){

            if (requestCode == Activity.RESULT_OK){

                Toast.makeText(this,"Successfully Paid",Toast.LENGTH_SHORT).show();

            }
            else{

                Toast.makeText(this,"Something went wrong during Payment",Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void goToHome(View view) {
        startActivity(new Intent(getApplicationContext(),showproducts.class));
    }

}
