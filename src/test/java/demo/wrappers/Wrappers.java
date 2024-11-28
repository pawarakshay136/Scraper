package demo.wrappers;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;

public class Wrappers {

    // Method to get the current epoch time (in seconds)
    public static long getEpochTime() {
        long epochSeconds = Instant.now().getEpochSecond();
        return epochSeconds;
    }

    // Method to click the "Next" button for pagination
    public static void clickNext(WebDriver driver) throws InterruptedException {
        WebElement ele = driver.findElement(By.xpath("//a[@aria-label='Next']"));
        ele.click();
        Thread.sleep(3000); // wait for the next page to load
    }

    // Method to click on the "Hockey Teams: Forms, Searching and Pagination" link
    public static void clickHockeyTeams(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement link = driver.findElement(By.xpath("//a[text()='Hockey Teams: Forms, Searching and Pagination']"));
        link.click();
        wait.until(ExpectedConditions.urlContains("/pages/forms/"));
    }

    // Method to click on the "Oscar Winning Films" link
    public static void clickOscarWinning(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement link = driver.findElement(By.xpath("//a[text()='Oscar Winning Films: AJAX and Javascript']"));
        link.click();
        wait.until(ExpectedConditions.urlContains("ajax-javascript"));
    }

    // Method to get a list of all year elements on the Oscar Winning Films page
    public static List<WebElement> getYears(WebDriver driver) {
        return driver.findElements(By.xpath("//a[@class='year-link']"));
    }

    // Method to create a JSON file from a HashMap object using Jackson
    public static void createJsonFile(ObjectMapper mapper, HashMap<Integer, Object> map) throws IOException {
        try {
            String Json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            System.out.println(Json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

