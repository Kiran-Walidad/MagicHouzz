package dev.crew.magichouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private final static int CAMERA_REQUST_CODE = 100;
    private final static int STORAGE_REQUST_CODE = 200;
    private final static int GALLERY_IMAGE_PICKER_CODE = 300;
    private final static int CAMERA_IMAGE_PICKER_CODE = 400;


    String cameraPermecation [] ;
    String storagePermecation [] ;

    Uri image_uri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPermecation = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermecation = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



    }

    public void new_Image(View view) {
        Intent intent = new Intent(MainActivity.this,ModelImage.class);
        startActivity(intent);
        //CameraAndGalleryImage();
    }

    private void CameraAndGalleryImage() {
        String options [] = {"Camera" ,  "Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Action");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){

                    // Camera
                    if(!checkCameraPermecation()){
                        requestCameraPermecation();
                    }else {
                        pickImageFromCamera();
                    }
                }else if (i==1){
                    //Gallery
                    if(!checkStoragePermecation()){
                        requestStoragePermecation();
                    }else {
                        pickImageFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }


    private Boolean checkStoragePermecation(){
        Boolean result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requestStoragePermecation(){
        requestPermissions(storagePermecation,STORAGE_REQUST_CODE);
    }

    private Boolean checkCameraPermecation(){
        Boolean result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        Boolean result1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }


    private void requestCameraPermecation(){
        requestPermissions(cameraPermecation,CAMERA_REQUST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted ){
                        pickImageFromCamera();
                    }else {
                        Toast.makeText(MainActivity.this, "Please Grant camera & storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUST_CODE:
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if( storageAccepted ){
                        pickImageFromGallery();
                    }else {
                        Toast.makeText(MainActivity.this, "Please Grant storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_IMAGE_PICKER_CODE);
    }

    private void pickImageFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,CAMERA_IMAGE_PICKER_CODE);
        Toast.makeText(MainActivity.this, "camera", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode== GALLERY_IMAGE_PICKER_CODE){
                image_uri = data.getData();
                if(image_uri==null){
                    Toast.makeText(MainActivity.this, "select image", Toast.LENGTH_SHORT).show();
                }else {
                    Uri selectedImgUri = data.getData();
                    Intent intent = new Intent(MainActivity.this,ImageDesignActivity.class);
                    intent.setData(selectedImgUri);
                    startActivity(intent);
                }
            }
            if(requestCode== CAMERA_IMAGE_PICKER_CODE ){
                if(image_uri==null){
                    Toast.makeText(MainActivity.this, "select image", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this,ImageDesignActivity.class);
                    intent.setData(image_uri);
                    startActivity(intent);
                }
            }
        }

    }

}

