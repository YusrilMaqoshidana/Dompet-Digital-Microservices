package com.microservice.walletservice.controllers;

import com.microservice.walletservice.DTO.ApiResponse;
import com.microservice.walletservice.DTO.UserCreatedEvent;
import com.microservice.walletservice.models.WalletModel;
import com.microservice.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        try {
            List<WalletModel> wallets = walletService.getAllWallets();
            if (wallets.isEmpty()){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                "No wallets found",
                                wallets
                        ),
                        HttpStatus.OK
                );
            }
            ApiResponse<List<WalletModel>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get all wallets",
                    wallets
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<List<WalletModel>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching wallets. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WalletModel>> createWallet(@RequestBody UserCreatedEvent wallet) {
        try {
            WalletModel createdWallet = walletService.createWallet(wallet);
            ApiResponse<WalletModel> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Wallet created successfully",
                    createdWallet
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<WalletModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while creating wallet. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
