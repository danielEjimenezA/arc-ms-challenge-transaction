package com.pichincha.services.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("movement")
@Builder
@Data
public class Movement implements Persistable<Long>
{
    @Id
    @Column("movement_id")
    private Long id;
    @Column("account_id")
    private Long accountId;
    @Column("movement_date")
    private LocalDate movementDate;
    @Column("movement_type")
    private String movementType;
    @Column("movement_value")
    private Double movementValue;
    private Double balance;
    
    @Transient
    private boolean newMovement;
    
    @Override
    public boolean isNew()
    {
        return this.newMovement || id == null;
    }
    
    public Movement setAsNew()
    {
        this.newMovement = true;
        return this;
    }
}