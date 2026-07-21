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

    final Vehicle vehicle;
    TicketStatus ticketStatus;
    final LocalDateTime entryTime;
    LocalDateTime exitTime;

    public Ticket(String id, ParkingSpot spot, Vehicle vehicle) {
        this.id = id;
        this.parkingSpot = spot;
        this.vehicle = vehicle;
        this.ticketStatus = TicketStatus.ACTIVE;
        this.entryTime = LocalDateTime.now();
    }

    public long getDurationInHours() {
        return Duration.between(entryTime, exitTime).toHours() + 1; // +1 for partial hour
    }

    public void closeTicket() {
        this.exitTime = LocalDateTime.now();
        this.ticketStatus = TicketStatus.PAID;
    }
}
