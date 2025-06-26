package com.microservice.topupservice.controllers;

import com.microservice.topupservice.DTO.ApiResponse;
import com.microservice.topupservice.DTO.TopupRequestDTO;
import com.microservice.topupservice.models.TopupModel;
import com.microservice.topupservice.service.TopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/topup")
@RequiredArgsConstructor
@Slf4j
public class TopupController {
    private final TopupService topupService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopupModel>>> getAllTopup() {
        List<TopupModel> topups = topupService.getAll();
        ApiResponse<List<TopupModel>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully retrieved all topups",
                topups
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{topupId}")
    public ResponseEntity<ApiResponse<TopupModel>> getDetailTopup(@PathVariable String topupId) {
        TopupModel topup = topupService.getByTopupId(topupId);
        if (topup == null) {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Topup with ID " + topupId + " not found."),
                HttpStatus.NOT_FOUND
            );
        }
        ApiResponse<TopupModel> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Successfully retrieved topup details",
            topup
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createTopup(
            @RequestBody TopupRequestDTO newTopup) {
        try {
            TopupModel topup = topupService.create(newTopup);
            ApiResponse<Object> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Topup initiated successfully, waiting for finalization.",
                    topup
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(
                    HttpStatus.CONFLICT.value(),
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error creating topup: ", e);
            ApiResponse<Object> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An internal error occurred: " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}