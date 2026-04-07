import java.util.*;
import java.util.concurrent.*;

// Reservation class representing a guest booking request
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

// Room inventory management
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public synchronized void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public synchronized boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public synchronized void decrement(String type) throws Exception {
        int current = inventory.getOrDefault(type, -1);
        if (current <= 0) throw new Exception("No available rooms for type: " + type);
        inventory.put(type, current - 1);
    }

    public synchronized void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public synchronized int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public synchronized boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// Thread-safe booking service
class BookingService {
    private RoomInventory inventory;
    private Map<String, String> reservationToRoomId;
    private Set<String> allocatedRoomIds;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservationToRoomId = new ConcurrentHashMap<>();
        this.allocatedRoomIds = Collections.synchronizedSet(new HashSet<>());
    }

    // Thread-safe reservation processing
    public void processReservation(Reservation r) {
        synchronized (this) { // critical section
            try {
                validateReservation(r);

                String roomType = r.getRoomType();
                String roomId = generateUniqueRoomId(roomType);

                allocatedRoomIds.add(roomId);
                reservationToRoomId.put(r.getReservationId(), roomId);

                inventory.decrement(roomType);

                System.out.println(Thread.currentThread().getName() + " -> Confirmed: " + r);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("Remaining " + roomType + " Rooms: " + inventory.getAvailable(roomType));
                System.out.println("--------------------------");

            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " -> Failed: " + r);
                System.out.println("Reason: " + e.getMessage());
                System.out.println("--------------------------");
            }
        }
    }

    private void validateReservation(Reservation r) throws Exception {
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty())
            throw new Exception("Guest name cannot be empty.");
        if (!inventory.isValidRoomType(r.getRoomType()))
            throw new Exception("Invalid room type: " + r.getRoomType());
        if (r.getNights() <= 0)
            throw new Exception("Number of nights must be positive.");
        if (!inventory.isAvailable(r.getRoomType()))
            throw new Exception("No rooms available for type: " + r.getRoomType());
    }

    private String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + (100 + new Random().nextInt(900));
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

// Main application for concurrent booking simulation
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC11: Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        BookingService service = new BookingService(inventory);

        // Shared booking requests
        List<Reservation> reservations = Arrays.asList(
                new Reservation("R001", "Alice", "Single", 3),
                new Reservation("R002", "Bob", "Double", 2),
                new Reservation("R003", "Charlie", "Single", 1),
                new Reservation("R004", "Diana", "Single", 2)
        );

        // Create threads for concurrent booking
        List<Thread> threads = new ArrayList<>();
        for (Reservation r : reservations) {
            Thread t = new Thread(() -> service.processReservation(r));
            t.setName("GuestThread-" + r.getGuestName());
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        System.out.println("=== All bookings processed ===");
    }
}