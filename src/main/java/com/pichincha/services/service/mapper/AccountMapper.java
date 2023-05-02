package com.pichincha.services.service.mapper;

import com.pichincha.services.domain.Account;
import com.pichincha.services.service.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper
{
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    
    @Mapping(
            source = "id",
            target = "id"
    )
    @Mapping(
            source = "clientId",
            target = "clientId"
    )
    @Mapping(
            source = "accountType",
            target = "accountType"
    )
    @Mapping(
            source = "initialAmount",
            target = "initialAmount"
    )
    @Mapping(
            source = "status",
            target = "status"
    )
    Account toAccount(AccountDto accountDto);
    
    @Mapping(
            source = "id",
            target = "id"
    )
    @Mapping(
            source = "clientId",
            target = "clientId"
    )
    @Mapping(
            source = "accountType",
            target = "accountType"
    )
    @Mapping(
            source = "initialAmount",
            target = "initialAmount"
    )
    @Mapping(
            source = "status",
            target = "status"
    )
    AccountDto toAccountDto(Account account);
}
