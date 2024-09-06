package dev.babatunde.transferservice.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.babatunde.transferservice.constants.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntry{

    @Id
    private UUID id;

    @ManyToOne
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    @JsonIgnore
    @ManyToOne
    private Transaction transaction;


}
