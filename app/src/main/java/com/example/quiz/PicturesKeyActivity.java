package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PicturesKeyActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button continueButton;
    private String nextSceneId;
    private int currentImageIndex = 0;
    private final int[] images = {
            R.drawable.klass,
            R.drawable.pictures,
            R.drawable.pictures_key
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_key);

        imageView = findViewById(R.id.imageView);
        continueButton = findViewById(R.id.continueButton);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("PictureKeyActivity", "Received nextSceneId: " + nextSceneId);

        imageView.setImageResource(images[currentImageIndex]);

        imageView.setOnClickListener(v -> {
            currentImageIndex = (currentImageIndex + 1) % images.length;
            imageView.setImageResource(images[currentImageIndex]);
        });

        continueButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}