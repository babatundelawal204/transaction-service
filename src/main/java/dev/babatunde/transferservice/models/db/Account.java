package dev.babatunde.transferservice.models.db;

import dev.babatunde.transferservice.constants.AccountStatus;
import dev.babatunde.transferservice.constants.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account{

    @Id
    private UUID id;

    private String accountNumber;

    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private String branchCode;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
