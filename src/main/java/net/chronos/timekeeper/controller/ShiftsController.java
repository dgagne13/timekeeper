package net.chronos.timekeeper.controller;

import net.chronos.timekeeper.dto.ShiftDTO;
import net.chronos.timekeeper.exception.NotFoundException;
import net.chronos.timekeeper.exception.ShiftCreationException;
import net.chronos.timekeeper.service.ShiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/shifts")
public class ShiftsController {
    private static final Logger log = LoggerFactory.getLogger(ShiftsController.class);

    @Autowired
    ShiftService shiftService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ShiftDTO> getEmployeeShifts(@RequestParam(value = "employee-id", required = true) Long employeeId) {
        log.info("Get shifts for employee " + employeeId);
        return shiftService.getShiftsForEmployee(employeeId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createShift(@RequestBody ShiftDTO shiftDTO, UriComponentsBuilder builder) {
        log.info("Creating a shift");
        Long id = shiftService.createShift(shiftDTO);
        UriComponents components = builder.path("/v1/shifts").buildAndExpand(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(components.toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/{shift-id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShift(@PathVariable(value = "shift-id") Long shiftId) {
        log.info("Deleting shift " + shiftId);
        shiftService.deleteShift(shiftId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(HttpServletRequest request, NotFoundException e) {
        Map<String,Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND);
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(ShiftCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleShiftException(HttpServletRequest request, ShiftCreationException e) {
        Map<String,Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST);
        error.put("message", e.getMessage());
        return error;
    }
}
