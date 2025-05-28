package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DiagramActivity extends AppCompatActivity {
    private ImageView mozg, zapisi, serdce;
    private Button continueButton;
    private Handler handler;
    private String nextSceneId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);




        mozg= findViewById(R.id.mozg);
        serdce = findViewById(R.id.serdce);
        zapisi = findViewById(R.id.zapisi);
        continueButton = findViewById(R.id.continue_button);


        nextSceneId = getIntent().getStringExtra("nextSceneId");


        handler = new Handler(Looper.getMainLooper());


        showImage1();
    }


    private void showImage1() {
        mozg.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> showImage2(), 3000);
    }


    private void showImage2() {
        serdce.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> showImage3(), 3000);
    }


    private void showImage3() {
        zapisi.setVisibility(View.VISIBLE);
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

