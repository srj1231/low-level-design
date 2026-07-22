package org.saumya.lld.parkingLot;

import org.saumya.lld.parkingLot.entities.*;
import org.saumya.lld.parkingLot.enums.*;
import org.saumya.lld.parkingLot.strategies.FeeStrategy;
import org.saumya.lld.parkingLot.strategies.HourlyFeeStrategy;
import org.saumya.lld.parkingLot.strategies.NearestToEntryStrategy;
import org.saumya.lld.parkingLot.strategies.SpotAssignmentStrategy;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotRunner {
    public static void main(String[] args) {
        // Create parking spots
        List<ParkingSpot> spotsFloor1 = new ArrayList<>();
        spotsFloor1.add(new ParkingSpot("S1", SpotType.SMALL, SpotStatus.AVAILABLE, null, 10));
        spotsFloor1.add(new ParkingSpot("S2", SpotType.SMALL, SpotStatus.AVAILABLE, null, 20));
        spotsFloor1.add(new ParkingSpot("M1", SpotType.MEDIUM, SpotStatus.AVAILABLE, null, 15));
        spotsFloor1.add(new ParkingSpot("M2", SpotType.MEDIUM, SpotStatus.AVAILABLE, null, 25));

        List<ParkingSpot> spotsFloor2 = new ArrayList<>();
        spotsFloor2.add(new ParkingSpot("S3", SpotType.SMALL, SpotStatus.AVAILABLE, null, 5));
        spotsFloor2.add(new ParkingSpot("L1", SpotType.LARGE, SpotStatus.AVAILABLE, null, 30));
        spotsFloor2.add(new ParkingSpot("M3", SpotType.MEDIUM, SpotStatus.AVAILABLE, null, 10));

        // Create floors
        List<Floor> floors = new ArrayList<>();
        floors.add(new Floor(1, spotsFloor1));
        floors.add(new Floor(2, spotsFloor2));

        // Create gates
        List<Gate> gates = new ArrayList<>();
        gates.add(new Gate("G1", GateType.ENTRY));
        gates.add(new Gate("G2", GateType.EXIT));

        // Create strategies
        SpotAssignmentStrategy spotAssignmentStrategy = new NearestToEntryStrategy();
        FeeStrategy feeStrategy = new HourlyFeeStrategy();

        // Create parking lot
        ParkingLot parkingLot = new ParkingLot(floors, gates, spotAssignmentStrategy, feeStrategy);

        // Create vehicles
        Vehicle bike1 = new Vehicle("1234", VehicleType.BIKE, false);
        Vehicle car1 = new Vehicle("5678", VehicleType.CAR, false);
        Vehicle truck1 = new Vehicle("9012", VehicleType.TRUCK, false);
        Vehicle car2 = new Vehicle("3456", VehicleType.CAR, true);

        // Get entry and exit gates
        Gate entryGate = gates.get(0);
        Gate exitGate = gates.get(1);

        // Park vehicles
        System.out.println("Parking vehicles...");
        Ticket ticket1 = entryGate.issueTicket(parkingLot, bike1);
        System.out.println("Bike parked. Ticket ID: " + ticket1.getId() + ", Spot: " + ticket1.getParkingSpot().getId());

        Ticket ticket2 = entryGate.issueTicket(parkingLot, car1);
        System.out.println("Car 1 parked. Ticket ID: " + ticket2.getId() + ", Spot: " + ticket2.getParkingSpot().getId());

        Ticket ticket3 = entryGate.issueTicket(parkingLot, truck1);
        System.out.println("Truck parked. Ticket ID: " + ticket3.getId() + ", Spot: " + ticket3.getParkingSpot().getId());

        Ticket ticket4 = entryGate.issueTicket(parkingLot, car2);
        System.out.println("Car 2 (EV) parked. Ticket ID: " + ticket4.getId() + ", Spot: " + ticket4.getParkingSpot().getId());

        System.out.println("\nUnparking vehicles...\n");

        // Unpark vehicles
        Payment payment1 = exitGate.processExit(parkingLot, ticket1.getId(), PaymentMethod.UPI);
        System.out.println("Bike unparked. Amount: $" + payment1.getAmount() + ", Status: " + payment1.getPaymentStatus());

        Payment payment2 = exitGate.processExit(parkingLot, ticket2.getId(), PaymentMethod.CARD);
        System.out.println("Car 1 unparked. Amount: $" + payment2.getAmount() + ", Status: " + payment2.getPaymentStatus());

        Payment payment3 = exitGate.processExit(parkingLot, ticket3.getId(), PaymentMethod.CASH);
        System.out.println("Truck unparked. Amount: $" + payment3.getAmount() + ", Status: " + payment3.getPaymentStatus());

        Payment payment4 = exitGate.processExit(parkingLot, ticket4.getId(), PaymentMethod.UPI);
        System.out.println("Car 2 (EV) unparked. Amount: $" + payment4.getAmount() + ", Status: " + payment4.getPaymentStatus());
    }
}
