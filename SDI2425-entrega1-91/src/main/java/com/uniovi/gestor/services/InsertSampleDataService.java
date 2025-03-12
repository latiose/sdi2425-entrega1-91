package com.uniovi.gestor.services;
import javax.annotation.PostConstruct;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class InsertSampleDataService {
    private final EmployeesService employeesService;
    private final RolesService rolesService;
    private final VehiclesService vehiclesService;

    private int numCars; // esto lo utilizo en los tests para saber exactamente cuántos coches hay en el sistema

    public InsertSampleDataService(EmployeesService employeesService, RolesService rolesService, VehiclesService vehiclesService) {
        this.employeesService = employeesService;
        this.rolesService = rolesService;
        this.vehiclesService = vehiclesService;
    }
    @PostConstruct
    public void init() {
        Employee employee1 = new Employee("12345678Z", "Pedro", "Díaz");
        employee1.setPassword("admin");
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

        addVehicles();

    }

    private void addVehicles() {
        String[] fuelTypes = {"GASOLINA", "DIESEL", "MICROHIBRIDO", "HIBRIDO", "ELECTRICO", "GLP", "GNL"};
        Vehicle[] vehicles = {
                new Vehicle("1234BCD", "1HGCM82633A123456", "Toyota", "Corolla", fuelTypes[0], 15234.5f, "LIBRE"),
                new Vehicle("5678DFG", "2HGFA16578H123456", "Ford", "Focus", fuelTypes[1], 78345.2f, "OCUPADO"),
                new Vehicle("9101GHJ", "3FAFP08192R123456", "Volkswagen", "Golf", fuelTypes[2], 21567.8f, "LIBRE"),
                new Vehicle("1121JKL", "4T1BF22K12U123456", "Honda", "Civic", fuelTypes[3], 9876.3f, "OCUPADO"),
                new Vehicle("3141MNP", "5NPEB4AC2BH123456", "Nissan", "Leaf", fuelTypes[4], 5432.1f, "LIBRE"),
                new Vehicle("5161PQR", "6G2EC57Y08L123456", "BMW", "X5", fuelTypes[5], 34567.9f, "OCUPADO"),
                new Vehicle("7181STV", "7FARW1H54HE123456", "Mercedes", "C-Class", fuelTypes[6], 67890.4f, "LIBRE"),
                new Vehicle("9202VWX", "8HGCM82633A123456", "Peugeot", "208", fuelTypes[0], 23456.7f, "OCUPADO"),
                new Vehicle("A1324BC", "9HGFA16578H123456", "Renault", "Clio", fuelTypes[1], 7890.2f, "LIBRE"),
                new Vehicle("B3545CD", "0FAFP08192R123456", "Tesla", "Model 3", fuelTypes[4], 1234.5f, "OCUPADO"),

                new Vehicle("1234BCE", "1HGCM8F633A123456", "Toyota", "Corolla", fuelTypes[0], 15234.5f, "LIBRE"),
                new Vehicle("5678DFH", "2HGFA1657GH123456", "Ford", "Focus", fuelTypes[1], 78345.2f, "OCUPADO"),
                new Vehicle("9101GHK", "3FAAP08192R123456", "Volkswagen", "Golf", fuelTypes[2], 21567.8f, "LIBRE"),
                new Vehicle("1121JKM", "4T1BFB2K12U123456", "Honda", "Civic", fuelTypes[3], 9876.3f, "OCUPADO"),
                new Vehicle("3141MNO", "5NPEB4AC2BB123456", "Nissan", "Leaf", fuelTypes[4], 5432.1f, "LIBRE"),
                new Vehicle("5161PQD", "6G2EC5AA08L123456", "BMW", "X5", fuelTypes[5], 34567.9f, "OCUPADO"),
                new Vehicle("7181STA", "7FARW1H54HEKK3456", "Mercedes", "C-Class", fuelTypes[6], 67890.4f, "LIBRE"),
                new Vehicle("9202VWG", "8HGCA82633A123456", "Peugeot", "208", fuelTypes[0], 23456.7f, "OCUPADO"),
                new Vehicle("A1324BE", "9HGFA16578HF23456", "Renault", "Clio", fuelTypes[1], 7890.2f, "LIBRE"),
                new Vehicle("B3545CA", "0FAFP081M2R123456", "Tesla", "Model 3", fuelTypes[4], 1234.5f, "OCUPADO")


        };
        for (Vehicle v : vehicles){
            vehiclesService.addVehicle(v);
            numCars++;
        }
    }

    public int getNumCars() {
        return numCars;
    }
}