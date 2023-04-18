package com.imaginnovate.test.service;

import com.imaginnovate.test.models.EmployeeDTO;
import com.imaginnovate.test.models.EmployeeTaxDTO;
import com.imaginnovate.test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO){
        employeeDTO.setId(UUID.randomUUID());
        return employeeRepository.save(employeeDTO);
    }

    public EmployeeTaxDTO getTaxAmount(UUID id) {
        var employeeDetails = employeeRepository.getById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the string to LocalDateTime
        var dateOfJoining = LocalDate.parse(employeeDetails.getDoj(), formatter);
        var totalSalary = getYearSalary(LocalDate.of(dateOfJoining.getYear(), dateOfJoining.getMonth(), dateOfJoining.getDayOfMonth()), employeeDetails.getSalary());
        var cessAmount = getCessAmount(totalSalary);
        var taxAmount = getTaxAmount(totalSalary);
        return new EmployeeTaxDTO(employeeDetails.getId(), employeeDetails.getFirstName(), employeeDetails.getLastName(), totalSalary,taxAmount,cessAmount);
    }

    private long getYearSalary(LocalDate doj, long monthlySalary){
        if(doj.getYear() < LocalDate.now().getYear())
            return (monthlySalary * 12);
        var lossOfPayDays =  ChronoUnit.DAYS.between(LocalDate.of(doj.getYear(), Month.APRIL, 1), doj);
        if(lossOfPayDays <= 0)
            return  (monthlySalary * 12);
        var lossOfPayPerDay = monthlySalary / 30;
        var lossOfPay = lossOfPayPerDay * (lossOfPayDays);
        var totalSalary = (monthlySalary * 12) - lossOfPay;
        return totalSalary;
    }

    public static double getCessAmount(long yearSalary) {
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
            taxAmount = (yearSalary - 500000) * 0.1 + 12500;
        } else if (yearSalary > 1000000) {
            taxAmount = (yearSalary - 1000000) * 0.2 + 12500 + 25000;
        }
        return taxAmount;
    }
}
