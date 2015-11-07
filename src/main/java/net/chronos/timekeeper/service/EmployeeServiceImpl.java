package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Employee;
import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.repository.EmployeeRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private static final String NOT_FOUND = "Could not find employee with id ";

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeDTO getEmployee(Long id) throws NotFoundException {
        Employee employee = getEmployeeEntity(id);
        return new EmployeeDTO(employee);
    }

    public List<EmployeeDTO> getEmployees(String departmentName, String lastName) {
        List<Employee> employees;
        if(departmentName != null && lastName != null) {
            employees = employeeRepository.findByDepartmentNameAndLastNameStartingWithIgnoreCase(departmentName, lastName);
        } else if(departmentName != null) {
            employees = employeeRepository.findByDepartmentName(departmentName);
        } else if(lastName != null) {
            employees = employeeRepository.findByLastNameStartingWithIgnoreCase(lastName);
        } else {
            Iterable<Employee> employeeIterable = employeeRepository.findAll();
            employees = StreamSupport.stream(employeeIterable.spliterator(),false)
                    .collect(Collectors.toList());
        }

        return employees.stream()
                .map(e -> new EmployeeDTO(e))
                .collect(Collectors.toList());
    }

    public void updateEmployeeVacation(Long employeeId, BigDecimal vacation) throws NotFoundException {
        Employee employee = getEmployeeEntity(employeeId);
        employee.setVacationHours(vacation);
        employeeRepository.save(employee);
    }

    public void updateEmployeeSick(Long employeeId, BigDecimal sick) throws NotFoundException {
        Employee employee = getEmployeeEntity(employeeId);
        employee.setSickHours(sick);
        employeeRepository.save(employee);
    }

    private Employee getEmployeeEntity(Long id) throws NotFoundException {
        Employee employee = employeeRepository.findOne(id);
        if(employee == null) {
            log.info(NOT_FOUND + id);
            throw new NotFoundException(NOT_FOUND + id);
        }
        return employee;
    }

}
