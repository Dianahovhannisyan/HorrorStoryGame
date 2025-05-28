package com.example.noturningback.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.R;
import com.example.noturningback.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView loginTestUserText, signupRedirect;
    private FirebaseAuth mAuth;

    private boolean isPasswordVisible = false;
    private boolean loggingIn = false;

    private final String TEST_EMAIL = "individualproject2025@gmail.com";
    private final String TEST_PASSWORD = "Samsung2025";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean forceLogin = getIntent().getBooleanExtra("forceLogin", false);

        // ✅ Автоматический вход, если пользователь уже в системе
        if (currentUser != null && currentUser.isEmailVerified() && !forceLogin) {
            goToGame(currentUser);
            return;
        }

        setContentView(R.layout.activity_login);

        // Привязка элементов
        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginTestUserText = findViewById(R.id.login_test_user);
        signupRedirect = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(view -> loginUser());
        loginTestUserText.setOnClickListener(view -> loginAsTestUser());
        signupRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            intent.putExtra("forceLogin", true);
            startActivity(intent);
            finish();
        });

        setupPasswordVisibilityToggle();
    }

    private void setupPasswordVisibilityToggle() {
        passwordField.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (passwordField.getCompoundDrawables()[DRAWABLE_END] != null &&
                        event.getRawX() >= (passwordField.getRight() -
                                passwordField.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    isPasswordVisible = !isPasswordVisible;

                    passwordField.setInputType(isPasswordVisible
                            ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    passwordField.setSelection(passwordField.getText().length());

                    passwordField.setCompoundDrawablesWithIntrinsicBounds(
                            passwordField.getCompoundDrawables()[0], null,
                            getResources().getDrawable(
                                    isPasswordVisible ? R.drawable.visibility : R.drawable.visibility_off
                            ), null
                    );
                    return true;
                }
            }
            return false;
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Email не может быть пустым");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Некорректный email");
            return;
        }
        if (password.isEmpty()) {
            passwordField.setError("Пароль не может быть пустым");
            return;
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loggingIn) return;
        loggingIn = true;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loggingIn = false;
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show();
                            goToGame(user);
                        } else {
                            Toast.makeText(this, "Подтвердите email перед входом", Toast.LENGTH_LONG).show();
                            if (user != null) user.sendEmailVerification();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    loggingIn = false;
                    Toast.makeText(this, "Ошибка сети: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loginAsTestUser() {
        if (loggingIn) return;
        loggingIn = true;

        String enteredEmail = emailField.getText().toString().trim();
        String enteredPassword = passwordField.getText().toString().trim();

        if (!enteredEmail.equals(TEST_EMAIL) || !enteredPassword.equals(TEST_PASSWORD)) {
            Toast.makeText(this, "Введите правильные тестовые данные!", Toast.LENGTH_SHORT).show();
            loggingIn = false;
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
            loggingIn = false;
            return;
        }

        mAuth.signInWithEmailAndPassword(TEST_EMAIL, TEST_PASSWORD)
                .addOnCompleteListener(task -> {
                    loggingIn = false;
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Вход как тестовый пользователь", Toast.LENGTH_SHORT).show();
                            goToGame(user);
                        }
                    } else {
                        Toast.makeText(this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    loggingIn = false;
                    Toast.makeText(this, "Ошибка сети: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void goToGame(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, StartWindowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
