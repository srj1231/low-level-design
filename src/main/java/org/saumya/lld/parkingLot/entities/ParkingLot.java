package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.PaymentMethod;
import org.saumya.lld.parkingLot.enums.SpotType;
import org.saumya.lld.parkingLot.exceptions.InvalidTicketException;
import org.saumya.lld.parkingLot.exceptions.ParkingLotFullException;
import org.saumya.lld.parkingLot.strategies.FeeStrategy;
import org.saumya.lld.parkingLot.strategies.SpotAssignmentStrategy;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLot {
    List<Floor> floors;
    List<Gate> gates;
    ConcurrentHashMap<String, Ticket> activeTickets;  // ticketId -> ticket
    ConcurrentHashMap<String, Payment> payments;  // ticketId -> payment
//    static final int RATE_PER_HOUR = 20; // flat rate

    public FeeStrategy feeStrategy;
    public SpotAssignmentStrategy spotAssignmentStrategy;

    public ParkingLot(List<Floor> floors, List<Gate> gates,
                      SpotAssignmentStrategy spotAssignmentStrategy, FeeStrategy feeStrategy) {
        this.floors = floors;
        this.gates = gates;
        this.activeTickets = new ConcurrentHashMap<>();
        this.payments = new ConcurrentHashMap<>();
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.feeStrategy = feeStrategy;
    }

    public boolean hasAvailableSpots(SpotType spotType) {
        for(Floor floor : floors) {
            if(floor.findAvailableSpot(spotType) != null) return true;
        }
        return false;
    }

    public Ticket parkVehicle(Vehicle vehicle, String entryGateId) {
        SpotType requiredSpotType = vehicle.getVehicleType().getRequiredSpotType();
        ParkingSpot assignedSpot = null;
        while (assignedSpot == null) {  // optimistic locking: try and retry
            ParkingSpot candidate = spotAssignmentStrategy.findSpot(floors, requiredSpotType);
            if(candidate == null) {
                throw new ParkingLotFullException("No " + requiredSpotType + " spots available.");
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
        Ticket ticket = activeTickets.get(ticketId);    // with ConcurrentHashMap it is atomic - only one thread can get the ticket at a time
        if(ticket == null) throw new InvalidTicketException("Ticket not found or already processed: " + ticketId);
        ticket.closeTicket(gateId);
        ticket.getParkingSpot().unparkVehicle();
        activeTickets.remove(ticketId); // this is an atomic operation with ConcurrentHashMap

        double amount = feeStrategy.calculateFee(ticket.getVehicle(), ticket.getDurationInHours());
        Payment payment = new Payment(ticketId, amount, paymentMethod);
        payment.markCompleted();
        payments.put(ticketId, payment);
        return payment;
    }
}
