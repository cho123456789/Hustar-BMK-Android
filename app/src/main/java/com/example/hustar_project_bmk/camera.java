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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
    Button back_btn,btnupload,btncapture,btnresult;
    ImageView img1;
    TextView upload;
    private Uri imageUri;
    ProgressBar dialog;
    private File file;
    final static int TAKE_PICTURE = 1;
    private static final String TAG = "camera";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        img1 = (ImageView) findViewById(R.id.Click_img);
        btnupload = findViewById(R.id.upload);
        btnresult  = findViewById(R.id.result);
        dialog = findViewById(R.id.progressBar);
        upload = findViewById(R.id.textupload);

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                //galleryIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(galleryIntent, TAKE_PICTURE);
                //activityResult.launch(galleryIntent);\
                upload.setVisibility(View.VISIBLE);



            }
        });
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
                Intent myIntent = new Intent(camera.this,camera_data.class);   // camera-> camera_data class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
    }
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        imageUri = result.getData().getData();
                        img1.setImageURI(imageUri);
                        System.out.println("path: " +imageUri);
                    }
                }
            });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            Log.d("로그", "Permission: " + permissions[0] + " was " + grantResults[0]);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private  void uploadToFirebase() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";
        //System.out.println("파일이름 :"+filename);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hustar-9eeb8.appspot.com").child("my_folder/" + filename);
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            int progress = 0;

            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int bytesTransferred = (int) snapshot.getBytesTransferred();
                int totalBytes = (int) snapshot.getTotalByteCount();
                int progress = (100 * bytesTransferred) / totalBytes;
                dialog.setProgress(progress);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }   // 퍼미션 설정
        // 권한 요청
        }
}


