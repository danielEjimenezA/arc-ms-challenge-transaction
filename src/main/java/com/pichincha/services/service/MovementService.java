package com.pichincha.services.service;

import com.pichincha.services.service.dto.MovementDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public interface MovementService
{
    Mono<MovementDto> get(Long movementId);
    
    Flux<MovementDto> findByPersonIdAndInitialDateAndFinishDate(
            Long personId,
            LocalDate initDate,
            LocalDate finishDate
    );
    
    Mono<MovementDto> create(MovementDto movementDto) throws NoSuchAlgorithmException;
    
    Mono<MovementDto> update(
            Long movementId,
            MovementDto movementDto
    );
    
    Mono<Void> delete(
            Long movementId
    );
}
