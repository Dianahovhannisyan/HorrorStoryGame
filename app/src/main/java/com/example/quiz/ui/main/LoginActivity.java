package com.example.quiz.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginButton;
    TextView signupRedirect;

    FirebaseAuth mAuth;

    boolean isPasswordVisible = false; // флаг видимости пароля

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

        setupPasswordVisibilityToggle();
    }

    private void setupPasswordVisibilityToggle() {
        passwordField.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordField.getRight()
                        - passwordField.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

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
        if (password.isEmpty()) {
            passwordField.setError("Пароль не может быть пустым");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, StartWindowActivity.class);
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
