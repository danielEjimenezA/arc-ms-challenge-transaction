package com.pichincha.services.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person")
@Builder
@Data
public class Person implements Persistable<String>
{
    @Id
    private String id;
    @Column("person_name")
    private String name;
    private String gender;
    private Integer age;
    private String address;
    private String phone;
    
    @Transient
    private boolean newPerson;
    
    @Override
    public boolean isNew()
    {
        return this.newPerson || id == null;
    }
    
    public Person setAsNew()
    {
        this.newPerson = true;
        return this;
    }
}