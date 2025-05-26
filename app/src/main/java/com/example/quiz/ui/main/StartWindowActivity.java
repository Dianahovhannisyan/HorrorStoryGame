package com.example.quiz.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.GameActivity;
import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartWindowActivity extends AppCompatActivity {

    TextView back_titleText;
    TextView window_titleText1;
    TextView window_titleText2;
    Button startButton;
    Button settingsButton;
    Button rulesButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(StartWindowActivity.this, LoginActivity.class);
            intent.putExtra("forceLogin", true);
            startActivity(intent);
            finish();
            return;
        }

        back_titleText = findViewById(R.id.windowback_titleText);
        window_titleText1 = findViewById(R.id.window_titleText1);
        window_titleText2 = findViewById(R.id.window_titleText2);
        startButton = findViewById(R.id.windowstartButton);
        rulesButton = findViewById(R.id.windowrulesButton);
        exitButton = findViewById(R.id.windowexitButton);

        startButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                        .child(currentUser.getUid())
                        .child("currentSceneId");

                ref.get().addOnSuccessListener(dataSnapshot -> {
                    String savedScene = dataSnapshot.getValue(String.class);
                    if (savedScene != null && !savedScene.equals("start")) {
                        showContinueDialog(savedScene);
                    } else {
                        startGame("start");
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка загрузки прогресса", Toast.LENGTH_SHORT).show();
                    startGame("start");
                });
            } else {
                startGame("start");
            }
        });

        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartWindowActivity.this, RulesActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> showExitDialog());
    }

    private void startGame(String sceneId) {
        Intent intent = new Intent(StartWindowActivity.this, GameActivity.class);
        intent.putExtra("startSceneId", sceneId);
        startActivity(intent);
    }

    private void showContinueDialog(String savedSceneId) {
        new AlertDialog.Builder(this)
                .setTitle("Продолжить игру?")
                .setMessage("Хотите продолжить с момента остановки или начать заново?")
                .setPositiveButton("Продолжить", (dialog, which) -> startGame(savedSceneId))
                .setNegativeButton("Начать заново", (dialog, which) -> startGame("start"))
                .show();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Выход")
                .setMessage("Ты уверен, что хочешь выйти из аккаунта?")
                .setPositiveButton("Да", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(StartWindowActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finishAffinity();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
