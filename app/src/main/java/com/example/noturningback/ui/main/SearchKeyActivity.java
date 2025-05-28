package com.example.noturningback.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.SurchKey;

public class SearchKeyActivity extends AppCompatActivity {
    private SurchKey surchKeyView;
    private Handler handler;
    private Runnable gameOverRunnable;
    private static final long TIME_LIMIT = 7000;
    private String nextSceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nextSceneId = getIntent().getStringExtra("nextSceneId");

        surchKeyView = new SurchKey(this);
        setContentView(surchKeyView);

        handler = new Handler(Looper.getMainLooper());
        gameOverRunnable = () -> {
            if (!surchKeyView.isGameWon()) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        handler.postDelayed(gameOverRunnable, TIME_LIMIT);

        surchKeyView.setOnGameWonListener(() -> {
            handler.removeCallbacks(gameOverRunnable);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gameOverRunnable);
    }
}