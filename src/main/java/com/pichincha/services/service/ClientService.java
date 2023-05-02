package com.pichincha.services.service;

import com.pichincha.services.service.dto.ClientDto;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;

public interface ClientService
{
    Mono<ClientDto> get(String personId);
    
    Mono<ClientDto> create(ClientDto clientDto) throws NoSuchAlgorithmException;
    
    Mono<ClientDto> update(
            Long clientId,
            ClientDto clientDto
    );
    
    Mono<Void> delete(
            Long clientId
    );
}
