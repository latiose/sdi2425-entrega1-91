package com.uniovi.gestor.services;
import javax.annotation.PostConstruct;

import com.uniovi.gestor.VehicleStatusConfig;
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

    private int numCars;
    private int numEmployees;

    public InsertSampleDataService(EmployeesService employeesService, RolesService rolesService, VehiclesService vehiclesService, JourneysService journeysService) {
        this.employeesService = employeesService;
        this.rolesService = rolesService;
        this.vehiclesService = vehiclesService;
        this.journeysService = journeysService;
    }
    @PostConstruct
    public void init() {
        Employee employee1 = new Employee("12345678Z", "Pedro", "Díaz");
        numEmployees++;
        employee1.setPassword("@Dm1n1str@D0r");
        employee1.setRole(rolesService.getRoles()[1]);
        employeesService.addEmployee(employee1);
//        Nombre: Usuario1, DNI: 10000001S, Password: Us3r@1-PASSW
//        Nombre: Usuario2, DNI: 10000002Q, Password: Us3r@2-PASSW
//        Nombre: Usuario3, DNI: 10000003V, Password: Us3r@3-PASSW
//        Nombre: Usuario4, DNI: 10000004H, Password: Us3r@4-PASSW
//        Nombre: Usuario5, DNI: 10000005L, Password: Us3r@5-PASSW
//        Nombre: Usuario6, DNI: 10000006C, Password: Us3r@6-PASSW
//        Nombre: Usuario7, DNI: 10000007K, Password: Us3r@7-PASSW
//        Nombre: Usuario8, DNI: 10000008E, Password: Us3r@8-PASSW
//        Nombre: Usuario9, DNI: 10000009T, Password: Us3r@9-PASSW
//        Nombre: Usuario10, DNI: 10000010R, Password: Us3r@10-PASSW
//        Nombre: Usuario11, DNI: 10000011W, Password: Us3r@11-PASSW
//        Nombre: Usuario12, DNI: 10000012A, Password: Us3r@12-PASSW
//        Nombre: Usuario13, DNI: 10000013G, Password: Us3r@13-PASSW
//        Nombre: Usuario14, DNI: 10000014M, Password: Us3r@14-PASSW
//        Nombre: Usuario15, DNI: 10000015Y, Password: Us3r@15-PASSW
        for (int i = 1; i <= 15; i++) {
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            String dni =  i < 10 ? String.format("1000000%d", i) :  String.format("100000%d", i);
            dni += letras.charAt(Integer.parseInt(dni) % 23);
            String nombre = "Usuario" + i;
            String password = "Us3r@"+i+"-PASSW";
            Employee employee = new Employee(dni, nombre, "Apellido" + i);
            employee.setPassword(password);
            employee.setRole(rolesService.getRoles()[0]);
            employeesService.addEmployee(employee);
            numEmployees++;
        }

        addVehicles();
        createTestJourneys();

    }

    private void addVehicles() {
        String[] fuelTypes = {"GASOLINA", "DIESEL", "MICROHIBRIDO", "HIBRIDO", "ELECTRICO", "GLP", "GNL"};
        Vehicle[] vehicles = {
                new Vehicle("1234BCD", "1HGCM82633A123456", "Toyota", "Corolla", fuelTypes[0], 15234.5f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("5678DFG", "2HGFA16578H123456", "Ford", "Focus", fuelTypes[1], 78345.2f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("9101GHJ", "3FAFP08192R123456", "Volkswagen", "Golf", fuelTypes[2], 21567.8f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("1121JKL", "4T1BF22K12U123456", "Honda", "Civic", fuelTypes[3], 9876.3f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("3141MNP", "5NPEB4AC2BH123456", "Nissan", "Leaf", fuelTypes[4], 5432.1f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("5161PQR", "6G2EC57Y08L123456", "BMW", "X5", fuelTypes[5], 34567.9f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("7181STV", "7FARW1H54HE123456", "Mercedes", "C-Class", fuelTypes[6], 67890.4f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("9202VWX", "8HGCM82633A123456", "Peugeot", "208", fuelTypes[0], 23456.7f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),
                new Vehicle("A1324BC", "9HGFA16578H123456", "Renault", "Clio", fuelTypes[1], 7890.2f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("B3545CD", "0FAFP08192R123456", "Tesla", "Model 3", fuelTypes[4], 1234.5f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),

                new Vehicle("1234BCE", "1HGCM8F633A123456", "Toyota", "Corolla", fuelTypes[0], 15234.5f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("5678DFH", "2HGFA1657GH123456", "Ford", "Focus", fuelTypes[1], 78345.2f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),
                new Vehicle("9101GHK", "3FAAP08192R123456", "Volkswagen", "Golf", fuelTypes[2], 21567.8f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("1121JKM", "4T1BFB2K12U123456", "Honda", "Civic", fuelTypes[3], 9876.3f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),
                new Vehicle("3141MNO", "5NPEB4AC2BB123456", "Nissan", "Leaf", fuelTypes[4], 5432.1f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("5161PQD", "6G2EC5AA08L123456", "BMW", "X5", fuelTypes[5], 34567.9f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),
                new Vehicle("7181STA", "7FARW1H54HEKK3456", "Mercedes", "C-Class", fuelTypes[6], 67890.4f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("9202VWG", "8HGCA82633A123456", "Peugeot", "208", fuelTypes[0], 23456.7f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE),
                new Vehicle("A1324BE", "9HGFA16578HF23456", "Renault", "Clio", fuelTypes[1], 7890.2f, VehicleStatusConfig.VehicleStatus.AVAILABLE),
                new Vehicle("B3545CA", "0FAFP081M2R123456", "Tesla", "Model 3", fuelTypes[4], 1234.5f, VehicleStatusConfig.VehicleStatus.UNAVAILABLE)


        };
        for (Vehicle v : vehicles){
            vehiclesService.addVehicle(v);
            numCars++;
        }
    }

    public int getNumCars() {
        return numCars;
    }
    public int getNumEmployees(){ return numEmployees; }



    private void createTestJourneys() {
        Employee employee = employeesService.getEmployeeByDni("12345678Z");
        Vehicle vehicle = vehiclesService.getVehicleByNumberPlate("3141MNP");
        Vehicle vehicle2 = vehiclesService.getVehicleByNumberPlate("5678DFG");
        Vehicle vehicle3 = vehiclesService.getVehicleByNumberPlate("9101GHJ");
        Vehicle vehicle4 = vehiclesService.getVehicleByNumberPlate("5161PQR");

        Journey journey1 = new Journey(vehicle);
        journey1.setEmployee(employee);
        journey1.setOdometerStart(15234.5);
        journey1.setOdometerEnd(15300.5);
        journey1.setStartDate(LocalDateTime.now().minusHours(2));
        journey1.setEndDate(LocalDateTime.now());
        journey1.setDuration(journey1.getDuration());
        journeysService.addJourney(journey1);

        Journey journey2 = new Journey(vehicle2);
        journey2.setEmployee(employee);
        journey2.setOdometerStart(78345.2);
        journey2.setOdometerEnd(78410.2);
        journey2.setStartDate(LocalDateTime.now().minusHours(1));
        journey2.setEndDate(LocalDateTime.now());
        journey2.setDuration(journey2.getDuration());
        journeysService.addJourney(journey2);

        Journey journey3 = new Journey(vehicle3);
        journey3.setEmployee(employee);
        journey3.setOdometerStart(78345.2);
        journey3.setStartDate(LocalDateTime.now().minusHours(1));
        journeysService.addJourney(journey3);

        Employee employee2 = employeesService.getEmployeeByDni("10000005L");
        Journey journey4 = new Journey(vehicle4);
        journey4.setEmployee(employee2);
        journey4.setOdometerStart(78345.2);
        journey4.setStartDate(LocalDateTime.now().minusHours(1));
        journeysService.addJourney(journey4);

        employee2 = employeesService.getEmployeeByDni("10000010R");
        Vehicle vehicleEmp2 = vehiclesService.getVehicleByNumberPlate("B3545CA");
        journey4 = new Journey(vehicleEmp2);
        journey4.setEmployee(employee2);
        journey4.setOdometerStart(1234.5);
        journey4.setStartDate(LocalDateTime.now().minusHours(2));
        journeysService.addJourney(journey4);

        for (int i = 1; i <= 15; i++) {
            String dni = i < 10 ? String.format("1000000%d", i) : String.format("100000%d", i);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            dni += letras.charAt(Integer.parseInt(dni) % 23);
            Employee emp = employeesService.getEmployeeByDni(dni);
            if (emp != null) {
                for (int j = 0; j < 10; j++) {
                    Vehicle veh = vehiclesService.getAvailableVehicle(j);
                    if (veh != null) {
                        Journey journey = new Journey(veh);
                        journey.setEmployee(emp);
                        journey.setOdometerStart(veh.getMileage() + (j * 10));
                        journey.setOdometerEnd(journey.getOdometerStart() + 50);
                        journey.setStartDate(LocalDateTime.now().minusDays(j).minusHours(j));
                        journey.setEndDate(journey.getStartDate().plusHours(1));
                        journey.setDuration(journey.getDuration());
                        journeysService.addJourney(journey);
                    }
                }
            }
        }
    }

}