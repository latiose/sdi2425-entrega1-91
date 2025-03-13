package com.uniovi.gestor.controllers;
import com.uniovi.gestor.PasswordGenerator;
import com.uniovi.gestor.services.EmployeesService;
import com.uniovi.gestor.services.RolesService;
import com.uniovi.gestor.services.SecurityService;
import com.uniovi.gestor.validators.AddEmployeeFormValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.uniovi.gestor.entities.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EmployeesController {
    private final EmployeesService employeesService;
    private final SecurityService securityService;
    private final AddEmployeeFormValidator addEmployeeFormValidator;
    private final RolesService rolesService;

    public EmployeesController(EmployeesService employeesService, SecurityService securityService, AddEmployeeFormValidator
            addEmployeeFormValidator, RolesService rolesService) {
        this.employeesService = employeesService;
        this.securityService = securityService;
        this.addEmployeeFormValidator = addEmployeeFormValidator;
        this.rolesService = rolesService;
    }
    @RequestMapping("/employee/list")
    public String getListado(Model model,HttpSession session) {
        model.addAttribute("employeesList", employeesService.getEmployees());
        String password = (String) session.getAttribute("generatedPassword");
        if (password != null) {
            model.addAttribute("generatedPassword", password);
           session.removeAttribute("generatedPassword");
        }
        return "employee/list";
    }
    @RequestMapping(value = "/employee/add")
    public String getEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/add";
    }

    @RequestMapping(value = "/employee/add", method = RequestMethod.POST)
    public String signup(@Validated Employee employee, BindingResult result, Model model, HttpSession session) {
        addEmployeeFormValidator.validate(employee, result);
        model.addAttribute("employee", employee);
        if (result.hasErrors()) {
            return "/employee/add";
        }
        String generatedPassword = PasswordGenerator.generateSecurePassword();
        employee.setPassword(generatedPassword);

        employee.setRole(rolesService.getRoles()[0]);
        employeesService.addEmployee(employee);
        session.setAttribute("generatedPassword", generatedPassword);
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
        return "employee/edit";
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
    public String login(Model model, @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
        if (logout != null) {
            Object logoutAttribute = request.getSession().getAttribute("SPRING_SECURITY_LOGOUT");

            if (logoutAttribute != null || request.getAuthType() == null) { //para que no se muestre el mensaje si se añade el atributo sin estar loggeado
                model.addAttribute("logout", true);
                request.getSession().removeAttribute("SPRING_SECURITY_LOGOUT");
            }
        }
        return "login";
    }

    @RequestMapping(value = "/login/success", method = RequestMethod.GET)
    public String loginSuccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String dni = auth.getName();
        Employee activeEmployee = employeesService.getEmployeeByDni(dni);
        if (activeEmployee.getRole().equals(rolesService.getRoles()[0])) {
            return "redirect:/journey/list";
        }
        return "redirect:/employee/list";
    }
@RequestMapping(value = "/login/error", method = RequestMethod.GET)
public String loginError(Model model) {
    model.addAttribute("error", true);
    return "login";
}

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping("/employee/list/update")
    public String updateList(Model model){
        model.addAttribute("employeesList", employeesService.getEmployees() );
        return "employee/list :: employeeTable";
    }


}