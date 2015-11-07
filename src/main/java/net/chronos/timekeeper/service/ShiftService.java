package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Shift;
import net.chronos.timekeeper.dto.ShiftDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.exception.ShiftCreationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShiftService {
    Long createShift(ShiftDTO shiftDTO) throws ShiftCreationException;

    ShiftDTO getShift(Long shiftId) throws NotFoundException;

    void deleteShift(Long shiftId) throws NotFoundException;

    Page<ShiftDTO> getShiftsForEmployee(Long employeeId, Pageable pageable) throws NotFoundException;
}
