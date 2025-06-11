package com.microservice.transactionservice.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.microservice.transactionservice.DTO.ApiResponse;
import com.microservice.transactionservice.DTO.TransactionDTOResponse;
import com.microservice.transactionservice.models.TransactionModel;
import com.microservice.transactionservice.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionModel>>> getAllTransactions() {
        try {
            List<TransactionModel> transactions = transactionService.getAll();
            if (transactions.isEmpty()){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                "No transactions found",
                                transactions
                        ),
                        HttpStatus.OK
                );
            }
            ApiResponse<List<TransactionModel>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get all transactions",
                    transactions
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<List<TransactionModel>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching users. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<TransactionModel>> getDetailTransaction(
            @RequestParam("transaction_id") String transactionId
    ) {
        try {
            TransactionModel users = transactionService.getByTransactionId(transactionId);
            if (users == null){
                return new ResponseEntity<>(
                        new ApiResponse<>(
                                HttpStatus.NOT_FOUND.value(),
                                "No users found"
                        ),
                        HttpStatus.NOT_FOUND
                );
            }
            ApiResponse<TransactionModel> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get detail users",
                    users
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<TransactionModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching users. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Post
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionModel>> createTransaction(
            @RequestBody TransactionDTOResponse newTransaction
    ) {
        try{
            TransactionModel transaction = transactionService.create(newTransaction);
            ApiResponse<TransactionModel> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Transaction created successfully",
                    transaction
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            ApiResponse<TransactionModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error : " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TransactionModel>> updateStatus(
            @RequestParam("user_id") String transactionId,
            @RequestParam("status") boolean newStatus
    ) {
        try {
            TransactionModel transactions = transactionService.updateStatus(transactionId, newStatus);
            ApiResponse<TransactionModel> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully get detail transactions",
                    transactions
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<TransactionModel> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while fetching transactions. %s." + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(
            @RequestParam("transaction_id") String transactionId
    ) {
        try {
            transactionService.delete(transactionId);

            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "User successfully deleted"
            );
            return ResponseEntity.ok(response);
        } catch (HttpServerErrorException e) {
            ApiResponse<Void> error = new ApiResponse<>(
                    e.getStatusCode().value(),
                    "Transaction deletion failed due to an external service error. Details: " + e.getResponseBodyAsString() // Hati-hati
            );
            return new ResponseEntity<>(error, e.getStatusCode());
        } catch (Exception e) {
            ApiResponse<Void> error = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
