package com.uniovi.gestor.services;
import javax.annotation.PostConstruct;

import com.uniovi.gestor.entities.Employee;
import com.uniovi.gestor.entities.Journey;
import com.uniovi.gestor.entities.Vehicle;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InsertSampleDataService {
    private final EmployeesService employeesService;
    private final RolesService rolesService;
    private final VehiclesService vehiclesService;
    private final JourneysService journeysService;

    private int numCars; // esto lo utilizo en los tests para saber exactamente cuántos coches hay en el sistema

    public InsertSampleDataService(EmployeesService employeesService, RolesService rolesService, VehiclesService vehiclesService, JourneysService journeysService) {
        this.employeesService = employeesService;
        this.rolesService = rolesService;
        this.vehiclesService = vehiclesService;
        this.journeysService = journeysService;
    }
    @PostConstruct
    public void init() {
        Employee employee1 = new Employee("12345678Z", "Pedro", "Díaz");
        employee1.setPassword("123");
        employee1.setRole(rolesService.getRoles()[1]);
        employeesService.addEmployee(employee1);
//        10000001S Us3r@1-PASSW
//        10000002Q Us3r@2-PASSW
//        10000003V Us3r@3-PASSW
//        10000004H Us3r@4-PASSW
//        10000005L Us3r@5-PASSW
//        10000006C Us3r@6-PASSW
//        10000007K Us3r@7-PASSW
//        10000008E Us3r@8-PASSW
//        10000009T Us3r@9-PASSW
//        100000010N Us3r@10-PASSW
//        100000011J Us3r@11-PASSW
//        100000012Z Us3r@12-PASSW
//        100000013S Us3r@13-PASSW
//        100000014Q Us3r@14-PASSW
//        100000015V Us3r@15-PASSW
        for (int i = 1; i <= 15; i++) {
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            String dni = String.format("1000000%d", i);
            dni += letras.charAt(Integer.parseInt(dni) % 23);
            String nombre = "Usuario" + i;
            String password = "Us3r@"+i+"-PASSW";


            Employee employee = new Employee(dni, nombre, "Apellido" + i);
            employee.setPassword(password);
            employee.setRole(rolesService.getRoles()[0]);
//            employeesService.addEmployee(employee);
        }

        addVehicles();
        createTestJourneys();

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


    private void createTestJourneys() {
        Employee employee = employeesService.getEmployees().get(0);
        Vehicle vehicle = vehiclesService.getVehicleByNumberPlate("1234BCE");

        Journey journey1 = new Journey(vehicle);
        journey1.setEmployee(employee);
        journey1.setOdometerStart(15234.5);
        journey1.setOdometerEnd(15300.5);
        journey1.setStartDate(LocalDateTime.now().minusHours(2));
        journey1.setEndDate(LocalDateTime.now());
        journey1.setDuration(2);
        journeysService.addJourney(journey1);

        Journey journey2 = new Journey(vehicle);
        journey2.setEmployee(employee);
        journey2.setOdometerStart(78345.2);
        journey2.setOdometerEnd(78410.2);
        journey2.setStartDate(LocalDateTime.now().minusHours(1));
        journey2.setEndDate(LocalDateTime.now());
        journey2.setDuration(1);
        journeysService.addJourney(journey2);
    }
}