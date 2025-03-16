package com.uniovi.gestor.util;


import java.util.List;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {



	/**
	 * Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver. Para ello se empleará una consulta xpath.
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param xpath: consulta xpath.
	 * @param timeout: el tiempo máximo que se esperará por la aparición del elemento a buscar con xpath
	 * @return  Se retornará la lista de elementos resultantes de la búsqueda con xpath.
	 */
	static public List<WebElement> waitLoadElementsByXpath(WebDriver driver, String xpath, int timeout)
	{
		WebElement result =
				new WebDriverWait(driver, timeout)
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		Assertions.assertNotNull(result);
		return driver.findElements(By.xpath(xpath));


	}

	/**
	 * Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver. Para ello se empleará una consulta xpath 
	 * según varios criterios..
	 * 
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param criterio: "id" or "class" or "text" or "@attribute" or "free". Si el valor de criterio es free es una expresion xpath completa. 
	 * @param text: texto correspondiente al criterio.
	 * @param timeout: el tiempo máximo que se esperará por la apareción del elemento a buscar con criterio/text.
	 * @return Se retornará la lista de elementos resultantes de la búsqueda.
	 */
	static public List<WebElement> waitLoadElementsBy(WebDriver driver, String criterio, String text, int timeout)
	{
		String searchCriterio = switch (criterio) {
            case "id" -> "//*[contains(@id,'" + text + "')]";
            case "class" -> "//*[contains(@class,'" + text + "')]";
            case "text" -> "//*[contains(text(),'" + text + "')]";
            case "free" -> text;
            default -> "//*[contains(" + criterio + ",'" + text + "')]";
        };

        return waitLoadElementsByXpath(driver, searchCriterio, timeout);
	}


}
