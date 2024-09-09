package dev.babatunde.transferservice.testdata;

import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.db.Transaction;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionTestData {

    public static Transaction transaction1(){
        return Transaction.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(400))
                .currency("NGN")
                .paymentReference("paymentReference1234")
                .createdAt(LocalDateTime.now())
                .channel(TransactionChannel.MOBILE)
                .updatedAt(LocalDateTime.now())
                .status(TransactionStatus.SUCCESSFUL)
                .statusMessage(TransactionStatus.SUCCESSFUL.getMessage())

                .build();
    }
}
