package com.example.foodsharingapplication.authentication.AuthemnticationFragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.authentication.SignIn;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.facebook.AccessTokenManager.TAG;

public class UpdateProfile extends Fragment implements DialogeConfirmCredentials.ConfirmUserCredentials {

    FirebaseAuth firebaseAuth;
    private TextInputEditText editUserName, addUserNewPassword, confirmUserNewPassword;
    private Button btnSubmitChanges;
    private TextView editProfile;
    private ImageView userProfilePic, logoImg;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String email, password, userName;
    private DialogeConfirmCredentials dialogeConfirmCredentials;
    private User userData;
    private SignIn signIn;
    private Uri filePath;
    private StorageReference firebaseStorageReference;
    private ImageButton btnUser_ChooseImage, btnUser_UploadImage;
    private StorageTask uploadImageTask;
    private String imageUrl;

    public UpdateProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        editUserName = view.findViewById(R.id.editUserName);
        addUserNewPassword = view.findViewById(R.id.editUserNewPassword);
        confirmUserNewPassword = view.findViewById(R.id.editUserConfirmPassword);
        editProfile = view.findViewById(R.id.editProfile);
        btnSubmitChanges = view.findViewById(R.id.btnSubmitUpdates);
        userProfilePic = view.findViewById(R.id.userProfilePic);
        btnUser_ChooseImage = view.findViewById(R.id.btnChoose_SignUp);
        logoImg = view.findViewById(R.id.logoImg);

        userProfilePic.setVisibility(View.VISIBLE);
        logoImg.setVisibility(View.GONE);

        userData = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        dialogeConfirmCredentials = new DialogeConfirmCredentials(getContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        dialogeConfirmCredentials.setDatabaseReference(databaseReference);

        firebaseStorageReference = FirebaseStorage.getInstance().getReference("User");
        editProfile.setText(firebaseUser.getEmail());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(User.class);
                editUserName.setText(userData.getUserName());
                Picasso.get().load(userData.getUserProfilePicUrl()).centerCrop().fit().into(userProfilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnUser_ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("editUserName : ", editUserName.getText().toString());
                userName = editUserName.getText().toString();
                userData.setUserName(userName);
                uploadData();
                try {
                    if (!TextUtils.isEmpty(addUserNewPassword.getText().toString())) {
                        if (addUserNewPassword.getText().toString().length() >= 6) {
                            if (!TextUtils.isEmpty(confirmUserNewPassword.getText().toString())) {
                                if (addUserNewPassword.getText().toString().equals(confirmUserNewPassword.getText().toString())) {
                                    //intent.putExtra("uPassword", pass.getText().toString().trim());
                                    openLoginDialog();
                                } else {
                                    toastMessage("Password does not match");
                                }
                            } else {
                                toastMessage("Confirm Password can't be empty");
                            }
                        } else {
                            toastMessage("password must include at least 6 characters");
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "Catch: on Password nullException" + e.getMessage());
                }
            }
        });

        return view;
    }

    private void uploadData() {
        databaseReference.setValue(userData);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    Picasso.get().load(filePath).centerCrop().resize(90, 90).into(userProfilePic);
                    uploadImage();
                    // btnUser_UploadImage.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data.getData() != null) {
                    filePath = data.getData();
                    Picasso.get().load(filePath).centerCrop().resize(90, 90).into(userProfilePic);
                    uploadImage();
                    //btnUser_UploadImage.setVisibility(View.VISIBLE);
                    btnUser_ChooseImage.setVisibility(View.GONE);
                }
                break;

        }
    }

    //For image extension e.g. .jpg or .png
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
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
                                        userData.setUserProfilePicUrl(imageUrl);
                                        uploadData();


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
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void insertUpdatedData(){

        firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                toastMessage("Email Updated1");
            }
        });
        firebaseUser.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            toastMessage("Password Update1");
                        }
                    }
                });
        userData.setUserName(userName);
        databaseReference.child(firebaseUser.getUid()).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //startActivity(new Intent(getContext(), Profile.class));
                        toastMessage("Profile1 Updated successfully");
                    }
                });
    }*/
    public void openLoginDialog() {
        dialogeConfirmCredentials.show(getFragmentManager(), "Before Updating Confirm Credentials");
        dialogeConfirmCredentials.setTargetFragment(UpdateProfile.this, 1);
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            toastMessage("Email is not Valid");
        }
        return matcher.matches();
    }

    private void toastMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmUser(final String Email, String confirmedPassword) {
        final String userEmail = Email;
        final String mPassword = confirmedPassword;
        Log.i("Pass,email 4m DCC", confirmedPassword + "  " + Email);
        AuthCredential authCredential = EmailAuthProvider
                .getCredential(firebaseUser.getEmail(), confirmedPassword);
        firebaseUser.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, " User Re_Authenticated");
                            /*try {
                                if (!editUserEmail.getText().toString().isEmpty()){
                                    // check if new email is not already in use

                                    firebaseAuth.fetchSignInMethodsForEmail(editUserEmail.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                                    if (isNewUser) {
                                                        Log.e("TAG", "Email is Available!");
                                                        firebaseUser.updateEmail(editUserEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getContext(),"Email Updated",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    } else {
                                                        Log.e("TAG", "Email already exists!");
                                                    }

                                                }
                                            });
                                }
                                else {
                                    Toast.makeText(getContext(),"Email is not Updated",Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (NullPointerException e){
                                Log.e(TAG,"Catch: on Email NullException" +e.getMessage());
                            }*/

                            try {
                                password = addUserNewPassword.getText().toString();
                                if (mPassword != password) {

                                    firebaseUser.updatePassword(addUserNewPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();

                                                        firebaseAuth.signOut();
                                                        startActivity(new Intent(getContext(), SignIn.class));
                                                    } else {
                                                        toastMessage("Can't Update Password");
                                                    }
                                                }
                                            });
                                } else {
                                    toastMessage("Must enter new Password");
                                }
                            } catch (NullPointerException e) {
                                Log.e(TAG, "Catch: on Password nullException" + e.getMessage());
                            }

                        } else {
                            Log.d(TAG, " User Re_Authentication failed");
                        }
                    }
                });

    }

}
