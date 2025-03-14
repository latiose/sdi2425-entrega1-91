package com.uniovi.gestor.pageobjects;

import com.uniovi.gestor.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    static public void fillFormAddEmployee(WebDriver driver, String dnip, String namep, String lastNamep)
    {
        WebElement dniInput = driver.findElement(By.id("dni"));
        dniInput.clear();
        dniInput.sendKeys(dnip);
        WebElement nameInput = driver.findElement(By.id("name"));
        nameInput.clear();
        nameInput.sendKeys(namep);
        WebElement lastNameInput = driver.findElement(By.id("lastName"));
        lastNameInput.clear();
        lastNameInput.sendKeys(lastNamep);
        driver.findElement(By.className("btn-primary")).click();
    }

    static public void fillFormAddVehicle(WebDriver driver, String numberPlatep, String vinp, String brandp,
                                          String modelp, String fuelp)
    {
        WebElement numberPlate = driver.findElement(By.id("numberPlate"));
        numberPlate.clear();
        numberPlate.sendKeys(numberPlatep);
        WebElement vin = driver.findElement(By.id("vin"));
        vin.clear();
        vin.sendKeys(vinp);
        WebElement brand = driver.findElement(By.id("brand"));
        brand.clear();
        brand.sendKeys(brandp);
        WebElement model = driver.findElement(By.id("model"));
        model.clear();
        model.sendKeys(modelp);
        WebElement fuelDropdown = driver.findElement(By.id("fuel"));
        Select fuelSelect = new Select(fuelDropdown);
        fuelSelect.selectByVisibleText(fuelp);
        driver.findElement(By.className("btn-primary")).click();
    }


    static public void goThroughNav(WebDriver driver,String type1,String text1,String type2,String text2){
        List<WebElement> elements = PO_View.checkElementBy(driver, type1,
                text1);
        elements.get(0).click();
        elements = PO_View.checkElementBy(driver, type2, text2);
        elements.get(0).click();
    }

    static public void checkAndClick(WebDriver driver, String type, String text,int index){
        List<WebElement> elements =SeleniumUtils.waitLoadElementsBy(driver, type, text, getTimeout());
        elements.get(index).click();
    }



}