import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }

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

// Serializable inventory
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Map<String, Integer> getInventoryMap() {
        return inventory;
    }
}

// Persistence service
class PersistenceService {
    private static final String FILE_NAME = "bookMyStayData.ser";

    public static void saveData(RoomInventory inventory, List<Reservation> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(bookings);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static Object[] loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            List<Reservation> bookings = (List<Reservation>) ois.readObject();
            System.out.println("System state loaded successfully.");
            return new Object[]{inventory, bookings};
        } catch (Exception e) {
            System.out.println("Failed to load system state: " + e.getMessage());
            return null;
        }
    }
}

// Booking service
class BookingService {
    private RoomInventory inventory;
    private List<Reservation> bookingHistory;

    public BookingService(RoomInventory inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public void addBooking(Reservation r) {
        String roomType = r.getRoomType();
        if (inventory.isAvailable(roomType)) {
            inventory.decrement(roomType);
            bookingHistory.add(r);
            System.out.println("Reservation confirmed: " + r);
        } else {
            System.out.println("Reservation failed (no availability): " + r);
        }
    }

    public void showBookingHistory() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
    }

    public RoomInventory getInventory() {
        return inventory;
    }

    public List<Reservation> getBookingHistory() {
        return bookingHistory;
    }
}

// Main Application
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC12: Data Persistence & System Recovery\n");

        // Load saved state if available
        Object[] savedData = PersistenceService.loadData();
        RoomInventory inventory;
        List<Reservation> bookingHistory;

        if (savedData != null) {
            inventory = (RoomInventory) savedData[0];
            bookingHistory = (List<Reservation>) savedData[1];
        } else {
            inventory = new RoomInventory();
            inventory.addRoom("Single", 2);
            inventory.addRoom("Double", 1);
            bookingHistory = new ArrayList<>();
        }

        BookingService bookingService = new BookingService(inventory, bookingHistory);

        // New reservations
        List<Reservation> newReservations = Arrays.asList(
                new Reservation("R001", "Alice", "Single", 2),
                new Reservation("R002", "Bob", "Double", 1),
                new Reservation("R003", "Charlie", "Single", 3)
        );

        for (Reservation r : newReservations) {
            bookingService.addBooking(r);
        }

        bookingService.showBookingHistory();

        // Save state for future recovery
        PersistenceService.saveData(bookingService.getInventory(), bookingService.getBookingHistory());
    }
}