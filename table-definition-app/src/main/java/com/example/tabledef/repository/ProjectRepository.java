package com.example.tabledef.repository;

import com.example.tabledef.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Optional<Project> findByCode(String code);
    
    boolean existsByCode(String code);
}