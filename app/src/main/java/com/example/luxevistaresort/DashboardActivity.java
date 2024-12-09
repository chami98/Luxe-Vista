package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DashboardActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private Button contactButton, exploreRoomsButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);  // Set the layout for DashboardActivity

        ImageView welcomeGifImageView = findViewById(R.id.welcomeGifImageView);

        // Load the GIF using Glide
        Glide.with(this)
                .load(R.drawable.welcome)  // Replace with your actual GIF resource
                .into(welcomeGifImageView);  // Set the ImageView

        // Initialize the TextView
        userNameTextView = findViewById(R.id.userNameTextView);

        // Get the user name from the Intent
        String userName = getIntent().getStringExtra("USER_NAME");
        String email = getIntent().getStringExtra("USER_EMAIL");

        // Display the user's name (or email if name is null)
        if (userName != null) {
            userNameTextView.setText("Hi, " + userName);
        } else {
            userNameTextView.setText("Hi, User");
        }

        // Initialize buttons
        contactButton = findViewById(R.id.contactButton);
        exploreRoomsButton = findViewById(R.id.exploreRoomsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Handle Contact Us button click
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ContactusActivity.class);
            startActivity(intent);
        });

        // Handle Explore Rooms button click
        exploreRoomsButton.setOnClickListener(v -> {
            Log.d("DashboardActivity", "Passing data to ExploreRoomsActivity: USER_EMAIL = " + email + ", USER_NAME = " + userName);

            // Create the intent and pass the data
            Intent intent = new Intent(DashboardActivity.this, ExploreRoomsActivity.class);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_NAME", userName);

            startActivity(intent);
        });

        // Handle Logout button click
        logoutButton.setOnClickListener(v -> {
            // Clear any session data here if needed (e.g., Firebase Auth or SharedPreferences)
            // For example, FirebaseAuth.getInstance().signOut();

            // Redirect to LoginActivity
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Finish the current activity so the user can't return to it
        });
    }
}
