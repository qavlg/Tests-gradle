package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

public class ActionsTest {
    private WebDriver driver;

    @BeforeAll
    public static void downloadDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void sliderTest(){

        driver.get("http://85.192.34.140:8081/");
        WebElement elements = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elements.click();

        WebElement upload = driver.findElement(By.xpath("//span[text()='Widgets']"));
        upload.click();

        driver.findElement(By.xpath("//span[text()='Slider']")).click();

        WebElement slider = driver.findElement(By.xpath("//input[@class='range-slider range-slider--primary']"));
        WebElement expectedResult = driver.findElement(By.xpath("//input[@id='sliderValue']"));

//        Actions actions = new Actions(driver);
//        actions.dragAndDropBy(slider,50, 0);

    }
}
