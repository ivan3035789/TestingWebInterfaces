package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class OrderingCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setupBrowserDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void mustSubmitRequest() {
        driver.findElement(By.name("name")).sendKeys("Иванов Иван");
        driver.findElement(By.name("phone")).sendKeys("+79250987654");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.tagName("p")).getText();
        Assertions.assertEquals(expectedText, actualText);

    }
    @Test
    void mustSubmitRequestWithDoubleSurname() {
        driver.findElement(By.name("name")).sendKeys("Иванов-Иванов Иван");
        driver.findElement(By.name("phone")).sendKeys("+79250987654");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = driver.findElement(By.tagName("p")).getText();
        Assertions.assertEquals(expectedText, actualText);

    }

    @Test
    void itShouldDisplayWarningUnderTheFirstAndLastNameLine() {
        driver.findElement(By.name("name")).sendKeys("Ivanov Ivan");
        driver.findElement(By.name("phone")).sendKeys("+79250987654");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id=name] span.input__sub")).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    void itShouldDisplayWarningUnderThePhoneNumberLine() {
        driver.findElement(By.name("name")).sendKeys("Иванов Иван");
        driver.findElement(By.name("phone")).sendKeys("79250987654");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id=phone] span.input__sub")).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    void itShouldHighlightTheWarningLabelInRedIfTheCheckMarkIsNotSet() {
        driver.findElement(By.name("name")).sendKeys("иван иванов");
        driver.findElement(By.name("phone")).sendKeys("+79250987654");
        driver.findElement(By.tagName("button")).click();

        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actualText = driver.findElement(By.className("checkbox__text")).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    void itShouldDisplayWarningMessageIfTheRequiredPhoneNumberFieldIsNotFilledIn() {
        driver.findElement(By.name("name")).sendKeys("Иванов Иван");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id=phone] span.input__sub")).getText();
        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    void itShouldDisplayWarningLabelIfTheRequiredFirstAndLastNameFieldIsNotFilledIn() {
        driver.findElement(By.name("phone")).sendKeys("+79250987654");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id=name] span.input__sub")).getText();
        Assertions.assertEquals(expectedText, actualText);
    }
}
