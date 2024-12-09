package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private BookingsAdapter bookingsAdapter;
    private ArrayList<Booking> bookingsList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and data list
        rvBookings = findViewById(R.id.rvBookings);
        bookingsList = new ArrayList<>();
        bookingsAdapter = new BookingsAdapter(bookingsList);

        // Set up RecyclerView
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setAdapter(bookingsAdapter);

        // Fetch bookings data from Firestore
        fetchBookings();

        // Set up the logout button
        Button logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(v -> logout());
    }

    private void fetchBookings() {
        db.collection("bookings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Create booking objects and add them to the list
                            String roomTitle = document.getString("roomTitle");
                            String bookingDate = document.getString("bookingDate");
                            int roomQuantity = document.getLong("roomQuantity").intValue();
                            String userName = document.getString("userName");
                            String email = document.getString("email");

                            Booking booking = new Booking(roomTitle, bookingDate, roomQuantity, userName, email);
                            bookingsList.add(booking);
                        }

                        // Notify the adapter that data has changed
                        bookingsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminActivity.this, "Error fetching bookings: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logout() {
        // Log out the user from Firebase Authentication
        mAuth.signOut();

        // Show a Toast message confirming the logout
        Toast.makeText(AdminActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to the LoginActivity
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
