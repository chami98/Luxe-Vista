package com.example.luxevistaresort;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RoomDetailsActivity extends AppCompatActivity {
    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

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
        String imageUrl = intent.getStringExtra("imageUrl");
        String userName = getIntent().getStringExtra("USER_NAME");
        String email = getIntent().getStringExtra("USER_EMAIL");

        // Set data to views
        tvRoomTitle.setText(title);
        tvRoomDescription.setText(description);
        tvRoomPrice.setText(price);
        tvRoomAmenities.setText(amenities);
        tvRoomOccupancy.setText(occupancy);
        tvRoomPolicies.setText(policies);

        // Load image using Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.login)
                .error(R.drawable.login)
                .into(ivRoomImage);

        // Set booking button click listener
        btnBookNow.setOnClickListener(v -> {
            // First, check if the user already has a booking for the same room
            checkExistingBooking(email, title, userName);
        });
    }

    private void checkExistingBooking(String email, String roomTitle, String userName) {
        db.collection("bookings")
                .whereEqualTo("email", email)
                .whereEqualTo("roomTitle", roomTitle)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // If the user has already booked this room, retrieve the existing booking data
                        showExistingBookingDialog(task.getResult().getDocuments().get(0).getId()); // Pass the document ID for updating
                    } else {
                        // If no existing booking, proceed with new booking
                        showDatePickerDialog(email, roomTitle, userName, null); // Pass null for existing booking
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RoomDetailsActivity.this, "Error checking bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showExistingBookingDialog(String bookingId) {
        new AlertDialog.Builder(RoomDetailsActivity.this)
                .setTitle("Existing Booking Found")
                .setMessage("You have already booked this room. Would you like to update your booking or proceed with a new one?")
                .setPositiveButton("Update Booking", (dialog, which) -> {
                    // Fetch the existing booking details from Firestore and show them in the UI for updating
                    fetchExistingBookingDetails(bookingId);
                })
                .setNegativeButton("Proceed with New Booking", (dialog, which) -> {
                    // Proceed with new booking as usual
                    showDatePickerDialog(getIntent().getStringExtra("USER_EMAIL"), getIntent().getStringExtra("title"), getIntent().getStringExtra("USER_NAME"), bookingId);
                })
                .setCancelable(false)
                .show();
    }

    private void fetchExistingBookingDetails(String bookingId) {
        db.collection("bookings").document(bookingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String existingDate = documentSnapshot.getString("bookingDate");
                        int existingQuantity = documentSnapshot.getLong("roomQuantity").intValue();

                        // Show existing booking details in dialog or UI for updating
                        showUpdateBookingDialog(existingDate, existingQuantity, bookingId);
                    } else {
                        Toast.makeText(RoomDetailsActivity.this, "Booking not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RoomDetailsActivity.this, "Error fetching booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showUpdateBookingDialog(String existingDate, int existingQuantity, String bookingId) {
        View updateBookingView = getLayoutInflater().inflate(R.layout.dialog_room_quantity_picker, null);
        NumberPicker numberPicker = updateBookingView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(existingQuantity);

        // Show Date Picker Dialog to select new date
        DatePickerDialog datePickerDialog = new DatePickerDialog(RoomDetailsActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

            new AlertDialog.Builder(RoomDetailsActivity.this)
                    .setTitle("Update Booking")
                    .setView(updateBookingView)
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        int selectedQuantity = numberPicker.getValue();

                        // Prepare updated booking details
                        Map<String, Object> updatedBooking = new HashMap<>();
                        updatedBooking.put("roomTitle", getIntent().getStringExtra("title"));
                        updatedBooking.put("roomDescription", getIntent().getStringExtra("description"));
                        updatedBooking.put("roomPrice", getIntent().getStringExtra("price"));
                        updatedBooking.put("bookingDate", selectedDate);
                        updatedBooking.put("roomQuantity", selectedQuantity);
                        updatedBooking.put("email", getIntent().getStringExtra("USER_EMAIL"));
                        updatedBooking.put("userName", getIntent().getStringExtra("USER_NAME"));

                        // Update booking in Firestore
                        db.collection("bookings").document(bookingId)
                                .set(updatedBooking)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RoomDetailsActivity.this, "Booking updated successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RoomDetailsActivity.this, "Error updating booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDatePickerDialog(String email, String roomTitle, String userName, String bookingId) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RoomDetailsActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

            // Show Quantity Picker Dialog
            View quantityPickerView = getLayoutInflater().inflate(R.layout.dialog_room_quantity_picker, null);
            NumberPicker numberPicker = quantityPickerView.findViewById(R.id.numberPicker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(10);
            numberPicker.setValue(1);

            new AlertDialog.Builder(RoomDetailsActivity.this)
                    .setTitle("Select Room Quantity")
                    .setView(quantityPickerView)
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        int selectedQuantity = numberPicker.getValue();

                        // Prepare booking details
                        Map<String, Object> booking = new HashMap<>();
                        booking.put("roomTitle", roomTitle);
                        booking.put("roomDescription", getIntent().getStringExtra("description"));
                        booking.put("roomPrice", getIntent().getStringExtra("price"));
                        booking.put("bookingDate", selectedDate);
                        booking.put("roomQuantity", selectedQuantity);
                        booking.put("email", email);
                        booking.put("userName", userName);

                        // Show loading spinner
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.VISIBLE);

                        // Save booking to Firestore
                        db.collection("bookings")
                                .add(booking)
                                .addOnSuccessListener(documentReference -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RoomDetailsActivity.this,
                                            "Booking successful! Booking ID: " + documentReference.getId(),
                                            Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RoomDetailsActivity.this,
                                            "Booking failed: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        }, year, month, day);

        datePickerDialog.show();
    }
}
