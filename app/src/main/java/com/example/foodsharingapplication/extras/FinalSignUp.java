package com.example.foodsharingapplication.extras;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodsharingapplication.HomeActivity;
import com.example.foodsharingapplication.authentication.DatePickerFragment;
import com.example.foodsharingapplication.extras.MyDatePickerFragment;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.ShowData_Fragment;
import com.example.foodsharingapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class FinalSignUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button btnChoose;
    private Button btnUpload;
    private Button btnCancel;
    private Button btnChangeDate;
    private Button btnRegister;
    private DatePicker datePicker;
    private ProgressBar progressBar;
    private ImageView profileImageView;

    private StorageTask uploadImageTask;
    private FirebaseStorage firebasestorage;
    private StorageReference firebaseStorageReference;
    private DatabaseReference firebaseDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private Uri filePath;

    private User userData;
    private EditText imagename;
    private TextView dateText;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bundle dataBundle;
    private FragmentManager fragManger;
    private FragmentTransaction fragTrans;
    private ShowData_Fragment showDataInFragment;
    private String imageUrl, imageName;
    private String dateOfBirth="";
    private Spinner spinnerCountriesList;
    private String selectedCountry="";
    private String name;
    private String email ;
    private String password ;
    private String phoneNumber ;
    private String gender ;
    /*private Button btnCountry_picker;
    private ImageView countryFlag;
    private CountryPicker countryPicker;
    private TextView txtCountryName;*/
    private String strCountry_isoCode,strCountry_DialCode,strCountry_Name,strCountry_Currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_sign_up);


        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRegister =  (Button) findViewById(R.id.btnSignup);
        btnChangeDate =  (Button) findViewById(R.id.changeDate);
        datePicker =  (DatePicker) findViewById(R.id.datePicker);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar );
        profileImageView = (ImageView) findViewById(R.id.profileImgView);
        final ImageView logoImage = (ImageView) findViewById(R.id.logoImg);
        dateText=(TextView) findViewById(R.id.dateText);
        //spinnerCountriesList= (Spinner) findViewById(R.id.countriesList);

        final String userCountry = "";
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"DatePicker");
            }
        });
        userData = new User();
        dataBundle= new Bundle();
        fragManger=getSupportFragmentManager();
        fragTrans=fragManger.beginTransaction();
        showDataInFragment= new ShowData_Fragment();

        firebaseAuth = FirebaseAuth.getInstance();
        firebasestorage= FirebaseStorage.getInstance();
        firebaseStorageReference=firebasestorage.getReference("User");
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("User");

        /*//
    Country with Flag Picker starts

        btnCountry_picker=(Button) findViewById(R.id.countryPicker);
        countryFlag=(ImageView) findViewById(R.id.selected_country_flag_image_view);
        txtCountryName = (TextView) findViewById(R.id.countryName);
        btnCountry_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // Country with flag Picker ends*/

        Intent intent = getIntent();
        //dateText.setText("Current Date: " + getCurrentDate());
        name  = intent.getStringExtra("name");
        email = intent.getStringExtra("uEmail");
        password = intent.getStringExtra("uPassword");
        phoneNumber = intent.getStringExtra("uPhoneNumber");
        gender = intent.getStringExtra("uGender");
        strCountry_Name = intent.getStringExtra("country_Name");
        strCountry_Currency = intent.getStringExtra("country_Currency");
        strCountry_DialCode = intent.getStringExtra("country_DialCode");
        strCountry_isoCode = intent.getStringExtra("country_isoCode");
        dateOfBirth = dateText.getText().toString().trim();
        final String finalMobileNumber=strCountry_DialCode+phoneNumber;
        /*btnChangeDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.GONE);
                dateText.setText(getCurrentDate());
                datePicker.setVisibility(View.VISIBLE);
            }

        });*/


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            assert password != null;
            assert email != null;
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userData.setUserName(name);
                        userData.setUserEmail(email);
                        userData.setUserPassword(password);
                        userData.setUserPhoneNumber(finalMobileNumber);
                        userData.setSex(gender);
                        userData.setUserDateOfBirth(dateText.getText().toString());
                        userData.setUserCountryCurrency(strCountry_Currency);
                        //userData.setUserCountryDialCode(strCountry_DialCode);
                        userData.setUserCountryISO(strCountry_isoCode);
                        userData.setUserCountry(strCountry_Name);
                        //userData.setUserCountry(selectedCountry);
                        userData.setUserWithFb(Boolean.FALSE);
                        assert name != null;
                        Log.i(" User Name ",name);
                        Log.i(" User Email ",email);
                        Log.i(" User Password ",password);
                        assert phoneNumber != null;
                        Log.i(" User phonenumber ",phoneNumber);
                        assert gender != null;
                        Log.i(" User gender ",gender);
                        Log.i(" User DOB ",dateOfBirth);
                        //User profile picture and its Uri are set in userData but need to update in database

                        String userKey= firebaseDatabaseRef.push().getKey();
                        assert userKey != null;
                        //firebaseDatabaseRef.child(userKey).setValue(userData);
                        firebaseDatabaseRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(FinalSignUp.this,"Registered Successfully",Toast.LENGTH_SHORT).show();

                                /*dataBundle.putString("name", userData.getUserName());
                                dataBundle.putString("email", userData.getUserEmail());
                                dataBundle.putString("password", userData.getUserPassword());
                                dataBundle.putString("phoneNumber", userData.getUserPhoneNumber());
                                dataBundle.putString("imageUrl", userData.getUserProfilePicUrl());
                                //progress.dismiss();
                                showDataInFragment.setArguments(dataBundle);*/
                                btnCancel.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.GONE);
                                btnChoose.setVisibility(View.GONE);
                                btnUpload.setVisibility(View.GONE);
                                datePicker.setVisibility(View.GONE);
                                //fragTrans.add(R.id.fragment_container,showDataInFragment);
                               // fragTrans.commit();
                                Intent intent1=new Intent(FinalSignUp.this, HomeActivity.class);
                                //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent1);


                            }
                        });

                    }
                    else{
                        //do on failure

                        Toast.makeText(FinalSignUp.this,"Some Thing went wrong for Registration",Toast.LENGTH_SHORT).show();

                    }
                    }
                });
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                logoImage.setVisibility(View.GONE);
                profileImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadImageTask != null && uploadImageTask.isInProgress()){
                    Toast.makeText(FinalSignUp.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }
            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String pickedDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        dateText.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        Log.i("Picked Date",dateText.getText().toString());
    }
    //Country picker implementation
   /* @Override
    public void onSelectCountry(Country country){
        countryFlag.setImageResource(country.getFlag());
        txtCountryName.setText(country.getName());
        userData.setUserCountry(txtCountryName.toString());
        userData.setUserCountryCurrency(country.getCurrency());
        userData.setUserCountryDialCode(country.getDialCode());
        userData.setUserCountryISO(country.getCode());

        strCountry_Currency= country.getCurrency();
        strCountry_DialCode=country.getDialCode();
        strCountry_isoCode = country.getCode();
        strCountry_Name=country.getName();
        Log.i("Country Currency:",strCountry_Currency);
        Log.i("Country DialCode:",strCountry_DialCode);
        Log.i("Country ISO Code:",strCountry_isoCode);
        Log.i("Country Name:",strCountry_Name);
    }*/


    //Country picker implementation
   /* private String getCurrentDate() {
        datePicker.setVisibility(View.GONE);
        StringBuilder builder=new StringBuilder();
        builder.append((datePicker.getMonth() + 1)+"/");//month is 0 based
        builder.append(datePicker.getDayOfMonth()+"/");
        builder.append(datePicker.getYear());
        return builder.toString();
    }*/


    //Image Choose and Upload Related Start
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            filePath = data.getData();
            Picasso.get().load(filePath).into(profileImageView);
            try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            profileImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
            e.printStackTrace();
            }
        }
    }
    //For image extension e.g. .jpg or .png
    private  String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadImage() {
        if(filePath != null){
            final StorageReference ref = firebaseStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));
            uploadImageTask = ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 2000);

                            //progressDialog.dismiss();

                           //imageName = imagename.getText().toString().trim();
                           Task<Uri> dounloadUriTask= taskSnapshot.getMetadata().getReference().getDownloadUrl();
                           dounloadUriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                   //Log.e("download url : ", imageUrl);
                                   userData.setUserProfilePicUrl(imageUrl);
                               }
                           });
                           //imageUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();
                           userData.setUserProfilePicName(name+"_"+email);
                           //userData.setUserProfilePicUrl(imageUrl);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.dismiss();
                            Toast.makeText(FinalSignUp.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });
        }
        else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new MyDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }
}
