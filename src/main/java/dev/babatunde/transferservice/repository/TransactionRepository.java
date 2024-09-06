package dev.babatunde.transferservice.repository;

import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.db.Transaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    boolean existsByPaymentReference(String paymentReference);

    @QueryHints({
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "-2")
    })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Transaction> findTop50ByStatusOrderByCreatedAtAsc(TransactionStatus status);
}
