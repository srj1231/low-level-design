package org.saumya.lld.parkingLot.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.TicketStatus;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {
    final String id;
    final ParkingSpot parkingSpot;
    final String entryGateId;
    String exitGateId;

    final Vehicle vehicle;
    TicketStatus ticketStatus;
    final LocalDateTime entryTime;
    LocalDateTime exitTime;

    public Ticket(String id, ParkingSpot spot, Vehicle vehicle, String entryGateId) {
        this.id = id;
        this.parkingSpot = spot;
        this.vehicle = vehicle;
        this.ticketStatus = TicketStatus.ACTIVE;
        this.entryGateId = entryGateId;
        this.entryTime = LocalDateTime.now();
    }

    public long getDurationInHours() {
        return Duration.between(entryTime, exitTime).toHours() + 1; // +1 for partial hour
    }

    public void closeTicket(String exitGateId) {
        this.exitTime = LocalDateTime.now();
        this.exitGateId = exitGateId;
        this.ticketStatus = TicketStatus.PAID;
    }
}
