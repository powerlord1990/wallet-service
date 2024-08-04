package ru.company.walletservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class WalletServiceConcurrencyTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    @Transactional
    void testConcurrentWalletOperations() throws InterruptedException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // ID тестового кошелька, на который будут отправлены запросы
        UUID walletId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        // Количество параллельных запросов
        int numberOfRequests = 1000;

        // Пул потоков для параллельного выполнения запросов
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(() -> {
                try {
                    mockMvc.perform(post("/api/v1/wallets")
                                    .contentType("application/json")
                                    .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"WITHDRAW\", \"amount\": \"10.00\"}"))
                            .andExpect(status().isOk()); // Ожидаем, что каждый запрос завершится 200 OK
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Ждём завершения всех запросов
        latch.await();
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
