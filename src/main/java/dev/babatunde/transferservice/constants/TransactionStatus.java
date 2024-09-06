package dev.babatunde.transferservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {

    PENDING("PENDING"),
    SUCCESSFUL("SUCCESSFUL"),
    INSUFFICIENT_FUNDS("INSUFFICIENT FUNDS"),
    FAILED("FAILED"),
    INVALID_ACCOUNT_NUMBER("INVALID ACCOUNT NUMBER"),
    INVALID_RECIPIENT("INVALID RECIPIENT");

    private final String message;


}
