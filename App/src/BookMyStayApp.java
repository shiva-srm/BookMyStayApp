import java.util.*;

// Custom exception for invalid booking or cancellation
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) throws InvalidBookingException {
        int current = inventory.getOrDefault(type, -1);
        if (current <= 0) {
            throw new InvalidBookingException("Cannot decrement inventory. No available rooms for type: " + type);
        }
        inventory.put(type, current - 1);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// Booking & allocation service with cancellation
class BookingService {
    private RoomInventory inventory;
    private Map<String, String> reservationToRoomId; // reservationId -> roomId
    private Set<String> allocatedRoomIds;
    private Stack<String> releasedRoomIds; // Stack for rollback

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservationToRoomId = new HashMap<>();
        this.allocatedRoomIds = new HashSet<>();
        this.releasedRoomIds = new Stack<>();
    }

    // Confirm booking
    public void confirmReservation(Reservation r) {
        try {
            validateReservation(r);

            String roomType = r.getRoomType();
            String roomId = generateUniqueRoomId(roomType);

            allocatedRoomIds.add(roomId);
            reservationToRoomId.put(r.getReservationId(), roomId);

            inventory.decrement(roomType);

            System.out.println("Reservation Confirmed: " + r);
            System.out.println("Assigned Room ID: " + roomId);
            System.out.println("Remaining " + roomType + " Rooms: " + inventory.getAvailable(roomType));
            System.out.println("--------------------------");

        } catch (InvalidBookingException e) {
            System.out.println("Reservation Failed: " + r);
            System.out.println("Reason: " + e.getMessage());
            System.out.println("--------------------------");
        }
    }

    // Cancel booking
    public void cancelReservation(Reservation r) {
        String reservationId = r.getReservationId();
        if (!reservationToRoomId.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: " + r);
            System.out.println("Reason: Reservation does not exist or already cancelled.");
            System.out.println("--------------------------");
            return;
        }

        String roomId = reservationToRoomId.remove(reservationId);
        allocatedRoomIds.remove(roomId);
        releasedRoomIds.push(roomId);

        inventory.increment(r.getRoomType());

        System.out.println("Reservation Cancelled: " + r);
        System.out.println("Released Room ID: " + roomId);
        System.out.println("Updated " + r.getRoomType() + " Rooms: " + inventory.getAvailable(r.getRoomType()));
        System.out.println("--------------------------");
    }

    private void validateReservation(Reservation r) throws InvalidBookingException {
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }
        if (r.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be positive.");
        }
        if (!inventory.isAvailable(r.getRoomType())) {
            throw new InvalidBookingException("No rooms available for type: " + r.getRoomType());
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
        System.out.println("UC10: Booking Cancellation & Inventory Rollback\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        // Initialize booking service
        BookingService service = new BookingService(inventory);

        // Sample bookings
        Reservation r1 = new Reservation("R001", "Alice", "Single", 3);
        Reservation r2 = new Reservation("R002", "Bob", "Double", 2);
        Reservation r3 = new Reservation("R003", "Charlie", "Single", 1);

        // Confirm bookings
        service.confirmReservation(r1);
        service.confirmReservation(r2);
        service.confirmReservation(r3); // Should fail if no availability

        // Cancel a booking
        service.cancelReservation(r1);

        // Attempt to cancel non-existent booking
        service.cancelReservation(r3);
    }
}