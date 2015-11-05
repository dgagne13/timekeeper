package net.chronos.timekeeper.service;

import net.chronos.timekeeper.dto.DepartmentDTO;
import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getDepartments();
}
