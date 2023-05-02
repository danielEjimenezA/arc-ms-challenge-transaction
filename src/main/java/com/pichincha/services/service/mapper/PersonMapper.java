package com.pichincha.services.service.mapper;

import com.pichincha.services.domain.Person;
import com.pichincha.services.service.dto.ClientDto;
import com.pichincha.services.service.dto.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper
{
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    
    Person toPerson(PersonDto personDto);
    
    PersonDto toPersonDto(Person person);
    
    @Mapping(
            source = "id",
            target = "personId"
    )
    ClientDto toClientDto(Person person);
}
