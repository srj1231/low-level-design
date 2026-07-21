package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.SpotType;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Floor {
    int floorNumber;
    List<ParkingSpot> parkingSpots;

    public ParkingSpot findAvailableSpot(SpotType spotType) {
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.isAvailable() && parkingSpot.getSpotType() == spotType)
                .findFirst()    // return first available spot
                .orElse(null);
    }

    public ParkingSpot findSpotNearestToElevator(SpotType spotType) {
        return parkingSpots.stream()
                .filter(parkingSpot -> parkingSpot.isAvailable() && parkingSpot.getSpotType() == spotType)
                .min(Comparator.comparingInt(ParkingSpot::getDistanceFromElevator))
                .orElse(null);
    }
}
