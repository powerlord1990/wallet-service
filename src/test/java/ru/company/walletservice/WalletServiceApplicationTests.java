package ru.company.walletservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.company.walletservice.common.dto.OperationType;
import ru.company.walletservice.common.dto.WalletOperationRequest;
import ru.company.walletservice.common.dto.WalletOperationResponse;
import ru.company.walletservice.controller.WalletController;
import ru.company.walletservice.exception.InsufficientFundsException;
import ru.company.walletservice.exception.WalletNotFoundException;
import ru.company.walletservice.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
class WalletServiceApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    void testProcessOperationSuccess() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(100L));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testProcessOperationWalletNotFound() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d471"));
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(100L));
        doThrow(new WalletNotFoundException("Wallet not found")).when(walletService).processOperation(request);
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Wallet not found"));
    }

    @Test
    void testProcessOperation_InsufficientFunds() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(1500L));  // Сумма больше, чем доступный баланс

        doThrow(new InsufficientFundsException("Insufficient funds")).when(walletService).processOperation(request);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())  // Ожидаем статус 400 Bad Request
                .andExpect(content().string("Insufficient funds"));  // Проверяем сообщение об ошибке
    }

    @Test
    void testGetBalance() throws Exception {
        WalletOperationResponse response = new WalletOperationResponse(BigDecimal.valueOf(1000));

        when(walletService.getBalance(any(UUID.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    void testGetBalanceWalletNotFound() throws Exception {
        UUID walletId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d472");

        // Настроим mock для сервиса так, чтобы он выбрасывал исключение WalletNotFoundException
        when(walletService.getBalance(walletId)).thenThrow(new WalletNotFoundException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isNotFound())  // Ожидаем статус 404 Not Found
                .andExpect(MockMvcResultMatchers.content().string("Wallet not found"));  // Ожидаем сообщение об ошибке
    }
}
