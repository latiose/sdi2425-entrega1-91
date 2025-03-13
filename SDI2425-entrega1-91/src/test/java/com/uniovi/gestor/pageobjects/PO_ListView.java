package com.uniovi.gestor.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class PO_ListView extends PO_NavView{
    static public boolean goToNextPage(WebDriver driver) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");

        int currentPageIndex = -1;
        for (int i = 0; i < elements.size(); i++) {
            WebElement parent = elements.get(i).findElement(By.xpath("./.."));
            if (parent.getAttribute("class").contains("active")) {
                currentPageIndex = i;
                break;
            }
        }

        if (currentPageIndex != -1 && currentPageIndex + 1 < elements.size()) {
            String nextText = elements.get(currentPageIndex + 1).getText().trim();
            if (nextText.matches("\\d+")) {
                elements.get(currentPageIndex + 1).click();
                return true;
            }
        }
        return false;
    }

    static public void goToLastPage(WebDriver driver) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");

        if (!elements.isEmpty()) {
            elements.get(elements.size() - 1).click();
        }
    }

    static public boolean deleteVehiclesByIndexes(WebDriver driver, int[] indexes) {
        List<WebElement> vehicleRows = driver.findElements(By.xpath("//*[@id=\"vehicleTable\"]/tbody/tr"));
        List<String> numberPlates = new ArrayList<String>();
        for(int index : indexes){
            WebElement vehicleRow = vehicleRows.get(index);
            String vehiclePlate = vehicleRow.findElement(By.xpath(".//td[1]")).getText();
            numberPlates.add(vehiclePlate);
            vehicleRow.findElement(By.xpath(".//td[8]/label/input")).click();
        }

        List<WebElement> deleteButton = driver.findElements(By.xpath("//*[@id=\"deleteButton\"]"));
        deleteButton.get(0).click();
        vehicleRows = driver.findElements(By.xpath("//*[@id=\"vehicleTable\"]/tbody/tr"));
        boolean found = false;
        for (WebElement row : vehicleRows) {
            for(String numberPlate : numberPlates){
                if (row.findElement(By.xpath(".//td[1]")).getText().equals(numberPlate)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
}
