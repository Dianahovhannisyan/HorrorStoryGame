package com.example.quiz.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;


public class PicturesSeeActivity extends AppCompatActivity {
    private ImageView image1, image2, image3;
    private Button continueButton;
    private Handler handler;
    private String nextSceneId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_see);




        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        continueButton = findViewById(R.id.continue_button);


        nextSceneId = getIntent().getStringExtra("nextSceneId");


        handler = new Handler(Looper.getMainLooper());


        showImage1();
    }


    private void showImage1() {
        image1.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> showImage2(), 3000);
    }


    private void showImage2() {
        image2.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> showImage3(), 3000);
    }


    private void showImage3() {
        image3.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> showContinueButton(), 3000);
    }


    private void showContinueButton() {
        continueButton.setVisibility(View.VISIBLE);
        continueButton.setOnClickListener(v -> {
            handler.removeCallbacksAndMessages(null);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

