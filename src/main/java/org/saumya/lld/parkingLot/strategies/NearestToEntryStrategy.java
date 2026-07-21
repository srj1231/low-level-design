package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Floor;
import org.saumya.lld.parkingLot.entities.ParkingSpot;
import org.saumya.lld.parkingLot.enums.SpotType;

import java.util.List;

public class NearestToEntryStrategy implements SpotAssignmentStrategy {
    @Override
    public ParkingSpot findSpot(List<Floor> floors, SpotType spotType) {
        // assuming floors are ordered by proximity from the entrance (floor 0 = nearest to entry)
        for(Floor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(spotType);
            if(spot != null) return spot;
        }
        return null;
    }
}
