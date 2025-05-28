package com.example.noturningback;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noturningback.ui.main.HelperClass;
import com.example.noturningback.ui.main.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText confirmPasswordField, emailField, usernameField, passwordField;
    Button signupButton;
    FirebaseAuth mAuth;
    DatabaseReference database;

    boolean isPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        confirmPasswordField = findViewById(R.id.signup_confirm_password);
        emailField = findViewById(R.id.signup_email);
        usernameField = findViewById(R.id.signup_username);
        passwordField = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> registerUser());

        TextView loginRedirectText = findViewById(R.id.loginRedirectText);
        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.putExtra("forceLogin", true); // чтобы LoginActivity не запускал auto-login
            startActivity(intent);
            finish();
        });

        setupPasswordVisibilityToggle(passwordField, true);
        setupPasswordVisibilityToggle(confirmPasswordField, false);
    }

    private void setupPasswordVisibilityToggle(EditText editText, boolean isMainPasswordField) {
        editText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight()
                        - editText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    boolean isVisible = isMainPasswordField ? isPasswordVisible : isConfirmPasswordVisible;
                    isVisible = !isVisible;

                    editText.setInputType(isVisible
                            ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    editText.setSelection(editText.getText().length());

                    editText.setCompoundDrawablesWithIntrinsicBounds(
                            editText.getCompoundDrawables()[0], null,
                            getResources().getDrawable(isVisible ? R.drawable.visibility : R.drawable.visibility_off),
                            null
                    );

                    if (isMainPasswordField) {
                        isPasswordVisible = isVisible;
                    } else {
                        isConfirmPasswordVisible = isVisible;
                    }

                    return true;
                }
            }
            return false;
        });
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                        if (verifyTask.isSuccessful()) {
                            HelperClass helper = new HelperClass(email, username);
                            database.child(user.getUid()).setValue(helper);
                            Toast.makeText(this, "Подтвердите email. Проверьте почту", Toast.LENGTH_LONG).show();
                            mAuth.signOut(); // Обязательно разлогинить
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.putExtra("forceLogin", true);
                            startActivity(intent);
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
