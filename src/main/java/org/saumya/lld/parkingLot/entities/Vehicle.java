package org.saumya.lld.parkingLot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.VehicleType;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Vehicle {
    String vehicleNumber;
    VehicleType vehicleType;
}
