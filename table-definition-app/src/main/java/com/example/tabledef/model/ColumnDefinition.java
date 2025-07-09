package com.example.tabledef.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "column_definitions")
public class ColumnDefinition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String physicalName;
    
    @Column(nullable = false)
    private String logicalName;
    
    @Column(nullable = false)
    private String dataType;
    
    private Integer length;
    
    private Integer precision;
    
    private Integer scale;
    
    @Column(nullable = false)
    private boolean nullable = true;
    
    private boolean primaryKey = false;
    
    private boolean uniqueKey = false;
    
    private String defaultValue;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Integer columnOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_definition_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TableDefinition tableDefinition;
}