package ru.company.walletservice.service.impl;

import org.springframework.stereotype.Service;
import ru.company.walletservice.entity.Wallet;
import ru.company.walletservice.service.WalletOperationStrategy;

import java.math.BigDecimal;

@Service
public class DepositOperationStrategy implements WalletOperationStrategy {

    @Override
    public void apply(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
    }
}
