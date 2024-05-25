package com.example.mychat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TestChatRealtimeActivity extends AppCompatActivity {
    private EditText messageEditText;
    private Button sendButton;
    private ListView messageListView;
    private ArrayList<String> messagesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_chat_realtime);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messageListView = findViewById(R.id.messageListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        messageListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("messages");

        sendButton.setOnClickListener(view -> sendMessage());

        // Listen for new messages
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                String message = dataSnapshot.getValue(String.class);
                messagesList.add(message);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TestChatRealtimeActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            databaseReference.push().setValue(message);
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
        }
    }
}
