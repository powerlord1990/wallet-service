package ru.company.walletservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletOperationRequest {

    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
