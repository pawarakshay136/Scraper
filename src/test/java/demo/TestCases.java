package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.wrappers.Wrappers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;
import java.io.File;

public class TestCases {

    ChromeDriver driver;

     // Method to start the browser before tests
     @BeforeTest
     public void startBrowser() {
         System.setProperty("java.util.logging.config.file", "logging.properties");
         ChromeOptions options = new ChromeOptions();
         options.addArguments("--remote-allow-origins=*");
 
         driver = new ChromeDriver(options);
         driver.manage().window().maximize();
     }

    // Test case to scrape hockey teams data and store in JSON
    @Test
    public void testCase01() throws InterruptedException, IOException {
        ObjectMapper mapper = new ObjectMapper(); // Jackson ObjectMapper for JSON conversion
        driver.get("https://www.scrapethissite.com/pages/");
        HashMap<Integer, Object> map = new HashMap<>();
        
        // Clicking on the "Hockey Teams" page
        Wrappers.clickHockeyTeams(driver);

        int k = 1, n = 0;

        // Loop to iterate through multiple pages (4 pages as per the requirement)
        while (n < 4) {
            int rows = driver.findElements(By.xpath("//table/tbody/tr")).size();
            
            // Iterating through the rows of the table
            for (int i = 2; i < rows; i++) {
                String winPercentage = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[6]")).getText();
                float winPercent = Float.parseFloat(winPercentage);
                
                // Only consider teams with win percentage less than 40%
                if (winPercent < 0.4) {
                    ArrayList<String> list = new ArrayList<>();
                    String team = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[1]")).getText();
                    String year = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[2]")).getText();

                    // Adding epoch time, team name, year, and win percentage to the list
                    list.add(Long.toString(Wrappers.getEpochTime()));
                    list.add(team);
                    list.add(year);
                    list.add(winPercentage);
                    
                    // Storing in the map with key as an incremented integer
                    map.put(k, list);
                    k++;
                }
            }

            // Click "Next" to move to the next page
            Wrappers.clickNext(driver);
            n++;
        }

        // Print out the collected data to console
        for (Integer i : map.keySet()) {
            System.out.println(map.get(i));
        }

        // Create the JSON file
        Wrappers.createJsonFile(mapper, map);
        String userDir = System.getProperty("user.dir");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userDir + "\\src\\test\\resources\\hockey-team-data.json"), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test case to scrape Oscar-winning films data and store in JSON
    @Test
    public void testCase02() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int q = 1;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.scrapethissite.com/pages/");
        HashMap<Integer, Object> map = new HashMap<>();

        // Clicking on the "Oscar Winning Films" page
        Wrappers.clickOscarWinning(driver);

        // Iterating through the years of the Oscar winners
        for (WebElement yearElement : Wrappers.getYears(driver)) {
            String year = yearElement.getText();
            yearElement.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Title']")));
            
            // Loop through the top 5 movies for the current year
            for (int j = 1; j <= 5; j++) {
                ArrayList<String> list = new ArrayList<>();
                list.add(Long.toString(Wrappers.getEpochTime()));
                list.add(year);

                // Collecting details for each movie
                for (int k = 1; k <= 4; k++) {
                    if (k < 4) {
                        String movieDetail = driver.findElement(By.xpath("//table/tbody/tr[" + j + "]/td[" + k + "]")).getText();
                        list.add(movieDetail);
                    } else {
                        try {
                            // Check if the movie is the Best Picture winner
                            Boolean isWinner = driver.findElement(By.xpath("//table/tbody/tr[" + j + "]/td[" + k + "]/i")).isDisplayed();
                            list.add(String.valueOf(isWinner));
                        } catch (Exception e) {
                            list.add("false");
                        }
                    }
                }

                // Add the movie data to the map
                map.put(q, list);
                q++;
            }
        }

        // Print out the collected Oscar winner data
        for (Integer i : map.keySet()) {
            System.out.println(map.get(i));
        }

        // Create the JSON file
        Wrappers.createJsonFile(mapper, map);
        String userDir = System.getProperty("user.dir");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userDir + "\\src\\test\\resources\\oscar-winner-data.json"), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    // Method to close the browser after tests
    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();
    }
}
