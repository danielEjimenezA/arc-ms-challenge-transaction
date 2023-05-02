package com.pichincha.services.repository;

import com.pichincha.services.domain.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Long>
{
    Mono<Client> findByPersonId(String personId);
}
