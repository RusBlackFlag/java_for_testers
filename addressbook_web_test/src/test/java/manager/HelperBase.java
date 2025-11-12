package manager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.nio.file.Paths;

public class HelperBase {

    protected final ApplicationManager manager;

    public HelperBase(ApplicationManager manager) {
        this.manager = manager;
    }

    protected void click(By locator) {
        manager.driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = manager.driver.findElement(locator);
        element.click();
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

//    protected void type(By locator, String text) {
//        click(locator);
//        manager.driver.findElement(locator).clear();
//        manager.driver.findElement(locator).sendKeys(text);
//    }


    protected void attach(By locator, String file) {
        manager.driver.findElement(locator).sendKeys(Paths.get(file).toAbsolutePath().toString());
    }

    protected boolean isElementPresent(By locator) {
        try {
            manager.driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
