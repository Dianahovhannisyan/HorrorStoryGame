package com.example.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.quiz.ui.main.GameOverActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MirrorActivity extends AppCompatActivity {

    private Button option1, option2, option3, option4;
    private ImageView imageView;
    private final String CORRECT_ANSWER = "3L8W";
    private String nextSceneId = "mirror_path_continue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        imageView = findViewById(R.id.imageView);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clicked = (Button) v;
                String answer = clicked.getText().toString().replace("🔹", "").replace("🔸", "").trim();

                if (answer.equals(CORRECT_ANSWER)) {
                    Intent intent = new Intent(MirrorActivity.this, GameActivity.class);
                    intent.putExtra("sceneId", nextSceneId);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MirrorActivity.this, GameOverActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        option1.setOnClickListener(answerListener);
        option2.setOnClickListener(answerListener);
        option3.setOnClickListener(answerListener);
        option4.setOnClickListener(answerListener);
    }
}
