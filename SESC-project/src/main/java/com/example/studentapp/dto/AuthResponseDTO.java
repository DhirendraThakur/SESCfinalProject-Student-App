package com.example.studentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
}