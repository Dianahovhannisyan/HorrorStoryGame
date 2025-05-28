package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TrueActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button continueButton;
    private String nextSceneId;
    private int currentImageIndex = 0;
    private final int[] images = {
            R.drawable.shok,
            R.drawable.anketa,
            R.drawable.pacient7
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true);

        imageView = findViewById(R.id.imageView);
        continueButton = findViewById(R.id.continueButton);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("TrueActivity", "Received nextSceneId: " + nextSceneId);

        imageView.setImageResource(images[currentImageIndex]);

        imageView.setOnClickListener(v -> {
            currentImageIndex = (currentImageIndex + 1) % images.length;
            imageView.setImageResource(images[currentImageIndex]);
        });

        continueButton.setOnClickListener(v -> {
            StatsManager.updateStats(true);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}