package com.pichincha.services.repository;

import com.pichincha.services.domain.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long>
{
    Mono<Account> findByAccountNumber(Long accountNumber);
    
}
