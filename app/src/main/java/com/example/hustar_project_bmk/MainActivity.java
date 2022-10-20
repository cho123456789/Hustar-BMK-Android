package com.example.hustar_project_bmk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnlongin;  // 버튼 타입 정의 실시
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //  표시할 레이아웃 설정
        btnlongin  = findViewById(R.id.start);  // 버튼 레이아웃 매칭


        // 버튼 클릭 이벤트 발생 -> 다른 액티비티로 이동 실시
        btnlongin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //  버튼 클릭 이벤트
                Intent myIntent = new Intent(MainActivity.this,login.class);   // MAIN -> login class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
    }


}