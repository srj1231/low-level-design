package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Vehicle;

public class EVFeeStrategy implements FeeStrategy {
    private final FeeStrategy base;
    private static final double EV_DISCOUNT = 0.9;

    public EVFeeStrategy(FeeStrategy base) {
        this.base = base;
    }

    @Override
    public double calculateFee(Vehicle vehicle, long duration) {
        double baseFee = base.calculateFee(vehicle, duration);
        return vehicle.isEV ? baseFee * EV_DISCOUNT : baseFee;
    }
}
