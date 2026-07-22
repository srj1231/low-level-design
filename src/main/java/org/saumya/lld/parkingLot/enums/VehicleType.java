package org.saumya.lld.parkingLot.enums;

import lombok.Getter;

public enum VehicleType {
    BIKE(SpotType.SMALL),
    CAR(SpotType.MEDIUM),
    TRUCK(SpotType.LARGE);

    @Getter
    private final  SpotType requiredSpotType;

    VehicleType(SpotType spotType) {
        this.requiredSpotType = spotType;
    }
}
