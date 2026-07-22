package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.GateType;
import org.saumya.lld.parkingLot.enums.PaymentMethod;
import org.saumya.lld.parkingLot.enums.SpotType;
import org.saumya.lld.parkingLot.exceptions.ParkingLotFullException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Gate {
    String gateId;
    GateType type;

    public Ticket issueTicket(ParkingLot parkingLot, Vehicle vehicle) {
        if(type != GateType.ENTRY) {
            throw new IllegalStateException("Gate type is not ENTRY");
        }

        SpotType requiredSpotType = vehicle.getVehicleType().getRequiredSpotType();
        if(!parkingLot.hasAvailableSpots(requiredSpotType)) {
            throw new ParkingLotFullException("No available spot, parking lot is full for " + vehicle.getVehicleType() + " type.");
        }
        return parkingLot.parkVehicle(vehicle, this.gateId);
    }

    public Payment processExit(ParkingLot parkingLot, String ticketId, PaymentMethod paymentMethod) {
        if(type != GateType.EXIT) {
            throw new IllegalStateException("Gate type is not EXIT");
        }
        return parkingLot.unParkVehicle(ticketId, this.gateId, paymentMethod);
    }
}
