package com.pichincha.services.controller;

import com.pichincha.services.service.MovementService;
import com.pichincha.services.service.dto.MovementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController
{
    private final MovementService movementService;
    
    @GetMapping("/{movementId}")
    public Mono<ResponseEntity<Mono<MovementDto>>> get(
            @PathVariable
            Long movementId
    )
    {
        return Mono.just(ResponseEntity.ok(movementService.get(movementId)));
    }
    
    @GetMapping("/report/{personId}/{initDate}/{finishDate}")
    public Mono<ResponseEntity<Flux<MovementDto>>> getReportMovement(
            @PathVariable
            Long personId,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate initDate,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate finishDate
    )
    {
        return Mono.just(ResponseEntity.ok(movementService.findByPersonIdAndInitialDateAndFinishDate(
                "" + personId,
                initDate,
                finishDate
        )));
    }
    
    @PostMapping
    public Mono<ResponseEntity<Mono<MovementDto>>> create(
            @RequestBody
            @Valid MovementDto movementDto
    ) throws NoSuchAlgorithmException
    {
        return Mono.just(ResponseEntity.ok(movementService.create(movementDto)));
    }
    
    @PutMapping("/{movementId}")
    public Mono<ResponseEntity<Mono<MovementDto>>> update(
            @PathVariable
            Long movementId,
            @RequestBody
            @Valid MovementDto movementDto
    )
    {
        return Mono.just(ResponseEntity.ok(movementService.update(
                movementId,
                movementDto
        )));
    }
    
    @DeleteMapping("/{movementId}")
    public Mono<Void> delete(
            @PathVariable
            Long movementId
    )
    {
        return movementService.delete(movementId);
    }
}
