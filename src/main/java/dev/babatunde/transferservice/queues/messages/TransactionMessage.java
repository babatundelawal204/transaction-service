package dev.babatunde.transferservice.queues.messages;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionMessage(UUID id,
                                 BigDecimal amount) {
}
