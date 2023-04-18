package com.imaginnovate.test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTaxDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private long salary;
    private double taxAmount;
    private double cessAmount;
}
