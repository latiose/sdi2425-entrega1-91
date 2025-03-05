package com.uniovi.gestor.pageobjects;

import com.uniovi.gestor.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {
    static public void fillFormAddMark(WebDriver driver, int userOrder, String descriptionp, String scorep)
    {
        //Esperamos 5 segundo a que carge el DOM porque en algunos equipos falla
        SeleniumUtils.waitSeconds(driver, 5);
        //Seleccionamos el alumnos userOrder
        new Select(driver.findElement(By.id("user"))).selectByIndex(userOrder);
        //Rellenemos el campo de descripción
        WebElement description = driver.findElement(By.name("description"));
        description.clear();
        description.sendKeys(descriptionp);
        WebElement score = driver.findElement(By.name("score"));
        score.click();
        score.clear();
        score.sendKeys(scorep);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

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