package com.example.ayoub.nicper.MainActivity.post_address;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.MapsActivity;
import com.example.ayoub.nicper.Object.AppGeneral.User;
import com.example.ayoub.nicper.dialog.TimePickerFragment;
import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.example.ayoub.nicper.fragment.PickTimeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlaceInfoFormActivity extends AppCompatActivity{


    //Firebase
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userpost = root.child("data").child("places");
    private DatabaseReference userReference = root.child("data").child("users");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //Ui
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private CheckBox chekAllDay;
    private CheckBox lundi;
    private CheckBox mardi;
    private CheckBox mercredi;
    private CheckBox jeudi;
    private CheckBox vendredi;
    private CheckBox samedi;
    private CheckBox dimanche;
    private Slider slider;
    private Button buttonTime;
    private Button postButton;
    private android.widget.EditText placeInfoEdiText, priceInfoEditText;
    private RippleView post, time;


    //Place information
    private com.example.ayoub.nicper.Object.Map.PlaceInfo placeInfo;
    private double lat, lng;
    private String countryName;
    private int maxPost;
    private String username = "";
    private String userId = "";
    private double price = 0;
    private String infoPrice = "";
    private String dispoInfo = "";
    private  List<String> listDay = new ArrayList<>();
    private List<String> listTime =  new ArrayList<>();

    //temp variable
    private List<String> listChekSemaine = new ArrayList<>();
    DialogFragment newFragment;
    private HashMap<String, String> hashMap = new HashMap<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        post = (RippleView) findViewById(R.id.rippleViewPost);
        time = (RippleView) findViewById(R.id.rippleViewPick);

        userId = firebaseUser.getUid();
        userReference = userReference.child(firebaseUser.getUid()).child("profil");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user  = dataSnapshot.getValue(User.class);
                username = user.getFirstName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Add tool bar to the screen
        setToolbar();

        //Initialise UI component
        initialiseComponent();

        //Get extra info from the past intent
        getExtaForIntent();

        //CheckBox Listner
        checkBoxListner();

        newFragment = new TimePickerFragment();
        time.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                showFragment();
            }
        });

        post.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                price = slider.getValue();
                infoPrice = priceInfoEditText.getText().toString();
                dispoInfo = placeInfoEdiText.getText().toString();
                Set set = hashMap.entrySet();
                // Get an iterator
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    listDay.add(me.getKey().toString());
                    listTime.add(me.getValue().toString());
                }
                placeInfo = new PlaceInfo(price, infoPrice, dispoInfo, listDay, listTime, lat, lng, username, userId);
                postAddress(placeInfo);
            }
        });

    }

    private void showFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        PickTimeFragment pickTimeFragment = new PickTimeFragment();
        hashMap = new HashMap<>();
        pickTimeFragment.setList(listChekSemaine);
        pickTimeFragment.show(fragmentManager, "Time Picker");
    }

    private void checkBoxListner(){
        chekAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    collapse(relativeLayout);
                }else{
                    expand(relativeLayout);
                }
            }
        });
        lundi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "l");
            }
        });
        mardi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "ma");
            }
        });
        mercredi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "me");
            }
        });
        jeudi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "j");
            }
        });
        vendredi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "v");
            }
        });
        samedi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "s");
            }
        });
        dimanche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, "d");
            }
        });
    }










    public void postAddress(PlaceInfo placeInfo){

            double latTemp = placeInfo.getLat();
            double lngTemp = placeInfo.getLng();
            int latInt = (int) latTemp;
            int lngInt = (int) lngTemp;
            double k = latInt / 5.0;
            double i = lngInt / 5.0;
            latInt = ((int) k) * 5;
            lngInt = ((int) i) * 5;

            String ref = ""+latInt+"P"+lngInt;
            userpost.child(ref).push().setValue(placeInfo);
            userReference.child("post").setValue(maxPost+1);
            Snackbar snack = Snackbar.make(toolbar, "Good luck !", Snackbar.LENGTH_SHORT);
            snack.setAction("Action", null).show();
            snack.setCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    Intent intent = new Intent(PlaceInfoFormActivity.this, MapsActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onShown(Snackbar snackbar) {

                }
            });



    }

    private void getExtaForIntent(){
        Bundle b = getIntent().getExtras();
        lat = b.getDouble("Lat");
        lng = b.getDouble("Long");
        countryName = b.getString("Country");
        maxPost = b.getInt("Max");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initialiseComponent(){

        slider = (Slider) findViewById(R.id.sliderPrice);


        relativeLayout = (RelativeLayout) findViewById(R.id.layoutCheckBox);
        collapse(relativeLayout);

        chekAllDay = (CheckBox) findViewById(R.id.allDay);
        lundi = (CheckBox) findViewById(R.id.lundi);
        mardi = (CheckBox) findViewById(R.id.mardi);
        mercredi = (CheckBox) findViewById(R.id.mercredi);
        jeudi = (CheckBox) findViewById(R.id.jeudi);
        vendredi = (CheckBox) findViewById(R.id.vendredi);
        samedi = (CheckBox) findViewById(R.id.samedi);
        dimanche = (CheckBox) findViewById(R.id.dimache);

        postButton = (Button) findViewById(R.id.postButton);
        buttonTime = (Button) findViewById(R.id.chooseTime);

        placeInfoEdiText = (android.widget.EditText) findViewById(R.id.placeInfo);
        priceInfoEditText = (android.widget.EditText) findViewById(R.id.priceInfo);

    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setHashMap(String key, String time){
        hashMap.put(key, time);
    }
    public static void expand(final View v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void manageList(boolean isChecked, String day){
        if(isChecked){
            listChekSemaine.add(day);
        }else{
            for(int i = 0; i < listChekSemaine.size(); i++){
                if(listChekSemaine.get(i) == day){
                    listChekSemaine.remove(i);
                }
            }
        }

    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
