package com.example.hustar_project_bmk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class camera extends AppCompatActivity {
    // --------------------------------//
    Button back_btn,btnupload,btnresult;
    ImageView img1;
    TextView upload_txt;
    private Uri imageUri;
    ProgressBar dialog;
    // -------버튼 타입 설정-----------//

    // ----------------------------//
    final static int TAKE_PICTURE = 1;
    private static final String TAG = "camera";
    // ------ 카메라 설정을 위한 상수 설정----------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //-----------버튼 레이아웃 설정정-----------------/
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        img1 = (ImageView) findViewById(R.id.Click_img);
        btnupload = findViewById(R.id.upload);
        btnresult  = findViewById(R.id.result);
        dialog = findViewById(R.id.progressBar);
        upload_txt = findViewById(R.id.textupload);
        //-----------버튼 이벤트 설정-------------------//

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();   // 업로드 함수로 이동
                upload_txt.setVisibility(View.VISIBLE);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 사진 찍는 함수 설정
                startActivityForResult(galleryIntent, TAKE_PICTURE);
                // 카메라 찍기
                dialog.setVisibility(View.VISIBLE);
                btnresult.setVisibility(View.VISIBLE);
                btnupload.setVisibility(View.VISIBLE);

            }
        });

        // -------------------레이아웃 이동 코드 ------------------//
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(camera.this,login.class);   // camera-> login class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(camera.this, Text.class);   // camera-> camera_data class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
    }
    // -------------------레이아웃 이동 코드 ------------------//



    //------------------- 카메라 관련 액티비티 설정  ------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 사진 촬영 완료 후 응답
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK && data.hasExtra("data")) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) img1.setImageBitmap(bitmap);

                String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "사진 저장", "사진이 저장되었습니다.");
                imageUri = Uri.parse(imageSaveUri);
                Log.d(TAG, "MainActivity - onActivityResult() called" + imageUri);
            }
        }
    }
//-----------------------카메라 관련 액티비티 설정---------------------//


    // ------------------------ 카메라 퍼미션 설정 -----------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            Log.d("로그", "Permission: " + permissions[0] + " was " + grantResults[0]);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // ------------------------ 카메라 퍼미션 설정 -----------------//


    // ---- 파이어베이스 사진 업로드  -------------------------------//
    private  void uploadToFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // 파이어베이스 참조 설정
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        // 현재 년월일 시간 지정
        Date now = new Date();
        // 현재 날짜 클래스 설정
        String filename = formatter.format(now) + ".png";
        // 현재 날짜를 변환하여 문자열로 전환

        StorageReference storageRef = storage.getReferenceFromUrl("gs://hustar-9eeb8.appspot.com").child("my_folder/" + filename);
        // 현재 파이어베이스에서 접속하고자하는 url 및 폴더 및 저장할 이름설정
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            // 이미지를 파이어베이스에 저장


            // 저장 성공
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
            }
            // 저장 실패
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
            }
            // 저장 중 로딩 시간
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                // ------- 저장 중 로딩 시간을 구하는 식 --------------//
                int bytesTransferred = (int) snapshot.getBytesTransferred();
                int totalBytes = (int) snapshot.getTotalByteCount();
                int progress = (100 * bytesTransferred) / totalBytes;
                dialog.setProgress(progress);
                // ------- 저장 중 로딩 시간을 구하는 식 --------------//
            }
        });

        //----------카메라 권한 요청  ---------------------------------//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

    }
        //----------카메라 권한 요청  ---------------------------------//
}


