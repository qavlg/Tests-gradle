package wildberries;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IthemPage {
    private WebDriver driver;
    private By allFilters = By.xpath("//button[@class='dropdown-filter__btn dropdown-filter__btn--all']");
    private By startPrice = By.xpath("//input[@name='startN']");
    private By endPrice = By.xpath("//input[@name='endN']");
    private By applyBtn = By.xpath("//button[@class='filters-desktop__btn-main btn-main']");
    private By ithems = By.xpath("//div[@class='product-card-list']//article");

    private By titleOfIthem = By.xpath("//h1[@class='product-page__title']");

    private By ithemPrice = By.xpath("//span[@class='price-block__price']");


    public IthemPage(WebDriver driver){
        this.driver = driver;
    }


    public void selectAllFilters(){
        driver.findElement(allFilters).click();
    }

    public String getTitle(){
        return driver.findElement(titleOfIthem).getText();
    }

    public Integer getPrice(){
        String price = driver.findElement(ithemPrice).getText();
        price = price.replaceAll("[^0-9]","");
                return Integer.parseInt(price);
    }

    public void getIthem(Integer num){
    driver.findElements(ithems).get(num).click();
    }

    public void clickSubBtn(){
        driver.findElement(applyBtn).click();
    }

    public void selectStartPrice(Integer min){
        driver.findElement(startPrice).click();
        driver.findElement(startPrice).clear();
        driver.findElement(startPrice).sendKeys(String.valueOf(min));
    }

    public void selectEndPrice(Integer max){
        driver.findElement(endPrice).click();
        driver.findElement(endPrice).clear();
        driver.findElement(endPrice).sendKeys(String.valueOf(max));
    }




}
