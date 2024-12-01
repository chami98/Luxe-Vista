package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RoomDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        // Get views
        TextView tvRoomTitle = findViewById(R.id.tvRoomTitle);
        TextView tvRoomDescription = findViewById(R.id.tvRoomDescription);
        TextView tvRoomPrice = findViewById(R.id.tvRoomPrice);
        TextView tvRoomAmenities = findViewById(R.id.tvRoomAmenities);
        TextView tvRoomOccupancy = findViewById(R.id.tvRoomOccupancy);
        TextView tvRoomPolicies = findViewById(R.id.tvRoomPolicies);
        ImageView ivRoomImage = findViewById(R.id.ivRoomImage);
        Button btnBookNow = findViewById(R.id.btnBookNow);

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");
        String amenities = intent.getStringExtra("amenities");
        String occupancy = intent.getStringExtra("occupancy");
        String policies = intent.getStringExtra("policies");
        String imageUrl = intent.getStringExtra("imageUrl"); // This should be the image URL

        // Set data to views
        tvRoomTitle.setText(title);
        tvRoomDescription.setText(description);
        tvRoomPrice.setText(price);
        tvRoomAmenities.setText(amenities);
        tvRoomOccupancy.setText(occupancy);
        tvRoomPolicies.setText(policies);

        // Load image from URL using Glide
        Glide.with(this)
                .load(imageUrl)  // The image URL passed from the intent
                .placeholder(R.drawable.login)  // Placeholder image while loading
                .error(R.drawable.login)  // Error image if the image fails to load
                .into(ivRoomImage); // Set the image to the ImageView

        // Set booking button click listener
        btnBookNow.setOnClickListener(v -> {
            // Get the ProgressBar reference
            ProgressBar progressBar = findViewById(R.id.progressBar);

            // Show the loader (ProgressBar)
            progressBar.setVisibility(View.VISIBLE);

            // Delay for 1000ms (1 second) to simulate the booking process
            new android.os.Handler().postDelayed(() -> {
                // Hide the loader (ProgressBar) after the process is complete
                progressBar.setVisibility(View.GONE);

            }, 1000);

            new android.os.Handler().postDelayed(() -> {
                // Hide the loader (ProgressBar) after the process is complete
                progressBar.setVisibility(View.GONE);

                // Show a success toast message
                Toast.makeText(RoomDetailsActivity.this, "Booking completed successfully!", Toast.LENGTH_SHORT).show();

            }, 1500);
        });
    }
}
