package com.example.ayoub.nicper.MainActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayoub.nicper.MainActivity.chat_message.ListMessage;
import com.example.ayoub.nicper.MainActivity.place_info_map.PlaceMapInfo;
import com.example.ayoub.nicper.MainActivity.post_address.ChooseMapAddressActivity;
import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.example.ayoub.nicper.Object.StringFormater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private boolean snackBarWelcome = true;
    private FloatingActionButton post;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference countryRef;
    private boolean mapready = false;
    private List<PlaceInfo> placeList;
    private HashMap<String, PlaceInfo> hmap = new HashMap<>();
    private String latLng = "45P-70";
    private LatLng currentLocation;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private BottomSheetBehavior bottomSheetBehavior;
    private boolean sheetIsShowing = false;
    private TextView owner;
    private TextView price;
    private TextView priceInfo;
    private TextView availability;
    private TextView availabilityInfo;
    private PlaceInfo placeInfo;
    private Button sendMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseSheet();
        View bottomView = findViewById(R.id.bottomSheetBehavior);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomView);

        owner.setText("ok2");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView textViewUsername = (TextView) header.findViewById(R.id.username);
        textViewUsername.setText(firebaseUser.getDisplayName());
        TextView textViewEmail = (TextView) header.findViewById(R.id.email);
        textViewEmail.setText(firebaseUser.getEmail());



        getExtraCodeSnackBar();
        placeList = new ArrayList();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap =  mapFragment.getMap();



        post = (FloatingActionButton) findViewById(R.id.postButton);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMap.this, ChooseMapAddressActivity.class);
                startActivity(intent);
            }
        });

        countryRef = root.child("data").child("places").child(latLng);
        countryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    PlaceInfo placeInfo = dataSnapshot.getValue(PlaceInfo.class);
                    if(mapready) {
                        Marker marker = createMarker(placeInfo);
                        hmap.put(marker.getId(), placeInfo);
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





        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker != null) {

                    String tempMarkerId = marker.getId();
                    PlaceInfo placeInfo = hmap.get(tempMarkerId);
                    setValueSheet(placeInfo);

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            Snackbar.make(post, "Your are already in the map view", Snackbar.LENGTH_SHORT).show();
        }

        return true;
    }


    private void setValueSheet(PlaceInfo placeInfo) {
        owner.setText(placeInfo.getUsername());
        price.setText(""+placeInfo.getPriceHour()+" per hour");

        String priceInfoString = placeInfo.getPriceInfo();
        if(priceInfoString.isEmpty()){
            priceInfo.setText("The owner did not create a price description");
        }else{
            priceInfo.setText(priceInfoString);
        }

        StringFormater stringFormater = new StringFormater();
        String dispo = "";
        List<String> listDayTemp = new ArrayList<String>(placeInfo.getListDay());
        List<String> listTimeTemp = new ArrayList<String>(placeInfo.getListTime());
        for(int i = 0; i < listDayTemp.size(); i++){
            String day = listDayTemp.get(i);
            String time = stringFormater.availabilityFormater(listTimeTemp.get(i));
            dispo += day+" : "+ time + "\n";
        }
        availability.setText(dispo);

        String availabilityInfoString = placeInfo.getTimeInfo();
        if(availabilityInfoString.isEmpty()){
            availabilityInfo.setText("The owner did not create a price description");
        }else {
            availabilityInfo.setText(availabilityInfoString);
        }
    }


    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void getExtraCodeSnackBar(){
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String value = extras.getString("Value");
            if(value != null)
                snackBarWelcome = false;

        }
    }

    private void initialiseSheet(){
        // get the object from the MapActivity
        owner = (TextView) findViewById(R.id.owner);
        price = (TextView) findViewById(R.id.price);
        priceInfo = (TextView) findViewById(R.id.infoPrice);
        availability = (TextView) findViewById(R.id.availability);
        availabilityInfo = (TextView) findViewById(R.id.infoAvailability);

        sendMessage = (Button) findViewById(R.id.sendMessage);
    }

/*

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout: {
                Snackbar snack = Snackbar.make(toolbar, "Logout", Snackbar.LENGTH_LONG);
                snack.setAction("Action", null).show();
                snack.setCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ActivityMap.this, MyIntro.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });

            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        currentLocation = new LatLng(45.501689, -73.567256);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        this.mMap = googleMap;
        mapready = true;

        for(int z = 0; z < placeList.size(); z++){
            Marker marker  = createMarker(placeList.get(z));
            hmap.put(marker.getId(), placeList.get(z));
        }
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
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(placeInfo.getLat(), placeInfo.getLng()))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .alpha(0.8f));
        return marker;
    }
}
