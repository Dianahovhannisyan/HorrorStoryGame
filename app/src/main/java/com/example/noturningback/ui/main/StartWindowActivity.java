package com.example.noturningback.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.GameActivity;
import com.example.noturningback.R;
import com.example.noturningback.StatsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartWindowActivity extends AppCompatActivity {

    private TextView back_titleText;
    private TextView window_titleText1;
    private TextView window_titleText2;
    private Button startButton;
    private Button rulesButton;
    private Button exitButton;
    private TextView statsTitle;
    private TextView statsText;
    private DatabaseReference userStatsRef;

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
        statsTitle = findViewById(R.id.statsTitle);
        statsText = findViewById(R.id.statsText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = user.getUid();
        userStatsRef = database.getReference("users/" + userId + "/stats");

        StatsManager.initialize(userId);

        loadStats();

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
                .setMessage("Хотите продолжить с момент остановки или начать заново?")
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

    private void loadStats() {
        userStatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long wins = snapshot.child("wins").getValue(Long.class);
                    Long losses = snapshot.child("losses").getValue(Long.class);
                    int winsCount = (wins != null) ? wins.intValue() : 0;
                    int lossesCount = (losses != null) ? losses.intValue() : 0;
                    statsText.setText("Побед: " + winsCount + "\nПроигрышей: " + lossesCount);
                } else {
                    statsText.setText("Побед: 0\nПроигрышей: 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StartWindowActivity", "Failed to load stats: " + error.getMessage());
                statsText.setText("Побед: 0\nПроигрышей: 0");
            }
        });
    }

    public static class Stats {
        public int wins;
        public int losses;

        public Stats() {
        }

        public Stats(int wins, int losses) {
            this.wins = wins;
            this.losses = losses;
        }
    }
}