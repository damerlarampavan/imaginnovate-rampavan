package com.imaginnovate.test.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imaginnovate.test.models.EmployeeDTO;
import com.imaginnovate.test.models.EmployeeTaxDTO;
import com.imaginnovate.test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    Gson gson;

    public Map<String, Object> createEmployee(EmployeeDTO employeeDTO){
        employeeDTO.setId(UUID.randomUUID());
        employeeRepository.save(employeeDTO);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> employeeMap = gson.fromJson(gson.toJson(employeeDTO), type);
        return  employeeMap;
    }

    public EmployeeTaxDTO getTaxAmount(UUID id) {
        var employeeDetails = employeeRepository.getById(id);
        if(employeeDetails == null)
            return new EmployeeTaxDTO();
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
