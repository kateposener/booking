package responses;

public class GetHotelBookingResponse {
    public String firstname;
    public String lastname;
    public String totalprice;
    public String depositpaid;
    public BookingDates bookingdates;

    public GetHotelBookingResponse(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.totalprice = totalPrice;
        this.depositpaid = depositPaid;
        this.bookingdates = new BookingDates(checkIn, checkOut);
    }
}
