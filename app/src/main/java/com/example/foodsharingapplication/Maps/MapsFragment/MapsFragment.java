package com.example.foodsharingapplication.Maps.MapsFragment;


import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharingapplication.Maps.MapsActivity;
import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;
import com.example.foodsharingapplication.model.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.maps.android.SphericalUtil;

import java.sql.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    //var uid=user.uid;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SeekBar seekBar;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
//    final FirebaseDatabase database1 = FirebaseDatabase.getInstance("myfirstapplication-a9b8/User");
//    DatabaseReference ref = database1.getReference();

    //   DatabaseReference usersRef = ref.child("users");
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private int progress1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private String uid=new String();
    private GeoPoint geo;
    private CircleOptions circle =new CircleOptions();
    private Circle circle1;
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private FirebaseFirestore mDb;
    private ListenerRegistration mChatMessageEventListener, mUserListEventListener;
    private FirebaseDatabase database1;
    private DatabaseReference ref;
    private static ArrayList<UserLocation> Userloc=new ArrayList<>();;
    private ArrayAdapter<UserLocation> adapter;
    private UserLocation user;
    private LatLng diff;
    private LatLng curr;
    private static ArrayList<Double> distances=new ArrayList<>();
    private Array gmark;
    private ArrayList<Marker> mTripMarkers = new ArrayList<>();
    private View view;
    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_maps,container,false);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        Userloc.clear();
        distances.clear();
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        myRef.setValue("Hello, World!");

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }



        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current_place_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        getuser();

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) view.findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        circle1 = mMap.addCircle(circle
                .center(new LatLng(0,0))
                .radius(1000000)
                .strokeWidth(5f)
                .fillColor(0x515000FF));


        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            geo= new GeoPoint((float)mLastKnownLocation.getLatitude(),(float)mLastKnownLocation.getLongitude());
                            curr=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            writeNewUser(geo);
                            circle1.setCenter(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()));



                            //seekBar = (SeekBar) findViewById(R.id.seekBar);
                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override

                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    progress1=progress*1000;
                                    Log.e(TAG, "onProgressChanged: "+progress1);
                                    circle1.setRadius(progress1);
                                    Log.e(TAG,"user loc size= "+Userloc.size());
                                    for (int i = 0; i < Userloc.size(); i++){

                                        diff=new LatLng(Userloc.get(i).getLatitude(),Userloc.get(i).getLongitude());
                                        distances.add(distanceBetween(curr,diff));

                                    }
                                    Log.e(TAG,"distances size= "+Userloc.size());

                                    PointerPlacer(distances,progress1,Userloc);
                                }
                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });




                            //           FirebaseDatabase.getInstance().getReference("UserLocation/uid").setValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                             @Override
//                             public void onComplete(@NonNull Task<Void> task) {
//                                 if(task.isSuccessful()){
//                                     Toast.makeText(MapsActivity.this,"Location saved",Toast.LENGTH_LONG).show();
//                                 }
//                             }
//                         });


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void PointerPlacer(ArrayList<Double> distances, int progress1, ArrayList<UserLocation> userloc) {
        removeTripMarkers();

        if (progress1==1000) {
            for (int i = 0; i < userloc.size(); i++){
                if (distances.get(i)<1000 && distances.get(i)>10){
                    mTripMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(userloc.get(i).getLatitude(), userloc.get(i).getLongitude()))
                            .title("Hello world")));
                    //   return;

                }
                Log.e(TAG, "PointerPlacer: here5" );


            }

        }
        if (progress1==2000) {
            for (int i = 0; i < userloc.size(); i++){
                if (distances.get(i)<2000 && distances.get(i)>10){
                    mTripMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(userloc.get(i).getLatitude(), userloc.get(i).getLongitude()))
                            .title("Hello world")));
                    //   return;

                }
                Log.e(TAG, "PointerPlacer: here5" );


            }

        }
        if (progress1==3000) {
            for (int i = 0; i < userloc.size(); i++){
                if (distances.get(i)<3000 && distances.get(i)>10){
                    mTripMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(userloc.get(i).getLatitude(), userloc.get(i).getLongitude()))
                            .title("Hello world")));
                    //   return;

                }
                Log.e(TAG, "PointerPlacer: here5" );


            }

        }
        if (progress1==4000) {
            for (int i = 0; i < userloc.size(); i++){
                if (distances.get(i)<4000 && distances.get(i)>10){
                    mTripMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(userloc.get(i).getLatitude(), userloc.get(i).getLongitude()))
                            .title("Hello world")));
                    //   return;

                }
                Log.e(TAG, "PointerPlacer: here5" );


            }

        }
        if (progress1==5000) {
            for (int i = 0; i < userloc.size(); i++){
                if (distances.get(i)<5000 && distances.get(i)>10){
                    mTripMarkers.add(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(userloc.get(i).getLatitude(), userloc.get(i).getLongitude()))
                            .title("Hello world")));
                    //   return;

                }
                Log.e(TAG, "PointerPlacer: here5" );


            }

        }


    }
    /**
     * adds new user location to firebase
     */
    private void writeNewUser(GeoPoint geo ) {
        User user1=new User();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uid = user.getUid();
            Toast.makeText(getContext(),uid,Toast.LENGTH_LONG).show();

        }
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("UserLocation").child(uid);
        dR.setValue(user1);
        //mDatabase.child("User").child(uid).setValue(user1);
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {

            //bhatti changed from this to getActivity in following query

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();

        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private double distanceBetween(LatLng point1, LatLng point2) {

        if (point1 == null || point2 == null) {
            //return null;
            return 0;
        }
        double distance= SphericalUtil.computeDistanceBetween(point1, point2);
        Log.e(TAG, "Distance "+distance);
        return distance;

        //return ;
    }
    private void getuser(){
        database1=FirebaseDatabase.getInstance();
        ref=database1.getReference("UserLocation");
        user=new UserLocation();
        //adapter=new ArrayAdapter<UserLocation>(this,);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    user=ds.getValue(UserLocation.class);
                    Userloc.add(user);

                    Log.e(TAG,"UID "+user.getLatitude());


                }
                Log.e(TAG,"size= "+Userloc.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /**
     * removes markers from maps
     */
    private void removeTripMarkers(){
        for(Marker marker: mTripMarkers){
            marker.remove();
        }
        return;
    }
}
