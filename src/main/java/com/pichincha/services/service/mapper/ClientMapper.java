package com.pichincha.services.service.mapper;

import com.pichincha.services.domain.Client;
import com.pichincha.services.domain.Person;
import com.pichincha.services.service.dto.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper
{
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    
    @Mapping(
            source = "clientId",
            target = "id"
    )
    Client toClient(ClientDto clientDto);
    
    @Mapping(
            source = "id",
            target = "clientId"
    )
    ClientDto toClientDto(Client client);
    
    @Mapping(
            source = "personId",
            target = "id"
    )
    Person toPerson(ClientDto clientDto);
}
