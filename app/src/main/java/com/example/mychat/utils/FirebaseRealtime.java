package com.example.mychat.utils;

import androidx.annotation.NonNull;

import com.example.mychat.model.CallModel;
import com.example.mychat.model.IErrorCallBack;
import com.example.mychat.model.INewEventCallBack;
import com.example.mychat.model.ISuccessCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

public class FirebaseRealtime {
    private final Gson gson = new Gson();
    //Lấy ref database
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private String currentUsername;
    private static final String LATEST_EVENT_FIELD_NAME = "latest_event";

    public void login(String username, ISuccessCallBack callBack){
        dbRef.child(username).setValue("").addOnCompleteListener(task -> {
            currentUsername = username;
            callBack.onSuccess();
        });
    }

    public void sendMessageToOtherUser(CallModel dataModel, IErrorCallBack errorCallBack){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dataModel.getTarget()).exists()){
                    //Send the signal to other user
                    dbRef.child(dataModel.getTarget()).child(LATEST_EVENT_FIELD_NAME)
                            .setValue(gson.toJson(dataModel));
                }else {
                    errorCallBack.onError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorCallBack.onError();
            }
        });
    }

    public void observeIncomingLatestEvent(INewEventCallBack callBack){
        dbRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            String data = Objects.requireNonNull(snapshot.getValue()).toString();
                            CallModel dataModel = gson.fromJson(data, CallModel.class);
                            callBack.onNewEventReceived(dataModel);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}
