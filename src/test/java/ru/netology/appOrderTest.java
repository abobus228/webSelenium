package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class appOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сафронов Александр");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79506106210");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                result.getText().trim());
    }

    @Test  // Проверка на отправку с пустой формой ФИО
    void shouldReturnThatFieldIsEmpty() {
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Поле обязательно для заполнения",
                result.getText().trim());
    }

    @Test  // Проверка на отправку с неверной формой ФИО
    void shouldReturnThatFieldFIOIsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Safronov Aleksandr");
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector(".input_invalid .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                result.getText().trim());
    }

    @Test  // Проверка на отправку с пустой формой номера
    void shouldReturnThatFieldNumberIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сафронов Александр");
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Поле обязательно для заполнения",
                result.getText().trim());
    }

    @Test  // Проверка на отправку с неверной формой номера
    void shouldReturnThatFieldNumberIsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сафронов Александр");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("321312321");
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector(".input_invalid .input__sub"));
        Assertions.assertTrue(result.isDisplayed());
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                result.getText().trim());
    }
}
