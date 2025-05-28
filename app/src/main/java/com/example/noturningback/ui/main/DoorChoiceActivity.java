package com.example.noturningback.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.R;

public class DoorChoiceActivity extends AppCompatActivity {
    private TextView doorTimer;
    private ImageButton redDoorActionButton, yellowDoorActionButton;
    private Handler handler;
    private Runnable timerRunnable;
    private static final long TIME_LIMIT = 10000;
    private long startTime;
    private String nextSceneId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_choice);

        doorTimer = findViewById(R.id.doorTimer);
        redDoorActionButton = findViewById(R.id.redDoorActionButton);
        yellowDoorActionButton = findViewById(R.id.yellowDoorActionButton);


        nextSceneId = getIntent().getStringExtra("nextSceneId");


        handler = new Handler(Looper.getMainLooper());
        startTime = System.currentTimeMillis();

        timerRunnable = () -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            float secondsLeft = (TIME_LIMIT - elapsedTime) / 1000f;
            doorTimer.setText(String.format("%.1f", secondsLeft));
            if (secondsLeft <= 0) {
                loseGame();
            } else {
                handler.postDelayed(timerRunnable, 100);
            }
        };
        handler.post(timerRunnable);


        redDoorActionButton.setOnClickListener(v -> chooseDoor("red_door"));
        yellowDoorActionButton.setOnClickListener(v -> chooseDoor("yellow_door"));
    }

    private void chooseDoor(String door) {
        handler.removeCallbacks(timerRunnable);


        Intent resultIntent = new Intent();
        resultIntent.putExtra("nextSceneId", door);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void loseGame() {
        handler.removeCallbacks(timerRunnable);



        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("nextSceneId", "surch_key_fail");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);


    }
}
