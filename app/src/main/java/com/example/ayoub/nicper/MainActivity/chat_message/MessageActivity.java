package com.example.ayoub.nicper.MainActivity.chat_message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.ayoub.nicper.Object.Message.Message;
import com.example.ayoub.nicper.Object.Message.Time;
import com.example.ayoub.nicper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private MessageActivity message;
    private DatabaseReference messageRef  = FirebaseDatabase.getInstance().getReference().child("data").child("users");
    private FirebaseUser firebaseUser;
    private String userId  = "vKBmVcS6YHVMJZKcp5kV2DIXUGo1";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        messageRef = messageRef.child(""+userId).child("messages");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        List<Message> messages = new ArrayList();
        Message message1 = new Message(getTime(), "1");
        Message message2 = new Message(getTime(), "2");
        Message message3 = new Message(getTime(), "3");
        Message message4 = new Message(getTime(), "4");
        Message message5 = new Message(getTime(), "5");
        Message message6 = new Message(getTime(), "6");
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        messages.add(message5);
        messages.add(message6);
        messageRef.push().setValue(messages);
/*
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        messageRef = messageRef.child(""+userId).child("messages");
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
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
        });*/
        /*

         */
      /*  messageRef = messageRef.child(""+userId).child("messages").child(""+firebaseUser.getUid());
        messageRef.child("username").setValue(""+firebaseUser.getDisplayName().toString());
        final Time time = getTime();
        Message message = new Message(time, "Salut je suis");
        messageRef.push().setValue(message);*/

    }

    private Time getTime(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour  = c.get(Calendar.HOUR);
        int minute  = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        Time time  = new Time(year, month, day, hour, minute, seconds);
        return  time;
    }
}
