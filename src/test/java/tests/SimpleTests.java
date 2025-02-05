package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SimpleTests {
    @BeforeEach
    public void before() {
    }

    @Test
    public void testTwoLessThanThree() {
        int a = 2;
        int b = 3;
        Assertions.assertTrue(a < b, "Число " + a + " больше чем число " + b);
    }

    @Test
    @DisplayName("Сумма")
    @Disabled("Тест не критичный. Исправим позже")
    public void sumTest() {
        int a = 3;
        int b = 3;
        int sum = a + b;
        Assertions.assertEquals(6, sum);
    }

    @ParameterizedTest
    @MethodSource
    public void parameterizedTest(String name, String age, String sex) {
        System.out.println(name + " " + age + " " + sex);
        Assertions.assertTrue(name.contains("s"));
    }
}