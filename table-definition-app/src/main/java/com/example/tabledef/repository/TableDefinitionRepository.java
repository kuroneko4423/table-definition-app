package com.example.tabledef.repository;

import com.example.tabledef.model.TableDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableDefinitionRepository extends JpaRepository<TableDefinition, Long> {
    
    List<TableDefinition> findByProjectId(Long projectId);
    
    Optional<TableDefinition> findByProjectIdAndPhysicalName(Long projectId, String physicalName);
    
    boolean existsByProjectIdAndPhysicalName(Long projectId, String physicalName);
    
    @Query("SELECT t FROM TableDefinition t LEFT JOIN FETCH t.columns WHERE t.project.id = :projectId ORDER BY t.physicalName")
    List<TableDefinition> findByProjectIdWithColumns(Long projectId);
    
    @Query("SELECT t FROM TableDefinition t LEFT JOIN FETCH t.columns WHERE t.id = :id")
    Optional<TableDefinition> findByIdWithColumns(Long id);
}