package responses;

public class BookingDates {
    public String checkin;
    public String checkout;

    public BookingDates(String checkIn, String checkOut) {
        this.checkin = checkIn;
        this.checkout = checkOut;
    }
}
