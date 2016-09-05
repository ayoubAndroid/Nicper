package com.example.ayoub.nicper.MainActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.Intro.MyIntro;
import com.example.ayoub.nicper.MainActivity.chat.ListMessage;
import com.example.ayoub.nicper.MainActivity.place_info_map.PlaceMapInfo;
import com.example.ayoub.nicper.MainActivity.post_address.ChooseMapAddressActivity;
import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static GoogleMap mMap;

    private DatabaseReference countryRef;
    private boolean mapready = false;
    private List<PlaceInfo> placeList = new ArrayList<>();
    private HashMap<String, PlaceInfo> hmap = new HashMap<>();
    private String latLng = "45P-70";
    private LatLng currentLocation = new LatLng(45.501689, -73.567256);

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView owner;
    private TextView price;
    private Button buttonSeeMore;
    private TextView moreInfo;
    private TextView toolbarTitle;
    private TextView textViewDetail;
    private TextView textViewOwner;
    private TextView textViewPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Check for data
        countryRef = root.child("data").child("places").child(latLng);
        new LoadDataMap().execute("");

        //Custom Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        initialiseSheet();

        //Bootom sheet that show quick info
        View bottomView = findViewById(R.id.bottomSheetBehavior);
        if(bottomView != null)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomView);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Menu of the app
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Modify spefic menu info (email and username)
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView textViewUsername = (TextView) header.findViewById(R.id.username);
        textViewUsername.setText(firebaseUser.getDisplayName());
        TextView textViewEmail = (TextView) header.findViewById(R.id.email);
        textViewEmail.setText(firebaseUser.getEmail());

        //The user want to post


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        changeFont();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ActivityMap.this, MyIntro.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_message) {
            intent = new Intent(ActivityMap.this, ListMessage.class);
        }else if(id == R.id.map_view){
        }else if (id == R.id.nav_post) {
            intent = new Intent(ActivityMap.this,ChooseMapAddressActivity.class);
        }else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(intent != null){
            startActivity(intent);
        }else{
            Snackbar.make(owner, "Your are already in the map view bro", Snackbar.LENGTH_SHORT).show();
        }

        return true;
    }


    private void setValueSheet(PlaceInfo placeInfo) {
        owner.setText(placeInfo.getUsername());
        price.setText(placeInfo.getPriceHour()+" per hour");
    }




    private void initialiseSheet(){
        // get the object from the MapActivity
        owner = (TextView) findViewById(R.id.owner);
        price = (TextView) findViewById(R.id.price);
        moreInfo = (TextView) findViewById(R.id.moreInfo);
        buttonSeeMore = (Button) findViewById(R.id.seeMore);
        toolbarTitle = (TextView) findViewById(R.id.title);
        textViewDetail = (TextView) findViewById(R.id.textViewDetail);
        textViewOwner = (TextView) findViewById(R.id.textViewOwner);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11));
        this.mMap = googleMap;
        mapready = true;

        // The user touch a marker so the bottom sheet display
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    String tempMarkerId = marker.getId();
                    final PlaceInfo placeInfo = hmap.get(tempMarkerId);
                    setValueSheet(placeInfo);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    buttonSeeMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(placeInfo != null) {
                                Intent intentDetail = new Intent(ActivityMap.this, PlaceMapInfo.class);
                                intentDetail.putExtra("placeInfo", placeInfo);
                                startActivity(intentDetail);
                            }else{
                                Snackbar.make(owner, "Select a place first", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                    return true;
                }else {
                    return false;
                }
            }
        });




        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String tempMarkerId = marker.getId();
                PlaceInfo placeInfo = hmap.get(tempMarkerId);

                Intent goPlaceMapInfo = new Intent(ActivityMap.this, PlaceMapInfo.class);
                goPlaceMapInfo.putExtra("placeInfo", placeInfo);
                startActivity(goPlaceMapInfo);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        //Load data if the map was not ready
        new LoadMarkerMap().execute("");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Hey no wifi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    Marker createMarker(PlaceInfo placeInfo){
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(placeInfo.getLat(), placeInfo.getLng())
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            return tempMarker;
    }

    private class LoadDataMap extends AsyncTask<String, PlaceInfo, String> {


        @Override
        protected String doInBackground(String... params) {
            countryRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getValue() != null) {
                        PlaceInfo placeInfo = dataSnapshot.getValue(PlaceInfo.class);
                        if(mapready) {
                            publishProgress(placeInfo);

                        }else{
                            placeList.add(placeInfo);
                        }
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Finish

        }

        @Override
        protected void onPreExecute() {};

        @SuppressWarnings("unchecked")
        @Override
        protected void onProgressUpdate(PlaceInfo... values) {
            Marker marker = createMarker(values[0]);
            hmap.put(marker.getId(), values[0]);
        }
    }
    private class LoadMarkerMap extends AsyncTask<String, PlaceInfo, String> {


        @Override
        protected String doInBackground(String... params) {
            for(int z = 0; z < placeList.size(); z++){
                publishProgress(placeList.get(z));

            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Finish
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(PlaceInfo... values) {
            Marker marker  = createMarker(values[0]);
            if(marker != null)
                hmap.put(marker.getId(), values[0]);
        }
    }


    private void changeFont(){
        Typeface tf = Typeface.createFromAsset(getAssets(), "Geomanist-Regular.otf");
        owner.setTypeface(tf);
        price.setTypeface(tf);
        moreInfo.setTypeface(tf);
        toolbarTitle.setTypeface(tf);
        textViewOwner.setTypeface(tf);
        textViewDetail.setTypeface(tf);
        textViewPrice.setTypeface(tf);
    }
}
