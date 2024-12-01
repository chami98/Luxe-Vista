package com.example.luxevistaresort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ExploreRoomsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRooms;
    private Spinner spinnerRoomType, spinnerPriceRange;
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

        // Setup RecyclerView
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));

        // Set onClickListener for Spa Button
        btnBookSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExploreRoomsActivity.this, "Spa reservation feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for Dining Button
        btnBookDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExploreRoomsActivity.this, "Dining reservation feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for Reserve Button
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to finalize reservation
                Toast.makeText(ExploreRoomsActivity.this, "Reservation confirmed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Placeholder logic to populate RecyclerView with rooms
        populateRoomList();
    }

    // Method to populate the RecyclerView with rooms (This can be enhanced with actual data)
    private void populateRoomList() {
        // Create a list of rooms (Placeholder data)
        // You can replace this with actual data coming from a server or local database

        Room[] rooms = {
                new Room("Ocean View Suite",
                        "Ocean view with a king-size bed, perfect for a luxurious stay in Tissamaharama. Experience breathtaking views of the Indian Ocean and the surrounding nature.",
                        "$350 per night / LKR 129,500 per night",R.drawable.ocean_view),
                new Room("Deluxe Room",
                        "Luxury room with a queen-size bed, featuring modern amenities and a view of the picturesque Tissamaharama lake and its wildlife.",
                        "$250 per night / LKR 92,500 per night", R.drawable.deluxe),
                new Room("Garden View",
                        "Room with a serene garden view and twin beds, ideal for guests looking to unwind amidst the lush greenery and natural beauty of Sri Lanka.",
                        "$180 per night / LKR 66,600 per night", R.drawable.garden_view)
        };

        RoomAdapter adapter = new RoomAdapter(rooms);
        recyclerViewRooms.setAdapter(adapter);
    }

    // Room class to represent room data
    // Room class to represent room data
    public static class Room {
        String title;
        String description;
        String price;
        int imageResId; // Drawable resource ID for the room image

        public Room(String title, String description, String price, int imageResId) {
            this.title = title;
            this.description = description;
            this.price = price;
            this.imageResId = imageResId;
        }
    }


    // RoomAdapter class for RecyclerView
    public static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
        private Room[] rooms;

        public RoomAdapter(Room[] rooms) {
            this.rooms = rooms;
        }

        @Override
        public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the item layout and create ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
            return new RoomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RoomViewHolder holder, int position) {
            // Set room details in the ViewHolder
            Room room = rooms[position];
            holder.tvRoomTitle.setText(room.title);
            holder.tvRoomDescription.setText(room.description);
            holder.tvRoomPrice.setText(room.price);
            holder.ivRoomImage.setImageResource(room.imageResId);

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