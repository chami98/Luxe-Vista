package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DashboardActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private Button contactButton, exploreRoomsButton;

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

        // Display the user's name (or email if name is null)
        if (userName != null) {
            userNameTextView.setText("Hi, " + userName);
        } else {
            userNameTextView.setText("Hi, User");
        }

        // Initialize the Contact Us button
        contactButton = findViewById(R.id.contactButton);
        exploreRoomsButton = findViewById(R.id.exploreRoomsButton);

        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ContactusActivity.class);
            startActivity(intent);
        });

        exploreRoomsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ExploreRoomsActivity.class);
            startActivity(intent);
        });
    }

}
