package com.pichincha.services.service;

import com.pichincha.services.service.dto.AccountDto;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;

public interface AccountService
{
    Mono<AccountDto> get(Long accountId);
    
    Mono<AccountDto> getByAccountNumber(Long accountNumber);
    
    Mono<AccountDto> create(AccountDto accountDto) throws NoSuchAlgorithmException;
    
    Mono<AccountDto> update(
            Long accountId,
            AccountDto accountDto
    );
    
    Mono<Void> delete(
            Long accountId
    );
    
}
