package com.example.luxevistaresort;

public class Booking {
    private String roomTitle;
    private String bookingDate;
    private int roomQuantity;
    private String userName;
    private String email;

    // Constructor
    public Booking(String roomTitle, String bookingDate, int roomQuantity, String userName, String email) {
        this.roomTitle = roomTitle;
        this.bookingDate = bookingDate;
        this.roomQuantity = roomQuantity;
        this.userName = userName;
        this.email = email;
    }

    // Getters
    public String getRoomTitle() {
        return roomTitle;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
