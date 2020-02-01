package com.example.foodsharingapplication.products.ProductsFragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.foodsharingapplication.HomeActivity;
import com.example.foodsharingapplication.HomeDefinition;
import com.example.foodsharingapplication.Maps.MapsActivity;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.UserUploadedFoodDetails;
import com.example.foodsharingapplication.products.ViewPageAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.Executor;


import me.abhinay.input.CurrencyEditText;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadDataFragment extends Fragment {

    /*Model Class*/
    UserUploadFoodModel userUploadFoodModel;


    FirebaseAuth firebaseAuth;

    private User currentUser = null;

    /*Layouts*/
    private LinearLayout imageViewLayout, buttonLayout;
    RelativeLayout viewPagerLayout;

    /*Buttons*/
    private Button chooseImage, btnChoose, btnSubmit;

    /*Image View*/
    ImageView imageView, imgView, mainImageView;

    /*EditText*/
    private static CurrencyEditText editTextPrice;
    private static EditText editTextTitle, editTextDescription, editTextPickUpDetails;

    /*Radio UI*/
    private RadioGroup foodTypeRadioGroup, paymentGroup;
    private RadioButton cookedFoodRadioBtn, rawFoodRadioBtn, paypalOption, bankTransferOption, cashOnDeliveryOption;

    /*Spinner UI*/
    private SmartMaterialSpinner cuisineTypeSpinner, listFoodSpinner;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    /*ViewPager*/
    private ViewPager viewPager;
    private ViewPageAdapter adapter = null;

    /*Other Variable Declaration*/
    private String cuisine_type, days, title, description, pickUpDetails, price;
    private static final String TAG = UploadDataFragment.class.getSimpleName();

    /*ArrayList*/
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    final ArrayList<String> arrayList = new ArrayList<>();
    private Uri mImageUri;
    private int uploadCount = 0;


    private String[] cuisines = {"Asian" , "Italian", "French", "FastFood", "German", "Continental",
    "South American", "Arabic", "African"};

    ArrayAdapter<String> foodListingAdapter;
    private HomeActivity home = new HomeActivity();

    /*Other Static declarations*/
    private static final int MULTIPLE_IMAGE_REQUEST = 2;
    ProgressDialog progressDialog;

    public UploadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_upload_data, container, false);

        /*Model Class*/
        userUploadFoodModel = new UserUploadFoodModel();
        firebaseAuth = FirebaseAuth.getInstance();

        /*Layouts*/
        viewPagerLayout =  view.findViewById(R.id.test1);
        imageViewLayout =  view.findViewById(R.id.imageViewLayout);
        buttonLayout =  view.findViewById(R.id.layout_buttons);

        /*ViewPager*/
        viewPager = view.findViewById(R.id.flipperView);
        adapter = new ViewPageAdapter(getContext(), mArrayUri);
        viewPager.setAdapter(adapter);

        /*ImageView*/
        imageView = view.findViewById(R.id.mainImage);

        /*EditTexts*/
        editTextTitle =  view.findViewById(R.id.editTextTitle);
        editTextDescription =  view.findViewById(R.id.editTextDescription);
        editTextPickUpDetails = view.findViewById(R.id.editTextPickUpDetails);
        editTextPrice =  view.findViewById(R.id.etPrice);
        editTextPrice.setCurrency("€");
        editTextPrice.setDelimiter(false);
        editTextPrice.setSpacing(false);
        editTextPrice.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        editTextPrice.setSeparator(".");


        /*Buttons*/
        btnChoose = view.findViewById(R.id.btnChoosePhoto);
        btnSubmit = view.findViewById(R.id.submitData);

        /*RadioGroups*/
        /*FoodType*/
        foodTypeRadioGroup = view.findViewById(R.id.radioGroup);
        cookedFoodRadioBtn =  view.findViewById(R.id.radioCookedFood);
        rawFoodRadioBtn = view.findViewById(R.id.radioRawFood);
        /*Payment*/
        paymentGroup = view.findViewById(R.id.radioGroupPaymentMethods);
        paypalOption = view.findViewById(R.id.paypal);
        bankTransferOption = view.findViewById(R.id.bankTransfer);
        cashOnDeliveryOption = view.findViewById(R.id.cashOnDelivery);

        /*Spinners*/
        cuisineTypeSpinner = view.findViewById(R.id.cuisine_spinner);
        ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,
                cuisines);
        cuisineTypeSpinner.setAdapter(cuisineAdapter);
        cuisineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cuisineType =parent.getItemAtPosition(position).toString();
                userUploadFoodModel.setFoodTypeCuisine(cuisineType);
                /*cuisine_type = cuisineTypeSpinner.getSelectedItem().toString();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listFoodSpinner = view.findViewById(R.id.days_spinner);
        initDaysSpinner();

        /*ProgressDialog*/
        progressDialog = new ProgressDialog(getContext());

        /*Listeners*/
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagesFromGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mArrayUri.isEmpty()) {
                    uploadMultipleImages();
                } else if (mImageUri != null) {
                    uploadSingleImage();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        foodTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (cookedFoodRadioBtn.isChecked()) {
                    /*sData.setCookedFood("Cooked Food");*/
                    userUploadFoodModel.setFoodType("CookedFood");
                } else {
                    /*sData.setCookedFood("Raw Food");*/
                    userUploadFoodModel.setFoodType("RawFood");
                }
            }
        });

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (paypalOption.isChecked()) {
                    userUploadFoodModel.setPayment("Paypal");
                } else if (bankTransferOption.isChecked()) {
                    userUploadFoodModel.setPayment("Bank Transfer");
                } else if (cashOnDeliveryOption.isChecked()) {
                    userUploadFoodModel.setPayment("Cash On Delivery");
                }
            }
        });

        /*Fetching User Model From Firebase*/
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);


                    if (firebaseAuth.getUid().equals(user.getUserId())) {
                        currentUser = user;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    /*Intent Function for Open Gallery*/
    private void selectImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), MULTIPLE_IMAGE_REQUEST);
    }

    /*Function for Converting Dp to Pixel*/
    public int convertDpToPixelInt(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    /*Activity Result for Image Intent*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MULTIPLE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                mArrayUri.clear();
                imageView.setImageBitmap(null);
                int count = data.getClipData().getItemCount();
                Log.i("count", String.valueOf(count));
                int currentItem = 0;
                while (currentItem < count) {
                    mImageUri = data.getClipData().getItemAt(currentItem).getUri();
                    Log.i("uri", mImageUri.toString());
                    mArrayUri.add(mImageUri);
                    currentItem = currentItem + 1;

                }
                Log.i("listsize", String.valueOf(mArrayUri.size()));
                imageView.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                imageViewLayout.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewPagerLayout.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300, getContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);

                adapter.notifyDataSetChanged();

            } else if (data.getData() != null) {
                mImageUri = data.getData();
                Bitmap new_bitmap = null;
                try {

                    new_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                    imageView.setImageBitmap(new_bitmap);
                    imageViewLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    buttonLayout.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                lp.height = (int) convertDpToPixelInt(300, getContext()); //convert pixel to dp
                viewPagerLayout.setLayoutParams(lp);

                String imagePath = data.getData().getPath();
                Log.i("count", "imagePath:" + imagePath);
            }
        }
    }

    /*Spinner init()*//*
    private void initCuisineSpinner() {

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> countries_adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, countries);
        cuisineTypeSpinner.setAdapter(countries_adapter);



    }
*/
    private void initDaysSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            if (i == 1) {
                arrayList.add(i + " day");
            } else {
                arrayList.add(i + " days");
            }
        }
        listFoodSpinner.setItem(Collections.singletonList(days));
        foodListingAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listFoodSpinner.setAdapter(foodListingAdapter);

        listFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userUploadFoodModel.setAvailabilityDays(listFoodSpinner.getSelectedItem().toString());
                /*days = listFoodSpinner.getSelectedItem().toString();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /*Upload Single Image*/
    private void uploadSingleImage() {

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("SellerImageFolder").child("Images");

        final StorageReference singleImageName = ImageFolder.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

        singleImageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                singleImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = String.valueOf(uri);
                        Log.i("image Url", url);
                        userUploadFoodModel.setmImageUri(url);
                        uploadFood();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Log.i("Progress", "checking progress" + progress);
                progressDialog.setMessage("Uploaded" + (int) progress + "%");
            }
        });

    }
    /*Getting File Extension for single Image Uri*/
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /*Upload Multiple Image*/
    private void uploadMultipleImages() {

        progressDialog = ProgressDialog.show(getContext(), "Posting Data",
                "Uploading..", true);

        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference("SellerImageFolder").child("Images");

        arrayList.clear();
        for (uploadCount = 0; uploadCount < mArrayUri.size(); uploadCount++) {
            final Uri individualImage = mArrayUri.get(uploadCount);
            Log.i("Individual Image Uri:", String.valueOf(individualImage));
            final StorageReference imageName = ImageFolder.child("Image" + individualImage.getLastPathSegment());

            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = String.valueOf(uri);
                            Log.i("images Url", url);
                            //sData.setUri(mArrayUri);
                            //userUploadFoodModel.setmArrayUri(mArrayUri.get());
                            arrayList.add(url);

                            if (arrayList.size() == mArrayUri.size()) {
                                uploadDataWithMultipleImages();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Log.i("Progress", "checking progress" + progress);
                    progressDialog.setMessage("Uploaded" + (int) progress + "%");
                }
            });

        }


    }

    private void uploadDataWithMultipleImages(){
        userUploadFoodModel.setmArrayString(arrayList);
        progressDialog.cancel();
        uploadFood();
    }

    /*Sending Data to DB*/
    private void uploadFood() {

        String uploadDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        userUploadFoodModel.setFoodUploadDateAndTime(uploadDateTime);

        userUploadFoodModel.setFoodTitle(editTextTitle.getText().toString().trim());
        userUploadFoodModel.setFoodDescription(editTextDescription.getText().toString().trim());
        userUploadFoodModel.setFoodPickUpDetail(editTextPickUpDetails.getText().toString().trim());
        userUploadFoodModel.setFoodPrice(editTextPrice.getText().toString());
        userUploadFoodModel.setFoodPostedBy(currentUser);

        Log.i("Longitude latitude" ,String.valueOf(home.curr));
        Log.i("latitude" ,String.valueOf(home.curr.latitude));
        Log.i("Longitude" ,String.valueOf(home.curr.longitude));
        userUploadFoodModel.setLatitude(home.curr.latitude);
        userUploadFoodModel.setLongitude(home.curr.longitude);


        DatabaseReference userFoodPostingReference;
        userFoodPostingReference = FirebaseDatabase.getInstance().getReference("Food")
        .child("FoodByUser").child(firebaseAuth.getUid());

        String pushKey = userFoodPostingReference.push().getKey();
        userUploadFoodModel.setAdId(pushKey);
        userFoodPostingReference.child(pushKey).setValue(userUploadFoodModel);

        DatabaseReference allFoodPostingReference;
        allFoodPostingReference = FirebaseDatabase.getInstance().getReference("Food")
                .child("FoodByAllUsers");
        allFoodPostingReference.child(pushKey).setValue(userUploadFoodModel);
        Toast.makeText(getContext(), "Food Uploaded", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), HomeDefinition.class));
    }

}
