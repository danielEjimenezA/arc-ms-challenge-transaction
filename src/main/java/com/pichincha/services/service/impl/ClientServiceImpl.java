package com.pichincha.services.service.impl;

import com.pichincha.services.error.ApiRequestException;
import com.pichincha.services.repository.ClientRepository;
import com.pichincha.services.repository.PersonRepository;
import com.pichincha.services.service.ClientService;
import com.pichincha.services.service.dto.ClientDto;
import com.pichincha.services.service.mapper.ClientMapper;
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
                        clientRepository.findByPersonId(personId),
                        personRepository.findById(personId)
                )
                .map(objects -> ClientDto
                        .builder()
                        .clientId(objects
                                          .getT1()
                                          .getId())
                        .personId(objects
                                          .getT2()
                                          .getId())
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
                                          .getT1()
                                          .getPassword())
                        .status(objects
                                        .getT1()
                                        .getStatus())
                        .build());
    }
    
    @Override
    @Transactional
    public Mono<ClientDto> create(ClientDto clientDto) throws NoSuchAlgorithmException
    {
        Random rand = SecureRandom.getInstanceStrong();
        clientDto.setClientId(rand.nextLong());
        return Mono
                .zip(
                        personRepository.save(ClientMapper.INSTANCE
                                                      .toPerson(clientDto)
                                                      .setAsNew()),
                        clientRepository.save(ClientMapper.INSTANCE
                                                      .toClient(clientDto)
                                                      .setAsNew())
                )
                .doOnError(throwable ->
                           {
                               throw new ApiRequestException("Client already exist");
                           })
                .map(objects -> ClientDto
                        .builder()
                        .clientId(objects
                                          .getT2()
                                          .getId())
                        .personId(objects
                                          .getT1()
                                          .getId())
                        .name(objects
                                      .getT1()
                                      .getName())
                        .gender(objects
                                        .getT1()
                                        .getGender())
                        .age(objects
                                     .getT1()
                                     .getAge())
                        .address(objects
                                         .getT1()
                                         .getAddress())
                        .phone(objects
                                       .getT1()
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
    public Mono<ClientDto> update(
            Long clientId,
            ClientDto clientDto
    )
    {
        return Mono
                .zip(
                        personRepository.save(ClientMapper.INSTANCE.toPerson(clientDto)),
                        clientRepository.save(ClientMapper.INSTANCE.toClient(clientDto))
                )
                .doOnError(throwable ->
                           {
                               throw new ApiRequestException("Error to update client.");
                           })
                .map(objects -> ClientDto
                        .builder()
                        .clientId(objects
                                          .getT2()
                                          .getId())
                        .personId(objects
                                          .getT1()
                                          .getId())
                        .name(objects
                                      .getT1()
                                      .getName())
                        .gender(objects
                                        .getT1()
                                        .getGender())
                        .age(objects
                                     .getT1()
                                     .getAge())
                        .address(objects
                                         .getT1()
                                         .getAddress())
                        .phone(objects
                                       .getT1()
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
    public Mono<Void> delete(Long clientId)
    {
        clientRepository
                .findById(clientId)
                .map(client ->
                     {
                         personRepository.deleteById(client.getPersonId());
                         return client;
                     })
                .doOnSuccess(client -> log.info(
                        "Process to delete client {} success",
                        client.getId()
                ));
        return clientRepository.deleteById(clientId);
    }
}
