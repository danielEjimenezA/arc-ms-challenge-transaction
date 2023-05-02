package com.pichincha.services.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
@Builder
@Data
public class Client implements Persistable<Long>
{
    @Id
    @Column("client_id")
    private Long id;
    @Column("client_password")
    private String password;
    private Boolean status;
    @Column("person_id")
    private String personId;
    
    @Transient
    private boolean newClient;
    
    @Override
    public boolean isNew()
    {
        return this.newClient || id == null;
    }
    
    public Client setAsNew()
    {
        this.newClient = true;
        return this;
    }
}