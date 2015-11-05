package net.chronos.timekeeper.dto;

import net.chronos.timekeeper.data.Employee;

import java.math.BigDecimal;
import java.util.Date;

public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Date hireDate;
    private BigDecimal hourlyRate;
    private BigDecimal vacationHours;
    private BigDecimal sickHours;
    private String position;
    private String department;
    private String employmentStatus;

    public EmployeeDTO() {}

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.hireDate = employee.getHireDate();
        this.hourlyRate = employee.getHourlyRate();
        this.vacationHours = employee.getVacationHours();
        this.sickHours = employee.getSickHours();
        this.position = employee.getPosition().getTitle();
        this.department = employee.getDepartment().getName();
        this.employmentStatus = employee.getEmploymentStatus().getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getVacationHours() {
        return vacationHours;
    }

    public void setVacationHours(BigDecimal vacationHours) {
        this.vacationHours = vacationHours;
    }

    public BigDecimal getSickHours() {
        return sickHours;
    }

    public void setSickHours(BigDecimal sickHours) {
        this.sickHours = sickHours;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }


}
