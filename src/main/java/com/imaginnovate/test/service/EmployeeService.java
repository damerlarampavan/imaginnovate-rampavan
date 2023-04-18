package com.imaginnovate.test.service;

import com.imaginnovate.test.models.EmployeeDTO;
import com.imaginnovate.test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO){
        employeeDTO.setId(UUID.randomUUID());
        return employeeRepository.save(employeeDTO);
    }

    private long getYearSalary(LocalDate doj, long monthlySalary){
        var lossOfPayPerDay = monthlySalary / 30;
        var daysInCurrentMonth = LocalDate.now().lengthOfMonth();
        var daysInLastMonth = doj.lengthOfMonth();
        var lossOfPay = lossOfPayPerDay * (daysInCurrentMonth + daysInLastMonth);
        var totalSalary = (monthlySalary * 12) - lossOfPay;
        return totalSalary;
    }

    public static double getCessAmount(int yearSalary) {
        double cessAmount = 0;
        if (yearSalary > 2500000) {
            cessAmount = (yearSalary - 2500000) * 0.02;
        }
        return cessAmount;
    }

    private double getTaxAmount(long yearSalary){
        double taxAmount = 0;
        if (yearSalary > 250000 && yearSalary <= 500000) {
            taxAmount = (yearSalary - 250000) * 0.05;
        } else if (yearSalary > 500000 && yearSalary <= 1000000) {
            taxAmount = (yearSalary - 500000) * 0.1 + 25000;
        } else if (yearSalary > 1000000) {
            taxAmount = (yearSalary - 1000000) * 0.2 + 75000;
        }
        return taxAmount;
    }
}
