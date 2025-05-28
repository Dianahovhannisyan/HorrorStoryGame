package com.example.noturningback;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.noturningback.ui.main.StartWindowActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatsManager {
    private static DatabaseReference userStatsRef;

    public static void initialize(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userStatsRef = database.getReference("users/" + userId + "/stats");
    }

    public static void updateStats(boolean isWin) {
        if (userStatsRef == null) return;

        userStatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int wins = snapshot.child("wins").getValue(Integer.class) != null ? snapshot.child("wins").getValue(Integer.class) : 0;
                int losses = snapshot.child("losses").getValue(Integer.class) != null ? snapshot.child("losses").getValue(Integer.class) : 0;

                if (isWin) {
                    wins++;
                } else {
                    losses++;
                }

                userStatsRef.setValue(new StartWindowActivity.Stats(wins, losses));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StatsManager", "Failed to update stats: " + error.getMessage());
            }
        });
    }
}