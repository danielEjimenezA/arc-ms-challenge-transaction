package com.pichincha.services.service.impl;

import com.pichincha.services.domain.Account;
import com.pichincha.services.error.ApiRequestException;
import com.pichincha.services.repository.AccountRepository;
import com.pichincha.services.repository.ClientRepository;
import com.pichincha.services.repository.MovementRepository;
import com.pichincha.services.service.MovementService;
import com.pichincha.services.service.dto.MovementDto;
import com.pichincha.services.service.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService
{
    private final ClientRepository clientRepository;
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    @Value("${transaction.movement.limitAmountDaily}")
    private Double limitAmountDaily;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<MovementDto> get(Long movementId)
    {
        return movementRepository
                .findById(movementId)
                .map(MovementMapper.INSTANCE::toMovementDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Flux<MovementDto> findByPersonIdAndInitialDateAndFinishDate(
            String personId,
            LocalDate initDate,
            LocalDate finishDate
    )
    {
        return movementRepository
                .findByPersonIdAndInitialDateAndFinishDate(
                        new Long(personId),
                        initDate,
                        finishDate
                )
                .map(MovementMapper.INSTANCE::toMovementDto);
    }
    
    @Override
    @Transactional
    public Mono<MovementDto> create(MovementDto movementDto) throws NoSuchAlgorithmException
    {
        accountRepository
                .findById(movementDto.getAccountId())
                .doOnSuccess(account ->
                             {
                                 switch (movementDto.getMovementType())
                                 {
                                     case "Depósito":
                                         depositMovement(
                                                 movementDto,
                                                 account
                                         );
                                         break;
                                     case "Débito":
                                         debitMovement(
                                                 movementDto,
                                                 account
                                         );
                                         break;
                                     default:
                                 }
                    
                                 movementDto.setBalance(account.getInitialAmount());
                             });
        Random rand = SecureRandom.getInstanceStrong();
        movementDto.setId(rand.nextLong());
        return movementRepository
                .save(MovementMapper.INSTANCE
                              .toMovement(movementDto)
                              .setAsNew())
                .map(MovementMapper.INSTANCE::toMovementDto);
    }
    
    private void debitMovement(
            MovementDto movementDto,
            Account account
    )
    {
        if (account
                    .getInitialAmount()
                    .compareTo(movementDto.getMovementValue()) > 0)
        {
            limitAmountDaily = 0.0;
            movementRepository
                    .findAllByMovementDate(LocalDate.now())
                    .map(movement -> limitAmountDaily + movement.getMovementValue());
            
            if (limitAmountDaily != 1000)
            {
                account.setInitialAmount(account.getInitialAmount() - movementDto.getMovementValue());
                accountRepository.save(account);
            }
            else
            {
                throw new ApiRequestException("Cupo diario excedido");
            }
            
        }
        else
        {
            throw new ApiRequestException("Saldo no disponible");
        }
    }
    
    private void depositMovement(
            MovementDto movementDto,
            Account account
    )
    {
        account.setInitialAmount(account.getInitialAmount() + movementDto.getMovementValue());
        accountRepository.save(account);
    }
    
    @Override
    @Transactional
    public Mono<MovementDto> update(
            Long movementId,
            MovementDto movementDto
    )
    {
        return movementRepository
                .findById(movementId)
                .map(movement ->
                     {
                         movement.setMovementValue(movementDto.getMovementValue());
                         movement.setMovementDate(movementDto.getMovementDate());
                         movement.setAccountId(movementDto.getAccountId());
                         movement.setBalance(movementDto.getBalance());
                         movement.setMovementType(movementDto.getMovementType());
                         return movement;
                     })
                .flatMap(movementRepository::save)
                .map(MovementMapper.INSTANCE::toMovementDto);
    }
    
    @Override
    @Transactional
    public Mono<Void> delete(Long movementId)
    {
        return movementRepository.deleteById(movementId);
    }
}
