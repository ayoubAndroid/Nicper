package com.example.ayoub.nicper.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayoub.nicper.Object.Map.PlaceInfo;
import com.example.ayoub.nicper.R;
import com.google.android.gms.location.places.Place;

import java.util.List;

/**
 * Created by Admin on 07/06/2016.
 */
public class RecyclerViewGoogleInfoWindowAdapter extends RecyclerView.Adapter<RecyclerViewGoogleInfoWindowAdapter.ContactViewHolder> {

    private List<PlaceInfo> placeInfos;

    public RecyclerViewGoogleInfoWindowAdapter(List<PlaceInfo> placeInfos) {
        this.placeInfos = placeInfos;
    }

    @Override
    public int getItemCount() {
        return placeInfos.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        PlaceInfo pinfo = placeInfos.get(i);
       // List<Integer> list = pinfo.getListJours();

        /*for(int k = 0; k < list.size(); k++){

        }*/
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_dispo, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public ContactViewHolder(View v) {
            super(v);
            //vName =  (TextView) v.findViewById(R.id.txtName);
        }
    }
}