package com.example.luxevistaresort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookingsList;

    // Constructor
    public BookingsAdapter(List<Booking> bookingsList) {
        this.bookingsList = bookingsList;
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        Booking booking = bookingsList.get(position);
        holder.tvRoomTitle.setText("Package: " + booking.getRoomTitle());
        holder.tvBookingDate.setText("Booking Date: " + booking.getBookingDate());
        holder.tvRoomQuantity.setText("Room Count: " + booking.getRoomQuantity());
        holder.tvUserName.setText("Visitor Name: " + booking.getUserName());
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView tvRoomTitle, tvBookingDate, tvRoomQuantity, tvUserName;

        public BookingViewHolder(View itemView) {
            super(itemView);
            tvRoomTitle = itemView.findViewById(R.id.tvRoomTitle);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvRoomQuantity = itemView.findViewById(R.id.tvRoomQuantity);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}
