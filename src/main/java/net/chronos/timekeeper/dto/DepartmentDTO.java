package net.chronos.timekeeper.dto;

import net.chronos.timekeeper.data.Department;
import net.chronos.timekeeper.data.Employee;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

public class DepartmentDTO {
    private Long id;
    private String name;
    private BigDecimal annualBudget;

    public DepartmentDTO() {}
    public DepartmentDTO(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.annualBudget = department.getAnnualBudget();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAnnualBudget() {
        return annualBudget;
    }

    public void setAnnualBudget(BigDecimal annualBudget) {
        this.annualBudget = annualBudget;
    }


}
