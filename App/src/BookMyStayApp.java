
/**
 * UseCase2RoomInitialization demonstrates object modeling using inheritance,
 * abstraction, and encapsulation.
 * * @author Developer
 * @version 2.0
 */

// 1. Abstract Class - Defines the blueprint for all rooms
abstract class Room {
    private String type;
    private double pricePerNight;

    public Room(String type, double pricePerNight) {
        this.type = type;
        this.pricePerNight = pricePerNight;
    }

    public String getType() { return type; }
    public double getPricePerNight() { return pricePerNight; }

    // Abstract method to be implemented by concrete classes
    public abstract void displayFeatures();
}

// 2. Concrete Classes - Specialized room types
class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 100.0); }
    @Override
    public void displayFeatures() {
        System.out.println("Features: 1 Single Bed, High-speed Wi-Fi, Desk.");
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 180.0); }
    @Override
    public void displayFeatures() {
        System.out.println("Features: 1 Queen Bed, Mini-fridge, City View.");
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 350.0); }
    @Override
    public void displayFeatures() {
        System.out.println("Features: King Bed, Private Lounge, Luxury Tub.");
    }
}

// 3. Application Entry Point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("--- Hotel Booking System v2.0 ---");

        // Initializing Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability Variables (Simple state management)
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 1;

        // Displaying Room Details
        displayRoomInfo(single, singleAvailability);
        displayRoomInfo(dbl, doubleAvailability);
        displayRoomInfo(suite, suiteAvailability);
    }

    private static void displayRoomInfo(Room room, int count) {
        System.out.println("\nRoom Type: " + room.getType());
        System.out.println("Price: $" + room.getPricePerNight());
        room.displayFeatures();
        System.out.println("Available Units: " + count);
        System.out.println("---------------------------------");
    }
}