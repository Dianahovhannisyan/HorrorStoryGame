package com.example.quiz.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.GameActivity;
import com.example.quiz.R;
import com.example.quiz.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginButton;
    TextView signupRedirect;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirect = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(view -> loginUser());

        signupRedirect.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Email не может быть пустым");
            return;
        }
        if (password.isEmpty()) {
            passwordField.setError("Пароль не может быть пустым");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                    intent.putExtra("email", user.getEmail());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Подтвердите email перед входом", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
