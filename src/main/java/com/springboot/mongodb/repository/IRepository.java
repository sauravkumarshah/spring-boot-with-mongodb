package com.springboot.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.mongodb.entity.Employee;

@Repository
public interface IRepository extends MongoRepository<Employee, String> {

}
