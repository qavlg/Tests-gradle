package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class iFrameDebugger {
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
    public void iFrame(){
        driver.get("https://mail.ru/");
        driver.findElement(By.xpath("//button[@class='resplash-btn resplash-btn_primary knndmfb__1ebh38x']")).click();
        WebElement iFrame = driver.findElement(By.xpath("//iframe[@class='ag-popup__frame__layout__iframe']"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("aleksei@mail.ru");
    }



//      Тест для выпадающего списка, по которому невозможно кликнуть
//      Использовать JS-код в консоле браузера
//      setTimeout(function() {
//          debugger;
//      }, 3000);

    @Test
    public void SelectDropDownList() {
        driver.get("http://85.192.34.140:8081");
        driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']")).click();
        driver.findElement(By.xpath("//span[text()='Select Menu']")).click();
        driver.findElement(By.id("selectOne")).click();
        driver.findElement(By.xpath("//div[text()='Pick one title']//following::div[text()='Mrs.']")).click();
        String selectedText = driver.findElement(By.xpath("//div[text()='Mrs.']")).getText();
        Assertions.assertEquals("Mrs.", selectedText);
    }
}