package com.example.luxevistaresort;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link XML elements
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Register button click listener
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Please confirm your password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase authentication for registration
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Optionally, set the user's display name (full name)
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullName)
                                            .build())
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            // Successfully updated name
                                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                            // Add user details to Firestore
                                            addUserToFirestore(user);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Name update failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Registration failure
                        Log.w("RegisterError", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToFirestore(FirebaseUser user) {
        String userId = user.getUid();
        String email = user.getEmail();
        String fullName = user.getDisplayName();

        // Assuming by default new users are not admins
        boolean isAdmin = false;  // Set this value as required, for example, if you're adding admin manually

        // Create a new user object to add to Firestore
        User userObj = new User(fullName, email, isAdmin);

        // Add user data to Firestore
        db.collection("users").document(email)  // Use email as the document ID
                .set(userObj)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User added to Firestore successfully");
                    finish();  // Close RegisterActivity and return to MainActivity
                })
                .addOnFailureListener(e -> {
                    Log.w("FirestoreError", "Error adding user to Firestore", e);
                    Toast.makeText(RegisterActivity.this, "Error saving user data to Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    // User model class
    public class User {
        private String name;
        private String email;
        private boolean isAdmin;

        public User(String name, String email, boolean isAdmin) {
            this.name = name;
            this.email = email;
            this.isAdmin = isAdmin;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public boolean isAdmin() {
            return isAdmin;
        }
    }
}
