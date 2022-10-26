package com.example.hustar_project_bmk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class barcode extends AppCompatActivity {
    // --------------------------------//
    Button back_btn,btnupload,btnnext;
    ImageView img1;
    TextView upload_txt,bookname,bookwriter;
    private Uri imageUri;
    ProgressBar dialog;
    // -------버튼 타입 설정-----------//

    // ----------------------------//
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    final static int TAKE_PICTURE = 1;
    private static final String TAG = "camera";
    private String imageFilePath;
    private Uri photoUri;
    // ------ 카메라 설정을 위한 상수 설정----------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);

        //-----------버튼 레이아웃 설정정-----------------/
        back_btn = (Button) findViewById(R.id.btn_back_sign);
        img1 = (ImageView) findViewById(R.id.Click_img1);
        btnupload = findViewById(R.id.loadbtn);
        btnnext  = findViewById(R.id.nextbtn);
        bookname = findViewById(R.id.bookname);
        bookwriter = findViewById(R.id.bookwirter);

        //-----------버튼 이벤트 설정-------------------//

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase1();   // 업로드 함수로 이동
                //upload_txt.setVisibility(View.VISIBLE);
                bookname.setVisibility(View.VISIBLE);
                bookwriter.setVisibility(View.VISIBLE);

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent();
                //Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 사진 찍는 함수 설정
                //startActivityForResult(galleryIntent, TAKE_PICTURE);
                // 카메라 찍기
                // dialog.setVisibility(View.VISIBLE);
                btnnext.setVisibility(View.VISIBLE);
                btnupload.setVisibility(View.VISIBLE);
                bookname.setVisibility(View.VISIBLE);
                bookwriter.setVisibility(View.VISIBLE);

                final FirebaseFirestore db  = FirebaseFirestore.getInstance();
                // Firestore DB 접속을 위해서 참조 생성

                DocumentReference docRef  =db.collection("user").document("test");
                // 생성된 참조를 이용하여 원하는 위치에 있는 collection 및 document 값 입력

                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    // addSnapshotListner -> 해당하는 위치에 데이터의 변경이 실시될때 이벤트 발생

                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot value, @javax.annotation.Nullable FirebaseFirestoreException error) {
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
                        else{
                            System.out.println("Current data: null");
                        }
                    }
                });

            }
        });

        // -------------------레이아웃 이동 코드 ------------------//
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(barcode.this,login.class);   // barcode -> camera class로 이동하는 이벤트
                startActivity(myIntent); // 이벤트 시작하는 코드
                finish(); // 이벤트 종료
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(barcode.this,camera.class);   // barcode -> camera class로 이동하는 이벤트
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, 720, 1280, true);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView)findViewById(R.id.Click_img1)).setImageBitmap(rotate(resize, exifDegree));
        }
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
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
    private  void uploadToFirebase1() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // 파이어베이스 참조 설정
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        // 현재 년월일 시간 지정
        Date now = new Date();
        // 현재 날짜 클래스 설정
        //String filename = formatter.format(now) + ".png";
        // 현재 날짜를 변환하여 문자열로 전환

        String filename2 = "barcode" + ".png";

        StorageReference storageRef = storage.getReferenceFromUrl("gs://hustar-9eeb8.appspot.com").child("my_folder/" + filename2);
        // 현재 파이어베이스에서 접속하고자하는 url 및 폴더 및 저장할 이름설정
        storageRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

    //----------카메라 권한 요청  ---------------------------------//
}


