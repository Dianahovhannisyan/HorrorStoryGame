package com.example.quiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quiz.ui.main.GameOverActivity;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FindBookActivity extends Activity {

    private FrameLayout rootLayout;
    private TextView timerText;
    private CountDownTimer timer;
    private boolean gameEnded = false;
    private String nextSceneId;
    private Random random = new Random();

    private ImageView diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book);

        rootLayout = findViewById(R.id.rootLayout);
        timerText = findViewById(R.id.timerText);
        nextSceneId = getIntent().getStringExtra("nextSceneId");

        setupGame();
        startTimer();
    }

    private void setupGame() {
        diary = new ImageView(this);
        diary.setImageResource(R.drawable.book);
        diary.setLayoutParams(new FrameLayout.LayoutParams(200, 200));
        diary.setX(getRandomX());
        diary.setY(getRandomY());
        diary.setVisibility(View.INVISIBLE);
        diary.setOnClickListener(v -> {
            if (!gameEnded && diary.getVisibility() == View.VISIBLE) {
                endGame(true);
            }
        });
        rootLayout.addView(diary);
        int paperCount = 60;
        AtomicInteger papersOverDiary = new AtomicInteger(0);


        for (int i = 0; i < paperCount; i++) {
            ImageView paper = new ImageView(this);
            int paperNumber = random.nextInt(10) + 1;
            int resId = getResources().getIdentifier("paper" + paperNumber, "drawable", getPackageName());
            paper.setImageResource(resId);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(250, 250);
            paper.setLayoutParams(params);

            float x = getRandomX();
            float y = getRandomY();
            paper.setX(x);
            paper.setY(y);

            makeDraggable(paper);
            rootLayout.addView(paper);

            rootLayout.post(() -> {
                if (isOverlapping(paper, diary)) {
                    papersOverDiary.incrementAndGet();
                    diary.setVisibility(View.INVISIBLE);
                }
            });
        }

        rootLayout.postDelayed(() -> {
            if (papersOverDiary.get() == 0) {
                diary.setVisibility(View.VISIBLE);
            }
            checkForBookVisibility();
        }, 500);
    }


    private float getRandomX() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return random.nextInt(Math.max(1, size.x - 300));
    }

    private float getRandomY() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return random.nextInt(Math.max(1, size.y - 500)) + 100;
    }

    private void makeDraggable(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameEnded) return true;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() + dX);
                        v.setY(event.getRawY() + dY);
                        rootLayout.postDelayed(() -> checkForBookVisibility(), 50);
                        break;
                }
                return true;
            }
        });
    }

    private void checkForBookVisibility() {
        boolean isCovered = false;
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            View child = rootLayout.getChildAt(i);
            if (child != diary && isOverlapping(child, diary)) {
                isCovered = true;
                break;
            }
        }
        diary.setVisibility(isCovered ? View.INVISIBLE : View.VISIBLE);
    }

    private boolean isOverlapping(View view1, View view2) {
        int[] loc1 = new int[2];
        int[] loc2 = new int[2];
        view1.getLocationOnScreen(loc1);
        view2.getLocationOnScreen(loc2);

        int left1 = loc1[0], top1 = loc1[1], right1 = left1 + view1.getWidth(), bottom1 = top1 + view1.getHeight();
        int left2 = loc2[0], top2 = loc2[1], right2 = left2 + view2.getWidth(), bottom2 = top2 + view2.getHeight();

        return !(right1 < left2 || left1 > right2 || bottom1 < top2 || top1 > bottom2);
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Осталось: " + (millisUntilFinished / 1000) + " сек");
            }

            public void onFinish() {
                if (!gameEnded) {
                    endGame(false);
                }
            }
        }.start();
    }

    private void endGame(boolean success) {
        gameEnded = true;
        if (timer != null) timer.cancel();

        if (success) {
            Intent result = new Intent();
            result.putExtra("nextSceneId", nextSceneId);
            setResult(RESULT_OK, result);
            finish();
        } else {
            Intent intent = new Intent(FindBookActivity.this, GameOverActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }
}
