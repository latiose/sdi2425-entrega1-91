package com.uniovi.notaneitor.services;
import javax.annotation.PostConstruct;

import com.uniovi.notaneitor.entities.Employee;
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
        Employee employee2 = new Employee("99999991B", "Lucas", "Núñez");
        employee2.setPassword("123456");
        employee2.setRole(rolesService.getRoles()[0]);



//            Set user1Marks = new HashSet<Mark>() {
//            {
//                add(new Mark("Nota A1", 10.0, employee1));
//                add(new Mark("Nota A2", 9.0, employee1));
//                add(new Mark("Nota A3", 7.0, employee1));
//                add(new Mark("Nota A4", 6.5, employee1));
//            }
//        };
//        employee1.setMarks(user1Marks);
//        Set user2Marks = new HashSet<Mark>() {
//            {
//                add(new Mark("Nota B1", 5.0, employee2));
//                add(new Mark("Nota B2", 4.3, employee2));
//                add(new Mark("Nota B3", 8.0, employee2));
//                add(new Mark("Nota B4", 3.5, employee2));
//            }
//        };
//        employee2.setMarks(user2Marks);
//        Set user3Marks = new HashSet<Mark>() {
//            {
//                add(new Mark("Nota C1", 5.5, empleado3));
//                add(new Mark("Nota C2", 6.6, empleado3));
//                add(new Mark("Nota C3", 7.0, empleado3));
//            }
//        };
//        empleado3.setMarks(user3Marks);
//        Set user4Marks = new HashSet<Mark>() {
//            {
//                add(new Mark("Nota D1", 10.0, empleado4));
//                add(new Mark("Nota D2", 8.0, empleado4));
//                add(new Mark("Nota D3", 9.0, empleado4));
//            }
//        };
//        empleado4.setMarks(user4Marks);
        employeesService.addEmployee(employee1);
        employeesService.addEmployee(employee2);

    }
}