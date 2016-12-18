package ua.qatestlab;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String fs = System.getProperty("file.separator");
        String geckoDriverPath = System.getProperty("user.dir") +
                fs + "drivers" + fs + "geckodriver.exe";
        String testWord = "automatio";
        String wordEnding ="n";

        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        WebDriver firefoxDriver = new FirefoxDriver();
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(firefoxDriver, 15);

        firefoxDriver.navigate().to("https://www.bing.com");
        WebElement e = firefoxDriver.findElement(By.xpath("//li[@id='scpt1']//a"));
        e.click();

        wait.until( ExpectedConditions.textToBe(By.tagName("title"), "Лента изображений Bing"));
        int before;
        int after = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img"))).size();
        JavascriptExecutor jSExecutor = (JavascriptExecutor) firefoxDriver;
        for (int i = 0; i < 2; i++) {
            before = after ;
            jSExecutor.executeScript("scrollBy(0, document.body.scrollHeight/2)", "");
            Thread.sleep(3000);
            after = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img"))).size();
            if(after <= before) throw new RuntimeException("no new images load during scrolling");
        }
        jSExecutor.executeScript("scrollTo(0, 0)", "");


        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//header//*")));
        firefoxDriver.findElement(By.xpath("//input[@id='sb_form_q']")).sendKeys(testWord);

        e = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[text()='" + wordEnding + "']/..")));
        System.out.println(e.getText());
        e.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ftrD']//a")));
        e = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@title='Фильтр: Дата']")));
        e.click();
        e = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='В прошлом месяце']")));
        e.click();

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='dg_u']"))).get(0).click();

        firefoxDriver.quit();
    }
}
