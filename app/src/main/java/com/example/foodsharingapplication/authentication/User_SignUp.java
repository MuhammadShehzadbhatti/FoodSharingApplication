package com.example.foodsharingapplication.authentication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsharingapplication.HomeDefinition;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_SignUp extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText txtUser_FirstName, txtUser_LastName, txtUser_Email, txtUser_ConfirmPassword;
    private TextInputEditText txtUser_Password;
    private ImageButton btnUser_ChooseImage, btnUser_UploadImage;
    private Button btnUser_SignUp;
    private ImageView imgUser_ProfileImage, logoImg_SignUp;
    private TextView txtGo_ToLogin;
    private FirebaseAuth firebaseAuth;
    private StorageReference firebaseStorageReference;
    private StorageTask uploadImageTask;
    private String imageUrl, userFullName, userEmail, userPassword;
    private Uri filePath;
    private User userData;
    private Authentication_Firebase authentication_firebase;
    private DatabaseReference firebaseDatabaseRef;
    private String firbaseEmail;
    private ArrayList<String> arrayListEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__sign_up);
        Toast.makeText(User_SignUp.this, "User_SignUp ", Toast.LENGTH_SHORT).show();
        txtUser_FirstName = findViewById(R.id.firstName_SignUp);
        txtUser_LastName = findViewById(R.id.lastName_SignUp);
        txtUser_Email = findViewById(R.id.enterEmail_SignUp);
        txtUser_Password = findViewById(R.id.enterPassword_SignUp);
        txtUser_ConfirmPassword = findViewById(R.id.confirmPassword_SignUp);
        btnUser_ChooseImage = findViewById(R.id.btnChoose_SignUp);
        btnUser_UploadImage = findViewById(R.id.btnUpload_SignUp);
        btnUser_SignUp = findViewById(R.id.btnSignup_SignUp);
        imgUser_ProfileImage = findViewById(R.id.profileImgView_SignUp);
        logoImg_SignUp = findViewById(R.id.logoImg_SignUp);
        txtGo_ToLogin = findViewById(R.id.txtSignIn_SignUp);
        arrayListEmail = new ArrayList<>();
        userData = new User();
        authentication_firebase = new Authentication_Firebase(getApplicationContext());
        firebaseStorageReference = FirebaseStorage.getInstance().getReference("User");
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        btnUser_ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        /*btnUser_UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });*/

        txtGo_ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        btnUser_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        /*if (!uploadImage().isEmpty()){
            userData.setUserProfilePicUrl(uploadImage());
        }
        else {
            toastMessage("Error in uploading picture ");
        }*/

        if (TextUtils.isEmpty(txtUser_FirstName.getText().toString())) {

            txtUser_FirstName.setError("First name Can't be empty");
            txtUser_FirstName.requestFocus();
        }
        if (TextUtils.isEmpty(txtUser_LastName.getText().toString())) {

            txtUser_LastName.setError("Last Name name Can't be empty");
            txtUser_LastName.requestFocus();

        } else {
            userFullName = txtUser_FirstName.getText().toString() + " " + txtUser_LastName.getText().toString();
            userData.setUserName(userFullName);
        }

        if (!TextUtils.isEmpty(txtUser_Email.getText().toString())) {
            if (isEmailValid(txtUser_Email.getText().toString())) {
                userData.setUserEmail(txtUser_Email.getText().toString().trim());
                userEmail = txtUser_Email.getText().toString();
            } else {
                txtUser_Email.setError("Enter a valid email address");
                txtUser_Email.requestFocus();
            }

        } else {

            txtUser_Email.setError("Email Can't be empty");
            txtUser_Email.requestFocus();

        }
        if (!TextUtils.isEmpty(txtUser_Password.getText().toString())) {
            if (txtUser_Password.getText().toString().length() >= 6) {

                if (!TextUtils.isEmpty(txtUser_ConfirmPassword.getText().toString())) {

                    if (txtUser_Password.getText().toString().equals(txtUser_ConfirmPassword.getText().toString())) {
                        userPassword = txtUser_Password.getText().toString().trim();

                    } else {

                        txtUser_ConfirmPassword.setError("Password Does not match");
                        txtUser_ConfirmPassword.requestFocus();
                        txtUser_Password.setError("Password Does not match");
                        txtUser_Password.requestFocus();

                    }

                } else {

                    txtUser_ConfirmPassword.setError("Confirm Password Can't be empty");
                    txtUser_ConfirmPassword.requestFocus();

                }
            } else {

                txtUser_Password.setError("Password must contain at least 6 characters");
                txtUser_Password.requestFocus();

            }

        } else {

            txtUser_Password.setError("Password Can't be empty");
            txtUser_Password.requestFocus();

        }
        try {
            if (!txtUser_Email.getText().toString().isEmpty() && !txtUser_Password.getText().toString().isEmpty()) {

                firebaseAuth.createUserWithEmailAndPassword(txtUser_Email.getText().toString(), txtUser_Password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userData.setUserId(firebaseAuth.getCurrentUser().getUid());
                                    if (!userData.getUserName().isEmpty()) {
                                        firebaseDatabaseRef.child(userData.getUserId()).setValue(userData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                                                    startActivity(new Intent(User_SignUp.this, HomeDefinition.class));
                                                                                    finish();
                                                                                } else {
                                                                                    firebaseAuth.signOut();
                                                                                    startActivity(new Intent(User_SignUp.this, HomeDefinition.class));
                                                                                    finish();
                                                                                }
                                                                                toastMessage("Check your email for verification");
                                                                                startActivity(new Intent(User_SignUp.this, HomeDefinition.class));
                                                                                finish();
                                                                            } else {
                                                                                toastMessage("Enter a valid email");
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });

                                    } else {
                                        //do on failure
                                        Toast.makeText(getApplicationContext(), "Verify your name", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //do on failure
                                    Toast.makeText(User_SignUp.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            } else {
                Toast.makeText(this, "Email or Password can't be empty", Toast.LENGTH_SHORT).show();
            }
            if (!userData.getUserProfilePicUrl().isEmpty()){

            }
        } catch (NullPointerException e) {
            Toast.makeText(User_SignUp.this, "Email already exists!", Toast.LENGTH_SHORT).show();
        }

    }

    //Image Choose and Upload Related Start
    private void chooseImage() {
        /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);*/
        Intent pickPicture = new Intent();
        pickPicture.setType("image/*");
        pickPicture.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickPicture, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    Picasso.get().load(filePath).centerCrop().resize(90, 90).into(logoImg_SignUp);
                    uploadImage();
                    btnUser_UploadImage.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data.getData() != null) {
                    filePath = data.getData();
                    Picasso.get().load(filePath).centerCrop().resize(90, 90).into(logoImg_SignUp);
                    uploadImage();
                    btnUser_UploadImage.setVisibility(View.VISIBLE);
                    btnUser_ChooseImage.setVisibility(View.GONE);
                }
                break;
        }
    }

    //For image extension e.g. .jpg or .png
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {
        toastMessage("file Path " + filePath.toString());
        if (filePath != null) {
            final StorageReference ref = firebaseStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));
            uploadImageTask = ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           /* Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 2000);*/

                            //progressDialog.dismiss();

                            //imageName = imagename.getText().toString().trim();
                            Task<Uri> downloadUriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            downloadUriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (!uri.toString().isEmpty()) {
                                        imageUrl = uri.toString();
                                        Log.i("download url1 : ", imageUrl);
                                        userData.setUserProfilePicUrl(imageUrl);
                                        //return imageUrl;
                                    } else {
                                        toastMessage("Profile picture not uploaded");
                                    }
                                }
                            });
                            //imageUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();
                            //userData.setUserProfilePicName(name+"_"+email);
                           /* Log.i("download url2 : ", imageUrl);
                            userData.setUserProfilePicUrl(imageUrl);*/

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.dismiss();
                            Toast.makeText(User_SignUp.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            //progressBar.setProgress((int)progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            toastMessage("please enter valid Email ");

        }
        return matcher.matches();
    }

    private void toastMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
