package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Department;
import net.chronos.timekeeper.dto.DepartmentDTO;
import net.chronos.timekeeper.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    public List<DepartmentDTO> getDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(d -> new DepartmentDTO(d))
                .collect(Collectors.toList());
    }
}
