package com.example.RestfulWebService.repositories;

import com.example.RestfulWebService.dtos.EmployeeDto;
import com.example.RestfulWebService.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}
