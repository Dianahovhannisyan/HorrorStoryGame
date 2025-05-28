package com.example.noturningback.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.noturningback.GameActivity;
import com.example.noturningback.R;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class RulesActivity extends AppCompatActivity {

    private ImageView swordsIcon;
    private ImageView rulesBackIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        swordsIcon = findViewById(R.id.swordsIcon);


        swordsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        rulesBackIcon = findViewById(R.id.rules_back_icon);
        rulesBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToStartWindow();
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(RulesActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    private void BackToStartWindow (){
        Intent intent = new Intent(RulesActivity.this, StartWindowActivity.class);
        startActivity(intent);
        finish();
    }
}