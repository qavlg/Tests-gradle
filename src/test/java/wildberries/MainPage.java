package wildberries;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class MainPage {

    private WebDriver driver;
    private final By searchField = By.xpath("//input[@id='searchInput']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void findResult (String input) {
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(input);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
    }




}
