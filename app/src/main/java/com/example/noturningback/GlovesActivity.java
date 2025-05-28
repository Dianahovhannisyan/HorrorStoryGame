package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GlovesActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView photoView;
    private int photoIndex = 0;
    private String nextSceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gloves);

        textView = findViewById(R.id.glovesText);
        photoView = findViewById(R.id.photoView);

        nextSceneId = getIntent().getStringExtra("nextSceneId");
        Log.d("GlovesActivity", "Received nextSceneId: " + nextSceneId);

        textView.setText("Через несколько шагов коридор начинает расширяться, открывая перед тобой странную лабораторию — покрытые пылью приборы, разбитые экраны, на полу валяются перчатки и фотографии… На одной из них — ты сам, связанный, с испуганными глазами. Сзади на фото — силуэт человека в белом халате, лицо его закрыто. По спине пробегает холод, а тебя окутывает чувства, что это место как-то связано с тобой. Возможно, твое прошлое — совсем не то, каким ты его помнишь.");

        setPhoto();

        photoView.setOnClickListener(v -> {
            photoIndex++;
            setPhoto();
            if (photoIndex == 2) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nextSceneId", nextSceneId);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setPhoto() {
        if (photoIndex == 0) {
            photoView.setImageResource(R.drawable.gloves);
        } else if (photoIndex == 1) {
            photoView.setImageResource(R.drawable.shirt);
        }
    }
}