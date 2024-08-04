package ru.company.walletservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.company.walletservice.common.dto.WalletOperationRequest;
import ru.company.walletservice.common.dto.WalletOperationResponse;
import ru.company.walletservice.service.WalletService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
@Slf4j
public class WalletController {

    @PostMapping
    @Operation(summary = "Process a wallet operation", description = "Processes a wallet operation based on the provided request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid operation type or bad request"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<Void> processOperation(@RequestBody WalletOperationRequest request) {
        walletService.processOperation(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private final WalletService walletService;

    @GetMapping("/{walletId}")
    @Operation(summary = "Get a wallet by id", description = "Returns a wallet balance as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The wallet was not found")
    })
    public ResponseEntity<WalletOperationResponse> getBalance(@PathVariable UUID walletId) {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.getBalance(walletId));
    }
}
