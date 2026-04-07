import java.util.*;

// Service class representing an optional add-on service
class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + price + ")";
    }
}

// Reservation class (existing booking entity)
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

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<Service>> reservationToServices;

    public AddOnServiceManager() {
        reservationToServices = new HashMap<>();
    }

    // Attach a service to a reservation
    public void addServiceToReservation(String reservationId, Service service) {
        reservationToServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    // Get all services for a reservation
    public List<Service> getServicesForReservation(String reservationId) {
        return reservationToServices.getOrDefault(reservationId, Collections.emptyList());
    }

    // Calculate total add-on cost for a reservation
    public double calculateTotalCost(String reservationId) {
        return getServicesForReservation(reservationId).stream()
                .mapToDouble(Service::getPrice)
                .sum();
    }
}

// Main application
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== Book My Stay App ===");
        System.out.println("UC7: Add-On Service Selection\n");

        // Sample reservations
        Reservation res1 = new Reservation("R001", "Alice", "Single", 3);
        Reservation res2 = new Reservation("R002", "Bob", "Double", 2);

        // Initialize add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define available services
        Service breakfast = new Service("Breakfast", 15.0);
        Service spa = new Service("Spa Access", 50.0);
        Service airportPickup = new Service("Airport Pickup", 30.0);

        // Attach services to reservations
        serviceManager.addServiceToReservation(res1.getReservationId(), breakfast);
        serviceManager.addServiceToReservation(res1.getReservationId(), airportPickup);
        serviceManager.addServiceToReservation(res2.getReservationId(), spa);

        // Display reservation details with add-on services
        List<Reservation> reservations = Arrays.asList(res1, res2);
        for (Reservation res : reservations) {
            System.out.println(res);
            List<Service> services = serviceManager.getServicesForReservation(res.getReservationId());
            if (services.isEmpty()) {
                System.out.println("No add-on services selected.");
            } else {
                System.out.println("Add-On Services: " + services);
                System.out.println("Total Add-On Cost: $" + serviceManager.calculateTotalCost(res.getReservationId()));
            }
            System.out.println("--------------------------");
        }
    }
}