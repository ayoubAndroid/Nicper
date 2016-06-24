package com.example.ayoub.nicper.MainActivity.chat_message;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayoub.nicper.Adapter.MessagesAdapter;
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
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button send;
    private EditText message;
    private RecyclerView recyclerViewMessage;
    private Toolbar toolbar;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
    private String userId = firebaseUser.getUid();
    private String username = firebaseUser.getDisplayName();
    private String otherUserId = "";
    private String otherUsername = "";

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("data");
    private DatabaseReference messageRefMe = root.child("users");
    private DatabaseReference messageRefOther = root.child("users");

    private List<Message> messageList = new ArrayList<>();
    private  MessagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getExtraBundle();
        initialiseUI();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new MessagesAdapter(messageList, userId);
        recyclerViewMessage.setAdapter(mAdapter);

        messageRefOther = messageRefOther.child(otherUserId).child("messages").child(userId);
        messageRefMe = messageRefMe.child(userId).child("messages").child(otherUserId);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageString = message.getText().toString();
                final Message messageObject = new Message(messageString, userId, username);
                final LastMessage lastMessageMe = new LastMessage(messageString, userId, username, otherUserId, otherUsername);
                final LastMessage lastMessageOther = new LastMessage(messageString, userId, username, userId, username);

                if(!messageString.isEmpty()){
                    sendMessage(messageObject, lastMessageMe, lastMessageOther);
                }else{
                    Snackbar.make(v, "please create a message first", Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });

        messageRefMe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    Message newMessage = dataSnapshot.getValue(Message.class);
                    messageList.add(newMessage);
                    recyclerViewMessage.scrollToPosition(messageList.size() - 1);
                    mAdapter.notifyItemInserted(messageList.size() - 1);
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

    private void sendMessage(Message messageToSend, LastMessage lastMessage, LastMessage lastMessageOther) {

        if(! messageRefMe.toString().equals(messageRefOther.toString()) ) {

            messageRefOther.child("lastMessage").setValue(lastMessageOther);
            messageRefMe.child("lastMessage").setValue(lastMessage);

            messageRefMe.push().setValue(messageToSend);
            messageRefOther.push().setValue(messageToSend);
        }else{
            Snackbar.make(toolbar, "You cant send a message to yourself !", Snackbar.LENGTH_LONG).show();
        }

        //Remove the precedent message
        message.setText("");
    }

    private void getExtraBundle(){
        Bundle bundle = getIntent().getExtras();
        otherUserId = bundle.getString("id");
        otherUsername = bundle.getString("username");
    }

    public void initialiseUI(){
        send = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        recyclerViewMessage = (RecyclerView) findViewById(R.id.recyclerView_message);

    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setTitle(otherUsername);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
            intent = new Intent(ChatActivity.this, ListMessage.class);
        }else if(id == R.id.map_view){
            intent = new Intent(ChatActivity.this, ActivityMap.class);
        }else if (id == R.id.nav_post) {
            intent = new Intent(ChatActivity.this,ChooseMapAddressActivity.class);
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




}
