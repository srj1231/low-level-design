package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Vehicle;

public class HourlyFeeStrategy implements FeeStrategy {
    private final int PRICE_PER_HOUR = 20;

    @Override
    public double calculateFee(Vehicle vehicle, long duration) {
        return switch (vehicle.getVehicleType()) {
            case CAR -> PRICE_PER_HOUR * duration + 15;
            case BIKE -> PRICE_PER_HOUR * duration;
            case TRUCK -> PRICE_PER_HOUR * duration + 30;
        };
    }
}
