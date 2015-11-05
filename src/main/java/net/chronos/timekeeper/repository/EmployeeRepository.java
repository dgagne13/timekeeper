package net.chronos.timekeeper.repository;

import net.chronos.timekeeper.data.Department;
import net.chronos.timekeeper.data.Employee;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findByLastNameStartingWithIgnoreCase(String lastName);
    List<Employee> findByDepartmentName(String departmentName);
}
