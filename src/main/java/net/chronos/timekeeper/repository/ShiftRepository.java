package net.chronos.timekeeper.repository;

import net.chronos.timekeeper.data.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift,Long> {
    List<Shift> findByEmployeeIdAndStartTimeBeforeAndEndTimeAfter(Long employeeId, Date startTime, Date endTime);
    List<Shift> findByEmployeeId(Long employeeId);
}
