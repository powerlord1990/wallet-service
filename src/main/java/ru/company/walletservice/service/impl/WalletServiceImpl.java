package ru.company.walletservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.company.walletservice.common.dto.WalletOperationRequest;
import ru.company.walletservice.common.dto.WalletOperationResponse;
import ru.company.walletservice.entity.Wallet;
import ru.company.walletservice.exception.WalletNotFoundException;
import ru.company.walletservice.repository.WalletRepository;
import ru.company.walletservice.service.WalletOperationStrategy;
import ru.company.walletservice.service.WalletService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    @Override
    public void processOperation(WalletOperationRequest request) {
        // Получаем кошелек с блокировкой на запись (PESSIMISTIC_WRITE)
        Wallet wallet = walletRepository.findByIdForUpdate(request.getWalletId())
                .orElseThrow(() -> {
                    log.error("wallet not found, wallet id: {}", request.getWalletId());
                    throw new WalletNotFoundException("Wallet not found");
                });

        // Выясняем какую стратегию необходимо применить для данного запроса
        WalletOperationStrategy strategy = request.getOperationType().getStrategy();

        // Выполняем стратегию
        strategy.apply(wallet, request.getAmount());
        // Сохраняем изменения в базу данных
        walletRepository.save(wallet);
    }

    @Override
    public WalletOperationResponse getBalance(UUID walletId) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.error("wallet not found, wallet id: {}", walletId);
                    throw new WalletNotFoundException("Wallet not found");
                });
        return new WalletOperationResponse(wallet.getBalance());
    }
}
