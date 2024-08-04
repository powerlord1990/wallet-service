package ru.company.walletservice.service;

import ru.company.walletservice.common.dto.WalletOperationRequest;
import ru.company.walletservice.common.dto.WalletOperationResponse;

import java.util.UUID;

public interface WalletService {
    void processOperation(WalletOperationRequest request);

    WalletOperationResponse getBalance(UUID walletId);
}
