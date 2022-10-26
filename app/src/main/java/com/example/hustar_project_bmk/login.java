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
    //--------  버튼 타입 설정 ---------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ---------------------------------///
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        year =findViewById(R.id.year);
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        login = (Button)findViewById(R.id.login);
        //-----------각 레이아웃에 연결-------------//


        final FirebaseFirestore db  = FirebaseFirestore.getInstance();
        // Firestore DB 접속을 위해서 참조 생성

        DocumentReference docRef  =db.collection("user").document("test");
        // 생성된 참조를 이용하여 원하는 위치에 있는 collection 및 document 값 입력

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            // addSnapshotListner -> 해당하는 위치에 데이터의 변경이 실시될때 이벤트 발생

            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // 이벤트 발생하는 자식 메소드

                if(error != null)
                {
                    System.err.println("Listen failed: " + error);
                    return;
                }
                // 에러가 발생할시  -> 에러 메세지 호출 및 반환 실시


                if (value != null && value.exists())
                {
                    // 에러가 없을시 사용자가 원하는 필드값을 가주고온다

                    //-------------------------------------------------///
                    name.setText(value.getData().get("name").toString());
                    //String a = value.getData().get("name").toString();
                    age.setText(value.getData().get("age").toString());
                    year.setText(value.getData().get("year").toString());
                    //----------------------------------------------------//
                    //  원하는 필드의 값을 getData로 가주고 오기
                    // toString으로 문자열 변환실시
                    // 그값을 TextView로 표시하기

                }

                // 데이터가 null 일때 null 데이터 표시 실시
                else{
                    System.out.println("Current data: null");
                }
            }
        });
// --------------------- 버튼 이벤트로 다른 액티티비 전환 실시--------------------------------//
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
                Intent myIntent = new Intent(login.this,barcode.class);   // login-> camera class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
    }
// ------------------------버튼 이벤트로 다른 액티티비 전환 실시-------------------------------------//
}