package com.pichincha.services.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MovementDto
{
    private Long id;
    private Long accountId;
    private LocalDate movementDate;
    private String movementType;
    private Double movementValue;
    private Double balance;
}
