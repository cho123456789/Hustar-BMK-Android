package com.example.hustar_project_bmk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

public class camera_data extends AppCompatActivity {
    private static final String TAG = "camera_data";
    private ImageView imageView;
    ImageView img1,img2;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    TextView txt1;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_data);
        home = findViewById(R.id.home);
        txt1 = findViewById(R.id.txt_name);
        img1 = findViewById(R.id.dect1);
        img2 = findViewById(R.id.dect2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(camera_data.this, MainActivity.class);   // camera-> login class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef1 = db.collection("user").document("test");
        docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("Listen failed: " + error);
                    return;
                }
                if (value != null && value.exists()) {
                    //extView txt = findViewById(R.id.txt1);
                    float nNumber = Float.parseFloat(String.valueOf(value.getLong("txt")));
                    //System.out.println("Current datavalue: " + nNumber);
                    String pm25 = String.format("%.0f", nNumber);
                    //txt1.setText(value.getData().get("data").toString());
                    //txt1.setText(pm25);
                    detect_data(pm25);
                } else {
                    System.out.println("Current data: null");
                }
            }
        });
    }
    void detect_data(String pm25)
    {
        int pm255 = Integer.parseInt(pm25);

        if (pm255 < 80)
        {
            txt1.setText("파손이 확인 되었습니다 "+"\n"+ "안내테스크로 가십시오.");
        }
        else{
            txt1.setText("파손이 없습니다 "+"\n"+ "반납을 실시해주세요.");
        }
    }
}


