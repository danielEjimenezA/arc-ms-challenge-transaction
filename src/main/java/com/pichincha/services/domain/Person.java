package com.pichincha.services.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person")
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
    
    public Person(
            String id,
            String name,
            String gender,
            Integer age,
            String address,
            String phone
    )
    {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }
    
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