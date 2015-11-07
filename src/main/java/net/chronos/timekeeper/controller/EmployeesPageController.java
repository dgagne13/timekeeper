package net.chronos.timekeeper.controller;

import net.chronos.timekeeper.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeesPageController {

    @Autowired
    DepartmentService departmentService;

    @RequestMapping("/")
    public String getEmployeesPage(Model model) {
        model.addAttribute("departments", departmentService.getDepartments());
        return "employees";
    }
}
