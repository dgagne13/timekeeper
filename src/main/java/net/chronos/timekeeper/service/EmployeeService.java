package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Employee;
import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.exception.EmployeeUpdateException;
import net.chronos.timekeeper.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO getEmployee(Long id) throws NotFoundException;

    List<EmployeeDTO> getEmployeesByLastName(String lastName);

    List<EmployeeDTO> getEmployeesByDepartment(String departmentName);

    List<EmployeeDTO> getEmployees();

    void updateEmployeeVacation(Long employeeId, BigDecimal vacation) throws NotFoundException;

    void updateEmployeeSick(Long employeeId, BigDecimal sick) throws NotFoundException;
}
