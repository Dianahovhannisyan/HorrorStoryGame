package com.example.quiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

public class SurchKey extends View {
    private Paint backgroundPaint, textPaint, flashlightPaint;
    private Bitmap keyBitmap;
    private Path flashlightPath;
    private float flashlightX, flashlightY;
    private float keyX, keyY;
    private float keySize = 40f;
    private final float flashlightRadius = 150f;
    private boolean gameWon = false;
    private boolean gameOver = false;
    private boolean keyVisible = false;
    private boolean timerStarted = false;
    private long startTime;
    private final long timeLimit = 7000;
    private Handler handler;
    private Runnable timerRunnable;
    private OnGameWonListener onGameWonListener;

    public interface OnGameWonListener {
        void onGameWon();
    }

    public SurchKey(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        keyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.key);
        setKeySize(60f);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        flashlightPaint = new Paint();
        flashlightPaint.setColor(Color.argb(200, 255, 255, 0));
        flashlightPaint.setStyle(Paint.Style.FILL);

        flashlightPath = new Path();
        flashlightX = -100;
        flashlightY = -100;

        setKeyPosition(200, 200);

        handler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!gameWon && !gameOver && timerStarted) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime >= timeLimit) {
                        gameOver = true;
                        invalidate();
                    } else {
                        handler.postDelayed(this, 100);
                        invalidate();
                    }
                }
            }
        };
    }

    public void setOnGameWonListener(OnGameWonListener listener) {
        this.onGameWonListener = listener;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setKeyPosition(float x, float y) {
        this.keyX = x;
        this.keyY = y;
        invalidate();
    }

    public void setKeySize(float size) {
        this.keySize = size;
        keyBitmap = Bitmap.createScaledBitmap(keyBitmap, (int)keySize, (int)keySize, true);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        if (flashlightX >= 0 && flashlightY >= 0) {
            canvas.drawCircle(flashlightX, flashlightY, flashlightRadius, flashlightPaint);
            flashlightPath.reset();
            flashlightPath.addCircle(flashlightX, flashlightY, flashlightRadius, Path.Direction.CW);
            Paint clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(flashlightPath, clearPaint);
        }

        if (keyVisible) {
            canvas.drawBitmap(keyBitmap, keyX, keyY, null);
        }

        if (timerStarted) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            float secondsLeft = (timeLimit - elapsedTime) / 1000f;
            if (secondsLeft < 0) secondsLeft = 0;
            canvas.drawText(String.format("%.1f", secondsLeft), getWidth() - 100, 100, textPaint);
        }

        if (gameWon) {
            canvas.drawText("Победа!", getWidth() / 2f, getHeight() / 2f, textPaint);
        } else if (gameOver) {
            canvas.drawText("Время вышло!", getWidth() / 2f, getHeight() / 2f, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameWon || gameOver) return true;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!timerStarted) {
                    timerStarted = true;
                    startTime = System.currentTimeMillis();
                    handler.post(timerRunnable);
                }
                flashlightX = x;
                flashlightY = y;
                keyVisible = isKeyVisible();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (keyVisible &&
                        x >= keyX && x <= keyX + keySize &&
                        y >= keyY && y <= keyY + keySize) {
                    gameWon = true;
                    handler.removeCallbacks(timerRunnable);
                    if (onGameWonListener != null) {
                        onGameWonListener.onGameWon();
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    private boolean isKeyVisible() {
        return flashlightX + flashlightRadius >= keyX &&
                flashlightX - flashlightRadius <= keyX + keySize &&
                flashlightY + flashlightRadius >= keyY &&
                flashlightY - flashlightRadius <= keyY + keySize;
    }
}