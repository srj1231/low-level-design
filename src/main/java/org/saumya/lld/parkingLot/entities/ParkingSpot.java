package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.SpotStatus;
import org.saumya.lld.parkingLot.enums.SpotType;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingSpot {
    String id;
    SpotType spotType;
    SpotStatus spotStatus;
    Vehicle parkedVehicle;
    int distanceFromElevator;

    public synchronized boolean isAvailable() {
        return spotStatus == SpotStatus.AVAILABLE;
    }

    public synchronized boolean assignVehicle(Vehicle vehicle) {
        if(!spotStatus.equals(SpotStatus.AVAILABLE)) return false; // check
        this.parkedVehicle = vehicle;   // act
        this.spotStatus = SpotStatus.OCCUPIED;
        return true;
    }

    public synchronized void unparkVehicle() {
        this.parkedVehicle = null;
        this.spotStatus = SpotStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return id;
    }
}
