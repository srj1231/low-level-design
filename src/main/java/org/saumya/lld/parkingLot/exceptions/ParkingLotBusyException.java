package org.saumya.lld.parkingLot.exceptions;

public class ParkingLotBusyException extends RuntimeException {
    public ParkingLotBusyException(String message) {
        super(message);
    }
}
