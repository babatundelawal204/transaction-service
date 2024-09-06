package dev.babatunde.transferservice.repository;

import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.models.db.TransactionEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, UUID> {

    List<TransactionEntry> findAllByTransaction(Transaction transaction);
}
