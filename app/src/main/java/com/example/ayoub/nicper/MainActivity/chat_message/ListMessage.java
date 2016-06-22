package com.example.ayoub.nicper.MainActivity.chat_message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ayoub.nicper.Adapter.InboxAdapter;
import com.example.ayoub.nicper.Adapter.MessagesAdapter;
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

public class ListMessage extends AppCompatActivity {


    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = firebaseUser.getProviderId();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("data");
    private DatabaseReference messageRefMe = root.child("users").child(userId).child("messages");

    private RecyclerView recyclerViewMessage;
    private List<Message> messageList = new ArrayList<>();
    private InboxAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);

        recyclerViewMessage = (RecyclerView) findViewById(R.id.recyclerView_message);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InboxAdapter(messageList, userId);
        recyclerViewMessage.setAdapter(mAdapter);

        messageRefMe.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Message message  = child.child("lastMessage").getValue(Message.class);
                    messageList.add(message);
                    mAdapter.notifyDataSetChanged();
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
}
