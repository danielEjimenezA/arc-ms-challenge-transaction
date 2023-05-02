package com.pichincha.services.controller;

import com.pichincha.services.service.AccountService;
import com.pichincha.services.service.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController
{
    private final AccountService accountService;
    
    @GetMapping("/{accountNumber}")
    public Mono<ResponseEntity<Mono<AccountDto>>> get(
            @PathVariable
            Long accountNumber
    )
    {
        return Mono.just(ResponseEntity.ok(accountService.getByAccountNumber(accountNumber)));
    }
    
    @GetMapping("/client/{clientId}")
    public Mono<ResponseEntity<Flux<AccountDto>>> getByClient(
            @PathVariable
            Long clientId
    )
    {
        return Mono.just(ResponseEntity.ok(accountService.getByClientId(clientId)));
    }
    
    @PostMapping
    public ResponseEntity<Mono<AccountDto>> create(
            @RequestBody
            @Valid AccountDto accountDto
    ) throws NoSuchAlgorithmException
    {
        return ResponseEntity.ok(accountService.create(accountDto));
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<Mono<AccountDto>> update(
            @PathVariable
            Long accountId,
            @RequestBody
            @Valid AccountDto accountDto
    )
    {
        return ResponseEntity.ok(accountService.update(
                accountId,
                accountDto
        ));
    }
    
    @DeleteMapping("/{accountId}")
    public Mono<Void> delete(
            @PathVariable
            Long accountId
    )
    {
        return accountService.delete(accountId);
    }
}
