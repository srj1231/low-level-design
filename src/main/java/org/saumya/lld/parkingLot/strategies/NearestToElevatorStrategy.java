package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Floor;
import org.saumya.lld.parkingLot.entities.ParkingSpot;
import org.saumya.lld.parkingLot.enums.SpotType;

import java.util.List;

public class NearestToElevatorStrategy implements SpotAssignmentStrategy {
    @Override
    public ParkingSpot findSpot(List<Floor> floors, SpotType spotType) {
        for(Floor floor : floors) {
            ParkingSpot spot = floor.findSpotNearestToElevator(spotType);
            if(spot != null) return spot;
        }

        return null;
    }
}
