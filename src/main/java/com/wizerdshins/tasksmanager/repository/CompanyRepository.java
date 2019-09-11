package com.wizerdshins.tasksmanager.repository;

import com.wizerdshins.tasksmanager.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
