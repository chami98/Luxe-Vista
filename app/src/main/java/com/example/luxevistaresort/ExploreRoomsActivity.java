package com.example.luxevistaresort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ExploreRoomsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRooms;
    private FirebaseFirestore db;
    private Button btnBookSpa, btnBookDining, btnReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_rooms);

        // Initialize Views
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        btnBookSpa = findViewById(R.id.btnBookSpa);
        btnBookDining = findViewById(R.id.btnBookDining);
        btnReserve = findViewById(R.id.btnReserve);

        // Get the email and username passed from the previous activity
        String userName = getIntent().getStringExtra("USER_NAME");
        String email = getIntent().getStringExtra("USER_EMAIL");

        // Log to verify if the data is received correctly
        Log.d("ExploreRoomsActivity", "Received data: USER_EMAIL = " + email + ", USER_NAME = " + userName);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));

        // Set onClickListener for Spa Button
        btnBookSpa.setOnClickListener(v -> {
            Toast.makeText(ExploreRoomsActivity.this, "Spa reservation feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Set onClickListener for Dining Button
        btnBookDining.setOnClickListener(v -> {
            Toast.makeText(ExploreRoomsActivity.this, "Dining reservation feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Set onClickListener for Reserve Button
        btnReserve.setOnClickListener(v -> {
            Toast.makeText(ExploreRoomsActivity.this, "Reservation confirmed!", Toast.LENGTH_SHORT).show();
        });

        // Fetch room data from Firestore
        fetchRoomData(userName, email);
    }

    // Fetch room data from Firestore
    private void fetchRoomData(String userName, String email) {
        db.collection("rooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Handle Firestore data
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Convert Firestore documents into room objects
                            Room[] rooms = new Room[querySnapshot.size()];
                            int index = 0;

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String title = document.getString("title");
                                String description = document.getString("description");
                                String price = document.getString("price");
                                String amenities = document.getString("amenities");
                                String occupancy = document.getString("occupancy");
                                String policies = document.getString("policies");
                                String imageUrl = document.getString("imageUrl"); // Get the image URL from Firestore

                                // Create Room object and add to the rooms array
                                rooms[index] = new Room(title, description, price, imageUrl, amenities, occupancy, policies);
                                index++;
                            }

                            // Set the adapter with the fetched rooms and pass user details
                            RoomAdapter adapter = new RoomAdapter(rooms, email, userName);
                            recyclerViewRooms.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(ExploreRoomsActivity.this, "Error getting rooms: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Room class to represent room data
    public static class Room {
        String title;
        String description;
        String price;
        String imageUrl;  // Store the image URL here
        String amenities; // Amenities available in the room
        String occupancy; // Maximum occupancy
        String policies;  // Room policies

        // Constructor with imageUrl
        public Room(String title, String description, String price, String imageUrl, String amenities, String occupancy, String policies) {
            this.title = title;
            this.description = description;
            this.price = price;
            this.imageUrl = imageUrl;
            this.amenities = amenities;
            this.occupancy = occupancy;
            this.policies = policies;
        }
    }

    // RoomAdapter class for RecyclerView
    public static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
        private Room[] rooms;
        private String email;
        private String userName;

        public RoomAdapter(Room[] rooms, String email, String userName) {
            this.rooms = rooms;
            this.email = email;
            this.userName = userName;
        }

        @Override
        public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the item layout and create ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
            return new RoomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RoomViewHolder holder, int position) {
            Room room = rooms[position];
            holder.tvRoomTitle.setText(room.title);
            holder.tvRoomDescription.setText(room.description);
            holder.tvRoomPrice.setText(room.price);

            // Use Glide to load the image from the URL into the ImageView
            Glide.with(holder.ivRoomImage.getContext())
                    .load(room.imageUrl)  // Load the image from the URL
                    .into(holder.ivRoomImage);

            // Set click listener on the image
            holder.ivRoomImage.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), RoomDetailsActivity.class);
                intent.putExtra("title", room.title);
                intent.putExtra("description", room.description);
                intent.putExtra("price", room.price);
                intent.putExtra("imageUrl", room.imageUrl);  // Pass the image URL
                intent.putExtra("amenities", room.amenities);
                intent.putExtra("occupancy", room.occupancy);
                intent.putExtra("policies", room.policies);

                // Pass email and userName
                intent.putExtra("USER_EMAIL", email);
                intent.putExtra("USER_NAME", userName);
                Log.d("RoomAdapter", "Passing data to RoomDetailsActivity: USER_EMAIL = " + email + ", USER_NAME = " + userName);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return rooms.length;
        }

        // ViewHolder class for each room item
        public static class RoomViewHolder extends RecyclerView.ViewHolder {
            TextView tvRoomTitle, tvRoomDescription, tvRoomPrice;
            ImageView ivRoomImage;

            public RoomViewHolder(View itemView) {
                super(itemView);
                tvRoomTitle = itemView.findViewById(R.id.tvRoomTitle);
                tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
                tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
                ivRoomImage = itemView.findViewById(R.id.ivRoomImage);
            }
        }
    }
}
