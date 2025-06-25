package com.microservice.walletservice.controllers;

import com.microservice.walletservice.DTO.ApiResponse;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WalletModel>>> getAllWallets() {
        List<WalletModel> wallets = walletService.getAllWallets();
        ApiResponse<List<WalletModel>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully retrieved all wallets",
                wallets
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ApiResponse<WalletModel>> getWalletByUserId(@PathVariable String userId) {
        return walletService.getWalletByUserId(userId)
                .map(wallet -> new ApiResponse<>(HttpStatus.OK.value(), "Wallet found", wallet))
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Wallet not found for user: " + userId),
                        HttpStatus.NOT_FOUND
                ));
    }
}