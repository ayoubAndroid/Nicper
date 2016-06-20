package com.example.ayoub.nicper.MainActivity.chat_message;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.ayoub.nicper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton send;
    private EditText message;
    private RecyclerView recyclerViewMessage;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;
    private String userId = firebaseUser.getUid();

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRef = root.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public void initialise(){
        send = (FloatingActionButton) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        recyclerViewMessage = (RecyclerView) findViewById(R.id.recyclerView_message);

    }
}
