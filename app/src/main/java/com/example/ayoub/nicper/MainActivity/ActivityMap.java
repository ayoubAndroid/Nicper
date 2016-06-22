package com.example.ayoub.nicper.MainActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayoub.nicper.Intro.MyIntro;
import com.example.ayoub.nicper.MainActivity.place_info_map.PlaceMapInfo;
import com.example.ayoub.nicper.MainActivity.post_address.ChooseMapAddressActivity;
import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.example.ayoub.nicper.StringFormater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
    private HashMap<String, PlaceInfo> hmap = new HashMap<String, PlaceInfo>();
    private String latLng = "45P-70";
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        getExtraCodeSnackBar();
        placeList = new ArrayList();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap =  mapFragment.getMap();



        post = (FloatingActionButton) findViewById(R.id.view4);
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
                    float container_height = getResources().getDimension(R.dimen.DIP_300);

                    Projection projection = mMap.getProjection();

                    Point markerScreenPosition = projection.toScreenLocation(marker.getPosition());
                    Point pointHalfScreenAbove = new Point(markerScreenPosition.x,(int) (markerScreenPosition.y - (container_height / 2)));

                    LatLng aboveMarkerLatLng = projection.fromScreenLocation(pointHalfScreenAbove);

                    marker.showInfoWindow();
                    CameraUpdate center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng);
                    mMap.moveCamera(center);
                    mMap.animateCamera(center);
                    marker.showInfoWindow();
                    return true;
                }
                return false;
            }
        });

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.map_info, null);
                v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));



                String tempMarkerId = marker.getId();
                PlaceInfo placeInfo = hmap.get(tempMarkerId);

                // Getting reference to the TextView to set latitude
                TextView username = (TextView) v.findViewById(R.id.username);
                TextView price = (TextView) v.findViewById(R.id.price);
                TextView dispoText = (TextView) v.findViewById(R.id.dispo);

                username.setText(placeInfo.getUsername());
                price.setText(""+placeInfo.getPriceHour()+ " per hour");

                StringFormater stringFormater = new StringFormater();
                String dispo = "";
                List<String> listDayTemp = new ArrayList<String>(placeInfo.getListDay());
                List<String> listTimeTemp = new ArrayList<String>(placeInfo.getListTime());
                for(int i = 0; i < listDayTemp.size(); i++){
                    String day = listDayTemp.get(i);
                    String time = stringFormater.availabilityFormater(listTimeTemp.get(i));
                    dispo += day+" : "+ time + "\n";
                }
                dispoText.setText(dispo);


                return v;

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


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void showMessage(String message){
        Snackbar snack = Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT);
        snack.setAction("Action", null).show();
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
