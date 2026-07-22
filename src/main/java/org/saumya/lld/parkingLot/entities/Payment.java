package org.saumya.lld.parkingLot.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.saumya.lld.parkingLot.enums.PaymentMethod;
import org.saumya.lld.parkingLot.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    final String paymentId;
    String ticketId;
    double amount;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    final LocalDateTime paymentTime;

    public Payment(String ticketId, double amount, PaymentMethod paymentMethod) {
        this.paymentId = UUID.randomUUID().toString();
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentMethod = paymentMethod;
        this.paymentTime = LocalDateTime.now();
    }

    public void markCompleted() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void markFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }
}
