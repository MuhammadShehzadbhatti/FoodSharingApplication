package com.example.foodsharingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.authentication.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class HomeDefinition extends AppCompatActivity {

    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                intent = new Intent(this, HomeActivity.class);
            }else {
                toastMessage("Email verification required");
                firebaseAuth.signOut();
                intent = new Intent(this, SignIn.class);
            }
        } else {
            toastMessage("Login is required");
            intent = new Intent(this, SignIn.class);
        }
        startActivity(intent);
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

/*@Override
    public void onCreate() {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_definition);
        super.onCreate();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isBackPressed)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.,attachedfragmentHere);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.commit();

            Intent intent=new Intent(HomeDefinition.this, ProductGridView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        else{

            Intent intent=new  Intent(HomeDefinition.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }*/

}
