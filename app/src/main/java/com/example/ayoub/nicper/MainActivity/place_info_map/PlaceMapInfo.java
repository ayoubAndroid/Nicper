package com.example.ayoub.nicper.MainActivity.place_info_map;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.chat.ChatActivity;
import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.example.ayoub.nicper.Object.StringFormater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlaceMapInfo extends AppCompatActivity implements /*OnMapReadyCallback*/ GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TextView owner;
    private TextView price;
    private TextView priceInfo;
    private TextView availability;
    private TextView addresseText;
    private LatLng currentLocation;
    private PlaceInfo placeInfo;
    private Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map_info);

        setUpToolBar();

        //Initialise the UI component
        initialise();
        changeFont();

        //Set the value of the PlaceInfo object to the UI
        setValue();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceMapInfo.this, ChatActivity.class);
                intent.putExtra("id", placeInfo.getUserId());
                intent.putExtra("username", placeInfo.getUsername());
                startActivity(intent);
            }
        });

        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(placeInfo.getLat(), placeInfo.getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null) {
            String address = addresses.get(0).getAddressLine(0);
            addresseText.setText(address);
        }

    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle("Place Info");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initialise(){
        // get the object from the MapActivity
        Intent i = getIntent();
        placeInfo = (PlaceInfo) i.getSerializableExtra("placeInfo");
        currentLocation = new LatLng(placeInfo.getLat(), placeInfo.getLng());

        owner = (TextView) findViewById(R.id.owner);
        price = (TextView) findViewById(R.id.price);
        priceInfo = (TextView) findViewById(R.id.infoPrice);
        availability = (TextView) findViewById(R.id.availability);
        addresseText = (TextView) findViewById(R.id.infoPlace);

        buttonSendMessage = (Button) findViewById(R.id.sendMessage);
    }

    private void setValue() {
        owner.setText(placeInfo.getUsername());
        price.setText(""+placeInfo.getPriceHour()+" per hour");

        String priceInfoString = placeInfo.getinfo();
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


    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                            .position(currentLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void changeFont(){
        Typeface tf = Typeface.createFromAsset(getAssets(), "Geomanist-Regular.otf");
        owner.setTypeface(tf);
        price.setTypeface(tf);
        owner.setTypeface(tf);
        price.setTypeface(tf);
        priceInfo.setTypeface(tf);
        availability.setTypeface(tf);
    }
}
