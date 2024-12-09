package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView forgotPasswordTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        ImageView loginGifImageView = findViewById(R.id.loginGifImageView);

        // Load the GIF using Glide
        Glide.with(this)
                .load(R.drawable.login)  // Replace with your actual GIF resource
                .into(loginGifImageView);  // Set the ImageView

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link XML elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Login button click listener
        loginButton.setOnClickListener(v -> loginUser());

        // Register button click listener
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Forgot password click listener
        forgotPasswordTextView.setOnClickListener(v -> {
            // Logic for password reset can be added here
            Toast.makeText(MainActivity.this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase authentication for login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String displayName = user.getDisplayName();
                        Toast.makeText(MainActivity.this, "Welcome, " + displayName, Toast.LENGTH_SHORT).show();

                        // Check if the user is an admin
                        checkIfAdmin(user.getEmail(), displayName, email);
                    } else {
                        // Sign-in failure
                        Log.w("LoginError", "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfAdmin(String email, String displayName, String userEmail) {
        // Assuming you have a collection for users and a field `isAdmin` that indicates if the user is an admin
        DocumentReference userRef = db.collection("users").document(userEmail);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean isAdmin = documentSnapshot.getBoolean("admin");
                if (isAdmin != null && isAdmin) {
                    // Redirect to AdminActivity if the user is an admin
                    Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                    adminIntent.putExtra("USER_NAME", displayName);
                    adminIntent.putExtra("USER_EMAIL", userEmail);
                    startActivity(adminIntent);
                    finish();  // Close MainActivity
                } else {
                    // Redirect to DashboardActivity if the user is a regular user
                    Intent userIntent = new Intent(MainActivity.this, DashboardActivity.class);
                    userIntent.putExtra("USER_NAME", displayName);
                    userIntent.putExtra("USER_EMAIL", userEmail);
                    startActivity(userIntent);
                    finish();  // Close MainActivity
                }
            } else {
                Toast.makeText(MainActivity.this, "User not found in database.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Error checking admin status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
