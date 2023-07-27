package com.springboot.mongodb;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.mongodb.entity.Employee;
import com.springboot.mongodb.repository.IRepository;
import com.springboot.mongodb.request.EmployeeRequest;
import com.springboot.mongodb.response.EmployeeDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
class SpringBootMongodbApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	private static List<EmployeeRequest> employees = new ArrayList<>();

	private static List<EmployeeDTO> employeeResponse = new ArrayList<>();

	// @Container
	// private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(
	// 		DockerImageName.parse("mongo:4.0.10"));

	@Container
	private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.3"));

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}


	static {
		mongoDBContainer.start();
	}

	static {

		EmployeeRequest emp1 = EmployeeRequest.builder().name("test emp1").address("address1").build();
		EmployeeRequest emp2 = EmployeeRequest.builder().name("test emp2").address("address2").build();
		EmployeeRequest emp3 = EmployeeRequest.builder().name("test emp3").address("address3").build();
		EmployeeRequest emp4 = EmployeeRequest.builder().name("test emp4").address("address4").build();
		EmployeeRequest emp5 = EmployeeRequest.builder().name("test emp5").address("address5").build();

		employees.add(emp1);
		employees.add(emp2);
		employees.add(emp3);
		employees.add(emp4);
		employees.add(emp5);
	}

	@Test
	@Order(value = 1)
	void testConnectionToDatabase() {
		Assertions.assertNotNull(repository);
	}

	@Test
	@Order(value = 2)
	void testAddEmployees() throws Exception {
		for (EmployeeRequest employee : employees) {
			String emp = objectMapper.writeValueAsString(employee);
			ResultActions andExpect = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
					.contentType(MediaType.APPLICATION_JSON).content(emp)).andExpect(status().isCreated());

			employeeResponse.add(objectMapper.readValue(andExpect.andReturn().getResponse().getContentAsString(),
					EmployeeDTO.class));
		}
	}

	@Test
	@Order(value = 3)
	void testGetAllEmployees() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")).andExpect(status().isOk());

		Assertions.assertEquals(employees.get(3).getName(), repository.findAll().get(3).getName());
	}

	@Test
	@Order(value = 4)
	void testGetEmployeeById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/" + employeeResponse.get(1).getId()))
				.andExpect(status().isOk());
		Assertions.assertEquals(employees.get(1).getName(),
				repository.findById(employeeResponse.get(1).getId()).get().getName());
	}

	@Test
	@Order(value = 5)
	void testDeleteEmployeeById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/" + employeeResponse.get(1).getId()))
				.andExpect(status().isOk());
	}

	@Test
	@Order(value = 6)
	void testUpdateEmployee() throws Exception {
		Employee employee = Employee.builder().id(employeeResponse.get(2).getId()).name("Saurav Kumar Shah")
				.address("India East").build();
		String emp = objectMapper.writeValueAsString(employee);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(emp))
				.andExpect(status().isOk());
		Assertions.assertEquals(employee.getName(),
				repository.findById(employeeResponse.get(2).getId()).get().getName());
	}

	@Test
	@Order(value = 7)
	void testDeleteAllEmployees() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees")).andExpect(status().isOk());
		Assertions.assertEquals(0, repository.findAll().size());
	}
}
