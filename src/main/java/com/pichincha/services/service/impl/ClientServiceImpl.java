package com.pichincha.services.service.impl;

import com.pichincha.services.repository.ClientRepository;
import com.pichincha.services.repository.PersonRepository;
import com.pichincha.services.service.ClientService;
import com.pichincha.services.service.dto.ClientDto;
import com.pichincha.services.service.mapper.ClientMapper;
import com.pichincha.services.service.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService
{
    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<ClientDto> get(String personId)
    {
        return Mono
                .zip(
                        clientRepository
                                .findByPersonId(personId)
                                .map(ClientMapper.INSTANCE::toClientDto),
                        personRepository
                                .findById(personId)
                                .map(PersonMapper.INSTANCE::toClientDto)
                )
                .map(objects -> ClientDto
                        .builder()
                        .clientId(objects
                                          .getT1()
                                          .getClientId())
                        .personId(objects
                                          .getT1()
                                          .getPersonId())
                        .name(objects
                                      .getT2()
                                      .getName())
                        .gender(objects
                                        .getT2()
                                        .getGender())
                        .age(objects
                                     .getT2()
                                     .getAge())
                        .address(objects
                                         .getT2()
                                         .getAddress())
                        .phone(objects
                                       .getT2()
                                       .getPhone())
                        .password(objects
                                          .getT2()
                                          .getPassword())
                        .status(objects
                                        .getT2()
                                        .getStatus())
                        .build());
    }
    
    @Override
    @Transactional
    public Mono<ClientDto> create(ClientDto clientDto) throws NoSuchAlgorithmException
    {
        Random rand = SecureRandom.getInstanceStrong();
        clientDto.setClientId(rand.nextLong());
        
        personRepository.save(ClientMapper.INSTANCE.toPerson(clientDto));
        
        return clientRepository
                .save(ClientMapper.INSTANCE
                              .toClient(clientDto)
                              .setAsNew())
                .map(ClientMapper.INSTANCE::toClientDto);
    }
    
    @Override
    @Transactional
    public Mono<ClientDto> update(
            Long clientId,
            ClientDto clientDto
    )
    {
        return clientRepository
                .findById(clientId)
                .map(client ->
                     {
                         personRepository.save(ClientMapper.INSTANCE.toPerson(clientDto));
                         
                         client.setPassword(clientDto.getPassword());
                         client.setPersonId(clientDto.getPersonId());
                         client.setStatus(clientDto.getStatus());
                         return client;
                     })
                .flatMap(clientRepository::save)
                .map(ClientMapper.INSTANCE::toClientDto);
    }
    
    @Override
    @Transactional
    public Mono<Void> delete(Long clientId)
    {
        clientRepository
                .findById(clientId)
                .map(client ->
                     {
                         personRepository.deleteById(client.getPersonId());
                         return client;
                     });
        return clientRepository.deleteById(clientId);
    }
}
