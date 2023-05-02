package com.pichincha.services.controller;

import com.pichincha.services.service.ClientService;
import com.pichincha.services.service.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController
{
    private final ClientService clientService;
    
    @GetMapping("/{personId}")
    public ResponseEntity<Mono<ClientDto>> get(
            @PathVariable
            String personId
    )
    {
        return ResponseEntity.ok(clientService.get(personId));
    }
    
    @PostMapping
    public ResponseEntity<Mono<ClientDto>> create(
            @RequestBody
            @Valid ClientDto clientDto
    ) throws NoSuchAlgorithmException
    {
        return ResponseEntity.ok(clientService.create(clientDto));
    }
    
    @PutMapping("/{clientId}")
    public ResponseEntity<Mono<ClientDto>> update(
            @PathVariable
            Long clientId,
            @RequestBody
            @Valid ClientDto clientDto
    )
    {
        return ResponseEntity.ok(clientService.update(
                clientId,
                clientDto
        ));
    }
    
    @DeleteMapping("/{clientId}")
    public Mono<Void> delete(
            @PathVariable
            Long clientId
    )
    {
        return clientService.delete(clientId);
    }
}
