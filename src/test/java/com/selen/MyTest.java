package com.selen;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class MyTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }

    //1,2,3
    @Test
    public void testHomePage() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA");

        final WebElement linksSiteContainer = driver.findElement(id("links-site"));
        wait.until(visibilityOf(linksSiteContainer.findElement(linkText("Home"))));
        wait.until(visibilityOf(linksSiteContainer.findElement(linkText("Subjects"))));
        wait.until(visibilityOf(linksSiteContainer.findElement(linkText("About Wiley"))));
        wait.until(visibilityOf(linksSiteContainer.findElement(linkText("Contact Us"))));
        wait.until(visibilityOf(linksSiteContainer.findElement(linkText("Help"))));

        final WebElement homepageLinksContainer = driver.findElement(id("homepage-links"));
        assertThat(homepageLinksContainer.findElements(tagName("a")).size(), is(9));
        final WebElement studentsLink = homepageLinksContainer.findElement(linkText("Students"));
        wait.until(visibilityOf(studentsLink));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Authors"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Instructors"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Librarians"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Societies"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Conferences"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Booksellers"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Corporations"))));
        wait.until(visibilityOf(homepageLinksContainer.findElement(linkText("Institutions"))));

        studentsLink.click();
        wait.until(urlToBe("http://eu.wiley.com/WileyCDA/Section/id-404702.html"));
        wait.until(titleContains("Students"));
    }

    //4,5
    //Cannot check if element is clickable :( because of element is always clickable (visible)
    @Test
    public void testStudents() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA/Section/id-404702.html");

        final WebElement sideBar = driver.findElement(id("sidebar"));

        wait.until(visibilityOf(sideBar.findElement(linkText("Authors"))));
        wait.until(visibilityOf(sideBar.findElement(linkText("Librarians"))));
        wait.until(visibilityOf(sideBar.findElement(linkText("Booksellers"))));
        wait.until(visibilityOf(sideBar.findElement(linkText("Instructors"))));
        final WebElement studentsMenuItem = sideBar.findElement(cssSelector(".active.autonavItem > span"));
        assertThat(studentsMenuItem.getText(), is("Students"));
        wait.until(visibilityOf(sideBar.findElement(linkText("Societies"))));
        wait.until(visibilityOf(sideBar.findElement(linkText("Corporate Partners"))));
        wait.until(visibilityOf(sideBar.findElement(linkText("Government Employees"))));
        assertThat(sideBar.findElements(cssSelector(".autonavLevel1 > .autonavItem")).size(), is(8));
    }

    //6,7
    @Test
    public void testSignUpPresence() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA/Section/id-404702.html");

        final WebElement linksSiteContainer = driver.findElement(id("links-site"));
        final WebElement homeLink = linksSiteContainer.findElement(linkText("Home"));
        homeLink.click();

        final WebElement submitButton = driver.findElement(id("id31"));
        wait.until(visibilityOf(submitButton));
        submitButton.click();

        final Alert alert = driver.switchTo().alert();
        assertThat(alert.getText(), is("Please enter email address"));
        alert.accept();
    }

    //8
    @Test
    public void testSingUpInvalidEmail() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA");

        final WebElement emailAddressInput = driver.findElement(id("EmailAddress"));
        wait.until(visibilityOf(emailAddressInput));
        emailAddressInput.sendKeys("invalidEmail.address");

        final WebElement submitButton = driver.findElement(id("id31"));
        wait.until(visibilityOf(submitButton));
        submitButton.click();

        final Alert alert = driver.switchTo().alert();
        assertThat(alert.getText(), is("Invalid email address."));
        alert.accept();
    }

    //9
    @Test
    public void testSearchPresence() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA");

        final WebElement queryInput = driver.findElement(id("query"));
        wait.until(visibilityOf(queryInput));

        queryInput.sendKeys("for dummies");

        final WebElement submitButton = driver.findElement(className("search-form-submit"));
        wait.until(visibilityOf(submitButton));
        submitButton.click();

        final WebElement searchResultsContainer = driver.findElement(xpath("//*[@id='search-results']"));
        wait.until(visibilityOf(searchResultsContainer));

        assertTrue(searchResultsContainer.findElements(className("product-listing")).size() > 0);
    }

    //10,11,12
    @Test
    public void testDummies() throws Exception {
        driver.navigate().to("http://eu.wiley.com/WileyCDA/Section/id-WILEYEUROPE2_SEARCH_RESULT.html?query=for%20dummies");

        final WebElement searchResultsContainer = driver.findElement(id("search-results"));
        wait.until(visibilityOf(searchResultsContainer));

        final List<WebElement> booksLinks = searchResultsContainer.findElements(cssSelector(".product-title > a"));
        final WebElement firstBook = booksLinks.get(0);
        firstBook.click();

        wait.until(titleContains(firstBook.getText()));

        final WebElement homeLink = driver.findElement(xpath("//a[contains(text(),'Home')]"));
        wait.until(elementToBeClickable(homeLink));
        homeLink.click();

        waitFor(5000);

        final WebElement institutions = driver.findElement(xpath("//a[contains(text(),'Institutions')]"));
        wait.until(elementToBeClickable(institutions));
        institutions.click();

        waitFor(5000);

        assertTrue(switchToWindowWithURL("https://edservices.wiley.com/", driver));
    }

    private boolean switchToWindowWithURL(final String url, final WebDriver driver) {
        final String currentWindowHandle = driver.getWindowHandle();
        for (final String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getCurrentUrl().equals(url)) {
                driver.switchTo().window(windowHandle);
                return true;
            }
        }

        driver.switchTo().window(currentWindowHandle);
        return false;
    }

    private void waitFor(final Integer timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}