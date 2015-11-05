package net.chronos.timekeeper.service;

import net.chronos.timekeeper.data.Department;
import net.chronos.timekeeper.data.Employee;
import net.chronos.timekeeper.data.EmploymentStatus;
import net.chronos.timekeeper.data.Position;
import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.repository.EmployeeRepository;
import net.chronos.timekeeper.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    EmployeeServiceImpl sut;

    @Mock
    EmployeeRepository employeeRepository;

    Employee employee;

    @Before
    public void setup() {
        Department department = new Department();
        department.setName("Shoes");

        EmploymentStatus employmentStatus = mock(EmploymentStatus.class);
        when(employmentStatus.getStatus()).thenReturn("Active");

        Position position = new Position();
        position.setTitle("Polisher");

        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setHireDate(DateUtil.from("2015-10-30T00:00:00.000Z"));
        employee.setHourlyRate(new BigDecimal("10.00"));
        employee.setVacationHours(new BigDecimal("5.00"));
        employee.setSickHours(new BigDecimal("20.00"));
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setEmploymentStatus(employmentStatus);

    }

    @Test
    public void shouldReturnEmployeeIfFound() throws Exception {
        when(employeeRepository.findOne(1L)).thenReturn(employee);

        EmployeeDTO actualEmployee = sut.getEmployee(1L);

       verifyDTOHasExpectedValues(actualEmployee);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionIfEmolyeeNotFound() throws Exception{
        when(employeeRepository.findOne(1L)).thenReturn(null);

        sut.getEmployee(1L);
    }

    @Test
    public void shouldSearchRepositoryForEmployeesByLastName(){
        List<Employee> expectedEmployees = Arrays.asList(employee);
        when(employeeRepository.findByLastNameIgnoreCase("foo")).thenReturn(expectedEmployees);

        List<EmployeeDTO> actualEmployeeList = sut.getEmployeesByLastName("foo");

        assertTrue(actualEmployeeList.size() == 1);
        verifyDTOHasExpectedValues(actualEmployeeList.get(0));
    }

    @Test
    public void shouldSearchRepositoryForEmployeesByDepartmentName(){
        List<Employee> expectedEmployees = Arrays.asList(employee);
        when(employeeRepository.findByDepartmentName("Shoes")).thenReturn(expectedEmployees);

        List<EmployeeDTO> actualEmployeeList = sut.getEmployeesByDepartment("Shoes");

        assertTrue(actualEmployeeList.size() == 1);
        verifyDTOHasExpectedValues(actualEmployeeList.get(0));
    }

    @Test
    public void shouldFetchAllEmployeesOrderedByLastName(){
        Department department = new Department();
        department.setName("Wigs");

        Position position = new Position();
        position.setTitle("Comber");

        EmploymentStatus status = mock(EmploymentStatus.class);
        when(status.getStatus()).thenReturn("Active");

        Employee employee2 = new Employee();
        employee2.setLastName("Allen");
        employee2.setPosition(position);
        employee2.setDepartment(department);
        employee2.setEmploymentStatus(status);

        List<Employee> expectedEmployees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        List<EmployeeDTO> actualEmployeeList = sut.getEmployees();

        assertTrue(actualEmployeeList.size() == 2);

        //employee should be 2nd (allen before Doe)
        verifyDTOHasExpectedValues(actualEmployeeList.get(1));
    }

    private void verifyDTOHasExpectedValues(EmployeeDTO dto) {
        assertEquals(employee.getFirstName(), dto.getFirstName());
        assertEquals(employee.getLastName(), dto.getLastName());
        assertEquals(employee.getHireDate(), dto.getHireDate());
        assertEquals(employee.getHourlyRate(), dto.getHourlyRate());
        assertEquals(employee.getVacationHours(), dto.getVacationHours());
        assertEquals(employee.getSickHours(), dto.getSickHours());
        assertEquals(employee.getPosition().getTitle(), dto.getPosition());
        assertEquals(employee.getDepartment().getName(), dto.getDepartment());
        assertEquals(employee.getEmploymentStatus().getStatus(), dto.getEmploymentStatus());
    }
}
