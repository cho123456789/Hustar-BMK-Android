package com.example.hustar_project_bmk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class login extends AppCompatActivity {
    Button back_btn;
    Button login;
    TextView name, age, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        year =findViewById(R.id.year);
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        login = (Button)findViewById(R.id.login);
        final FirebaseFirestore db  = FirebaseFirestore.getInstance();

        DocumentReference docRef  =db.collection("user").document("test");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                {
                    System.err.println("Listen failed: " + error);
                    return;
                }
                if (value != null && value.exists())
                {
                    name.setText(value.getData().get("name").toString());
                    age.setText(value.getData().get("age").toString());
                    year.setText(value.getData().get("year").toString());
                }
                else{
                    System.out.println("Current data: null");
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(login.this,MainActivity.class);   // login-> main class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(login.this,camera.class);   // login-> camera class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
    }
}