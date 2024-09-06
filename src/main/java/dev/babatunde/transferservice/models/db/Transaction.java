package dev.babatunde.transferservice.models.db;

import dev.babatunde.transferservice.constants.TransactionCategory;
import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private UUID id;

    @ManyToOne
    private Account account;

    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String statusMessage;

    private boolean isCommissionWorthy;

    private BigDecimal commission;

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private TransactionChannel channel;

    private String currency;

    @OneToMany(mappedBy = "transaction",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntry> transactionEntries;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal fee;

    public void setStatus(TransactionStatus status){
        this.status  = status;
        this.statusMessage = status.getMessage();
    }


}
