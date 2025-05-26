package com.example.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChasiActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText passwordInput;
    private Button checkButton;
    private TextView errorMessage;
    private String nextSceneId;
    private boolean showingSecondImage = false;
    private final String CORRECT_PASSWORD = "1042";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chasi);

        imageView = findViewById(R.id.imageView);
        passwordInput = findViewById(R.id.passwordInput);
        checkButton = findViewById(R.id.checkButton);
        errorMessage = findViewById(R.id.errorMessage);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("ChasiActivity", "Received nextSceneId: " + nextSceneId);

        imageView.setImageResource(R.drawable.komnatachasi);

        imageView.setOnClickListener(v -> {
            if (!showingSecondImage) {
                imageView.setImageResource(R.drawable.trichasi);
                passwordInput.setVisibility(View.VISIBLE);
                checkButton.setVisibility(View.VISIBLE);
                showingSecondImage = true;
            }
        });

        checkButton.setOnClickListener(v -> checkPassword());
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
            errorMessage.setText("Неверный код, попробуйте снова");
            errorMessage.setVisibility(View.VISIBLE);
            passwordInput.setText("");
        }
    }
}