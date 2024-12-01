package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
        int imageResId = intent.getIntExtra("imageResId", 0);

        // Set data to views
        tvRoomTitle.setText(title);
        tvRoomDescription.setText(description);
        tvRoomPrice.setText(price);
        tvRoomAmenities.setText(amenities);
        tvRoomOccupancy.setText(occupancy);
        tvRoomPolicies.setText(policies);
        ivRoomImage.setImageResource(imageResId);

        // Set booking button click listener
        btnBookNow.setOnClickListener(v -> {
            // Implement booking logic
        });
    }
}
