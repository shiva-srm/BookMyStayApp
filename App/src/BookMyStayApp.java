import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
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
                "Guest='" + guestName + '\'' +
                ", RoomType='" + roomType + '\'' +
                ", Nights=" + nights +
                '}';
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC5: Booking Request Queue (First-Come-First-Served)\n");

        // Step 1: Create a queue for booking requests
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Step 2: Guests submit booking requests
        bookingQueue.add(new Reservation("Alice", "Single", 3));
        bookingQueue.add(new Reservation("Bob", "Double", 2));
        bookingQueue.add(new Reservation("Charlie", "Suite", 5));
        bookingQueue.add(new Reservation("Diana", "Single", 1));

        // Step 3: Display booking requests in arrival order
        System.out.println("Booking Requests in Queue (Arrival Order):");
        for (Reservation r : bookingQueue) {
            System.out.println(r);
        }

        // Step 4: Process requests in FIFO order
        System.out.println("\nProcessing Booking Requests:");
        while (!bookingQueue.isEmpty()) {
            Reservation nextRequest = bookingQueue.poll(); // Retrieves and removes head
            System.out.println("Processing: " + nextRequest);
        }

        System.out.println("\nAll booking requests have been processed.");
    }
}