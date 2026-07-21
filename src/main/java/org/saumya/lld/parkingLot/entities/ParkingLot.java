package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.SpotType;
import org.saumya.lld.parkingLot.enums.VehicleType;
import org.saumya.lld.parkingLot.strategies.FeeStrategy;
import org.saumya.lld.parkingLot.strategies.SpotAssignmentStrategy;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLot {
    List<Floor> floors;
    Map<String, Ticket> activeTickets;
//    static final int RATE_PER_HOUR = 20; // flat rate

    public FeeStrategy feeStrategy;
    public SpotAssignmentStrategy spotAssignmentStrategy;

    private SpotType getSpotType(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> SpotType.MEDIUM;
            case BIKE -> SpotType.SMALL;
            case TRUCK -> SpotType.LARGE;
        };
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        SpotType type = getSpotType(vehicle.getVehicleType());
        ParkingSpot spot = spotAssignmentStrategy.findSpot(floors, type);
        if(spot == null)
            throw new RuntimeException("No available spot, parking lot is full for " + vehicle.getVehicleType() + " type.");

        spot.assignVehicle(vehicle);
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), spot, vehicle);
        activeTickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public double unParkVehicle(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if(ticket == null) throw new RuntimeException("Ticket not found");
        ticket.closeTicket();
        ticket.getParkingSpot().unparkVehicle();
        activeTickets.remove(ticketId);

        return feeStrategy.calculateFee(ticket.getVehicle(), ticket.getDurationInHours());
    }
}
