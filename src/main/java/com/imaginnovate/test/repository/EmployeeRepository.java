package com.imaginnovate.test.repository;

import com.imaginnovate.test.models.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeDTO, UUID> {

}
