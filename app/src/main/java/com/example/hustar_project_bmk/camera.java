package com.example.hustar_project_bmk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private Uri imageUri;
    ProgressBar dialog;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        img1 = (ImageView) findViewById(R.id.Click_img);
        btnupload = findViewById(R.id.upload);
        btncapture = findViewById(R.id.capture);
        btnresult  = findViewById(R.id.result);
        dialog = findViewById(R.id.progressBar);


        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                activityResult.launch(galleryIntent);
                btnupload.setVisibility(View.VISIBLE);
                btncapture.setVisibility(View.VISIBLE);
                btnresult.setVisibility(View.VISIBLE);

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
    private  void uploadToFirebase() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";
        //System.out.println("파일이름 :"+filename);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hustar-9eeb8.appspot.com").child("my_folder/"+filename);
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
            int progress=0;
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int bytesTransferred = (int) snapshot.getBytesTransferred();
                int totalBytes = (int) snapshot.getTotalByteCount();
                int progress = (100 *  bytesTransferred) / totalBytes ;
                dialog.setProgress(progress);
            }
        });
    }
}
