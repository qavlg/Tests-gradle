package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class UploadDownloadTests {
    private WebDriver driver;
    private String downloadFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "downloadFiles";

    @BeforeAll
    public static void downloadDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder);
        options.setExperimentalOption("prefs", prefs);
        //options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void uploadPic() {
        driver.get("http://85.192.34.140:8081/");
        WebElement elements = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elements.click();

        WebElement upload = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        upload.click();

        WebElement selectFile = driver.findElement(By.id("uploadFile"));
        selectFile.sendKeys(System.getProperty("user.dir") + "/src/test/resources/pic.jpg");

        WebElement fakePath = driver.findElement(By.id("uploadedFilePath"));
        Assertions.assertTrue(fakePath.getText().contains("pic.jpg"));
    }

    @Test
    public void downloadPic() {
        driver.get("http://85.192.34.140:8081/");
        WebElement elements = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elements.click();

        WebElement upload = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        upload.click();

        WebElement download = driver.findElement(By.id("downloadButton"));
        download.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(x -> Paths.get(downloadFolder, "sticker.png").toFile().exists());

        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertTrue(file.length() != 0);
        Assertions.assertNotNull(file);
    }

}