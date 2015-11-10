package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.EmploymentStatus;
import net.chronos.timekeeper.data.Shift;
import net.chronos.timekeeper.data.ShiftType;
import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.dto.ShiftDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.exception.ShiftCreationException;
import net.chronos.timekeeper.repository.ShiftRepository;
import net.chronos.timekeeper.repository.ShiftTypeRepository;
import net.chronos.timekeeper.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShiftServiceImplTest {

    @InjectMocks
    ShiftServiceImpl sut;

    @Mock
    ShiftRepository shiftRepository;

    @Mock
    ShiftTypeRepository shiftTypeRepository;

    @Mock
    EmployeeService employeeService;

    EmployeeDTO employee;
    ShiftDTO shiftDTO;

    @Before
    public void setup() throws Exception {
        shiftDTO = new ShiftDTO();
        shiftDTO.setEmployeeId(1L);
        shiftDTO.setStartTime(DateUtil.from("2015-01-01T09:00:00.000Z"));
        shiftDTO.setEndTime(DateUtil.from("2015-01-01T17:00:00.000Z"));
        shiftDTO.setLunchStartTime(DateUtil.from("2015-01-01T12:00:00.000Z"));
        shiftDTO.setLunchEndTime(DateUtil.from("2015-01-01T12:30:00.000Z"));
        shiftDTO.setShiftType("Regular");

        employee = new EmployeeDTO();
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE_STATUS);

        when(employeeService.getEmployee(1L)).thenReturn(employee);
        when(shiftRepository.findByEmployeeIdAndStartTimeBeforeAndEndTimeAfter(anyLong(), any(Date.class), any(Date.class))).thenReturn(Collections.EMPTY_LIST);

        ShiftType regular = new ShiftType();
        regular.setValue("Regular");

        when(shiftTypeRepository.findByValue("Regular")).thenReturn(regular);

        ShiftType vacation = new ShiftType();
        vacation.setValue("Vacation");

        when(shiftTypeRepository.findByValue("Vacation")).thenReturn(vacation);
    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForNullEmployee() throws Exception {
        shiftDTO.setEmployeeId(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForEmployeeNotFound() throws Exception {
        when(employeeService.getEmployee(1L)).thenThrow(new NotFoundException("MESSAGE"));

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForInactiveEmployee() throws Exception {
        employee.setEmploymentStatus("Terminated");

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForUnknownShiftType() throws Exception {
        shiftDTO.setShiftType("taco");

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForNullStartTime() throws Exception {
        shiftDTO.setStartTime(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForNullEndTime() throws Exception {
        shiftDTO.setEndTime(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForEndTimeBeforeStart() throws Exception {
        shiftDTO.setEndTime(DateUtil.from("2015-01-01T08:00:00.000Z"));

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForOverlappingShifts() throws Exception {
        Shift overlap = new Shift();
        when(shiftRepository.findByEmployeeIdAndStartTimeBeforeAndEndTimeAfter(1L, shiftDTO.getEndTime(), shiftDTO.getStartTime())).thenReturn(Arrays.asList(overlap));

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForMissingLunchStart() throws Exception {
        shiftDTO.setLunchStartTime(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForLunchStartBeforeShift() throws Exception {
        shiftDTO.setLunchStartTime(DateUtil.from("2015-01-01T08:00:00.000Z"));

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForLunchStartAfterShift() throws Exception {
        shiftDTO.setLunchStartTime(DateUtil.from("2015-01-01T21:00:00.000Z"));

        sut.createShift(shiftDTO);

    }

    public void shouldNotThrowSCEForMissingLunchWhenNotRegularShift() throws Exception {
        shiftDTO.setShiftType("Vacation");
        shiftDTO.setLunchStartTime(null);
        shiftDTO.setLunchEndTime(null);

        sut.createShift(shiftDTO);

    }

    public void shouldNotThrowSCEForMissingLunchWhenShortShift() throws Exception {
        shiftDTO.setEndTime(DateUtil.from("2015-01-01T10:00:00.000Z"));
        shiftDTO.setLunchStartTime(null);
        shiftDTO.setLunchEndTime(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForLunchWhenNotRegularShift() throws Exception {
        shiftDTO.setShiftType("Vacation");

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
     public void shouldThrowSCEForMissingLunchEnd() throws Exception {
        shiftDTO.setLunchEndTime(null);

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForLunchEndBeforeLunchStart() throws Exception {
        shiftDTO.setLunchEndTime(DateUtil.from("2015-01-01T11:00:00.000Z"));

        sut.createShift(shiftDTO);

    }

    @Test(expected = ShiftCreationException.class)
    public void shouldThrowSCEForLunchEndAfterEnd() throws Exception {
        shiftDTO.setLunchEndTime(DateUtil.from("2015-01-01T21:00:00.000Z"));

        sut.createShift(shiftDTO);

    }

    @Test
    public void shouldTransferDTOPropertiesToSavedShift() throws Exception{
        ArgumentCaptor<Shift> shiftArgumentCaptor = ArgumentCaptor.forClass(Shift.class);

        when(shiftRepository.save(shiftArgumentCaptor.capture())).thenReturn(new Shift());

        sut.createShift(shiftDTO);

        Shift shift = shiftArgumentCaptor.getValue();

        assertTrue(shift.getEmployeeId() == 1L);
        assertEquals(DateUtil.from("2015-01-01T09:00:00.000Z"), shift.getStartTime());
        assertEquals(DateUtil.from("2015-01-01T17:00:00.000Z"), shift.getEndTime());
        assertEquals(DateUtil.from("2015-01-01T12:00:00.000Z"), shift.getLunchStartTime());
        assertEquals(DateUtil.from("2015-01-01T12:30:00.000Z"), shift.getLunchEndTime());
        assertEquals("Regular", shift.getShiftType().getValue());
    }
    @Test
    public void shouldSaveShiftWithExpectedDuration() throws Exception{
        shiftDTO.setEndTime(DateUtil.from("2015-01-01T10:00:00.000Z"));
        shiftDTO.setLunchStartTime(null);
        shiftDTO.setLunchEndTime(null);

        ArgumentCaptor<Shift> shiftArgumentCaptor = ArgumentCaptor.forClass(Shift.class);

        when(shiftRepository.save(shiftArgumentCaptor.capture())).thenReturn(new Shift());

        sut.createShift(shiftDTO);

        Shift shift = shiftArgumentCaptor.getValue();

        assertTrue(new BigDecimal("1.00").compareTo(shift.getDuration()) == 0);
    }

    @Test
    public void shouldSubtractLunchFromRegularShift() throws Exception{

        ArgumentCaptor<Shift> shiftArgumentCaptor = ArgumentCaptor.forClass(Shift.class);

        when(shiftRepository.save(shiftArgumentCaptor.capture())).thenReturn(new Shift());

        sut.createShift(shiftDTO);

        Shift shift = shiftArgumentCaptor.getValue();

        assertTrue(new BigDecimal("7.50").compareTo(shift.getDuration()) == 0);
    }

}
