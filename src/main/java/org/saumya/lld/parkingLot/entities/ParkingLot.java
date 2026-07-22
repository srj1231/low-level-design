package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.PaymentMethod;
import org.saumya.lld.parkingLot.enums.SpotType;
import org.saumya.lld.parkingLot.enums.VehicleType;
import org.saumya.lld.parkingLot.strategies.FeeStrategy;
import org.saumya.lld.parkingLot.strategies.SpotAssignmentStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLot {
    List<Floor> floors;
    List<Gate> gates;
    Map<String, Ticket> activeTickets;  // ticketId -> ticket
    Map<String, Payment> payments;  // ticketId -> payment
//    static final int RATE_PER_HOUR = 20; // flat rate

    public FeeStrategy feeStrategy;
    public SpotAssignmentStrategy spotAssignmentStrategy;

    public ParkingLot(List<Floor> floors, List<Gate> gates,
                      SpotAssignmentStrategy spotAssignmentStrategy, FeeStrategy feeStrategy) {
        this.floors = floors;
        this.gates = gates;
        this.activeTickets = new HashMap<>();
        this.payments = new HashMap<>();
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.feeStrategy = feeStrategy;
    }

    private SpotType getSpotType(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> SpotType.MEDIUM;
            case BIKE -> SpotType.SMALL;
            case TRUCK -> SpotType.LARGE;
        };
    }

    public Ticket parkVehicle(Vehicle vehicle, String entryGateId) {
        SpotType type = getSpotType(vehicle.getVehicleType());
        ParkingSpot assignedSpot = null;
        while (assignedSpot == null) {  // optimistic locking: try and retry
            ParkingSpot candidate = spotAssignmentStrategy.findSpot(floors, type);
            if(candidate == null) {
                throw new RuntimeException("No available spot, parking lot is full for " + vehicle.getVehicleType() + " type.");
            }
            if(candidate.assignVehicle(vehicle)) {  // atomic check and set
                assignedSpot = candidate;
            }
            // else try next spot because current spot is already occupied
        }

        // locking the entire parkVehicle call would work but kills throughput across floors for no reason

        Ticket ticket = new Ticket(UUID.randomUUID().toString(), assignedSpot, vehicle, entryGateId);
        activeTickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public Payment unParkVehicle(String ticketId, String gateId, PaymentMethod paymentMethod) {
        Ticket ticket = activeTickets.get(ticketId);
        if(ticket == null) throw new RuntimeException("Ticket not found");
        ticket.closeTicket(gateId);
        ticket.getParkingSpot().unparkVehicle();
        activeTickets.remove(ticketId);

        double amount = feeStrategy.calculateFee(ticket.getVehicle(), ticket.getDurationInHours());
        Payment payment = new Payment(ticketId, amount, paymentMethod);
        payment.markCompleted();
        payments.put(ticketId, payment);
        return payment;
    }
}
