package com.pichincha.services.service.impl;

import com.pichincha.services.domain.Account;
import com.pichincha.services.error.ApiRequestException;
import com.pichincha.services.error.NotFoundException;
import com.pichincha.services.repository.AccountRepository;
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
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    @Value("${transaction.movement.limitAmountDaily}")
    private Double limitAmountDaily;
    private MovementDto movementDto;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<MovementDto> get(Long movementId)
    {
        return movementRepository
                .findById(movementId)
                .map(MovementMapper.INSTANCE::toMovementDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found Movement by id")));
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
                        personId,
                        initDate,
                        finishDate
                )
                .map(MovementMapper.INSTANCE::toMovementDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found Movements by person id in the entered date")));
    }
    
    @Override
    @Transactional
    public Mono<MovementDto> create(MovementDto movementDto) throws NoSuchAlgorithmException
    {
        this.movementDto = MovementDto
                .builder()
                .build();
        return accountRepository
                .findById(movementDto.getAccountId())
                .map(account ->
                     {
                         switch (movementDto.getMovementType())
                         {
                             case "Depósito":
                                 try
                                 {
                                     this.movementDto = depositMovement(
                                             movementDto,
                                             account
                                     );
                                 } catch (NoSuchAlgorithmException e)
                                 {
                                     throw new ApiRequestException(e.getMessage());
                                 }
                                 break;
                             case "Débito":
                                 try
                                 {
                                     this.movementDto = debitMovement(
                                             movementDto,
                                             account
                                     );
                                 } catch (NoSuchAlgorithmException e)
                                 {
                                     throw new ApiRequestException(e.getMessage());
                                 }
                                 break;
                             default:
                         }
                         return this.movementDto;
                     })
                .flatMap(movementDto1 ->
                         {
                             movementDto1.setMovementDate(LocalDate.now());
                             return movementRepository
                                     .save(MovementMapper.INSTANCE
                                                   .toMovement(this.movementDto)
                                                   .setAsNew())
                                     .map(MovementMapper.INSTANCE::toMovementDto);
                         });
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
    
    private MovementDto debitMovement(
            MovementDto movementDto,
            Account account
    ) throws NoSuchAlgorithmException
    {
        if (account
                    .getInitialAmount()
                    .compareTo(movementDto.getMovementValue()) > 0)
        {
            movementDto.setBalance(account.getInitialAmount() - movementDto.getMovementValue());
            movementRepository
                    .findAmountDailyByMovementDateAndMovementType(
                            LocalDate.now(),
                            "Débito"
                    )
                    .subscribe(amountDaily ->
                               {
                                   log.info(
                                           "AmountDaily query = {}",
                                           amountDaily
                                   );
                                   if (amountDaily < limitAmountDaily)
                                   {
                                       account.setInitialAmount(movementDto.getBalance());
                                       accountRepository.save(account);
                                   }
                                   else
                                   {
                                       throw new ApiRequestException("Cupo diario excedido");
                                   }
                               });
        }
        else
        {
            throw new ApiRequestException("Saldo no disponible");
        }
        Random rand = SecureRandom.getInstanceStrong();
        movementDto.setId(rand.nextLong());
        movementDto.setMovementDate(LocalDate.now());
        return movementDto;
    }
    
    private MovementDto depositMovement(
            MovementDto movementDto,
            Account account
    ) throws NoSuchAlgorithmException
    {
        movementDto.setBalance(account.getInitialAmount() + movementDto.getMovementValue());
        account.setInitialAmount(account.getInitialAmount() + movementDto.getMovementValue());
        accountRepository.save(account);
        
        Random rand = SecureRandom.getInstanceStrong();
        movementDto.setId(rand.nextLong());
        movementDto.setMovementDate(LocalDate.now());
        return movementDto;
    }
}
