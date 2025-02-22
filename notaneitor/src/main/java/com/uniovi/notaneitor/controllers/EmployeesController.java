package com.uniovi.notaneitor.controllers;
import com.uniovi.notaneitor.services.EmployeesService;
import com.uniovi.notaneitor.services.RolesService;
import com.uniovi.notaneitor.services.SecurityService;
import com.uniovi.notaneitor.validators.SignUpFormValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.uniovi.notaneitor.entities.*;

@Controller
public class EmployeesController {
    private final EmployeesService employeesService;
    private final SecurityService securityService;
    private final SignUpFormValidator signUpFormValidator;
    private final RolesService rolesService;

    public EmployeesController(EmployeesService employeesService, SecurityService securityService, SignUpFormValidator
            signUpFormValidator, RolesService rolesService) {
        this.employeesService = employeesService;
        this.securityService = securityService;
        this.signUpFormValidator = signUpFormValidator;
        this.rolesService = rolesService;
    }
    @RequestMapping("/employee/list")
    public String getListado(Model model) {
        model.addAttribute("employeesList", employeesService.getEmployees());
        return "employee/list";
    }
    @RequestMapping(value = "/employee/add")
    public String getEmployee(Model model) {
        model.addAttribute("rolesList", rolesService.getRoles());
        model.addAttribute("employeesList", employeesService.getEmployees());
        return "employee/add";
    }
    @RequestMapping(value = "/employee/add", method = RequestMethod.POST)
    public String setEmployee(@ModelAttribute Employee employee) {
        employeesService.addEmployee(employee);
        return "redirect:/employee/list";
    }
    @RequestMapping("/employee/details/{id}")
    public String getDetail(Model model, @PathVariable Long id) {
        model.addAttribute("employee", employeesService.getEmployee(id));
        return "employee/details";
    }
    @RequestMapping("/employee/delete/{id}")
    public String delete(@PathVariable Long id) {
        employeesService.deleteEmployee(id);
        return "redirect:/employee/list";
    }
    @RequestMapping(value = "/employee/edit/{id}")
    public String getEdit(Model model, @PathVariable Long id) {
        Employee employee = employeesService.getEmployee(id);
        model.addAttribute("employee", employee);
        return "empleado/edit";
    }

    @RequestMapping(value = "/employee/edit/{id}", method = RequestMethod.POST)
    public String setEdit(@ModelAttribute Employee employee, @PathVariable Long id) {
        Employee originalEmployee = employeesService.getEmployee(id);
        originalEmployee.setDni(employee.getDni());
        originalEmployee.setName(employee.getName());
        originalEmployee.setLastName(employee.getLastName());
        employeesService.addEmployee(originalEmployee);
        return "redirect:/employee/details/" + id;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Employee activeEmployee = employeesService.getEmployeeByDni(dni);

        return "home";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("employee", new Employee());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated Employee employee, BindingResult result) {
        signUpFormValidator.validate(employee, result);
        if (result.hasErrors()) {
            return "signup";
        }
        employee.setRole(rolesService.getRoles()[0]);
        employeesService.addEmployee(employee);
        securityService.autoLogin(employee.getDni(), employee.getPasswordConfirm());
        return "redirect:home";
    }



    @RequestMapping("/employee/list/update")
    public String updateList(Model model){
        model.addAttribute("employeesList", employeesService.getEmployees() );
        return "employee/list :: employeeTable";
    }


}