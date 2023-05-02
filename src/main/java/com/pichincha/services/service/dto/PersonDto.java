package com.pichincha.services.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonDto
{
    private String  id;
    private String  name;
    private String  gender;
    private Integer age;
    private String  address;
    private String  phone;
}
