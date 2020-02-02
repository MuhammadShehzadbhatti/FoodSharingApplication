package com.example.foodsharingapplication.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodsharingapplication.HomeActivity;
import com.example.foodsharingapplication.HomeDefinition;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.ShowData_Fragment;
import com.example.foodsharingapplication.model.User;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignIn extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button btnSignIn, btnsignOut, loginFb, btnProfile;
    EditText email, pass;
    TextView forgetPassword,txtRegister;
    public static final String id = "-LtvYIg-vEh0iY78nmOJ";
    private LoginButton FacebookButton;
    private CircleImageView circleImageView;
    private TextView txtFbName, txtFbEmail;
    private CallbackManager callBackManager;
    private ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRef, refToPointNode;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    User verifyUser;
    List<User> userList;
    ProgressDialog progress;
    private String loginEmail;
    private String loginPassword;
    private ShowData_Fragment showData_fragment;
    private FragmentManager fragManger;
    private FragmentTransaction fragTrans;
    private String fbName;
    private String fbEmail;
    private String fbid;
    private String fbCountry;
    private String fbAge;
    private String fbphoneNumber;
    private String fbImageUrl;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = (EditText) findViewById(R.id.enterUserEmail);
        pass = (EditText) findViewById(R.id.enterUserLoginPassword);
        txtFbEmail = findViewById(R.id.profile_email);
        txtFbName = findViewById(R.id.enterLoginPassword);
        circleImageView = findViewById(R.id.profile_pic);
        forgetPassword = (TextView) findViewById(R.id.forgetLoginPassword);

        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnSignIn = (Button) findViewById(R.id.btnLogin);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("signInPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();



        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, com.example.foodsharingapplication.authentication.forgetPassword.class);
                startActivity(intent);
            }
        });

        userList = new ArrayList<User>();
        verifyUser = new User();
        //database reference pointing to root of database
        firebaseRef = database.getInstance().getReference("User");
        //database reference pointing to User node
        refToPointNode = firebaseRef.child("User").child(id);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = email.getText().toString().trim();
                loginPassword = pass.getText().toString().trim();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword) ) {

                    firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                            startActivity(new Intent(SignIn.this, HomeActivity.class));
                                            finish();
                                        }
                                        else{
                                            email.setError("Email is unverified");
                                            email.requestFocus();
                                        }
                                    } else {
                                        Toast.makeText(SignIn.this, "Please check Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(SignIn.this, "Please enter valid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });



        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignIn.this, "Signing Out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignIn.this, User_SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignIn.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
