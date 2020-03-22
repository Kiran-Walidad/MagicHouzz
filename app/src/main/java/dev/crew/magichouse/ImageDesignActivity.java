package dev.crew.magichouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.sceneform.ux.ArFragment;

public class ImageDesignActivity extends AppCompatActivity {
    private ArFragment arFragment;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_design);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        imageView=findViewById(R.id.designImage);

        //arFragment.setOnTapArPlaneListener();

        Uri selectedImgUri = getIntent().getData();
        if (selectedImgUri != null) {
            imageView.setImageURI(selectedImgUri);
        }
        Intent i = getIntent();
        if(i.getStringExtra("image")!=""){
            Toast.makeText(this, ""+i.getStringExtra("image"), Toast.LENGTH_SHORT).show();
        }



    }

    public void floatActionBtn(View view) {
        Intent intent = new Intent(ImageDesignActivity.this,Model3DImage.class);
        startActivity(intent);
    }
}
