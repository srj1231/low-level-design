package org.saumya.lld.parkingLot.strategies;

import org.saumya.lld.parkingLot.entities.Vehicle;

import java.time.DayOfWeek;
import java.time.LocalDate;

/*
 * Decorator pattern to add weekend surcharge to any fee strategy implementation.
 *
 * Benefits:
 * - Open/Closed principle - adds weekend logic without modifying the base fee strategy.
 * - Composition over inheritance. Stackable decorators.
 * - Single responsibility.
 */
public class WeekendFeeStrategy implements FeeStrategy {
    private final FeeStrategy base;   // base fee strategy can be any other implementation of FeeStrategy
    private static final double WEEKEND_SURCHARGE = 1.5;

    public WeekendFeeStrategy(FeeStrategy base) {
        this.base = base;
    }

    @Override
    public double calculateFee(Vehicle vehicle, long duration) {
        double baseFee = base.calculateFee(vehicle, duration);
        return isWeekend() ? baseFee * WEEKEND_SURCHARGE : baseFee;
    }

    private boolean isWeekend() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}
