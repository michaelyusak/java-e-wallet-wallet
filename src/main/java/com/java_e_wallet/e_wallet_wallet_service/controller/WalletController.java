package com.java_e_wallet.e_wallet_wallet_service.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java_e_wallet.e_wallet_wallet_service.dto.BalanceResponseDTO;
import com.java_e_wallet.e_wallet_wallet_service.dto.ResponseDTO;
import com.java_e_wallet.e_wallet_wallet_service.dto.TransactionRequestDTO;
import com.java_e_wallet.e_wallet_wallet_service.dto.TransactionResultDTO;
import com.java_e_wallet.e_wallet_wallet_service.dto.WalletResponseDTO;
import com.java_e_wallet.e_wallet_wallet_service.model.Balance;
import com.java_e_wallet.e_wallet_wallet_service.model.TransactionResult;
import com.java_e_wallet.e_wallet_wallet_service.model.Wallet;
import com.java_e_wallet.e_wallet_wallet_service.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class WalletController {
    private final WalletService walletService;

    public WalletController(
            WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(path = "users/{user_id}/wallet")
    public ResponseDTO getUserWallet(@PathVariable("user_id") Long userId) {
        Optional<Wallet> wallet = walletService.getUserWallet(userId);

        return new ResponseDTO(HttpStatus.OK.value(), "ok",
                wallet.isPresent() ? new WalletResponseDTO(wallet.get()) : null);
    }

    @GetMapping(path = "users/{user_id}/wallet/{wallet_number}/balance")
    public ResponseDTO getUserBalance(@PathVariable("user_id") Long userId, @PathVariable("wallet_number") String walletNumber) {
        List<Balance> balances = walletService.getUserBalance(userId, walletNumber);

        return new ResponseDTO(HttpStatus.OK.value(), "ok",
                balances.stream().map(BalanceResponseDTO::new).collect(Collectors.toList()));
    }

    @PostMapping(path = "users/{user_id}/wallet/transaction")
    public ResponseDTO postTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequest, @PathVariable("user_id") Long userId) {
        TransactionResult result = walletService.transferAsset(userId, transactionRequest.getRecipientWalletNumber(), transactionRequest.getAsset(), transactionRequest.getAmount());

        return new ResponseDTO(HttpStatus.CREATED.value(), "transaction executed", new TransactionResultDTO(result));
    }

    @PostMapping(path = "users/{user_id}/wallet/{wallet_number}/balance/{asset}")
    public ResponseDTO createBalance(@PathVariable("wallet_number") String walletNumber, @PathVariable("asset") String asset, @PathVariable("user_id") Long userid) {
        Balance newBalance = walletService.createBalance(userid, asset);

        return new ResponseDTO(HttpStatus.CREATED.value(), "success create balance", new BalanceResponseDTO(newBalance));
    }
}
