package com.example.junmung.firebasechat;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText edit_input;
    private Button btn_send;
    private RecyclerView recyclerView;
    private ChatItemAdapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String myName;


    // 페이지 네이션

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMyName();
        getID();
        firebaseSetting();
    }

    private void setMyName() {
        SharedPreferences prefs;
        prefs = getSharedPreferences("MyData", MODE_PRIVATE);

        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if(isFirstRun){
            myName = "user" + new Random().nextInt(1000);
            prefs.edit();
            prefs.edit().putString("MyName", myName).apply();
            prefs.edit().commit();
        }
        else{
            myName = prefs.getString("MyName", "");
        }
    }

    private void firebaseSetting() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("message").addChildEventListener(childEventListener);
    }



    private void getID(){
        edit_input = findViewById(R.id.activity_main_editText_input);
        btn_send = findViewById(R.id.activity_main_button_send);
        btn_send.setOnClickListener(onClickListener);

        recyclerView = findViewById(R.id.activity_main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatItemAdapter(myName);
        recyclerView.setAdapter(adapter);
    }


    private Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.activity_main_button_send){
                // 메세지 전송
                UserData userData = new UserData(myName, edit_input.getText().toString());
                databaseReference.child("message").push().setValue(userData);
                edit_input.setText("");
            }
        }
    };


    // 채팅 받기 이벤트 리스너
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            UserData userData = dataSnapshot.getValue(UserData.class);
            adapter.addMessage(userData);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
