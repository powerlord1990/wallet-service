package ru.company.walletservice.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.company.walletservice.config.OperationTypeDeserializer;
import ru.company.walletservice.service.WalletOperationStrategy;
import ru.company.walletservice.service.impl.DepositOperationStrategy;
import ru.company.walletservice.service.impl.WithDrawOperationStrategy;

@JsonDeserialize(using = OperationTypeDeserializer.class)
public enum OperationType {
    WITHDRAW(new WithDrawOperationStrategy()),
    DEPOSIT(new DepositOperationStrategy());

    private final WalletOperationStrategy strategy;

    OperationType(WalletOperationStrategy strategy) {
        this.strategy = strategy;
    }

    public WalletOperationStrategy getStrategy() {
        return strategy;
    }
}
