package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Vehicle;

public interface FeeStrategy {
    double calculateFee(Vehicle vehicle, long duration);
}
