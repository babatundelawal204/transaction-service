package dev.babatunde.transferservice.repository;

import dev.babatunde.transferservice.constants.TransactionCategory;
import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.models.request.TransactionSearchRequest;
import dev.babatunde.transferservice.models.response.TransactionDTO;
import dev.babatunde.transferservice.models.response.TransactionEntryDTO;
import dev.babatunde.transferservice.models.response.TransactionSummaryResponse;
import dev.babatunde.transferservice.repository.jooq.Tables;
import dev.babatunde.transferservice.repository.jooq.tables.Accounts;
import dev.babatunde.transferservice.repository.jooq.tables.TransactionEntries;
import dev.babatunde.transferservice.repository.jooq.tables.Transactions;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static dev.babatunde.transferservice.repository.jooq.Tables.ACCOUNTS;
import static java.time.ZoneOffset.UTC;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.DATE;

@Repository
@RequiredArgsConstructor
public class JooqTransactionRepository {

    private final DSLContext dslContext;


    final Transactions t = Tables.TRANSACTIONS;
    final Accounts a = ACCOUNTS;
    final TransactionEntries te = Tables.TRANSACTION_ENTRIES;

    public List<TransactionDTO> findAllTransactions(TransactionSearchRequest request){
        SelectConditionStep<?> query = buildQuery(request);
        return query.fetchInto(TransactionDTO.class);
    }

    private SelectConditionStep<?> buildQuery(TransactionSearchRequest request) {
        String accountNumber = request.accountNumber();
        TransactionStatus status = request.status();
        TransactionCategory category = request.category();
        TransactionChannel channel = request.channel();
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();



        var query = dslContext.select(t.ID,
                        a.ACCOUNT_NUMBER,
                        a.ACCOUNT_NAME,
                        t.DESCRIPTION,
                        t.STATUS,
                        t.CATEGORY,
                        t.PAYMENT_REFERENCE,
                        t.CHANNEL,
                        t.CURRENCY,
                        t.AMOUNT,
                        t.CREATED_AT.as("date"),
                        t.IS_COMMISSION_WORTHY,
                        t.STATUS_MESSAGE,
                        t.COMMISSION,
                        t.FEE,
                        multiset(select(a.ACCOUNT_NUMBER, a.ACCOUNT_NAME, te.TYPE, te.AMOUNT)
                                .from(te)
                                .innerJoin(a).on(te.ACCOUNT_ID.eq(a.ID))
                                .where(te.TRANSACTION_ID.eq(t.ID))
                        ).as("transactionEntries").convertFrom(r -> r.map(mapping(TransactionEntryDTO::new)))
                )
                .from(t)
                .innerJoin(a).on(t.ACCOUNT_ID.eq(a.ID))
                .where(DSL.noCondition());
        if(!StringUtils.isBlank(accountNumber)){
            query = query.and(a.ACCOUNT_NUMBER.eq(accountNumber));
        }
        if(status != null){
            query = query.and(t.STATUS.eq(status.toString()));
        }
        if(category != null){
            query = query.and(t.CATEGORY.eq(category.getName()));
        }
        if(channel != null){
            query = query.and(t.CHANNEL.eq(channel.toString()));
        }
        if(startDate != null && endDate == null){
            query = query.and(t.CREATED_AT.eq(startDate.atStartOfDay(UTC).toOffsetDateTime()));
        }
        if(startDate != null && endDate !=null){
            query = query.and(t.CREATED_AT.between(startDate.atStartOfDay(UTC).toOffsetDateTime(), endDate.atStartOfDay(UTC).toOffsetDateTime()));
        }
        int size = request.size();
        int offset = (request.page() - 1) * size;
        return (SelectConditionStep<?>) query.orderBy(t.CREATED_AT).limit(size).offset(offset);
    }

    public List<TransactionSummaryResponse> generateTransactionSummary(LocalDate date) {
        return dslContext.select(sum(te.AMOUNT).as("totalAmount"),
                        t.CHANNEL,
                        t.CREATED_AT.as("transactionDate"),
                        sum(t.COMMISSION).as("totalCommission"), te.TYPE)
                .from(t)
                .innerJoin(te).on(t.ID.eq(te.TRANSACTION_ID))
                .where(cast(t.CREATED_AT, DATE).eq(date(date.toString())))
                .groupBy(t.CHANNEL, t.CREATED_AT, te.TYPE)
                .fetchInto(TransactionSummaryResponse.class);
    }
}
