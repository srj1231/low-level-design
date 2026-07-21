package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Floor;
import org.saumya.lld.parkingLot.entities.ParkingSpot;
import org.saumya.lld.parkingLot.enums.SpotType;

import java.util.List;

public interface SpotAssignmentStrategy {
    ParkingSpot findSpot(List<Floor> floors, SpotType spotType);
}
