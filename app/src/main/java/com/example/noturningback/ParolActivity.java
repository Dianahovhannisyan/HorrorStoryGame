package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ParolActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText passwordInput;
    private TextView errorMessage, passwordTitle, enterButton;
    private View passwordFrame;
    private String nextSceneId;
    private boolean firstClick = true;
    private final String CORRECT_PASSWORD = "5689";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parol);

        imageView = findViewById(R.id.imageView);
        passwordInput = findViewById(R.id.passwordInput);
        errorMessage = findViewById(R.id.errorMessage);
        passwordTitle = findViewById(R.id.passwordTitle);
        passwordFrame = findViewById(R.id.passwordFrame);
        enterButton = findViewById(R.id.enterButton);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("ParolActivity", "Received nextSceneId: " + nextSceneId);

        imageView.setVisibility(View.VISIBLE);
        passwordInput.setVisibility(View.INVISIBLE);
        passwordTitle.setVisibility(View.INVISIBLE);
        passwordFrame.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(v -> {
            if (firstClick) {
                firstClick = false;
                imageView.setVisibility(View.GONE);

                passwordInput.setVisibility(View.VISIBLE);
                passwordTitle.setVisibility(View.VISIBLE);
                passwordFrame.setVisibility(View.VISIBLE);
                enterButton.setVisibility(View.VISIBLE);
            }
        });

        enterButton.setOnClickListener(v -> checkPassword());
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
