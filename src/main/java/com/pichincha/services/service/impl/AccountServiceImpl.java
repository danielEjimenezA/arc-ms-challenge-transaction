package com.pichincha.services.service.impl;

import com.pichincha.services.repository.AccountRepository;
import com.pichincha.services.service.AccountService;
import com.pichincha.services.service.dto.AccountDto;
import com.pichincha.services.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService
{
    private final AccountRepository accountRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<AccountDto> get(Long accountId)
    {
        return accountRepository
                .findById(accountId)
                .map(AccountMapper.INSTANCE::toAccountDto);
    }
    
    @Override
    public Mono<AccountDto> getByAccountNumber(Long accountNumber)
    {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .map(AccountMapper.INSTANCE::toAccountDto);
    }
    
    @Override
    @Transactional
    public Mono<AccountDto> create(AccountDto accountDto) throws NoSuchAlgorithmException
    {
        Random rand = SecureRandom.getInstanceStrong();
        accountDto.setId(rand.nextLong());
        return accountRepository
                .save(AccountMapper.INSTANCE.toAccount(accountDto))
                .map(AccountMapper.INSTANCE::toAccountDto);
    }
    
    @Override
    @Transactional
    public Mono<AccountDto> update(
            Long accountId,
            AccountDto accountDto
    )
    {
        return accountRepository
                .findById(accountId)
                .map(account ->
                     {
                         account.setAccountNumber(accountDto.getAccountNumber());
                         account.setAccountType(accountDto.getAccountType());
                         account.setClientId(accountDto.getClientId());
                         account.setStatus(accountDto.getStatus());
                         account.setInitialAmount(accountDto.getInitialAmount());
                         return account;
                     })
                .flatMap(accountRepository::save)
                .map(AccountMapper.INSTANCE::toAccountDto);
    }
    
    @Override
    @Transactional
    public Mono<Void> delete(Long accountId)
    {
        return accountRepository.deleteById(accountId);
    }
}
