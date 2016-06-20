package com.example.ayoub.nicper.MainActivity.chat_message;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton send;
    private EditText message;
    private RecyclerView recyclerViewMessage;
    private Toolbar toolbar;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
    private String userId = firebaseUser.getUid();
    private String otherUserId = "";
    private String otherUsername = "";

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRefOther = root.child("users");
    private DatabaseReference messageRefMe = root.child("users");

    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getExtraBundle();
        setUpToolBar();
        initialiseUI();

        messageRefOther = messageRefOther.child(otherUserId).child("messages").child(userId);
        messageRefMe = messageRefMe.child(userId).child("messages").child(otherUserId);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageString = message.getText().toString();
                final Message messageObject = new Message(messageString, userId);
                if(!messageString.isEmpty()){
                    sendMessage(messageObject);
                }else{
                    Snackbar.make(v, "please create a message first", Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });

        messageRefOther.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage  = dataSnapshot.getValue(Message.class);
                messageList.add(newMessage);
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

    private void sendMessage(Message messageToSend) {
        messageRefMe.push().setValue(messageToSend);
        messageRefOther.push().setValue(messageToSend);
    }

    private void getExtraBundle(){
        Bundle bundle = getIntent().getExtras();
        otherUserId = bundle.getString("id");
        otherUsername = bundle.getString("username");
    }

    public void initialiseUI(){
        send = (FloatingActionButton) findViewById(R.id.send);
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
    protected void onStart(){
        super.onStart();
    }

    public static  class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_message);
        }

    }


}
