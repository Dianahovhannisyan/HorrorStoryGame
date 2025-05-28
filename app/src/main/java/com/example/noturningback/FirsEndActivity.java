package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.ui.main.GameOverActivity;

public class FirsEndActivity extends AppCompatActivity {
    private TextView endText;
    private ImageView endImage;
    private int clickStage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firs_end);

        endText = findViewById(R.id.endText);
        endImage = findViewById(R.id.endImage);

        String sceneText = getIntent().getStringExtra("sceneText");
        if (sceneText != null) {
            endText.setText(sceneText);
        }

        endText.setOnClickListener(v -> {
            if (clickStage == 0) {
                endImage.setVisibility(View.VISIBLE);
                endImage.setImageResource(R.drawable.siditvpalate);
                clickStage = 1;
            }
        });

        endImage.setOnClickListener(v -> {
            if (clickStage == 1) {
                endImage.setImageResource(R.drawable.mamafotografia);
                clickStage = 2;
            } else if (clickStage == 2) {
                endImage.setImageResource(R.drawable.smile);
                clickStage = 3;
            } else if (clickStage == 3) {
                Intent intent = new Intent(FirsEndActivity.this, GameOverActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}