import java.util.*;

// Enum for vehicle types
enum VehicleType {
    BIKE, CAR, TRUCK
}

// Enum for spot status
enum SpotStatus {
    AVAILABLE, OCCUPIED
}

// Vehicle class
class Vehicle {
    private String licensePlate;
    private VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " (" + licensePlate + ")";
    }
}

// ParkingSpot class
class ParkingSpot {
    private int spotNumber;
    private VehicleType allowedType;
    private SpotStatus status;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotNumber, VehicleType allowedType) {
        this.spotNumber = spotNumber;
        this.allowedType = allowedType;
        this.status = SpotStatus.AVAILABLE;
        this.parkedVehicle = null;
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    public boolean canFitVehicle(VehicleType vehicleType) {
        return allowedType == vehicleType;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (isAvailable() && canFitVehicle(vehicle.getType())) {
            this.parkedVehicle = vehicle;
            this.status = SpotStatus.OCCUPIED;
            return true;
        }
        return false;
    }

    public Vehicle unparkVehicle() {
        if (status == SpotStatus.OCCUPIED) {
            Vehicle vehicle = this.parkedVehicle;
            this.parkedVehicle = null;
            this.status = SpotStatus.AVAILABLE;
            return vehicle;
        }
        return null;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public VehicleType getAllowedType() {
        return allowedType;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    @Override
    public String toString() {
        return "Spot " + spotNumber + " [" + allowedType + "] - " + status;
    }
}

// ParkingFloor class
class ParkingFloor {
    private int floorNumber;
    private ArrayList<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, int bikeSpots, int carSpots, int truckSpots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();

        int spotNumber = 1;

        // Add bike spots
        for (int i = 0; i < bikeSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.BIKE));
        }

        // Add car spots
        for (int i = 0; i < carSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.CAR));
        }

        // Add truck spots
        for (int i = 0; i < truckSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, VehicleType.TRUCK));
        }
    }

    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFitVehicle(vehicleType)) {
                return spot;
            }
        }
        return null;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = findAvailableSpot(vehicle.getType());
        if (spot != null) {
            return spot.parkVehicle(vehicle);
        }
        return false;
    }

    public Vehicle unparkVehicle(int spotNumber) {
        for (ParkingSpot spot : spots) {
            if (spot.getSpotNumber() == spotNumber) {
                return spot.unparkVehicle();
            }
        }
        return null;
    }

    public int getAvailableSpots(VehicleType vehicleType) {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFitVehicle(vehicleType)) {
                count++;
            }
        }
        return count;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void displayStatus() {
        System.out.println("\n--- Floor " + floorNumber + " ---");
        System.out.println("Bike spots available: " + getAvailableSpots(VehicleType.BIKE));
        System.out.println("Car spots available: " + getAvailableSpots(VehicleType.CAR));
        System.out.println("Truck spots available: " + getAvailableSpots(VehicleType.TRUCK));
    }
}

// Main ParkingLot class
class ParkingLot {
    private String name;
    private ArrayList<ParkingFloor> floors;

    public ParkingLot(String name, int numberOfFloors, int bikeSpotsPerFloor, 
                      int carSpotsPerFloor, int truckSpotsPerFloor) {
        this.name = name;
        this.floors = new ArrayList<>();

        for (int i = 1; i <= numberOfFloors; i++) {
            floors.add(new ParkingFloor(i, bikeSpotsPerFloor, carSpotsPerFloor, truckSpotsPerFloor));
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            if (floor.parkVehicle(vehicle)) {
                System.out.println("✓ " + vehicle + " parked on Floor " + floor.getFloorNumber());
                return true;
            }
        }
        System.out.println("✗ No available spot for " + vehicle);
        return false;
    }

    public boolean unparkVehicle(int floorNumber, int spotNumber) {
        if (floorNumber > 0 && floorNumber <= floors.size()) {
            ParkingFloor floor = floors.get(floorNumber - 1);
            Vehicle vehicle = floor.unparkVehicle(spotNumber);
            if (vehicle != null) {
                System.out.println("✓ " + vehicle + " removed from Floor " + floorNumber + ", Spot " + spotNumber);
                return true;
            }
        }
        System.out.println("✗ Invalid floor or spot number, or spot is already empty");
        return false;
    }

    public void displayStatus() {
        System.out.println("\n========== " + name + " ==========");
        for (ParkingFloor floor : floors) {
            floor.displayStatus();
        }
        System.out.println("==============================\n");
    }
}

// Main class
public class MultiStoreyParking {
    public static void main(String[] args) {
        // Create parking lot with 3 floors
        // Each floor: 10 bike spots, 20 car spots, 5 truck spots
        ParkingLot parkingLot = new ParkingLot("City Center Parking", 3, 10, 20, 5);

        // Create vehicles
        Vehicle bike1 = new Vehicle("KA-01-1234", VehicleType.BIKE);
        Vehicle bike2 = new Vehicle("KA-01-5678", VehicleType.BIKE);
        Vehicle car1 = new Vehicle("KA-02-9999", VehicleType.CAR);
        Vehicle car2 = new Vehicle("KA-03-8888", VehicleType.CAR);
        Vehicle truck1 = new Vehicle("KA-04-7777", VehicleType.TRUCK);

        // Display initial status
        parkingLot.displayStatus();

        // Park vehicles
        System.out.println("--- Parking Vehicles ---");
        parkingLot.parkVehicle(bike1);
        parkingLot.parkVehicle(bike2);
        parkingLot.parkVehicle(car1);
        parkingLot.parkVehicle(car2);
        parkingLot.parkVehicle(truck1);

        // Display status after parking
        parkingLot.displayStatus();

        // Unpark a vehicle
        System.out.println("--- Unparking Vehicles ---");
        parkingLot.unparkVehicle(1, 1);  // Floor 1, Spot 1

        // Display final status
        parkingLot.displayStatus();
    }
}
