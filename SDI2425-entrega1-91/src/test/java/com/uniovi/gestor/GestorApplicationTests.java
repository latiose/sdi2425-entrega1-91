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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        String checkText = "10000001S";
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
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/login"));
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
        Assertions.assertTrue(logoutLink.isEmpty());
    }

    @Test
    @Order(7)
    //Añadir empleado válido
    public void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de usuarios","text","Agregar usuario");
        PO_PrivateView.fillFormAddEmployee(driver, "11111111H", "Pablo", "Perez Alvarez");
        String checkText = "11111111H";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertTrue(result.size() > 0);
        PO_LoginView.logOut(driver);
    }



    @Test
    @Order(8)
    //Empleado sin parmaetros
    public void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de usuarios","text","Agregar usuario");

        driver.findElement(By.className("btn-primary")).click();

        List<WebElement> requiredFieldErrors = driver.findElements(By.cssSelector(":invalid"));
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/employee/add"));

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(9)
    //Empleado con dni inválido
    public void PR09() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de usuarios","text","Agregar usuario");

        PO_PrivateView.fillFormAddEmployee(driver, "123", "Pablo", "Perez Alvarez");

        String checkText = PO_HomeView.getP().getString("Error.signup.dni.invalid", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertTrue(result.size() > 0);

        PO_LoginView.logOut(driver);
    }

    @Test
    @Order(10)
    //Empleado dni repetido
    public void PR010() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

        PO_PrivateView.goThroughNav(driver,"text","Gestión de usuarios","text","Agregar usuario");

        PO_PrivateView.fillFormAddEmployee(driver, "12345678Z", "Pablo", "Perez Alvarez");

        String checkText = PO_HomeView.getP().getString("Error.dni.duplicate", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertTrue(result.size() > 0);

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
        PO_PrivateView.goToLastPage(driver);
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
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/vehicle/add"));
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
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/vehicle/add"));
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
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/vehicle/add"));
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
        Assertions.assertFalse(requiredFieldErrors.isEmpty());
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/vehicle/add"));
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
            next = PO_PrivateView.goToNextPage(driver);
        }

        Assertions.assertEquals(totalCount, numCars, "El número de vehículos no coincide.");
    }

}

