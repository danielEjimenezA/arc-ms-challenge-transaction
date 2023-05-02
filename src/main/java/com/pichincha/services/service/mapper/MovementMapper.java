package com.pichincha.services.service.mapper;

import com.pichincha.services.domain.Movement;
import com.pichincha.services.service.dto.MovementDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovementMapper
{
    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);
    
    Movement toMovement(MovementDto movementDto);
    
    MovementDto toMovementDto(Movement movement);
}
