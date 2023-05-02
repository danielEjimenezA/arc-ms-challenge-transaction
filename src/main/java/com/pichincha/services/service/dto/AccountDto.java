package com.pichincha.services.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDto
{
    private Long id;
    private Long clientId;
    private Long accountNumber;
    private String accountType;
    private Double initialAmount;
    private Boolean status;
}
