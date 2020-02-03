package com.example.foodsharingapplication.products.ProductsFragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.Adapters.NearestAdapter;
import com.example.foodsharingapplication.Adapters.UserUploadedFoodAdapter;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.extras.AllProducts;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.products.PostDetailActivity;
import com.example.foodsharingapplication.products.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodsharingapplication.HomeActivity.curr;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductGridView extends Fragment implements NearestAdapter.ItemClickListener {

    RecyclerView myRecyclerView, sRecyclerView;
    private List<UserUploadFoodModel> orderList;
    BottomNavigationView nav_bar;
    RecyclerView myRecyclerView2;
    LinearLayout slider_linear_layout;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    private static ProductGridView productGridView;

    private UserUploadedFoodAdapter userUploadedFoodAdapter;
    NearestAdapter nearestAdapter;
    UserUploadFoodModel userUploadFoodModel;

    public ProductGridView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_slider, container, false);
        orderList = new ArrayList<>();
        nearestAdapter = new NearestAdapter(getContext(), orderList);
        nearestAdapter.setClickListener(this);

        productGridView = this;
        // ///////ACTION BAR////////////
      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today's Menu");*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser();
        if (!firebaseAuth.getCurrentUser().equals(null)) {

        }

        // ///////Recycler View Find by ID//////////////////
        slider_linear_layout = view.findViewById(R.id.sliders_linear_layout);

        myRecyclerView = view.findViewById(R.id.slider_recycler_view);
        myRecyclerView.setHasFixedSize(true);

        myRecyclerView2 = view.findViewById(R.id.slider2_recycler_view);
        myRecyclerView2.setHasFixedSize(true);


       // sRecyclerView = view.findViewById(R.id.search_recycler_view);
//        sRecyclerView.setHasFixedSize(true);
        //LinearLayoutManager LL = new LinearLayoutManager(getContext());
        //sRecyclerView.setLayoutManager(LL);


        // ////////Make it Horizontal/////////////

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        myRecyclerView.setLayoutManager(layoutManager);
        myRef = FirebaseDatabase.getInstance().getReference();
        //Database Reference

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerView2.setLayoutManager(layoutManager2);

        return view;
    }



    public static ProductGridView getInstance(){
        return productGridView;
    }



    public void searchGridResults(List<UserUploadFoodModel> userUploadFoodModels){

         userUploadedFoodAdapter = new UserUploadedFoodAdapter(getContext(),userUploadFoodModels);
         sRecyclerView.setAdapter(userUploadedFoodAdapter);

    }

    // firebase Search ends here

    // ///////// Inflating View for Search Bar /////////
    // //////Selecting Setting Options//////
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
/*        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // //////////////Query and Populating Recycler View 1 ///////////

        Query query = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        final FirebaseRecyclerOptions<UserUploadFoodModel> options =
                new FirebaseRecyclerOptions.Builder<UserUploadFoodModel>().setQuery(query, UserUploadFoodModel.class).build();

        FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserUploadFoodModel, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull UserUploadFoodModel model) {
                if(model.getmImageUri()!=null) {
                    holder.setDetails(getContext(), model.getFoodTitle(), model.getmImageUri(), model.getUser()
                            .getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
                }
                else{
                    holder.setDetails(getContext(), model.getFoodTitle(), model.getmArrayString().get(0), model.getUser()
                            .getUserProfilePicUrl(), model.getFoodPrice(), model.getFoodPickUpDetail());
                }
                holder.itemView.findViewById(R.id.fav).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myImage = getItem(position).getmImageUri();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        String available = getItem(position).getAvailabilityDays();
                        String mImageUri = getItem(position).getmImageUri();
                        ArrayList<String> imageArray = getItem(position).getmArrayString();

                        favourites_check(ad_id,myTitle,myDesc,myImage,myPrice,myTime,myType,myCuisineType,pay,
                                foodPostedBy,available,mImageUri,imageArray, v);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grid, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);

                // ////////// Handling Click on Slider View 1 /////////////////

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String ad_id = getItem(position).getAdId();
                        String myTitle = getItem(position).getFoodTitle();
                        String myDesc = getItem(position).getFoodDescription();
                        String myImage = getItem(position).getmImageUri();
                        String myPrice = getItem(position).getFoodPrice();
                        String myTime = getItem(position).getFoodPickUpDetail();
                        String myType = getItem(position).getFoodType();
                        String myCuisineType = getItem(position).getFoodTypeCuisine();
                        String pay = getItem(position).getPayment();
                        String available = getItem(position).getAvailabilityDays();
                        User foodPostedBy = getItem(position).getFoodPostedBy();
                        String mImageUri = getItem(position).getmImageUri();
                        ArrayList<String> imageArray = getItem(position).getmArrayString();



                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("image", myImage);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("foodPostedBy",foodPostedBy);
                        intent.putExtra("availability", available);

                        if(mImageUri!=null){
                            intent.putExtra("imageUri", mImageUri);
                        }
                        else{
                            intent.putStringArrayListExtra("imageArray",imageArray);
                        }

                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };
        firebaseRecyclerAdapter.startListening();
        myRecyclerView.setAdapter(firebaseRecyclerAdapter);


        // //////////////Query and Populating Slider Recycler View 2 ///////////

        Query near = FirebaseDatabase.getInstance().getReference("Food").child("FoodByAllUsers");
        near.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserUploadFoodModel orders = ds.getValue(UserUploadFoodModel.class);
                    double distance;
                    distance = calculateDistance(49.932098388671875, 11.586983680725098, orders.getLatitude(), orders.getLongitude());
                    //Toast.makeText(getContext(), "distance" + distance, Toast.LENGTH_SHORT).show();
                    // distance is in KM
                    if (distance < 3) {
                        orderList.add(orders);
                    }

                }
                myRecyclerView2.setAdapter(nearestAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    // checking favorites
    public void favourites_check(String ad_id,String myTitle,String myDesc,String myImage,String myPrice,String myTime , String myType,
                                 String myCuisineType, String pay, User foodPostedBy,String available,String mImageUri,
                                 ArrayList<String> imageArray, View view){
        ImageButton favImage = view.findViewById(R.id.fav);
        final DatabaseReference favorites = FirebaseDatabase.getInstance().getReference().child("favorites");
        UserUploadFoodModel objFav = new UserUploadFoodModel();


        if(favImage.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_favorite_border_black_24dp).getConstantState()){
            favImage.setImageResource(R.drawable.ic_favorite_black_24dp);

            objFav.setAdId(ad_id);
            objFav.setFoodTitle(myTitle);
            objFav.setFoodDescription(myDesc);
            //objFav.setmImageUri(myImage);
            objFav.setFoodPrice(myPrice);
            objFav.setAvailabilityDays(available);
            objFav.setFoodTypeCuisine(myCuisineType);
            objFav.setPayment(pay);
            objFav.setFoodPostedBy(foodPostedBy);
            objFav.setFoodPickUpDetail(myTime);
            objFav.setFoodType(myType);

            if(mImageUri!=null) {
                objFav.setmImageUri(mImageUri);
            }
            else{
                ArrayList<String> imageUris = new ArrayList<String>();
                for(int i=0; i<imageArray.size();i++){
                    String uri = imageArray.get(i);
                    imageUris.add(uri);
                }
                objFav.setmArrayString(imageUris);

            }


            String userID = firebaseAuth.getCurrentUser().getUid();
            favorites.child(userID).child(ad_id).setValue(objFav);
        }
        else {
            favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onNearestItemClick(View view, int position) {

        userUploadFoodModel = nearestAdapter.getItem(position);
        String ad_id = userUploadFoodModel.getAdId();
                        String myTitle = userUploadFoodModel.getFoodTitle();
                        String myDesc = userUploadFoodModel.getFoodDescription();
                        String myImage = userUploadFoodModel.getmImageUri();
                        String myPrice = userUploadFoodModel.getFoodPrice();
                        String myTime = userUploadFoodModel.getFoodPickUpDetail();
                        String myType = userUploadFoodModel.getFoodType();
                        String myCuisineType = userUploadFoodModel.getFoodTypeCuisine();
                        String pay = userUploadFoodModel.getPayment();
                        User foodPostedBy = userUploadFoodModel.getFoodPostedBy();
                        String available = userUploadFoodModel.getAvailabilityDays();
                        String mImageUri = userUploadFoodModel.getmImageUri();
                        ArrayList<String> imageArray = userUploadFoodModel.getmArrayString();



                        Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                        intent.putExtra("ad_id",ad_id);
                        intent.putExtra("image", myImage);
                        intent.putExtra("title", myTitle);
                        intent.putExtra("description", myDesc);
                        intent.putExtra("price", myPrice);
                        intent.putExtra("time", myTime);
                        intent.putExtra("type", myType);
                        intent.putExtra("cuisineType", myCuisineType);
                        intent.putExtra("pay", pay);
                        intent.putExtra("foodPostedBy",foodPostedBy);
                        intent.putExtra("availability", available);

                        if(mImageUri!=null){
                            intent.putExtra("imageUri", mImageUri);
                        }
                        else{
                            intent.putStringArrayListExtra("imageArray",imageArray);
                        }
                        startActivity(intent);

    }
}
