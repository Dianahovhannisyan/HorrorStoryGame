package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ParolActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText passwordInput;
    private TextView errorMessage;
    private String nextSceneId;
    private boolean showingSecondImage = false;
    private final String CORRECT_PASSWORD = "5689";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parol);

        imageView = findViewById(R.id.imageView);
        passwordInput = findViewById(R.id.passwordInput);
        errorMessage = findViewById(R.id.errorMessage);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("ParolActivity", "Received nextSceneId: " + nextSceneId);

        imageView.setImageResource(R.drawable.komp);

        imageView.setOnClickListener(v -> {
            if (!showingSecondImage) {
                imageView.setImageResource(R.drawable.parol2);
                passwordInput.setVisibility(View.VISIBLE);
                showingSecondImage = true;
            }
        });

        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && showingSecondImage) {
                // Проверяем, попал ли клик в область кнопки "ENTER"
                float x = event.getX();
                float y = event.getY();
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                int imageHeight = imageView.getHeight();
                int imageWidth = imageView.getWidth();
                if (y > imageHeight * 0.85 && y < imageHeight && x > imageWidth * 0.4 && x < imageWidth * 0.6) {
                    checkPassword();
                }
            }
            return false;
        });
    }

    private void checkPassword() {
        String enteredPassword = passwordInput.getText().toString().trim();
        if (enteredPassword.equals(CORRECT_PASSWORD)) {
            errorMessage.setVisibility(View.INVISIBLE);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            errorMessage.setText("Пароль неправильный");
            errorMessage.setVisibility(View.VISIBLE);
            passwordInput.setText("");
        }
    }
}