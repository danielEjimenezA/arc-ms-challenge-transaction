package com.pichincha.services.repository;

import com.pichincha.services.domain.Movement;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface MovementRepository extends ReactiveCrudRepository<Movement, Long>
{
    Flux<Movement> findAllByMovementDate(LocalDate date);
    
    @Query("SELECT m FROM Movement m WHERE m.account_id = :accountId AND m.movement_date >= :initDate AND m.movement_date <= :finishDate")
    Flux<Movement> findByPersonIdAndInitialDateAndFinishDate(
            Long accountId,
            LocalDate initDate,
            LocalDate finishDate
    );
}
