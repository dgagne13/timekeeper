package net.chronos.timekeeper.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column(name = "annual_budget")
    private BigDecimal annualBudget;

    @OneToMany(mappedBy = "department")
    protected Set<Employee> employees;

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

    public Set<Employee> getEmployees() {
        return employees;
    }
}
