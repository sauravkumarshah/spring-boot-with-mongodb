package com.springboot.mongodb.util;

import java.util.NoSuchElementException;

import com.springboot.mongodb.entity.Employee;
import com.springboot.mongodb.response.EmployeeDTO;

public class EmployeeUtil {
	private EmployeeUtil() {
	}

	public static NoSuchElementException notFound(String empId) {
		return new NoSuchElementException("Employee with id=" + empId + " not found.");
	}

	public static EmployeeDTO mapToEmployeeDTO(Employee emp) {
		return EmployeeDTO.builder().id(emp.getId()).name(emp.getName()).address(emp.getAddress()).build();
	}
}
