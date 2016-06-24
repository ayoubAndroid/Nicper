package com.example.ayoub.nicper.MainActivity.chat_message;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ayoub.nicper.Adapter.InboxAdapter;
import com.example.ayoub.nicper.Adapter.MessagesAdapter;
import com.example.ayoub.nicper.GoogleAutoComplete.Log;
import com.example.ayoub.nicper.MainActivity.ActivityMap;
import com.example.ayoub.nicper.MainActivity.post_address.ChooseMapAddressActivity;
import com.example.ayoub.nicper.Object.Message.LastMessage;
import com.example.ayoub.nicper.Object.Message.Message;
import com.example.ayoub.nicper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListMessage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = firebaseUser.getUid();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("data");
    private DatabaseReference messageRefMe = root.child("users").child(userId);

    private RecyclerView recyclerViewMessage;
    private List<LastMessage> messageList;
    private InboxAdapter mAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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
//********************************************
        messageList = new ArrayList<>();
        messageList.clear();

        recyclerViewMessage = (RecyclerView) findViewById(R.id.recyclerView_message);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InboxAdapter(messageList, userId);
        recyclerViewMessage.setAdapter(mAdapter);


        messageRefMe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    LastMessage message  = child.child("lastMessage").getValue(LastMessage.class);
                    if(message != null) {
                        messageList.add(message);
                        mAdapter.notifyItemInserted(messageList.size() - 1);
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
        Intent intent = null;

        if (id == R.id.nav_message) {

        }else if(id == R.id.map_view){
            intent = new Intent(ListMessage.this, ActivityMap.class);
        }else if (id == R.id.nav_post) {
            intent = new Intent(ListMessage.this,ChooseMapAddressActivity.class);
        }else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(intent != null){
            startActivity(intent);
        }else{
            Snackbar.make(toolbar, "Your are already in message section", Snackbar.LENGTH_SHORT).show();
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
