package com.springboot.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.mongodb.request.EmployeeRequest;
import com.springboot.mongodb.response.EmployeeDTO;
import com.springboot.mongodb.service.EmpoyeeService;

@RestController
@RequestMapping(value = "/api/v1")
public class EmployeeController {

	@Autowired
	private EmpoyeeService empoyeeService;

	@GetMapping(value = "/employees")
	@ResponseStatus(HttpStatus.OK)
	public List<EmployeeDTO> employees() {
		return empoyeeService.employees();
	}

	@GetMapping(value = "/employees/{id}")
	@ResponseStatus(HttpStatus.OK)
	public EmployeeDTO employee(@PathVariable(value = "id") String empId) {
		return empoyeeService.employee(empId);
	}

	@PostMapping(value = "/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeDTO save(@RequestBody EmployeeRequest emp) {
		return empoyeeService.save(emp);
	}

	@DeleteMapping(value = "/employees")
	@ResponseStatus(HttpStatus.OK)
	public String deleteAll() {
		return empoyeeService.deleteAll();
	}

	@DeleteMapping(value = "/employees/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String delete(@PathVariable(value = "id") String empId) {
		return empoyeeService.delete(empId);
	}

	@PutMapping(value = "/employees")
	@ResponseStatus(HttpStatus.OK)
	public EmployeeDTO update(@RequestBody EmployeeRequest emp) {
		return empoyeeService.update(emp);
	}

}
