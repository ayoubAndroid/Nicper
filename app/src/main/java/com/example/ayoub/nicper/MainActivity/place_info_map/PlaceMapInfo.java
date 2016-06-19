package com.example.ayoub.nicper.MainActivity.place_info_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;

public class PlaceMapInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map_info);
        Intent i = getIntent();
        PlaceInfo placeInfo = (PlaceInfo) i.getSerializableExtra("placeInfo");

    }
}
