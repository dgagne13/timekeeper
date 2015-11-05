package net.chronos.timekeeper.repository;

import net.chronos.timekeeper.data.Department;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface DepartmentRepository extends Repository<Department, Long> {
    List<Department> findAll();
}
