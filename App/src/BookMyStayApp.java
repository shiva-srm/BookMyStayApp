import java.util.*;

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

// Inventory Service to maintain room availability
class RoomInventory {
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

    public int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Booking & Allocation Service
class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds; // To ensure uniqueness
    private Map<String, Set<String>> roomTypeToAllocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomTypeToAllocatedRooms = new HashMap<>();
    }

    public void processReservations(Queue<Reservation> bookingQueue) {
        while (!bookingQueue.isEmpty()) {
            Reservation r = bookingQueue.poll();
            String roomType = r.getRoomType();

            if (inventory.isAvailable(roomType)) {
                // Generate unique room ID
                String roomId = generateUniqueRoomId(roomType);
                allocatedRoomIds.add(roomId);

                roomTypeToAllocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

                // Update inventory
                inventory.decrement(roomType);

                System.out.println("Reservation Confirmed: " + r);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("Remaining " + roomType + " Rooms: " + inventory.getAvailable(roomType));
                System.out.println("--------------------------");
            } else {
                System.out.println("Reservation Failed (No Availability): " + r);
                System.out.println("--------------------------");
            }
        }
    }

    private String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + (100 + new Random().nextInt(900));
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC6: Reservation Confirmation & Room Allocation\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        // Initialize booking queue
        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single", 3));
        bookingQueue.add(new Reservation("Bob", "Double", 2));
        bookingQueue.add(new Reservation("Charlie", "Suite", 1));
        bookingQueue.add(new Reservation("Diana", "Single", 1));
        bookingQueue.add(new Reservation("Eve", "Single", 2)); // Should fail due to no availability

        // Process bookings
        BookingService bookingService = new BookingService(inventory);
        bookingService.processReservations(bookingQueue);
    }
}