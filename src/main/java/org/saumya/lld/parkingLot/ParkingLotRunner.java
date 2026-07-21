package org.saumya.lld.parkingLot;

import lombok.extern.slf4j.Slf4j;
import org.saumya.lld.parkingLot.entities.Floor;
import org.saumya.lld.parkingLot.entities.ParkingLot;
import org.saumya.lld.parkingLot.entities.ParkingSpot;
import org.saumya.lld.parkingLot.entities.Vehicle;
import org.saumya.lld.parkingLot.enums.SpotStatus;
import org.saumya.lld.parkingLot.enums.SpotType;
import org.saumya.lld.parkingLot.enums.VehicleType;
import org.saumya.lld.parkingLot.strategies.HourlyFeeStrategy;
import org.saumya.lld.parkingLot.strategies.NearestToElevatorStrategy;
import org.saumya.lld.parkingLot.strategies.NearestToEntryStrategy;
import org.saumya.lld.parkingLot.strategies.SpotAssignmentStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ParkingLotRunner {
    public static void main(String[] args) throws InterruptedException {
        // Create parking spots for floor 1
        List<ParkingSpot> floor1Spots = new ArrayList<>();
        floor1Spots.add(createSpot("S1", SpotType.SMALL));
        floor1Spots.add(createSpot("S2", SpotType.SMALL));
        floor1Spots.add(createSpot("M1", SpotType.MEDIUM));
        floor1Spots.add(createSpot("M2", SpotType.MEDIUM));
        floor1Spots.add(createSpot("L1", SpotType.LARGE));

        // Create parking spots for floor 2
        List<ParkingSpot> floor2Spots = new ArrayList<>();
        floor2Spots.add(createSpot("S3", SpotType.SMALL));
        floor2Spots.add(createSpot("M3", SpotType.MEDIUM));
        floor2Spots.add(createSpot("L2", SpotType.LARGE));

        // Create floors
        List<Floor> floors = new ArrayList<>();
        floors.add(new Floor(1, floor1Spots));
        floors.add(new Floor(2, floor2Spots));

        // Create parking lot
        Map<String, org.saumya.lld.parkingLot.entities.Ticket> activeTickets = new HashMap<>();
        ParkingLot parkingLot = new ParkingLot(floors, activeTickets, new HourlyFeeStrategy(), new NearestToElevatorStrategy());

        log.info("Created parking lot with {} floors, using spot assignment strategy {}", floors.size(), parkingLot.spotAssignmentStrategy.getClass().getSimpleName());
        log.info("Using fee strategy {}", parkingLot.feeStrategy.getClass().getSimpleName());

        // Create vehicles
        Vehicle bike1 = new Vehicle("KA-01-AB-1234", VehicleType.BIKE, false);
        Vehicle car1 = new Vehicle("KA-02-CD-5678", VehicleType.CAR, true);
        Vehicle truck1 = new Vehicle("KA-03-EF-9012", VehicleType.TRUCK, false);
        Vehicle car2 = new Vehicle("KA-04-GH-3456", VehicleType.CAR, false);

        // Park vehicles
        System.out.println("Parking Vehicles");
        org.saumya.lld.parkingLot.entities.Ticket ticket1 = parkingLot.parkVehicle(bike1);
        System.out.println("Bike parked. Ticket ID: " + ticket1.getId() + ", Spot: " + ticket1.getParkingSpot());

        org.saumya.lld.parkingLot.entities.Ticket ticket2 = parkingLot.parkVehicle(car1);
        System.out.println("Car parked. Ticket ID: " + ticket2.getId() + ", Spot: " + ticket2.getParkingSpot());

        org.saumya.lld.parkingLot.entities.Ticket ticket3 = parkingLot.parkVehicle(truck1);
        System.out.println("Truck parked. Ticket ID: " + ticket3.getId() + ", Spot: " + ticket3.getParkingSpot());

        org.saumya.lld.parkingLot.entities.Ticket ticket4 = parkingLot.parkVehicle(car2);
        System.out.println("Car parked. Ticket ID: " + ticket4.getId() + ", Spot: " + ticket4.getParkingSpot());

        // Wait for some time to simulate parking duration
        System.out.println("\nWaiting 2 seconds to simulate parking time");
        Thread.sleep(2000);

        // Unpark vehicles
        System.out.println("\nUnparking Vehicle");
        double fee1 = parkingLot.unParkVehicle(ticket1.getId());
        System.out.println("Bike unparked. Fee: $" + fee1);

        double fee2 = parkingLot.unParkVehicle(ticket2.getId());
        System.out.println("Car unparked. Fee: $" + fee2);

        double fee3 = parkingLot.unParkVehicle(ticket3.getId());
        System.out.println("Truck unparked. Fee: $" + fee3);

        double fee4 = parkingLot.unParkVehicle(ticket4.getId());
        System.out.println("Car unparked. Fee: $" + fee4);
    }

    private static ParkingSpot createSpot(String id, SpotType spotType) {
        int distanceFromElevator = (int) (Math.random() * 100) + 1;
        log.info("Creating spot: {}, distanceFromElevator: {}", id, distanceFromElevator);
        return new ParkingSpot(id, spotType, SpotStatus.AVAILABLE, null, distanceFromElevator);
    }
}
