package com.pichincha.services.repository;

import com.pichincha.services.domain.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, String>
{
    Mono<Person> findById(String id);
}
