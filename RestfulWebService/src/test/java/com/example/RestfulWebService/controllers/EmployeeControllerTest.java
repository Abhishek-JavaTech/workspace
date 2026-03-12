package com.example.RestfulWebService.controllers;

import com.example.RestfulWebService.dtos.EmployeeDto;
import com.example.RestfulWebService.entities.EmployeeEntity;
import com.example.RestfulWebService.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {EmployeeController.class})
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;


    @DisplayName("testing get all methods")
    @Test
    public void testGetAllWithNull() throws Exception {

        Page<EmployeeEntity> page = new PageImpl<>(List.of());
        when(employeeService.getAll(any(Pageable.class))).thenReturn(null);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andReturn();

        Assertions.assertEquals(500, result.getResponse().getStatus());
    }

    @DisplayName("testing get all methods")
    @Test
    public void testGetAllWithValue() throws Exception {

        Page<EmployeeEntity> page = new PageImpl<>(List.of());
        when(employeeService.getAll(any(Pageable.class))).thenReturn(page);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @DisplayName("testing get employee by email")
    @Test
    public void getEmployee() throws Exception {
        when(employeeService.findByEmail(any())).thenReturn(Optional.empty());
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{email}", "rangari_a@yahoo.com")
        ).andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());

        var employeeDto = EmployeeDto.builder()
                .email("rangari_a@yahoo.com")
                .firstName("abhishek")
                .lastName("rangari")
                .mobileNumber("9960677004")
                .state("MH")
                .country("INDIA")
                .zipCode("440003")
                .build();
        when(employeeService.findByEmail(any())).thenReturn(Optional.of(employeeDto));
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{email}", "rangari_a@yahoo.com")
        ).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("abhishek", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getFirstName());
        Assertions.assertEquals("rangari", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getLastName());

        when(employeeService.findByEmail(any())).thenThrow(NullPointerException.class);
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{email}", "rangari_a@yahoo.com")
        ).andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());
    }

    @DisplayName("testing put employee by email")
    @Test
    public void puttEmployee() throws Exception {
        var employeeDto = EmployeeDto.builder()
                .email("rangari_a@yahoo.com")
                .firstName("abhishek")
                .lastName("rangari")
                .mobileNumber("9960677004")
                .state("MH")
                .country("INDIA")
                .zipCode("440003")
                .build();

        when(employeeService.updateByEmail(any(), any())).thenReturn(Optional.empty());
        var result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());

        when(employeeService.findByEmail(any())).thenReturn(Optional.of(employeeDto));
        when(employeeService.updateByEmail(any(), any())).thenReturn(Optional.of(employeeDto));
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("abhishek", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getFirstName());
        Assertions.assertEquals("rangari", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getLastName());

        when(employeeService.updateByEmail(any(), any())).thenThrow(NullPointerException.class);
        result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());
    }

    @DisplayName("testing patch employee by email")
    @Test
    public void patchEmployee() throws Exception {
        var employeeDto = EmployeeDto.builder()
                .email("rangari_a@yahoo.com")
                .firstName("abhishek")
                .lastName("rangari")
                .mobileNumber("9960677004")
                .state("MH")
                .country("INDIA")
                .zipCode("440003")
                .build();

        when(employeeService.patchUpdateByEmail(any(), any())).thenReturn(Optional.empty());
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(404, result.getResponse().getStatus());

        when(employeeService.findByEmail(any())).thenReturn(Optional.of(employeeDto));
        when(employeeService.patchUpdateByEmail(any(), any())).thenReturn(Optional.of(employeeDto));
        result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("abhishek", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getFirstName());
        Assertions.assertEquals("rangari", new ObjectMapper().readValue(result.getResponse().getContentAsString(), EmployeeDto.class).getLastName());

        when(employeeService.patchUpdateByEmail(any(), any())).thenThrow(NullPointerException.class);
        result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employees/{email}", "rangari_a@yahoo.com")
                .content(new ObjectMapper().writeValueAsString(employeeDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());
    }

    @DisplayName("test delete employee")
    @Test
    public void deleteEmployee() throws Exception {

        when(employeeService.deleteByEmail(any())).thenReturn(false);
        var result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/{email}", "rangari_a@yahoo.com"))
                .andReturn();
        Assertions.assertEquals(204, result.getResponse().getStatus());

        when(employeeService.deleteByEmail(any())).thenReturn(true);
        result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/{email}", "rangari_a@yahoo.com"))
                .andReturn();
        Assertions.assertEquals(202, result.getResponse().getStatus());

        when(employeeService.deleteByEmail(any())).thenThrow(NullPointerException.class);
        result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/{email}", "rangari_a@yahoo.com"))
                .andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());
    }

    @DisplayName("test post employee")
    @Test
    public void postEmployee() throws Exception {

        var employeeDto = EmployeeDto.builder()
                .email("rangari_a@yahoo.com")
                .firstName("abhishek")
                .lastName("rangari")
                .mobileNumber("9960677004")
                .state("MH")
                .country("INDIA")
                .zipCode("440003")
                .build();
        when(employeeService.createEmployee(any())).thenReturn(false);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                        .content(new ObjectMapper().writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());

        when(employeeService.createEmployee(any())).thenReturn(true);
        when(employeeService.findByEmail(any())).thenReturn(Optional.of(employeeDto));
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                        .content(new ObjectMapper().writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(201, result.getResponse().getStatus());

        when(employeeService.createEmployee(any())).thenThrow(NullPointerException.class);
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                        .content(new ObjectMapper().writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(500, result.getResponse().getStatus());
    }
}
