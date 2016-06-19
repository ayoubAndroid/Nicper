package com.example.ayoub.nicper.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.ayoub.nicper.MainActivity.post_address.PlaceInfoFormActivity;
import com.example.ayoub.nicper.R;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 15/06/2016.
 */
public class PickTimeFragment extends DialogFragment {
    Button selectTime, finsh;
    android.support.v7.widget.Toolbar toolbar;

    CheckBox selectAll, morningTime, nightTime;
    CheckBox checkBox1, checkBox2, checkBox3,checkBox4,checkBox5, checkBox6;
    CheckBox checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12;
    CheckBox checkBox13,checkBox14, checkBox15, checkBox16, checkBox17, checkBox18;
    CheckBox checkBox19, checkBox20, checkBox21,checkBox22, checkBox23, checkBox24;
    CheckBox l,ma, me, j, v, s, d;

    private List<String> listHeure = new ArrayList<>();
    private  List<CheckBox> checkBoxes = new ArrayList<>();
    private  List<CheckBox> checkBoxesDay = new ArrayList<>();
    private List<String> listDayChoosed = new ArrayList<>();
    private PlaceInfoFormActivity placeInfoFormActivity;

    public PickTimeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.select_time_fragment, container);

        placeInfoFormActivity = (PlaceInfoFormActivity) getActivity();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        toolbar = (Toolbar) view.findViewById(R.id.toolbarFragment);
        toolbar.setTitle("Time Picker");

        selectTime = (Button) view.findViewById(R.id.buttonSelectDay);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelection();
            }
        });

        finsh = (Button) view.findViewById(R.id.finish);
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getIdCheckBox(view);
        setCheckBoxWeek();


        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(int i = 0; i < checkBoxes.size(); i++){
                        checkBoxes.get(i).setChecked(true);
                    }
                }else{
                    for(int i = 0; i < checkBoxes.size(); i++){
                        checkBoxes.get(i).setChecked(false);
                    }
                }
            }
        });

        morningTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(int i = 0; i < checkBoxes.size()/2; i++){
                        checkBoxes.get(i).setChecked(true);
                    }
                }else{
                    for(int i = 0; i < checkBoxes.size()/2; i++){
                        checkBoxes.get(i).setChecked(false);
                    }
                }
            }
        });
        nightTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(int i = (checkBoxes.size() - 1) ; i > (checkBoxes.size()/2 - 1); i--){
                        checkBoxes.get(i).setChecked(true);
                    }
                }else{
                    for(int i = (checkBoxes.size() - 1) ; i > (checkBoxes.size()/2 - 1); i--){
                        checkBoxes.get(i).setChecked(false);
                    }
                }
            }
        });


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox1.getText().toString());
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox2.getText().toString());
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox3.getText().toString());
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox4.getText().toString());
            }
        });
        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox5.getText().toString());
            }
        });
        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox6.getText().toString());
            }
        });
        checkBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox7.getText().toString());
            }
        });
        checkBox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox8.getText().toString());
            }
        });
        checkBox9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox9.getText().toString());
            }
        });
        checkBox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox10.getText().toString());
            }
        });
        checkBox11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox11.getText().toString());
            }
        });
        checkBox12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox12.getText().toString());
            }
        });
        checkBox13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox13.getText().toString());
            }
        });
        checkBox14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox14.getText().toString());
            }
        });
        checkBox15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox15.getText().toString());
            }
        });
        checkBox16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox16.getText().toString());
            }
        });
        checkBox17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox17.getText().toString());
            }
        });
        checkBox18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox18.getText().toString());
            }
        });
        checkBox19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox19.getText().toString());
            }
        });
        checkBox20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox20.getText().toString());
            }
        });
        checkBox21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox21.getText().toString());
            }
        });
        checkBox22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox22.getText().toString());
            }
        });
        checkBox23.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox23.getText().toString());
            }
        });
        checkBox24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manageList(isChecked, checkBox24.getText().toString());
            }
        });


        return view;
    }

    public void manageList(boolean isChecked, String heure){
        if(isChecked){
            listHeure.add(heure);
        }else{
            for(int i = 0; i < listHeure.size(); i++){
                if(listHeure.get(i) == heure){
                    listHeure.remove(i);
                }
            }
        }

    }

    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    void getIdCheckBox(View view){
        selectAll = (CheckBox) view.findViewById(R.id.selectAll);
        morningTime = (CheckBox) view.findViewById(R.id.morning);
        nightTime = (CheckBox)  view.findViewById(R.id.night);

        l = (CheckBox) view.findViewById(R.id.lundi);
        ma = (CheckBox) view.findViewById(R.id.mardi);
        me = (CheckBox) view.findViewById(R.id.mercredi);
        j = (CheckBox) view.findViewById(R.id.jeudi);
        v = (CheckBox) view.findViewById(R.id.vendredi);
        s = (CheckBox) view.findViewById(R.id.samedi);
        d = (CheckBox) view.findViewById(R.id.dimache);

        checkBox1 = (CheckBox) view.findViewById(R.id.heure_1);
        checkBox2 = (CheckBox) view.findViewById(R.id.heure_2);
        checkBox3 = (CheckBox) view.findViewById(R.id.heure_3);
        checkBox4 = (CheckBox) view.findViewById(R.id.heure_4);
        checkBox5 = (CheckBox) view.findViewById(R.id.heure_5);
        checkBox6 = (CheckBox) view.findViewById(R.id.heure_6);
        checkBox7 = (CheckBox) view.findViewById(R.id.heure_7);
        checkBox8 = (CheckBox) view.findViewById(R.id.heure_8);
        checkBox9 = (CheckBox) view.findViewById(R.id.heure_9);
        checkBox10 = (CheckBox) view.findViewById(R.id.heure_10);
        checkBox11 = (CheckBox) view.findViewById(R.id.heure_11);
        checkBox12 = (CheckBox) view.findViewById(R.id.heure_12);
        checkBox13 = (CheckBox) view.findViewById(R.id.heure_13);
        checkBox14 = (CheckBox) view.findViewById(R.id.heure_14);
        checkBox15 = (CheckBox) view.findViewById(R.id.heure_15);
        checkBox16 = (CheckBox) view.findViewById(R.id.heure_16);
        checkBox17 = (CheckBox) view.findViewById(R.id.heure_17);
        checkBox18 = (CheckBox) view.findViewById(R.id.heure_18);
        checkBox19 = (CheckBox) view.findViewById(R.id.heure_19);
        checkBox20 = (CheckBox) view.findViewById(R.id.heure_20);
        checkBox21 = (CheckBox) view.findViewById(R.id.heure_21);
        checkBox22 = (CheckBox) view.findViewById(R.id.heure_22);
        checkBox23 = (CheckBox) view.findViewById(R.id.heure_23);
        checkBox24 = (CheckBox) view.findViewById(R.id.heure_24);

        checkBoxes.add(checkBox1);
        checkBoxes.add(checkBox2);
        checkBoxes.add(checkBox3);
        checkBoxes.add(checkBox4);
        checkBoxes.add(checkBox5);
        checkBoxes.add(checkBox6);
        checkBoxes.add(checkBox7);
        checkBoxes.add(checkBox8);
        checkBoxes.add(checkBox9);
        checkBoxes.add(checkBox10);
        checkBoxes.add(checkBox11);
        checkBoxes.add(checkBox12);
        checkBoxes.add(checkBox13);
        checkBoxes.add(checkBox14);
        checkBoxes.add(checkBox15);
        checkBoxes.add(checkBox16);
        checkBoxes.add(checkBox17);
        checkBoxes.add(checkBox18);
        checkBoxes.add(checkBox19);
        checkBoxes.add(checkBox20);
        checkBoxes.add(checkBox21);
        checkBoxes.add(checkBox22);
        checkBoxes.add(checkBox23);
        checkBoxes.add(checkBox24);


    }

    private void setCheckBoxWeek(){
        for(int i =0; i < listDayChoosed.size(); i++){
            switch (listDayChoosed.get(i)){
                case "l":
                    l.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(l);
                    break;
                case "ma":
                    ma.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(ma);
                    break;
                case "me":
                    me.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(me);
                    break;
                case "j":
                    j.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(j);
                    break;
                case "v":
                    v.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(v);
                    break;
                case "s":
                    s.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(s);
                    break;
                case "d":
                    d.setVisibility(View.VISIBLE);
                    checkBoxesDay.add(d);
                    break;
            }
        }
    }

    public void setList(List<String> listDayChoosed){
        this.listDayChoosed = listDayChoosed;
    }

    void checkSelection(){
        //Create a hour interval
        String hour = "";
        for(int i =0; i < listHeure.size(); i++){
            hour  = hour+listHeure.get(i)+ " ";
        }

        //check the day to apply the time
        for(int i = 0; i < checkBoxesDay.size(); i++){
            CheckBox dayCheckBox = checkBoxesDay.get(i);
            if(dayCheckBox.isChecked()){
                placeInfoFormActivity.setHashMap(dayCheckBox.getText().toString(), hour);
                removeDayChoosed(dayCheckBox.getText().toString());
                dayCheckBox.setVisibility(View.GONE);
            }
        }

        //Show the finish button
        if(listDayChoosed.size() == 0){
            selectTime.setVisibility(View.GONE);
            finsh.setVisibility(View.VISIBLE);
        }

    }

    void removeDayChoosed(String day){
        day = day.toLowerCase();
        for(int i = 0; i < listDayChoosed.size(); i++){
            String temp = listDayChoosed.get(i).toString();
            if(temp.equals(day)){
                listDayChoosed.remove(i);
            }
        }
    }




}
