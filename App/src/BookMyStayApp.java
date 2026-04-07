import java.util.*;

// Reservation class representing a confirmed booking
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "ID='" + reservationId + '\'' +
                ", Guest='" + guestName + '\'' +
                ", RoomType='" + roomType + '\'' +
                ", Nights=" + nights +
                '}';
    }
}

// Booking history manager
class BookingHistory {
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // Add a confirmed reservation to history
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    // Retrieve all reservations in insertion order
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedReservations);
    }

    // Generate a summary report
    public void generateReport() {
        System.out.println("=== Booking History Report ===");
        if (confirmedReservations.isEmpty()) {
            System.out.println("No bookings have been confirmed yet.");
            return;
        }
        Map<String, Integer> roomTypeCount = new HashMap<>();
        for (Reservation r : confirmedReservations) {
            roomTypeCount.put(r.getRoomType(), roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1);
        }
        for (Reservation r : confirmedReservations) {
            System.out.println(r);
        }
        System.out.println("--- Summary ---");
        roomTypeCount.forEach((type, count) ->
                System.out.println("Total " + type + " rooms booked: " + count));
        System.out.println("----------------");
    }
}

// Main application
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC8: Booking History & Reporting\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Sample confirmed reservations
        Reservation res1 = new Reservation("R001", "Alice", "Single", 3);
        Reservation res2 = new Reservation("R002", "Bob", "Double", 2);
        Reservation res3 = new Reservation("R003", "Charlie", "Suite", 1);

        // Add reservations to history
        history.addReservation(res1);
        history.addReservation(res2);
        history.addReservation(res3);

        // Retrieve and display all reservations
        List<Reservation> allReservations = history.getAllReservations();
        System.out.println("All confirmed reservations in chronological order:");
        for (Reservation r : allReservations) {
            System.out.println(r);
        }
        System.out.println();

        // Generate a summary report
        history.generateReport();
    }
}