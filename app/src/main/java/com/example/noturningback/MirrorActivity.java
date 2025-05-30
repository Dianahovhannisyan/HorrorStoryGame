package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.ui.main.GameOverActivity;

public class MirrorActivity extends AppCompatActivity {
    private Button option1, option2, option3, option4;
    private String nextSceneId;
    private final String CORRECT_ANSWER = "W8E3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("MirrorActivity", "Received nextSceneId: " + nextSceneId);

        option1.setOnClickListener(v -> checkAnswer("W3B8"));
        option2.setOnClickListener(v -> checkAnswer("3L8W"));
        option3.setOnClickListener(v -> checkAnswer("E8M3"));
        option4.setOnClickListener(v -> checkAnswer("W8E3"));
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(CORRECT_ANSWER)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Intent intent = new Intent(this, GameOverActivity.class);
            startActivity(intent);
            finish();
        }
    }
}