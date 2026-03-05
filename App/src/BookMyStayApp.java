import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup demonstrates centralized inventory management
 * using a HashMap to ensure a single source of truth for room availability.
 * * @author Developer
 * @version 3.0
 */

// 1. Room Domain Model (From Use Case 2)
abstract class Room {
    private String type;
    public Room(String type) { this.type = type; }
    public String getType() { return type; }
}

class SingleRoom extends Room { public SingleRoom() { super("Single"); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double"); } }
class SuiteRoom  extends Room { public SuiteRoom() { super("Suite"); } }

// 2. Centralized Inventory Manager
class RoomInventory {
    // Encapsulated Data Structure: Mapping Room Type -> Available Count
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    // Adds or updates room counts in the Map
    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Retrieves availability with O(1) complexity
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Displays the current state of the entire inventory
    public void displayInventory() {
        System.out.println("\n--- Current Inventory Status ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
        System.out.println("--------------------------------");
    }
}

// 3. Application Entry Point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Book My Stay App - Version 3.0");

        // Initialize the Centralized Inventory
        RoomInventory hotelInventory = new RoomInventory();

        // Registering Room Types and their initial counts
        hotelInventory.addRoomType("Single", 10);
        hotelInventory.addRoomType("Double", 5);
        hotelInventory.addRoomType("Suite", 2);

        // Displaying Initial State
        hotelInventory.displayInventory();

        // Demonstrating a controlled update (Simulating a booking or restock)
        System.out.println("Updating Suite availability...");
        hotelInventory.addRoomType("Suite", 1);

        // Final Check
        hotelInventory.displayInventory();
    }
}