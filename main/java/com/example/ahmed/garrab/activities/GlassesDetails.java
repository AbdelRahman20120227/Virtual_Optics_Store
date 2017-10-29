package com.example.ahmed.garrab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmed.R;
import com.example.ahmed.garrab.controllers.MyApplication;
import com.example.ahmed.garrab.controllers.TryController;
import com.example.ahmed.garrab.model.Glasses;
import com.example.ahmed.opengl.MyRenderer;
import com.example.ahmed.opengl.OpenGLActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GlassesDetails extends AppCompatActivity {

    Button tryit;
    String modelname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glasses_details);

        attachViewIDs();

        //loadGlassesDetails();
    }

    public void loadGlassesDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            Glasses glasses = (Glasses) intent.getSerializableExtra("glass");

            CircleImageView image = (CircleImageView) findViewById(R.id.imageView2);
            TextView t1 = (TextView) findViewById(R.id.setModelName);
            TextView t2 = (TextView) findViewById(R.id.setPrice);
            TextView t3 = (TextView) findViewById(R.id.setBrand);

            Picasso.with(this)
                    .load(glasses.getUrl())
                    .into(image);

            t1.setText(glasses.getModelName());
            t2.setText(String.valueOf(glasses.getPrice()));
            t3.setText(glasses.getBrand());
        }
    }

    public void attachViewIDs() {
        tryit = (Button) findViewById(R.id.TryIt);
        tryit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TryController.addTry(MyApplication.getEmail(), modelname);
                float[] color = null;
                double rand = Math.random();
                if(rand < 0.25){
                    color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                }
                else if(rand < 0.5){
                    color = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
                }
                else if(rand < 0.75){
                    color = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
                }
                else{
                    color = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
                }
                MyRenderer.publicColor = color;
                Intent intent = new Intent(getApplication(), OpenGLActivity.class);
                startActivity(intent);
            }
        });
    }

}
