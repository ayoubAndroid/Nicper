package com.example.ayoub.nicper.MainActivity.place_info_map;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapInfo extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TextView owner;
    private TextView price;
    private TextView priceInfo;
    private TextView availability;
    private TextView availabilityInfo;
    private MapFragment mapFragment;
    private double lat, lng;
    private GoogleMap mMap;
    private LatLng currentLocation;
    private PlaceInfo placeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map_info);

        initialise();
        setValue();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void initialise(){
        // get the object from the MapActivity
        Intent i = getIntent();
        placeInfo = (PlaceInfo) i.getSerializableExtra("placeInfo");
        currentLocation = new LatLng(placeInfo.getLat(), placeInfo.getLng());

        owner = (TextView) findViewById(R.id.owner);
        price = (TextView) findViewById(R.id.price);
        priceInfo = (TextView) findViewById(R.id.priceInfo);
        availability = (TextView) findViewById(R.id.availability);
        availabilityInfo = (TextView) findViewById(R.id.infoAvailability);
    }

    private void setValue() {
        owner.setText(placeInfo.getUsername());
        price.setText(""+placeInfo.getPriceHour()+" per hour");

        String priceInfoString = placeInfo.getPriceInfo();
        if(priceInfoString.isEmpty()){
            priceInfo.setText("The owner did not create a price description");
        }else{
            priceInfo.setText(priceInfoString);
        }

        String dispo = "";
        List<String> listDayTemp = new ArrayList<String>(placeInfo.getListDay());
        List<String> listTimeTemp = new ArrayList<String>(placeInfo.getListTime());
        for(int i = 0; i < listDayTemp.size(); i++){
            String day = listDayTemp.get(i);
            String time = listTimeTemp.get(i);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                            .position(currentLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        this.mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
