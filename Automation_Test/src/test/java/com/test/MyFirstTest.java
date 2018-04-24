package com.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: katrinaalcala
 * Date: 4/19/18
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyFirstTest {

    @Test
    public void StartWebDriver() throws InterruptedException, FileNotFoundException {

        /**

         Open browser and navigate to https://mailchimp.com/

         */

        System.setProperty("webdriver.chrome.driver", "/Volumes/Patriot SSD/Applications/chromedriver_mac64/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://mailchimp.com/");
        Thread.sleep(15000);

        /**

         Navigate to About Page

         */

        final String ABOUT_MAILCHIMP_XPATH = "//*[@class='site-footer']//a[contains(text(), 'About MailChimp')]";

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='site-footer']"))
        ));

        try {
            driver.findElement(By.xpath("//*[contains(text(), 'Accept Cookies')]")).click();
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("Cookies enabled.");
        }

        driver.findElement(By.xpath(ABOUT_MAILCHIMP_XPATH)).click();
        Thread.sleep(5000);

        final String LEADERSHIP_XPATH = "//*[@id='page-about']//div[contains(@class, 'bio_wrapper')]";
        List<WebElement> leadershipTeam = driver.findElements(By.xpath(LEADERSHIP_XPATH + "/div"));

        /**

         Create csv file to write information on

         */

        PrintWriter printwriter = new PrintWriter(new File("MailChimpData.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("Name");
        sb.append(',');
        sb.append("Position");
        sb.append(',');
        sb.append("Bio");
        sb.append('\n');


        /**

         Scrape data and write data onto csv file

         */

        for (int i = 0; i<leadershipTeam.size(); i++) {

            String name =  driver.findElement(By.xpath(LEADERSHIP_XPATH + "/div" + "[" + (i+1) + "]/a[@class='bio_link']//*[@class='mb1']")).getText();
            String position = driver.findElement(By.xpath(LEADERSHIP_XPATH + "/div" + "[" + (i+1) + "]/a[@class='bio_link']/span")).getText();

            driver.findElement(By.xpath(LEADERSHIP_XPATH + "/div" + "[" + (i+1) + "]//img")).click();
            Thread.sleep(2000);

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='bio_view']//*[contains(@class, 'close_btn')]")))
            ));

            String bio = driver.findElement(By.xpath("//*[@id='bio_view']//p")).getText();
            //driver.findElement(By.xpath("//*[contains(@class, 'close_btn')]")).click();
            driver.navigate().refresh();
            Thread.sleep(5000);

            sb.append(name);
            sb.append(',');
            sb.append(position);
            sb.append(',');
            sb.append(bio);
            sb.append('\n');

        }

        printwriter.write(sb.toString());
        printwriter.close();

        driver.close();
    }
}
