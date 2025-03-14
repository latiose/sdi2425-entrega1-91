package com.uniovi.gestor;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCL", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "DIESEL");
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

        PO_PrivateView.fillFormAddVehicle(driver, "", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "Toyota", "", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "123", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCF", "ASDFGHJKLQWERTYUII", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCF", "ASDFGHJKLQWERTYU", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BCD", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "DIESEL");

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

        PO_PrivateView.fillFormAddVehicle(driver, "1234BGD", "ASDFGHJKLQWERTYUI", "Toyota", "Corolla", "DIESEL");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "Error.vin.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.vin.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    @Test
    @Order(21)
    // Listado de vehículos de la empresa
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
        List<String> matriculasEsperadas = List.of("9101GHJ", "5678DFG", "1234BCD");
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
        //FALTA
        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(27)
    //Añadir ya tiene trayecto en curso
    public void PR026() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); //tiene uno en curso por defecto
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("B3545CD");

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
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r"); //tiene uno en curso por defecto
        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Agregar trayecto");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("9101GHJ");

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
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de repostajes","text","Agregar repostaje");
        PO_ListView.fillFormAddRefuel(driver, "Repsol", 1.2, 50.0, true, 100000, "Repostaje de prueba");

        WebElement dropdown = driver.findElement(By.id("plateNumber"));
        Select select = new Select(dropdown);
        select.selectByValue("9101GHJ");

        List<WebElement> result = PO_PrivateView.checkElementByKey(driver, "vehicle.selection.refuel",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("vehicle.selection.refuel",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='refuelsTable']/tbody/tr"));
        boolean isPresent = false;
        for (WebElement row : rows) {
            if (row.getText().contains("Repsol")) {
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
        PO_LoginView.fillForm(driver, "12345678Z","@Dm1n1str@D0r");

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
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de trayectos","text","Ver trayectos");
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='journeyTable']/tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().contains("9101GHJ") && row.findElement(By.xpath(".//td/a[contains(text(),'Finalizar')]")).isDisplayed()) {
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
    //comprobar que se rededirija
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

}

