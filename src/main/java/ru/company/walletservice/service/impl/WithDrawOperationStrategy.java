package ru.company.walletservice.service.impl;

import org.springframework.stereotype.Service;
import ru.company.walletservice.entity.Wallet;
import ru.company.walletservice.exception.InsufficientFundsException;
import ru.company.walletservice.service.WalletOperationStrategy;

import java.math.BigDecimal;

@Service
public class WithDrawOperationStrategy implements WalletOperationStrategy {

    @Override
    public void apply(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
    }
}
