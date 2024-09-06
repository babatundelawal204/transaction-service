package dev.babatunde.transferservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionCategory {

    TRANSFERS("TRANSFERS"),
    BILL_PAYMENTS("BILL PAYMENTS"),
    AIRTIME("AIRTIME");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}
