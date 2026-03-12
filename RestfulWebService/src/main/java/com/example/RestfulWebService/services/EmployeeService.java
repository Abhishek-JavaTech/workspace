package com.example.RestfulWebService.services;

import com.example.RestfulWebService.dtos.EmployeeDto;
import com.example.RestfulWebService.entities.EmployeeEntity;
import com.example.RestfulWebService.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Page<EmployeeEntity> getAll(Pageable pageable){
        return employeeRepository.findAll(pageable);
    }

    public Optional<EmployeeDto> findByEmail(String email){
        var optionalEmployee = employeeRepository.findByEmail(email);

        if(optionalEmployee.isPresent()){
            return Optional.of(objectMapper.convertValue(optionalEmployee.get(), EmployeeDto.class));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<EmployeeDto> updateByEmail(String email, EmployeeDto employeeDto){
        var optionalExistingEmployee = employeeRepository.findByEmail(email);
        if(optionalExistingEmployee.isEmpty()){
            return Optional.empty();
        }else{
            var existingEmployee = optionalExistingEmployee.get();
            existingEmployee.setEmail(employeeDto.getEmail());
            existingEmployee.setCountry(employeeDto.getCountry());
            existingEmployee.setState(employeeDto.getState());
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setZipCode(employeeDto.getZipCode());
            existingEmployee.setMobileNumber(employeeDto.getMobileNumber());
//            employeeRepository.save(objectMapper.convertValue(existingEmployee, EmployeeEntity.class));
            employeeRepository.save(existingEmployee);
            return Optional.of(objectMapper.convertValue(existingEmployee, EmployeeDto.class));
        }
    }

    @Transactional
    public Optional<EmployeeDto> patchUpdateByEmail(String email, EmployeeDto employeeDto){
        var optionalExistingEmployee = employeeRepository.findByEmail(email);
        if(optionalExistingEmployee.isEmpty()){
            return Optional.empty();
        }else{
            var existingEmployee = optionalExistingEmployee.get();

            if(employeeDto.getEmail() != null)
                existingEmployee.setEmail(employeeDto.getEmail());

            if(employeeDto.getCountry() != null)
                existingEmployee.setCountry(employeeDto.getCountry());

            if(employeeDto.getState() != null)
                existingEmployee.setState(employeeDto.getState());

            if(employeeDto.getFirstName() != null)
                existingEmployee.setFirstName(employeeDto.getFirstName());

            if(employeeDto.getLastName() != null)
                existingEmployee.setLastName(employeeDto.getLastName());

            if(employeeDto.getZipCode() != null)
                existingEmployee.setZipCode(employeeDto.getZipCode());

            if(employeeDto.getMobileNumber() != null)
                existingEmployee.setMobileNumber(employeeDto.getMobileNumber());

            employeeRepository.save(objectMapper.convertValue(existingEmployee, EmployeeEntity.class));
            return Optional.of(objectMapper.convertValue(existingEmployee, EmployeeDto.class));
        }
    }

    public boolean deleteByEmail(String email){
        try {
            employeeRepository.deleteByEmail(email);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean createEmployee(EmployeeDto employeeDto){
        var createdEmployee = employeeRepository.save(new ObjectMapper().convertValue(employeeDto, EmployeeEntity.class));
        return createdEmployee != null ? true : false;
    }

}
