package com.pichincha.services.service.impl;

import com.pichincha.services.domain.Account;
import com.pichincha.services.domain.Client;
import com.pichincha.services.domain.Movement;
import com.pichincha.services.domain.Person;
import com.pichincha.services.repository.AccountRepository;
import com.pichincha.services.repository.ClientRepository;
import com.pichincha.services.repository.MovementRepository;
import com.pichincha.services.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {MovementServiceImplTest.class})
@SpringBootTest
class MovementServiceImplTest
{
    @InjectMocks
    MovementServiceImpl movementService;
    
    @Mock
    MovementRepository movementRepository;
    @Mock
    PersonRepository personRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    AccountRepository accountRepository;
    
    @BeforeEach
    void setUp()
    {
    }
    
    @Test
    void findByPersonIdAndInitialDateAndFinishDate()
    {
        Mockito
                .when(personRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(buildPerson()));
        Mockito
                .when(clientRepository.findByPersonId(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(buildClient()));
        Mockito
                .when(accountRepository.findByClientId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.fromStream(buildAccounts().stream()));
        Mockito
                .when(movementRepository.findByPersonIdAndInitialDateAndFinishDate(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.any()
                ))
                .thenReturn(Flux.fromStream(buildMovements().stream()));
    }
    
    public static List<Movement> buildMovements()
    {
        Movement movement1 = new Movement(
                1L,
                1123L,
                LocalDate.now(),
                "Débito",
                200.0,
                3000.0
        );
        Movement movement2 = new Movement(
                1L,
                1123L,
                LocalDate.now(),
                "Depósito",
                200.0,
                3000.0
        );
        
        List<Movement> movementList = new ArrayList<>();
        movementList.add(movement1);
        movementList.add(movement2);
        
        return movementList;
    }
    
    public static Account buildAccount()
    {
        Account account = new Account(
                1123L,
                4321L,
                123567L,
                "Ahorros",
                3000.0,
                true
        );
        
        return account;
    }
    
    public static List<Account> buildAccounts()
    {
        Account account1 = new Account(
                1123L,
                4321L,
                123567L,
                "Ahorros",
                3000.0,
                true
        );
        
        Account account2 = new Account(
                45675L,
                4321L,
                987463L,
                "Ahorros",
                3000.0,
                true
        );
        
        List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);
        return accountList;
    }
    
    public static Client buildClient()
    {
        Client client = new Client(
                4321L,
                "ABCD123",
                true,
                "1725374134"
        );
        return client;
    }
    
    public static Person buildPerson()
    {
        Person person = new Person(
                "1725374126",
                "Daniel Jmenez",
                "Masculino",
                27,
                "Iñaquito",
                "0998320368"
        );
        return person;
    }
    
}