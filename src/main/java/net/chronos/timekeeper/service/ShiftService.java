package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Shift;
import net.chronos.timekeeper.dto.ShiftDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.exception.ShiftCreationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShiftService {
    Long createShift(ShiftDTO shiftDTO) throws ShiftCreationException;

    ShiftDTO getShift(Long shiftId) throws NotFoundException;

    void deleteShift(Long shiftId) throws NotFoundException;

    List<ShiftDTO> getShiftsForEmployee(Long employeeId) throws NotFoundException;
}
