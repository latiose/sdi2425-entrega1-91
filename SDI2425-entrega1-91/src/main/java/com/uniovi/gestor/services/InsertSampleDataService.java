package com.uniovi.gestor.services;
import javax.annotation.PostConstruct;

import com.uniovi.gestor.entities.Employee;
import org.springframework.stereotype.Service;

@Service
public class InsertSampleDataService {
    private final EmployeesService employeesService;
    private final RolesService rolesService;
    public InsertSampleDataService(EmployeesService employeesService, RolesService rolesService) {
        this.employeesService = employeesService;
        this.rolesService = rolesService;
    }
    @PostConstruct
    public void init() {
        Employee employee1 = new Employee("12345678Z", "Pedro", "Díaz");
        employee1.setPassword("@Dm1n1str@D0r");
        employee1.setRole(rolesService.getRoles()[1]);
        employeesService.addEmployee(employee1);
//        10000001S Usuario1 Us3r@1-PASSW
//        10000002Q Usuario2 Us3r@2-PASSW
//        10000003S Usuario3 Us3r@3-PASSW
//        10000004Q Usuario4 Us3r@4-PASSW
//        10000005S Usuario5 Us3r@5-PASSW
//        10000006Q Usuario6 Us3r@6-PASSW
//        10000007S Usuario7 Us3r@7-PASSW
//        10000008Q Usuario8 Us3r@8-PASSW
//        10000009S Usuario9 Us3r@9-PASSW
//        100000010Q Usuario10 Us3r@10-PASSW
//        100000011S Usuario11 Us3r@11-PASSW
//        100000012Q Usuario12 Us3r@12-PASSW
//        100000013S Usuario13 Us3r@13-PASSW
//        100000014Q Usuario14 Us3r@14-PASSW
//        100000015S Usuario15 Us3r@15-PASSW
        for (int i = 1; i <= 15; i++) {
            String dni = String.format("1000000%d%s", i, (i % 2 == 0) ? "Q" : "S");
            String nombre = "Usuario" + i;
            String password = "Us3r@"+i+"-PASSW";


            Employee employee = new Employee(dni, nombre, "Apellido" + i);
            employee.setPassword(password);
            employee.setRole(rolesService.getRoles()[0]);

            employeesService.addEmployee(employee);
        }

    }
}