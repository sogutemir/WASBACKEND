package com.codefusion.wasbackend.transaction.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionTriggerUtil implements CommandLineRunner {

    private static final String TRIGGER_SQL = "CREATE TRIGGER track_transaction AFTER INSERT ON transaction " +
            "FOR EACH ROW BEGIN " +
            "IF NEW.is_buying THEN " +
            "UPDATE product SET profit = profit - (NEW.price * NEW.quantity) WHERE id = NEW.product_id; " +
            "ELSE " +
            "UPDATE product SET profit = profit + (NEW.price * NEW.quantity) WHERE id = NEW.product_id; " +
            "END IF; " +
            "END";

    private static final String TRIGGER_EXISTENCE_SQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_NAME = 'track_transaction'";
    private static final String TRIGGER_CREATED_MESSAGE = "Transaction trigger created successfully.";

    private final JdbcTemplate jdbcTemplate;

    public void createIfAbsentTransactionTrigger() {
        Integer count = jdbcTemplate.queryForObject(TRIGGER_EXISTENCE_SQL, Integer.class);
        if (count != null && count == 0) {
            jdbcTemplate.execute(TRIGGER_SQL);
            logTriggerStatus(TRIGGER_CREATED_MESSAGE);
        }
    }

    private void logTriggerStatus(String message) {
        System.out.println(message);
    }

    @Override
    public void run(String... args) {
        createIfAbsentTransactionTrigger();
    }
}
