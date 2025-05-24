package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AnketaActivity extends AppCompatActivity {
    private ImageView photo1, photo2, photo3;
    private TextView sceneText;
    private Button continueButton;
    private String nextSceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anketa);

        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);
        sceneText = findViewById(R.id.sceneText);
        continueButton = findViewById(R.id.continueButton);


        String sceneTextStr = getIntent().getStringExtra("sceneText");
        nextSceneId = getIntent().getStringExtra("nextSceneId");

        sceneText.setText(sceneTextStr);

        sceneText.setOnClickListener(v -> {
            photo1.setVisibility(View.GONE);
            sceneText.setVisibility(View.GONE);
            photo2.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
        });

        photo2.setOnClickListener(v -> {
            photo2.setVisibility(View.GONE);
            photo3.setVisibility(View.VISIBLE);
        });

        photo3.setOnClickListener(v -> {
        });

        continueButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}