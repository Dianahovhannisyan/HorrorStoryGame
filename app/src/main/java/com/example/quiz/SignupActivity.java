package com.example.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.ui.main.HelperClass;
import com.example.quiz.ui.main.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText nameField, emailField, usernameField, passwordField;
    Button signupButton;
    FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        nameField = findViewById(R.id.signup_name);
        emailField = findViewById(R.id.signup_email);
        usernameField = findViewById(R.id.signup_username);
        passwordField = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                        if (verifyTask.isSuccessful()) {
                            HelperClass helper = new HelperClass(name, email, username, password);
                            database.child(user.getUid()).setValue(helper);
                            Toast.makeText(this, "Подтвердите email. Проверьте почту", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
