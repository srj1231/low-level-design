package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.GateType;
import org.saumya.lld.parkingLot.enums.PaymentMethod;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Gate {
    String gateId;
    GateType type;

    public Ticket issueTicket(ParkingLot parkingLot, Vehicle vehicle) {
        if(type != GateType.ENTRY) {
            throw new RuntimeException("This is not an entry gate");
        }
        return parkingLot.parkVehicle(vehicle);
    }

    public Payment processExit(ParkingLot parkingLot, String ticketId, PaymentMethod paymentMethod) {
        if(type != GateType.EXIT) {
            throw new RuntimeException("This is not an exit gate");
        }
        return parkingLot.unParkVehicle(ticketId, this.gateId, paymentMethod);
    }
}
