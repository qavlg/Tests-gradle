package wildberries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class IthemTest extends BaseTest {

  @Test
    public void ithemTest(){
      String text = "iPhone";
      Integer minPrice = 30000;
      Integer maxPrice = 60000;
      Integer index = 0;

      IthemPage ip = new IthemPage(driver);
      MainPage mp = new MainPage(driver);
      mp.findResult(text);
      ip.selectAllFilters();
      ip.selectStartPrice(minPrice);
      ip.selectEndPrice(maxPrice);
      ip.clickSubBtn();
      ip.getIthem(index);

      String actualIthemTitle = ip.getTitle();
      Integer actualIthemPrice = ip.getPrice();

    Assertions.assertEquals(actualIthemTitle.toLowerCase(),text.toLowerCase(), "error");
    Assertions.assertTrue(actualIthemPrice >= minPrice && actualIthemPrice <= maxPrice);

  }



}
