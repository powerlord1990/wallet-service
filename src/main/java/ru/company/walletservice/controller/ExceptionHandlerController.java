package ru.company.walletservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.company.walletservice.exception.InsufficientFundsException;
import ru.company.walletservice.exception.InvalidOperationTypeException;
import ru.company.walletservice.exception.WalletNotFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<String> handleWalletNotFound(WalletNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({InsufficientFundsException.class, InvalidOperationTypeException.class})
    public ResponseEntity<String> handleInsufficientFunds(Throwable ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
