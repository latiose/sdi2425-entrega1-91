package com.uniovi.gestor;

import com.uniovi.gestor.entities.Vehicle;
import com.uniovi.gestor.pageobjects.*;
import com.uniovi.gestor.repositories.VehiclesRepository;
import com.uniovi.gestor.services.InsertSampleDataService;
import com.uniovi.gestor.services.VehiclesService;
import org.hibernate.sql.Insert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GestorApplicationTests {

    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "geckodriver.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8090";

    @Autowired
    private InsertSampleDataService insertSampleDataService;


    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp(){
        driver.navigate().to(URL);
    }

    @AfterEach
    public void tearDown(){
        driver.manage().deleteAllCookies();
    }

    @BeforeAll
    static public void begin() {}

    @AfterAll
    static public void end() {
        driver.quit();
    }


    @Test
    @Order(1)
    //Iniciar sesión con datos validos (Admin)
    public void PR01() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");
        String checkText = "12345678Z"; //como siempre es el primer usuario en la lista su propio dni siempre estará presente
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(2)
    //Iniciar sesión con datos validos (Empleado)
    public void PR02() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000001S","Us3r@1-PASSW");
        String checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(3)
    //Iniciar sesión con datos contraseña y dni vacíos
    public void PR03() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "","");
        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/login"));
    }

    @Test
    @Order(4)
    //Iniciar sesión con dni válido pero contraseña inválida
    public void PR04() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver,"10000001S" ,"1234");
        String checkText = PO_HomeView.getP().getString("login.message", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(5)
    //Cerrar sesión y comprobar los mensajes
    public void PR05() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000001S","Us3r@1-PASSW");
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(6)
    //Comprobar que el botón de cerrar sesión no es visible sin estar auténticado.
    public void PR06() {
        List<WebElement> logoutLink = driver.findElements(By.linkText("Desconectar"));
        assertTrue(logoutLink.isEmpty());
    }

    @Test
    @Order(7)
    //Añadir empleado válido
    public void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Agregar empleado");
        PO_PrivateView.fillFormAddEmployee(driver, "07112884L", "Pablo", "Perez Alvarez");
        PO_ListView.goToLastPage(driver);
        String checkText = "07112884L";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
        PO_LoginView.logOut(driver);
    }



    @Test
    @Order(8)
    //Empleado sin parmaetros
    public void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Agregar empleado");

        driver.findElement(By.className("btn-primary")).click();

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/employee/add"));

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(9)
    //Empleado con dni inválido
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Agregar empleado");

        PO_PrivateView.fillFormAddEmployee(driver, "123", "Pablo", "Perez Alvarez");

        String checkText = PO_HomeView.getP().getString("Error.signup.dni.invalid", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(10)
    //Empleado dni repetido
    public void PR010() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Agregar empleado");

        PO_PrivateView.fillFormAddEmployee(driver, "12345678Z", "Pablo", "Perez Alvarez");

        String checkText = PO_HomeView.getP().getString("Error.dni.duplicate", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(11)
    // Registro de un vehículo con datos válidos
    public void PR011() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCL", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "Diésel");
        PO_ListView.goToLastPage(driver);
        String checkText = "1234BCL";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(12)
    // Registro de un vehículo con datos inválidos: matrícula vacía
    public void PR012A() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "Diésel");

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/vehicle/add"));
    }

    @Test
    @Order(13)
    // Registro de un vehículo con datos inválidos: número de bastidor vacío
    public void PR012B() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "", "Toyota", "Corolla", "Diésel");

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/vehicle/add"));
    }
    @Test
    @Order(14)
    // Registro de un vehículo con datos inválidos: Marca vacía
    public void PR012C() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "", "Corolla", "Diésel");

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/vehicle/add"));
    }
    @Test
    @Order(15)
    // Registro de un vehículo con datos inválidos: Modelo vacío
    public void PR012D() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "Toyota", "", "Diésel");

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/vehicle/add"));
    }

    @Test
    @Order(16)
    // Registro de un vehículo con datos inválidos: formato de matrícula inválido
    public void PR013() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "123", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "Diésel");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.addvehicle.plate.invalid",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.addvehicle.plate.invalid",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(17)
    // Registro de un vehículo con datos inválidos: longitud del número de bastidor inválido -> mayor de 17
    public void PR014A() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCF", "ASDFGHJKLQWERTYUII", "Toyota", "Corolla", "Diésel");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.vin.length",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.vin.length",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(18)
    // Registro de un vehículo con datos inválidos: longitud del número de bastidor inválido -> menor de 17
    public void PR014B() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCF", "ASDFGHJKLQWERTYU", "Toyota", "Corolla", "Diésel");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.vin.length",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.vin.length",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(19)
    // Registro de un vehículo con datos inválidos: matrícula existente
    public void PR015() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "Diésel");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.plate.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.plate.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(20)
    // Registro de un vehículo con datos inválidos: número de bastidor existente
    public void PR016() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Agregar vehículo");

        PO_PrivateView.fillFormAddVehicle(driver, "1234BGD", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "Diésel");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.vin.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.vin.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    public void PR017() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "admin");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Ver empleados");

        int numEmployees = insertSampleDataService.getNumEmployees() + 1;

        int totalCount = 0;
        boolean next = true;
        while (next) {
            List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"employeeTable\"]/tbody/tr"));
            totalCount += employeeRows.size();
            next = PO_PrivateView.goToNextPage(driver);
        }

        Assertions.assertEquals(numEmployees, totalCount, "El número de empleados no coincide");
    }

    @Test
    public void PR018() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "admin");

        List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"employeeTable\"]/tbody/tr"));

        WebElement secondEditLink = driver.findElement(By.xpath("(//table[@id='employeeTable']//a[contains(text(),'modificar')])[3]"));
        secondEditLink.click();

        String dni = "58435079Z";
        String name = "Marcos";
        String lastName = "Caraduje Martínez";
        String role = "ROLE_ADMIN";

        PO_PrivateView.filFormEditEmployee(driver, dni, name, lastName, role);

        WebElement dniElement = driver.findElement(By.id("dni"));
        Assertions.assertEquals(dni, dniElement.getText());

        WebElement nameElement = driver.findElement(By.id("name"));
        Assertions.assertEquals(name, nameElement.getText());

        WebElement lastNameElement = driver.findElement(By.id("lastName"));
        Assertions.assertEquals(lastName, lastNameElement.getText());

        WebElement roleElement = driver.findElement(By.id("role"));
        Assertions.assertEquals(role, roleElement.getText());

        PO_LoginView.logOut(driver);


        PO_LoginView.fillForm(driver, dni, "Us3r@2-PASSW");

        String url = driver.getCurrentUrl();

        Assertions.assertTrue(url.contains("/employee/list"));


    }

    @Test
    public void PR019() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "admin");

        List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"employeeTable\"]/tbody/tr"));

        WebElement secondEditLink = driver.findElement(By.xpath("(//table[@id='employeeTable']//a[contains(text(),'modificar')])[3]"));
        secondEditLink.click();

        String dni = "12345678Z";
        String name = "";
        String lastName = "";
        String role = "ROLE_ADMIN";

        PO_PrivateView.filFormEditEmployee(driver, dni, name, lastName, role);

        String checkTextDni = PO_View.getP().getString("Error.dni.duplicate", PO_Properties.getSPANISH());
        String checkTextName = PO_View.getP().getString("Error.empty", PO_Properties.getSPANISH());
        String checkTextLastName = PO_View.getP().getString("Error.empty", PO_Properties.getSPANISH());

        List<WebElement> resultDni = PO_View.checkElementByKey(driver,  "Error.dni.duplicate" ,PO_Properties.getSPANISH());
        assertFalse(resultDni.isEmpty());

        List<WebElement> resultName = PO_View.checkElementByKey(driver,  "Error.empty", PO_Properties.getSPANISH());
        assertFalse(resultName.isEmpty());

        List<WebElement> resultLastName = PO_View.checkElementByKey(driver, "Error.empty", PO_Properties.getSPANISH());
        assertFalse(resultLastName.isEmpty());





    }

    @Test
    @Order(21)
    public void PR020() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");

        int numCars = insertSampleDataService.getNumCars() + 1; // el que se añade en uno de los tests anteriores

        int totalCount = 0;
        boolean next = true;
        while (next) {
            List<WebElement> vehicleRows = driver.findElements(By.xpath("//*[@id=\"vehicleTable\"]/tbody/tr"));
            totalCount += vehicleRows.size();
            next = PO_ListView.goToNextPage(driver);
        }

        Assertions.assertEquals(totalCount, numCars, "El número de vehículos no coincide.");
    }

    @Test
    @Order(22)
    @Transactional
    // Ir a lista de vehículos, borrar el primero de la lista, comprobar que la lista se actualiza y que el vehículo desaparece
    public void PR021() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");

        boolean found = PO_ListView.deleteVehiclesByIndexes(driver, new int[]{0});
        assertFalse(found, "El vehículo no se ha eliminado correctamente.");
    }

    @Test
    @Order(23)
    @Transactional
    // Ir a lista de vehículos, borrar el último de la lista, comprobar que la lista se actualiza y que el vehículo desaparece
    public void PR022() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");

        List<WebElement> vehicleRows = driver.findElements(By.xpath("//*[@id=\"vehicleTable\"]/tbody/tr"));
        PO_ListView.goToLastPage(driver);
        boolean found = PO_ListView.deleteVehiclesByIndexes(driver, new int[]{vehicleRows.size() - 1});
        assertFalse(found, "El vehículo no se ha eliminado correctamente.");
    }

    @Test
    @Order(24)
    @Transactional
    // Ir a lista de vehículos, borrar 3 vehículos, comprobar que la lista se actualiza y que dichos vehículos desaparecen
    public void PR023() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");

        boolean found = PO_ListView.deleteVehiclesByIndexes(driver, new int[]{0, 1, 2});
        assertFalse(found, "Los vehículos no se han eliminado correctamente.");
    }

    @Test
    @Order(25)
    // Mostrar el listado de trayectos
    public void PR024() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));
        List<String> matriculasEsperadas = List.of("9101GHJ", "5678DFG", "3141MNP");
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (!cells.isEmpty()) {
                String matricula = cells.get(1).getText();
                assertTrue(matriculasEsperadas.contains(matricula));
            }
        }
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(26)
    //Añadir válido
    public void PR025() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000001S", "Us3r@1-PASSW");
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("B3545CD");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String checkText = PO_HomeView.getP().getString("journey.list", PO_Properties.getSPANISH());
        assertFalse(checkText.isEmpty());
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(27)
    //Añadir ya tiene trayecto en curso
    public void PR026() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000001S", "Us3r@1-PASSW");
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("5161PQR");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = driver.findElement(By.className("alert-danger"));
        String checkText = PO_HomeView.getP().getString("Error.journeyStarted", PO_Properties.getSPANISH());
        assertTrue(errorMessage.getText().contains(checkText));
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(28)
    //Añadir el coche ya esta siendo usado
    public void PR027() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q", "Us3r@2-PASSW");
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("5161PQR");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = driver.findElement(By.className("alert-danger"));
        String checkText = PO_HomeView.getP().getString("Error.vehicleInUse", PO_Properties.getSPANISH());
        assertTrue(errorMessage.getText().contains(checkText));
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(29)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje válido
    public void PR028() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000010R","Us3r@10-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Manolo", 1.2, 50.0, true, 100000, "Repostaje de prueba");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("B3545CA");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "vehicle.selection.refuel",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("vehicle.selection.refuel",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='refuelsTable']/tbody/tr"));
        boolean isPresent = false;
        for (WebElement row : rows) {
            if (row.getText().contains("ELECTRICO")) {
                isPresent = true;
                break;
            }
        }
        Assertions.assertTrue(isPresent, "El repostaje no se ha añadido correctamente.");
    }


    @Test
    @Order(30)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje inválido - no hay trayecto asignado
    public void PR029() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q","Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", 1.2, 50.0, true, 100000, "Repostaje de prueba");

        WebElement errorMessage = driver.findElement(By.className("alert-danger"));
        String checkText = PO_HomeView.getP().getString("Error.journeyNotStarted", PO_Properties.getSPANISH());
        assertTrue(errorMessage.getText().contains(checkText));
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(31)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje inválido - nombre de estación vacío, precio vacío, cantidad vacía, odómetro vacío
    public void PR030() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.clickSendButton(driver);
        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/refuel/add"));
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(32)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje inválido - precio y cantidad negativos
    public void PR031() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", -1.2, -50.0, true, 100000, "Repostaje de prueba");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.negative",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.negative",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(33)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje inválido -  odómetro anterior al inicial del trayecto
    public void PR032() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000010R","Us3r@10-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", 1.2, 50.0, true, 100, "Repostaje de prueba");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.odometer",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.odometer",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(34)
    // Finalizar trayecto válido
    public void PR033() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000005L","Us3r@5-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().contains("5161PQR") && row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]")).isDisplayed()) {
                WebElement finishButton = row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]"));
                finishButton.click();
                break;
            }
        }


        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("55000000000");

        WebElement observationsField = driver.findElement(By.id("observations"));
        observationsField.clear();
        observationsField.sendKeys("Finalización del trayecto");

        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Finalizar')]"));
        submitButton.click();
        String checkText = PO_HomeView.getP().getString("journey.list", PO_Properties.getSPANISH());
        assertFalse(checkText.isEmpty());
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(35)
    // Finalizar trayecto odometro vacio
    public void PR034() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("9202VWG");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().contains("9202VWG") && row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]")).isDisplayed()) {
                WebElement finishButton = row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]"));
                finishButton.click();
                break;
            }
        }


        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();


        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Finalizar')]"));
        submitButton.click();
        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/end"));

        PO_LoginView.logOut(driver);
    }
    @Test
    @Order(36)
    // Finalizar trayecto odometro negativo
    public void PR035() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().contains("9202VWG") && row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]")).isDisplayed()) {
                WebElement finishButton = row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]"));
                finishButton.click();
                break;
            }
        }


        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("-5");

        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(),'Finalizar')]"));
        submitButton.click();
        WebElement errorMessage = driver.findElement(By.className("text-danger"));
        String checkText = PO_HomeView.getP().getString("Error.odometer.negativo", PO_Properties.getSPANISH());
        assertTrue(errorMessage.getText().contains(checkText));
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(37)
    // No hay en curso
    public void PR036() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q","Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));

        for (WebElement row : rows) {
            assertFalse(row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]")).isDisplayed());
        }
        driver.navigate().to("http://localhost:8090/journey/end");

        new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("/journey/list"));
        //nos devuelve a list si no existe ninguno en curso
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/journey/list"));
        PO_LoginView.logOut(driver);
    }

    @Test
    public void PR037() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver, "text","Gestión de trayectos","text","Historial de trayectos de un vehículo");

        WebElement element= driver.findElement(By.id("plateNumber"));
        Select select = new Select(element);
        select.selectByVisibleText("5678DFG");

        List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"journeysTable\"]/tbody/tr"));
        assertEquals( 1, employeeRows.size());


        element= driver.findElement(By.id("plateNumber"));
        select = new Select(element);
        select.selectByVisibleText("3141MNP");
        employeeRows = driver.findElements(By.xpath("//*[@id=\"journeysTable\"]/tbody/tr"));
        assertEquals( 1, employeeRows.size());
    }

    @Test
    public void PR039() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q", "Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");

        int numVehicles = 13; // Número de vehiculos disponibles

        int totalCount = 0;
        boolean next = true;
        while (next) {
            List<WebElement> vehicleRows = driver.findElements(By.xpath("//*[@id=\"vehicleTable\"]/tbody/tr"));
            totalCount += vehicleRows.size();
            next = PO_PrivateView.goToNextPage(driver);
        }

        Assertions.assertEquals(numVehicles, totalCount, "El número de empleados no coincide");
    }


    @Test
    public void PR040(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q","Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Cambiar contrasena");

        PO_PrivateView.fillFormChangePassword(driver, "Us3r@2-PASSW", "Ahorasoyadministrad0r?", "Ahorasoyadministrad0r?");

        PO_LoginView.logOut(driver);

        PO_LoginView.fillForm(driver, "10000002Q","Ahorasoyadministrad0r?");

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/journey/list"));
    }

    @Test
    public void PR041() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q", "Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Cambiar contrasena");

        PO_PrivateView.fillFormChangePassword(driver, "Ahorasoyadministrad0r", "Ahorasoyadministrad1r?", "Ahorasoyadministrad1r?");

        String checkText = PO_HomeView.getP().getString("Error.password.incorrect", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
    }

    @Test
    public void PR042() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q", "Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Cambiar contrasena");

        PO_PrivateView.fillFormChangePassword(driver, "Us3r@2-PASSW", "contraseñadebil", "contraseñadebil");

        String checkText = PO_HomeView.getP().getString("Error.password.weak", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
    }

    @Test
    public void PR043() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000002Q", "Us3r@2-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de empleados","text","Cambiar contrasena");

        PO_PrivateView.fillFormChangePassword(driver, "Us3r@2-PASSW", "Ahorasoyadministrad1r?", "Ahorasoyadministrad1r");

        String checkText = PO_HomeView.getP().getString("Error.password.passwordConfirm", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
    }


    @Test
    @Order(39)
    // Registrar un repostaje realizado en el vehículo que el usuario tiene asignado
    // Repostaje válido
    public void PR038() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000010R","Us3r@10-PASSW");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", 1.2, 50.0, true, 100000, "Repostaje de prueba");
        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", 1.2, 50.0, true, 100100, "Repostaje de prueba2");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("B3545CA");

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='refuelsTable']/tbody/tr"));
        int repsolCount = 0;
        for (WebElement row : rows) {
            if (row.getText().contains("Repsol")) {
                repsolCount++;
                if(repsolCount == 2)
                    break;
            }
        }
        Assertions.assertEquals(repsolCount, 2,"Los repostajes asignados no figuran en la lista.");
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(43) //Español ingles español en 3 paginas
    public void PR044() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_NavView.changeLanguage(driver, "Spanish");
        String checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "Spanish");
        checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getSPANISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_PrivateView.goThroughNav(driver,"text","Gestión de vehículos","text","Ver vehículos");


        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getSPANISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "Spanish");
        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getSPANISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");

        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getSPANISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "Spanish");
        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getSPANISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
        PO_LoginView.logOut(driver);

    }

    @Test
    @Order(46) //ingles aleman ingles
    public void PR045() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_NavView.changeLanguage(driver, "English");
        String checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getENGLISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "German");
        checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getGERMAN());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("employees.title", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_PrivateView.goThroughNav(driver,"text","Vehicle management","text","View vehicles");


        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "German");
        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getGERMAN());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("vehicles.message.extra", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_PrivateView.goThroughNav(driver,"text","Journey management","text","View journeys");

        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "German");
        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getGERMAN());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_NavView.changeLanguage(driver, "English");
        checkText = PO_HomeView.getP().getString("journey.list.title", PO_Properties.getENGLISH());
        result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());
        PO_NavView.changeLanguage(driver, "Spanish");
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(47)
    // Intentar acceder sin estar autenticado a la opción de listado de empleados. Debe devolver al login.
    public void PR046() {
        driver.get("http://localhost:8090/employee/list");
        new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("/login"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/login"));
    }

    @Test
    @Order(48)
    // Intentar acceder sin estar autenticado a la opción de listado de vehículos. Debe devolver al login.
    public void PR047() {
        driver.get("http://localhost:8090/vehicle/list");
        new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("/login"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/login"));
    }

    @Test
    @Order(49)
    // Estando autenticado, intentar acceder a opción de listado de logs.
    public void PR048() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "10000001S", "Us3r@1-PASSW");

        driver.get("http://localhost:8090/logs/list");

        String checkText = PO_HomeView.getP().getString("Error.forbidden", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        assertFalse(result.isEmpty());

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(50)
    public void PR049() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); // LOGIN-EX
        PO_LoginView.logOut(driver);                                                // LOGOUT
        PO_LoginView.fillForm(driver, "12345678Z", "admin");         // LOGIN_ERR
        PO_LoginView.fillForm(driver, "12345678Z", "admin");         // LOGIN_ERR
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); // LOGING-EX
        PO_LoginView.logOut(driver);                                                // LOUGOUT
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); // LOGING-EX

        driver.get("http://localhost:8090/logs/list");

        WebElement typeDrodown = driver.findElement(By.id("logTypeFilter"));
        Select select = new Select(typeDrodown);


        select.selectByVisibleText("PET");
        List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertFalse(employeeRows.isEmpty());

        typeDrodown = driver.findElement(By.id("logTypeFilter"));
        select = new Select(typeDrodown);
        select.selectByVisibleText("LOGIN-EX");
        employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertEquals(3, employeeRows.size());


        typeDrodown = driver.findElement(By.id("logTypeFilter"));
        select = new Select(typeDrodown);
        select.selectByVisibleText("LOGIN-ERR");
        employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertEquals(2, employeeRows.size());


        typeDrodown = driver.findElement(By.id("logTypeFilter"));
        select = new Select(typeDrodown);
        select.selectByVisibleText("LOGOUT");
        employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertEquals(2, employeeRows.size());
    }

    @Test
    @Order(51)
    public void PR050() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "admin");         // LOGIN_ERR
        PO_LoginView.fillForm(driver, "12345678Z", "admin");         // LOGIN_ERR

        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); // LOGIN-EX

        driver.get("http://localhost:8090/logs/list");

        WebElement typeDrodown = driver.findElement(By.id("logTypeFilter"));
        Select select = new Select(typeDrodown);

        select.selectByVisibleText("LOGIN-ERR");
        List<WebElement> employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertEquals(2, employeeRows.size());

        List<WebElement> deleteButton = driver.findElements(By.xpath("//*[@id=\"deleteLogsBtn\"]"));
        deleteButton.get(0).click();
        employeeRows = driver.findElements(By.xpath("//*[@id=\"logsTable\"]/tbody/tr"));
        assertTrue(employeeRows.isEmpty());

    }

    @Test
    @Order(60)
    public void PR057() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Historial de trayectos de un vehículo");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("3141MNP");
        WebElement boton = driver.findElement(By.cssSelector("a[href*='/journey/edit/']"));


        boton.click();

        WebElement startDateField = driver.findElement(By.id("startDate"));
        startDateField.clear();
        startDateField.sendKeys("2025-03-20T08:00");

        WebElement endDateField = driver.findElement(By.id("endDate"));
        endDateField.clear();
        endDateField.sendKeys("2025-03-20T18:00");

        WebElement odometerStartField = driver.findElement(By.id("odometerStart"));
        odometerStartField.clear();
        odometerStartField.sendKeys("500000");

        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("500001");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        List<WebElement> km = driver.findElements(By.xpath("//*[@id=\"journeysTable\"]/tbody/tr/td[4]"));
        assertFalse(km.isEmpty());
        assertEquals("1.0", km.get(0).getText());
        List<WebElement> nombre = driver.findElements(By.xpath("//*[@id=\"journeysTable\"]/tbody/tr/td[2]"));
        assertFalse(nombre.isEmpty());
        assertEquals("Pedro", nombre.get(0).getText());
        PO_LoginView.logOut(driver);
    }


    @Test
    @Order(61)
    public void PR058() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Historial de trayectos de un vehículo");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("3141MNP");
        WebElement boton = driver.findElement(By.cssSelector("a[href*='/journey/edit/']"));


        boton.click();

        WebElement startDateField = driver.findElement(By.id("startDate"));
        startDateField.clear();
        startDateField.sendKeys("2025-05-20T08:00");

        WebElement endDateField = driver.findElement(By.id("endDate"));
        endDateField.clear();
        endDateField.sendKeys("2025-03-20T18:00");

        WebElement odometerStartField = driver.findElement(By.id("odometerStart"));
        odometerStartField.clear();
        odometerStartField.sendKeys("500000");

        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("500001");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.dateAfter",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.dateAfter",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(62)
    public void PR059() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Historial de trayectos de un vehículo");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("3141MNP");
        WebElement boton = driver.findElement(By.cssSelector("a[href*='/journey/edit/']"));


        boton.click();

        WebElement startDateField = driver.findElement(By.id("startDate"));
        startDateField.clear();
        startDateField.sendKeys("2025-02-20T08:00");

        WebElement endDateField = driver.findElement(By.id("endDate"));
        endDateField.clear();
        endDateField.sendKeys("2025-03-20T18:00");

        WebElement odometerStartField = driver.findElement(By.id("odometerStart"));
        odometerStartField.clear();
        odometerStartField.sendKeys("500002");

        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("500001");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.odometer.menor",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.odometer.menor",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(63)
    public void PR060() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Historial de trayectos de un vehículo");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("3141MNP");
        WebElement boton = driver.findElement(By.cssSelector("a[href*='/journey/edit/']"));


        boton.click();

        WebElement startDateField = driver.findElement(By.id("startDate"));
        startDateField.clear();
        startDateField.sendKeys("2025-02-20T08:00");

        WebElement endDateField = driver.findElement(By.id("endDate"));
        endDateField.clear();
        endDateField.sendKeys("2025-03-20T18:00");

        WebElement odometerStartField = driver.findElement(By.id("odometerStart"));
        odometerStartField.clear();
        odometerStartField.sendKeys("-57");

        WebElement odometerEndField = driver.findElement(By.id("odometerEnd"));
        odometerEndField.clear();
        odometerEndField.sendKeys("-20");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.odometer.negativo",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.odometer.negativo",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_LoginView.logOut(driver);
    }


}

