package com.pichincha.services.repository;

import com.pichincha.services.domain.Movement;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface MovementRepository extends ReactiveCrudRepository<Movement, Long>
{
    Flux<Movement> findAllByMovementDate(LocalDate date);
    Flux<Movement> findAllByMovementDateAndMovementType(LocalDate date, String movementType);
    @Query("SELECT SUM(m.movement_value) FROM movement m WHERE movement_date = :date AND movement_type = :movementType")
    Mono<Double> findAmountDailyByMovementDateAndMovementType(LocalDate date, String movementType);
    
    @Query("SELECT * FROM movement m INNER JOIN account a on m.account_id = a.account_id INNER JOIN client c on a.client_id = c.client_id WHERE c.person_id = :personId AND m.movement_date >= :initDate AND m.movement_date <= :finishDate")
    Flux<Movement> findByPersonIdAndInitialDateAndFinishDate(
            String personId,
            LocalDate initDate,
            LocalDate finishDate
    );
}
