package com.example.RestfulWebService.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class EmployeeEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(length = 15, name = "mobile")
    private String mobileNumber;

    @Column(length = 2, name = "state")
    private String state;

    private String country;

    @Column(length = 6, name = "zip_code")
    private String zipCode;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist(){
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void postUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
