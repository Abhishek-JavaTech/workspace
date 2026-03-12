package com.example.RestfulWebService.controllers;

import com.example.RestfulWebService.dtos.EmployeeDto;
import com.example.RestfulWebService.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam("pageNumber") int pageNumber,
                                    @RequestParam("pageSize") int pageSize,
                                    @RequestParam(value = "orderBy", defaultValue = "email") List<String> ordersBy,
                                    @RequestParam(value = "orderDir", defaultValue = "asc") List<String> ordersDir) {

        try {
            List<Sort.Order> orders = new ArrayList<>();
            for (int counter = 0; counter < ordersBy.size(); counter++) {
                String property = ordersBy.get(counter);
                String direction = (counter < ordersDir.size()) ? ordersDir.get(counter) : "asc";
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), property));
            }

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orders));
            return ResponseEntity.status(HttpStatus.OK).body(
                    employeeService.getAll(pageable)
                            .getContent()
                            .stream()
                            .map(eachEmp -> objectMapper.convertValue(eachEmp, EmployeeDto.class))
                            .collect(Collectors.toList()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


    @GetMapping("/{email}")
    public ResponseEntity<?> getEmployee(@PathVariable("email") String email) {
        try {
            var optionalEmployee = employeeService.findByEmail(email);
            if (optionalEmployee.isPresent()) {
                var employeeDto = optionalEmployee.get();

                EntityModel<EmployeeDto> entityModel = EntityModel.of(employeeService.findByEmail(employeeDto.getEmail()).get());
                var getEmployee = linkTo(methodOn(EmployeeController.class).getEmployee(employeeDto.getEmail())).withSelfRel();
                var deleteEmployee = linkTo(methodOn(EmployeeController.class).deleteEmployee(employeeDto.getEmail())).withRel("deleteEmployee");
                entityModel.add(getEmployee, deleteEmployee);

                return ResponseEntity.status(HttpStatus.OK).body(entityModel);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> putEmployee(@PathVariable("email") String email, @RequestBody @Valid EmployeeDto employeeDto) {
        try {
            var optionalEmployee = employeeService.updateByEmail(email, employeeDto);
            if (optionalEmployee.isPresent()) {
                var employee = optionalEmployee.get();

                EntityModel<EmployeeDto> entityModel = EntityModel.of(employeeService.findByEmail(employee.getEmail()).get());
                var getEmployee = linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmail())).withRel("getEmployee");
                var deleteEmployee = linkTo(methodOn(EmployeeController.class).deleteEmployee(employee.getEmail())).withRel("deleteEmployee");
                entityModel.add(getEmployee, deleteEmployee);

                return ResponseEntity.status(HttpStatus.OK).body(entityModel);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<?> patchEmployee(@PathVariable("email") String email, @RequestBody @Valid EmployeeDto employeeDto) {
        try {
            var optionalEmployee = employeeService.patchUpdateByEmail(email, employeeDto);
            if (optionalEmployee.isPresent()) {
                var employee = optionalEmployee.get();

                EntityModel<EmployeeDto> entityModel = EntityModel.of(employeeService.findByEmail(employee.getEmail()).get());
                var getEmployee = linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmail())).withRel("getEmployee");
                var deleteEmployee = linkTo(methodOn(EmployeeController.class).deleteEmployee(employee.getEmail())).withRel("deleteEmployee");
                entityModel.add(getEmployee, deleteEmployee);

                return ResponseEntity.status(HttpStatus.OK).body(entityModel);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("email") String email) {
        try {
            var deleted = employeeService.deleteByEmail(email);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("Employee with %s email has been deleted", email));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> postEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            var created = employeeService.createEmployee(employeeDto);
            if (created) {
                EntityModel<EmployeeDto> entityModel = EntityModel.of(employeeService.findByEmail(employeeDto.getEmail()).get());
                var self = linkTo(methodOn(EmployeeController.class).getEmployee(employeeDto.getEmail())).withSelfRel();
                var delete = linkTo(methodOn(EmployeeController.class).deleteEmployee(employeeDto.getEmail())).withRel("delete");
                entityModel.add(self, delete);
                return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Employee with %s email has NOT been created", employeeDto.getEmail()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
