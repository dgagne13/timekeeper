package net.chronos.timekeeper.controller;

import net.chronos.timekeeper.dto.EmployeeDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/employees")
public class EmployeesContoller {

    private static final Logger log = LoggerFactory.getLogger(EmployeesContoller.class);
    @Autowired
    EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EmployeeDTO> getEmployees(@RequestParam(value = "last", required = false) String lastName,
                                          @RequestParam(value = "department", required = false) String department) {
        log.info("Find employees with params " + lastName + ", " + department);
        if(lastName != null) {
            return employeeService.getEmployeesByLastName(lastName);
        } else if (department != null) {
            return employeeService.getEmployeesByDepartment(department);
        } else {
            return employeeService.getEmployees();
        }
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleNotFound(HttpServletRequest request, NotFoundException e) {
        Map<String,Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND);
        error.put("message", e.getMessage());
        return error;
    }
}
