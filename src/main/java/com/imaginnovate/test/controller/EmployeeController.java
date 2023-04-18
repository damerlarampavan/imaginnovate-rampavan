package com.imaginnovate.test.controller;

import com.imaginnovate.test.models.EmployeeDTO;
import com.imaginnovate.test.models.EmployeeTaxDTO;
import com.imaginnovate.test.service.EmployeeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping(path = "")
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Map<String, Object> errorMessage = new HashMap<>();
        if (StringUtils.isEmpty(employeeDTO.getDoj())) {
            errorMessage.put("ERROR_01", "date of joining is missing");
        } else if (StringUtils.isEmpty(employeeDTO.getEmail())) {
            errorMessage.put("ERROR_02", "email is missing");
        } else if (StringUtils.isEmpty(employeeDTO.getFirstName())) {
            errorMessage.put("ERROR_03", "firstName is missing");
        } else if (StringUtils.isEmpty(employeeDTO.getLastName())) {
            errorMessage.put("ERROR_04", "lastName is missing");
        } else if (ObjectUtils.isEmpty(employeeDTO.getSalary())) {
            errorMessage.put("ERROR_05", "salary is missing");
        } else {
            return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "")
    public EmployeeTaxDTO getEmployeeTaxDetails(@RequestParam UUID employeeId){
        return employeeService.getTaxAmount(employeeId);
    }

}
