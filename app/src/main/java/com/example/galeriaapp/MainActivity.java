package com.example.galeriaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public ActivityResultLauncher<Intent> galleryResultLauncher = null;
    ActivityResultLauncher<Intent> cameraResultLauncher = null;
    public static int RC_PHOTO_PICKER = 0;
    public static int REQUEST_IMAGE_CAPTURE = 0;
    private Uri URIFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button galleryButton = findViewById(R.id.galleryButton);
        Button cameraButton = findViewById(R.id.cameraButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryResultLauncher(null);

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraResultLauncher(null);
            }
        });
        this.cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            ImageView imageView = findViewById(R.id.img);
                            imageView.setImageURI(URIFoto);

                        }
                    }
                });
        this.galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            ImageView imageView = findViewById(R.id.img);
                            imageView.setImageURI(uri);
                        }
                    }
                });
    }

    public void openGalleryResultLauncher(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Setting image type
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        // Launching app
        galleryResultLauncher.launch(intent);

    }

    public void openCameraResultLauncher(View view) {
        //Create Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //generate File dir
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fileFoto = null;
        try {
            //Create temp file
            fileFoto = File.createTempFile("foto",".jpg",storageDir);
        } catch (IOException ex) {
            //Error controlling the file creation
            ex.printStackTrace();
        }
        if (fileFoto != null) {
            // Getting the URIFoto
            URIFoto = FileProvider.getUriForFile(this,
                    "com.example.galeriaapp.fileprovider",
                    fileFoto);
            //Add the photo into the intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, URIFoto);
            //Camera Launch
            cameraResultLauncher.launch(intent);
        }
    }

}