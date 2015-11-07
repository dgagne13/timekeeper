package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.EmploymentStatus;
import net.chronos.timekeeper.data.Shift;
import net.chronos.timekeeper.data.ShiftType;
import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.dto.ShiftDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.exception.ShiftCreationException;
import net.chronos.timekeeper.repository.ShiftRepository;
import net.chronos.timekeeper.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class ShiftServiceImpl implements ShiftService {

    private static final long MAX_HOURS_NO_LUNCH = 6L;
    private static final long MAX_SHIFT_HOURS = 24L;
    private static final Logger log = LoggerFactory.getLogger(ShiftServiceImpl.class);
    private static final String NO_EMPLOYEE_MESSAGE = "Could not create shift without active employee";
    private static final String NO_SHIFT_TYPE = "Could not create shift with type {} for employee id {}";
    private static final String MISSING_START_TIME = "Could not create shift without start time";
    private static final String MISSING_END_TIME = "Could not create shift without end time";
    private static final String INVERTED_TIMES = "Could not create shift with start, lunch, end events out of sequence";
    private static final String MISSING_LUNCH_START_TIME = "Could not create shift over "+ MAX_HOURS_NO_LUNCH +" hrs without lunch start time";
    private static final String MISSING_LUNCH_END_TIME = "Could not create shift over "+ MAX_HOURS_NO_LUNCH +" without lunch end time";
    private static final String OVERLAPPING_SHIFT_MESSAGE = "Could not create shift conflicting with another shift";
    private static final String NO_SHIFT_WITH_ID = "No shift found with id ";
    private static final String MISMATCHED_EMPLOYEE = "Could not update shift with a different employee from original";
    private static final String INSUFFICIENT_VACATION = "Could not create a shift with more vacation than employee has";
    private static final String INSUFFICIENT_SICK = "Could not create a shift with more sick time than employee has";

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private EmployeeService employeeService;

    public Long createShift(ShiftDTO shiftDTO) throws ShiftCreationException {
        try {
            EmployeeDTO employee = employeeService.getEmployee(shiftDTO.getEmployeeId());

            validateShiftRequest(shiftDTO, employee);

            Shift shift = new Shift();
            copyShiftDTOToShift(shift, shiftDTO);
            shiftRepository.save(shift);
            adjustEmployeeHours(shiftDTO, employee);

            return shift.getId();

        } catch (NotFoundException nfe) {
            throw new ShiftCreationException(NO_EMPLOYEE_MESSAGE);
        }


    }

    public ShiftDTO getShift(Long shiftId) throws NotFoundException {
        Shift shift = getShiftById(shiftId);
        return new ShiftDTO(shift);

    }

    public void deleteShift(Long shiftId) throws NotFoundException {
        Shift shift = getShiftById(shiftId);
        EmployeeDTO employee = employeeService.getEmployee(shift.getEmployeeId());
        switch (shift.getShiftType()) {
           case Vacation:
               //put back vacation time
               BigDecimal adjustedVacation = employee.getVacationHours().add(shift.getDuration());
               employeeService.updateEmployeeVacation(employee.getId(), adjustedVacation);
               break;
            case Sick:
                //put back vacation time
                BigDecimal adjustedSick = employee.getSickHours().add(shift.getDuration());
                employeeService.updateEmployeeSick(employee.getId(), adjustedSick);
                break;
        }
       shiftRepository.delete(shift);
    }

    public Page<ShiftDTO> getShiftsForEmployee(Long employeeId, Pageable pageable) throws NotFoundException {
        //throw notfound if no employee has id
        EmployeeDTO employee = employeeService.getEmployee(employeeId);

        Page<Shift> shiftsForEmployee = shiftRepository.findByEmployeeId(employeeId, pageable);

        List<ShiftDTO> shiftDTOs =
                shiftsForEmployee.getContent().stream()
                .map(s -> new ShiftDTO(s))
                .collect(Collectors.toList());

        Page<ShiftDTO> shiftDTOPage = new PageImpl<ShiftDTO>(shiftDTOs, pageable, shiftsForEmployee.getTotalElements());
        return shiftDTOPage;
    }

    private void validateShiftRequest(ShiftDTO shiftDTO, EmployeeDTO employee) throws ShiftCreationException {
        //validation for any shift type
        validateEmployeeExistsAndIsActive(employee);
        validateHasValidShiftType(shiftDTO);
        validateHasStartTime(shiftDTO);
        validateHasValidEndTime(shiftDTO);
        vaidateNoOverlappingShifts(shiftDTO);

        BigDecimal shiftDuration = getFullShiftDuration(shiftDTO);
        ShiftType shiftType = ShiftType.valueOf(shiftDTO.getShiftType());

        //additional validation based on type
        switch(shiftType) {
            case Regular:
                validateLunchPesentIfRequired(shiftDTO, shiftDuration);
                validateLunchValidIfPresent(shiftDTO);
                break;
            case Vacation:
                validateHasNoLunch(shiftDTO);
                validateHasEnoughVacationTime(shiftDuration,employee);
                break;
            case Sick:
                validateHasNoLunch(shiftDTO);
                validateHasEnoughSickTime(shiftDuration, employee);
                break;
            default:
                validateHasNoLunch(shiftDTO);
        }
    }
    private void validateEmployeeExistsAndIsActive(EmployeeDTO employee) throws ShiftCreationException {
        //need an active employee
        if(employee == null || !EmploymentStatus.ACTIVE_STATUS.equals(employee.getEmploymentStatus())) {
            log.info(NO_EMPLOYEE_MESSAGE);
            throw new ShiftCreationException(NO_EMPLOYEE_MESSAGE);
        }
    }

    private void validateHasStartTime(ShiftDTO shiftDTO) throws ShiftCreationException {
        if(shiftDTO.getStartTime() == null) {
            log.info(MISSING_START_TIME + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(MISSING_START_TIME);
        }
    }

    private void validateHasValidEndTime(ShiftDTO shiftDTO) throws ShiftCreationException {
        if(shiftDTO.getEndTime() == null) {
            log.info(MISSING_END_TIME + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(MISSING_END_TIME);
        }
        if(!shiftDTO.getEndTime().after(shiftDTO.getStartTime())) {
            log.info(INVERTED_TIMES + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(INVERTED_TIMES);
        }
    }

    private void validateLunchPesentIfRequired(ShiftDTO shiftDTO, BigDecimal duration) throws ShiftCreationException {
        Date lunchStart = shiftDTO.getLunchStartTime();
        boolean hasLunch = lunchStart != null;

        //length of shift 6 hours or more requires lunch
        if (duration.compareTo(new BigDecimal(MAX_HOURS_NO_LUNCH)) >= 0 && !hasLunch) {
            log.info(MISSING_LUNCH_START_TIME + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(MISSING_LUNCH_START_TIME);
        }
    }

    private void validateLunchValidIfPresent(ShiftDTO shiftDTO) throws ShiftCreationException {
        //lunch is optional for shorter shift but must be verified if present
        Date lunchStart = shiftDTO.getLunchStartTime();
        boolean hasLunch = lunchStart != null;
        if(hasLunch) {
            if (!lunchStart.after(shiftDTO.getStartTime())) {
                log.info(INVERTED_TIMES + " Employee: " + shiftDTO.getEmployeeId());
                throw new ShiftCreationException(INVERTED_TIMES);
            }
            //lunch needs to end.  mmmm...infinite luch
            if (shiftDTO.getLunchEndTime() == null) {
                log.info(MISSING_LUNCH_END_TIME + " Employee: " + shiftDTO.getEmployeeId());
                throw new ShiftCreationException(MISSING_LUNCH_END_TIME);
            }

            //validate sequence of events
            if (!lunchStart.before(shiftDTO.getLunchEndTime())) {
                log.info(INVERTED_TIMES + " Employee: " + shiftDTO.getEmployeeId());
                throw new ShiftCreationException(INVERTED_TIMES);
            }

            //validate sequence of events
            if (!shiftDTO.getLunchEndTime().before(shiftDTO.getEndTime())) {
                log.info(INVERTED_TIMES + " Employee: " + shiftDTO.getEmployeeId());
                throw new ShiftCreationException(INVERTED_TIMES);
            }
        }
    }

    // non-regular shifts dont have a lunch
    private void validateHasNoLunch(ShiftDTO shiftDTO) throws ShiftCreationException {
        Date lunchStart = shiftDTO.getLunchStartTime();
        boolean hasLunch = lunchStart != null;

        if (hasLunch) {
            String badLunch = "Could not create shift of type " + shiftDTO.getShiftType() + " with a lunch. ";
            log.info( badLunch + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(badLunch);
        }
    }

    private void validateHasEnoughVacationTime(BigDecimal shiftDuration, EmployeeDTO employee) throws ShiftCreationException{
        if(shiftDuration.compareTo(employee.getVacationHours()) <= 0) {
            log.info(INSUFFICIENT_VACATION + " Employee " + employee.getLastName());
            throw new ShiftCreationException(INSUFFICIENT_VACATION);
        }
    }

    private void validateHasEnoughSickTime(BigDecimal shiftDuration, EmployeeDTO employee) throws ShiftCreationException {
        if(shiftDuration.compareTo(employee.getSickHours()) <= 0) {
            log.info(INSUFFICIENT_SICK + " Employee " + employee.getLastName());
            throw new ShiftCreationException(INSUFFICIENT_SICK);
        }
    }

    private void validateHasValidShiftType(ShiftDTO shiftDTO) throws ShiftCreationException {
        ShiftType shiftType = null;
        String requestShiftType = shiftDTO.getShiftType();
        if (requestShiftType != null) {
            try {
                shiftType = ShiftType.valueOf(requestShiftType);
            } catch (IllegalArgumentException iee) {
                log.info(NO_SHIFT_TYPE, requestShiftType, shiftDTO.getEmployeeId());
                throw new ShiftCreationException("Could not create shift of type " + requestShiftType);
            }

        }
    }


    private void vaidateNoOverlappingShifts(ShiftDTO shiftDTO) throws ShiftCreationException{
        //on overlapping shift would start sometime before requested shift ends and end sometime after requested shift starts
        //should cover all types of overlap
        List<Shift> overlappingShifts = shiftRepository.findByEmployeeIdAndStartTimeBeforeAndEndTimeAfter(shiftDTO.getEmployeeId(), shiftDTO.getEndTime(), shiftDTO.getStartTime());
      if(!overlappingShifts.isEmpty()) {
            log.info(OVERLAPPING_SHIFT_MESSAGE + " Employee: " + shiftDTO.getEmployeeId());
            throw new ShiftCreationException(OVERLAPPING_SHIFT_MESSAGE);
        }
    }

    private void copyShiftDTOToShift(Shift shift, ShiftDTO shiftDTO) {
        shift.setEmployeeId(shiftDTO.getEmployeeId());
        shift.setShiftType(ShiftType.valueOf(shiftDTO.getShiftType()));
        shift.setStartTime(shiftDTO.getStartTime());
        shift.setLunchStartTime(shiftDTO.getLunchStartTime());
        shift.setLunchEndTime(shiftDTO.getLunchEndTime());
        shift.setEndTime(shiftDTO.getEndTime());

        BigDecimal duration = getFullShiftDuration(shiftDTO);
        //if lunch was taken, subtract it
        if (shiftDTO.getLunchStartTime() != null) {
            duration = duration.subtract(DateUtil.hoursBetween(shiftDTO.getLunchStartTime(), shiftDTO.getLunchEndTime()));
        }

        shift.setDuration(duration);
    }

    private Shift getShiftById(Long shiftId) throws NotFoundException {
        Shift shift = shiftRepository.findOne(shiftId);
        if (shift == null) {
            log.info(NO_SHIFT_WITH_ID + shiftId);
            throw  new NotFoundException(NO_SHIFT_WITH_ID + shiftId);
        }
        return shift;
    }

    private BigDecimal getFullShiftDuration(ShiftDTO shiftDTO) {
        return DateUtil.hoursBetween(shiftDTO.getStartTime(), shiftDTO.getEndTime());
    }

    private void adjustEmployeeHours(ShiftDTO shiftDTO, EmployeeDTO employeeDTO) throws NotFoundException {
        BigDecimal duration = getFullShiftDuration(shiftDTO);

        if(ShiftType.Vacation.equals(shiftDTO.getShiftType())) {
            BigDecimal currentVacation = employeeDTO.getVacationHours();
            employeeService.updateEmployeeVacation(shiftDTO.getEmployeeId(), currentVacation.subtract(duration));
        } else if (ShiftType.Sick.equals(shiftDTO.getShiftType())) {
            BigDecimal currentSick = employeeDTO.getSickHours();
            employeeService.updateEmployeeSick(shiftDTO.getEmployeeId(), currentSick.subtract(duration));
        }
    }
}
