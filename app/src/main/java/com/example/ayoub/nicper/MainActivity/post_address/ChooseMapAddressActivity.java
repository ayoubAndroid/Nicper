package com.example.ayoub.nicper.MainActivity.post_address;
import com.andexert.library.RippleView;
import com.example.ayoub.nicper.GoogleAutoComplete.Log;
import com.example.ayoub.nicper.GoogleAutoComplete.SampleActivityBase;
import com.example.ayoub.nicper.Object.Map.*;
import com.example.ayoub.nicper.Object.AppGeneral.User;
import com.example.ayoub.nicper.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseMapAddressActivity extends SampleActivityBase implements PlaceSelectionListener, OnMapReadyCallback{

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Toolbar toolbar;
    private Button buttonPost;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userReference = root.child("data").child("users");
    private DatabaseReference userpost = root.child("data").child("places");
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private LatLng latLngAddressChoose = null;
    private User user = null;
    private int maxPost = 0;
    private  String countryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_adress);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);

        // Retrieve the TextViews that will display details about the selected place.

        //Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAdress);
        mapFragment.getMapAsync(this);


        //Button post
        final Button button = (Button) findViewById(R.id.postAdress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latLngAddressChoose != null) {

                    userReference.child(firebaseUser.getUid()).child("profil").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            maxPost = user.getPost();
                            if (maxPost < 1 && !countryName.isEmpty()){
                                Intent intent = new Intent(ChooseMapAddressActivity.this, PlaceInfoFormActivity.class);
                                Bundle b = new Bundle();
                                b.putDouble("Lat", latLngAddressChoose.latitude);
                                b.putDouble("Long", latLngAddressChoose.longitude);
                                b.putInt("Max", maxPost);
                                intent.putExtra("Country", countryName);
                                intent.putExtras(b);
                                startActivity(intent);
                            } else {
                                showMessage("You have exceeded the limit");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    showMessage("Please selected a location first");
                }
            }
        });

    }


    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());
        latLngAddressChoose = place.getLatLng();
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLngAddressChoose));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAddressChoose, 14));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLngAddressChoose.latitude, latLngAddressChoose.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        countryName = addresses.get(0).getAddressLine(2);
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void showMessage(String message){
        Snackbar snack = Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT);
        snack.setAction("Action", null).show();
    }


}
