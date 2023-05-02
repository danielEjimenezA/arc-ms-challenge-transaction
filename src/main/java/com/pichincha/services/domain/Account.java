package com.pichincha.services.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("account")
@Data
public class Account implements Persistable<Long>
{
    @Id
    @Column("account_id")
    private Long id;
    @Column("client_id")
    private Long clientId;
    @Column("account_number")
    private Long accountNumber;
    @Column("account_type")
    private String accountType;
    @Column("initial_amount")
    private Double initialAmount;
    private Boolean status;
    
    public Account(
            Long id,
            Long clientId,
            Long accountNumber,
            String accountType,
            Double initialAmount,
            Boolean status
    )
    {
        this.id = id;
        this.clientId = clientId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.initialAmount = initialAmount;
        this.status = status;
    }
    
    @Transient
    private boolean newAccount;
    
    @Override
    public boolean isNew()
    {
        return this.newAccount || id == null;
    }
    
    public Account setAsNew()
    {
        this.newAccount = true;
        return this;
    }
}