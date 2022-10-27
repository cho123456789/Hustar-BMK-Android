package com.example.hustar_project_bmk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class bookImage extends AppCompatActivity {
    TextView bookname, bookwriter;
    Button btnnext, btnback;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_image);
        btnback = findViewById(R.id.btn_back_sign);
        btnnext = findViewById(R.id.nextbtn_0);
        bookname = findViewById(R.id.textView2);
        bookwriter =findViewById(R.id.textView7);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(bookImage.this, barcode.class);
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(bookImage.this, camera.class);
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Firestore DB 접속을 위해서 참조 생성
        DocumentReference docRef = db.collection("user").document("test");
        // 생성된 참조를 이용하여 원하는 위치에 있는 collection 및 document 값 입력
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            // addSnapshotListner -> 해당하는 위치에 데이터의 변경이 실시될때 이벤트 발생
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot value, @javax.annotation.Nullable FirebaseFirestoreException error) {
                // 이벤트 발생하는 자식 메소드
                if (error != null) {
                    System.err.println("Listen failed: " + error);
                    return;
                }
                // 에러가 발생할시  -> 에러 메세지 호출 및 반환 실시
                if (value != null && value.exists()) {
                    // 에러가 없을시 사용자가 원하는 필드값을 가주고온다
                    float nNumber = Float.parseFloat(String.valueOf(value.getLong("bookno")));
                    String pm25 = String.format("%.0f", nNumber);
                    Startbook(pm25);
                    //-------------------------------------------------///
                    //bookname.setText(value.getData().get("bookname").toString());
                    //String a = value.getData().get("name").toString();
                    //bookwriter.setText(value.getData().get("bookwriter").toString());
                    //year.setText(value.getData().get("year").toString());
                    //----------------------------------------------------//
                    //  원하는 필드의 값을 getData로 가주고 오기
                    // toString으로 문자열 변환실시
                    // 그값을 TextView로 표시하기

                }

                // 데이터가 null 일때 null 데이터 표시 실시
                else {
                    System.out.println("Current data: null");
                }
            }
        });

    }
    private void Startbook(String pm25)
    {
        int pm255 = Integer.parseInt(pm25);

        if(pm255 == 1)
        {
            final FirebaseFirestore db  = FirebaseFirestore.getInstance();
            DocumentReference docRef  =db.collection("user").document("book1");
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot value, @javax.annotation.Nullable FirebaseFirestoreException error) {
                    if(error != null) {
                        System.err.println("Listen failed: " + error);
                        return;
                    }
                    if (value != null && value.exists())
                    {
                        bookname.setText(value.getData().get("bookname").toString());
                        bookwriter.setText(value.getData().get("bookwriter").toString());
                    }
                    // 데이터가 null 일때 null 데이터 표시 실시
                    else{
                        System.out.println("Current data: null");
                    }
                }
            });

        }
        if(pm255 == 2)
        {
            final FirebaseFirestore db  = FirebaseFirestore.getInstance();
            DocumentReference docRef  =db.collection("user").document("book2");
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot value, @javax.annotation.Nullable FirebaseFirestoreException error) {
                    if(error != null) {
                        System.err.println("Listen failed: " + error);
                        return;
                    }
                    if (value != null && value.exists())
                    {
                        bookname.setText(value.getData().get("bookname").toString());
                        bookwriter.setText(value.getData().get("bookwriter").toString());
                    }
                    // 데이터가 null 일때 null 데이터 표시 실시
                    else{
                        System.out.println("Current data: null");
                    }
                }
            });

        }
        if(pm255 == 3)
        {
            final FirebaseFirestore db  = FirebaseFirestore.getInstance();
            DocumentReference docRef  =db.collection("user").document("book3");
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot value, @javax.annotation.Nullable FirebaseFirestoreException error) {
                    if(error != null) {
                        System.err.println("Listen failed: " + error);
                        return;
                    }
                    if (value != null && value.exists())
                    {
                        bookname.setText(value.getData().get("bookname").toString());
                        bookwriter.setText(value.getData().get("bookwriter").toString());
                    }
                    // 데이터가 null 일때 null 데이터 표시 실시
                    else{
                        System.out.println("Current data: null");
                    }
                }
            });

        }
    }
}
