package com.codefusion.wasbackend.transaction.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StockUpdateTriggerUtil implements CommandLineRunner {

    private static final String TRIGGER_SQL = "CREATE TRIGGER update_current_stock " +
            "AFTER INSERT ON transaction " +
            "FOR EACH ROW " +
            "BEGIN " +
            "IF NEW.is_buying = TRUE THEN " +
            "UPDATE product SET current_stock = current_stock + NEW.quantity " +
            "WHERE id = NEW.product_id; " +
            "ELSE " +
            "UPDATE product SET current_stock = current_stock - NEW.quantity " +
            "WHERE id = NEW.product_id; " +
            "END IF; " +
            "END;";

    private static final String TRIGGER_EXISTENCE_SQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_NAME = 'update_current_stock'";
    private static final String TRIGGER_CREATED_MESSAGE = "Stock update trigger created successfully.";

    private final JdbcTemplate jdbcTemplate;

    public void createIfAbsentStockUpdateTrigger() {
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
        createIfAbsentStockUpdateTrigger();
    }
}
