package com.example.luxevistaresort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// RoomAdapter class for RecyclerView
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
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
        holder.ivRoomImage.setImageResource(room.imageResId); // Set the room image
    }

    @Override
    public int getItemCount() {
        return rooms.length;
    }

    // ViewHolder class for each room item
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomTitle, tvRoomDescription, tvRoomPrice;
        ImageView ivRoomImage; // Add ImageView for room image

        public RoomViewHolder(View itemView) {
            super(itemView);
            tvRoomTitle = itemView.findViewById(R.id.tvRoomTitle);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            ivRoomImage = itemView.findViewById(R.id.ivRoomImage); // Initialize ImageView
        }
    }


    // Room class representing the model
    public static class Room {
        private final String title;
        private final String description;
        private final String price;
        private final int imageResId;

        public Room(String title, String description, String price, int imageResId) {
            this.title = title;
            this.description = description;
            this.price = price;
            this.imageResId = imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPrice() {
            return price;
        }

        public int getImageResId() {
            return imageResId;
        }
    }
}
