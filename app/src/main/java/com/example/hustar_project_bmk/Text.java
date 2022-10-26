package com.example.hustar_project_bmk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hustar_project_bmk.databinding.TextBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

public class Text extends AppCompatActivity {
    // --------버튼 타입 설정------------//
    TextBinding binding;
    StorageReference storageReference;
    ProgressDialog progressDialog; // 진행 상태바
    ImageView img1, img2;
    TextView txt;
    Button home;
    // -------버튼 타입 설정-----------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 뷰바인딩 => 뷰와 상호작용하는 코드 쉽게 작성 //
        // findViewByID 대체해서 사용할수있다
        binding = TextBinding.inflate(getLayoutInflater());
        // infalate 다른 화면의 레이아웃을 불러오기 위해서 접근을 가능하게 함
        setContentView(binding.getRoot());
        // 바인딩의 부모를 레이아웃을 보여준다

        // --------버튼 레이아웃 연결------------//
        img1 = findViewById(R.id.dect1);
        img2 = findViewById(R.id.dect2);
        //txt = findViewById(R.id.txt_name);
        home = findViewById(R.id.home);
        // --------버튼 레이아웃 연결------------//


        // ------ 다른 액티비티로 이동 ------------//
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Text.this, barcode.class);   // Text -> camera class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        // ------ 다른 액티비티로 이동 ------------//


        binding.getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ------진행 상태바 설정 --------//
                progressDialog = new ProgressDialog(Text.this);
                progressDialog.setMessage("Fetching image...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                // ----- 진행 상태바 설정 ------//
                //String imageID = binding.etimageId.getText().toString();


                // 원하는 collection 에서 값이 변경될경우 이벤트 발생
                storageReference = FirebaseStorage.getInstance().getReference("my_folder/" + "1" + ".png");
                // 다운로드할 파일명 설정
                try {
                    File localfile = File.createTempFile("tempfile", ".jpg");
                    // 임시폴더 경로 가주고오기
                    storageReference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    // 대화상자 종료

                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    //  가주온 파일을 비트맵으로 디코딩실시
                                    int mDegree = 0;
                                    mDegree = mDegree + 90;
                                    img2 = findViewById(R.id.dect2);
                                    binding.dect1.setImageBitmap(rotateImage(bitmap ,mDegree));


                                    //binding.dect1.setImageBitmap(bitmap);
                                    // 이미지뷰에 이미지 표시하기

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Toast.makeText(Text.this, "Failed to retrieve", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                storageReference = FirebaseStorage.getInstance().getReference("my_folder/" + "2" + ".png");
                try {
                    File localfile = File.createTempFile("tempfile", ".jpg");
                    storageReference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    //Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                                    Bitmap bitmap1 = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    //  가주온 파일을 비트맵으로 디코딩실시
                                    int mDegree1 = 0;
                                    mDegree1 = mDegree1 + 90;
                                    img2 = findViewById(R.id.dect2);
                                    binding.dect2.setImageBitmap(rotateImage(bitmap1 ,mDegree1));

                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    // DB 참조 설정
                                    DocumentReference docRef = db.collection("user").document("test");
                                    // 원하는 collection , document 설정
                                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                System.err.println("Listen failed: " + error);
                                                return;
                                            }
                                            if (value != null && value.exists()) {
                                                try {
                                                    Thread.sleep(2000);
                                                    Toast.makeText(Text.this, value.getData().get("txt2").toString(), Toast.LENGTH_SHORT).show();
                                                    //txt.setText(value.getData().get("txt2").toString());
                                                    //float nNumber = Float.parseFloat(String.valueOf(value.getLong("txt2")));
                                                    //System.out.println("Current datavalue: " + nNumber);
                                                    //String pm25 = String.format("%.0f", nNumber);
                                                    //result_view(pm25);
                                                }catch (InterruptedException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                System.out.println("Current data: null");
                                            }
                                        }
                                    });


                                    //binding.dect2.setImageBitmap(bitmap);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Toast.makeText(Text.this, "Failed to retrieve", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
}
