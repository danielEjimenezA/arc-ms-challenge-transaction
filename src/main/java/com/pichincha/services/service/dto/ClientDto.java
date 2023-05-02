package com.pichincha.services.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientDto
{
    private Long clientId;
    private String personId;
    private String name;
    private String gender;
    private Integer age;
    private String address;
    private String phone;
    private String password;
    private Boolean status;
}
