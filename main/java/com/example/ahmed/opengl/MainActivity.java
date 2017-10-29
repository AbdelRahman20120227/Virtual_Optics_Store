package com.example.ahmed.opengl;


import android.content.Context;
import android.content.Intent;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ahmed.R;
import com.tzutalin.dlib.FaceDet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {


    public void loadFaceDetector(){
        InputStream reader = getApplicationContext().getResources().openRawResource(R.raw.shape_predictor_68_face_landmarks);
        File dir = getApplicationContext().getDir("FaceDetector", Context.MODE_PRIVATE);
        File detectorFile = new File(dir, "detector.dat");
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(detectorFile);
            int i;
            byte[] buffer = new byte[1024];
            while((i = reader.read(buffer)) != -1){
                writer.write(buffer,0,i);
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        CameraView.det = new FaceDet(detectorFile.getAbsolutePath());
        CameraView.loaded = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
     //   Toast.makeText(this, "dim = " + 4, Toast.LENGTH_LONG).show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), OpenGLActivity.class);
                startActivity(intent);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadFaceDetector();
            }
        });
        thread.start();
    }
}
