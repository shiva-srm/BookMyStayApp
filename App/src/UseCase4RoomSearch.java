import java.util.HashMap;
import java.util.Map;

// Search Service (READ-ONLY)
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : roomCatalog.keySet()) {

            int available = inventory.getAvailableCount(type);

            if (available > 0) {
                Room room = roomCatalog.get(type);

                if (room != null) {
                    System.out.println("Room Type: " + room.getType());
                    System.out.println("Available Count: " + available);
                    System.out.println("----------------------");
                }
            }
        }
    }
}

// Main Class
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", new SingleRoom());
        roomCatalog.put("Double", new DoubleRoom());
        roomCatalog.put("Suite", new SuiteRoom());

        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.searchAvailableRooms();
    }
}