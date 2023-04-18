package com.imaginnovate.test.controller;

import com.imaginnovate.test.models.EmployeeDTO;
import com.imaginnovate.test.models.EmployeeTaxDTO;
import com.imaginnovate.test.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping(path ="")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.createEmployee(employeeDTO);
    }

    @GetMapping(path = "")
    public EmployeeTaxDTO getEmployeeTaxDetails(@RequestParam UUID employeeId){
        return employeeService.getTaxAmount(employeeId);
    }

}
