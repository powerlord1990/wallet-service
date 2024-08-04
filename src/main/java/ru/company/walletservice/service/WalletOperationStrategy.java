package ru.company.walletservice.service;

import ru.company.walletservice.entity.Wallet;

import java.math.BigDecimal;


public interface WalletOperationStrategy {
    void apply(Wallet wallet, BigDecimal amount);
}
