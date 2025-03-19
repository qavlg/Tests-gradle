package wildberries;

import org.junit.jupiter.api.Test;

public class MainTest extends BaseTest {

    @Test
            public void searchTest (){
        String input = "car";

        MainPage mp = new MainPage(driver);
        mp.findResult(input);
    }



}
