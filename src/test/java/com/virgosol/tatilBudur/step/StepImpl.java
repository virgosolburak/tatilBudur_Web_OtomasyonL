package com.virgosol.tatilBudur.step;

import com.github.javafaker.Faker;
import com.thoughtworks.gauge.Step;
import com.virgosol.tatilBudur.helper.ElementHelper;
import com.virgosol.tatilBudur.helper.StoreHelper;
import com.virgosol.qa.web.core.di.InjectionHelper;
import com.virgosol.qa.web.core.element.Element;
import com.virgosol.qa.web.core.model.Configuration;
import com.virgosol.qa.web.core.wait.WaitingAction;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StepImpl {
    String winHandleBefore;
    private final Logger logger = LoggerFactory.getLogger(StepImpl.class);
    @Inject
    Element element;
    @Inject
    WebDriver driver;
    @Inject
    WaitingAction waitingAction;
    @Inject
    Configuration configuration;

    public StepImpl() {
        InjectionHelper.getInstance().getFeather().injectFields(this);
    }

    // tarayıcıyı aç
    @Step({"<browserType> tarayıcını aç",
            "<browserType> open your browser"})
    public void openBrowser(String browserName) {
        if (browserName.contains("Firefox")) {
            System.setProperty("webdriver.gecko.driver", "drivers/geckodriver");
            WebDriver driver = new FirefoxDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            this.driver = driver;
        } else if (browserName.contains("Microsoft Edge")) {
            if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                throw new WebDriverException("Your OS doesn't support Edge");
            }
            System.setProperty("webdriver.edge.driver", "drivers/msedgedriver");
            WebDriver driver = new EdgeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            this.driver = driver;
        }else if (browserName.contains("IE")) {
            if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                throw new WebDriverException("Your OS doesn't support Internet Explorer");
            }
            System.setProperty("webdriver.edge.driver", "drivers/IEDriverServer.exe");
            WebDriver driver = new InternetExplorerDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            this.driver = driver;
        }
    }
    //url
    @Step({"<url> urle git", "<url> go to url"})
    public void goToUrl(String url) {
        driver.navigate().to(url);
    }

    // Alert accept
    @Step({"Mesajda tamam butonuna basılır.",
            "Alert:Click the ok button in the message."})
    public void popUpAccept() {
        Alert simpleAlert = driver.switchTo().alert();
        simpleAlert.accept();
    }
    public void swipeToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 25; i++) {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            js.executeScript("arguments[0].scrollIntoView({behavior: \"auto\", block: \"center\", inline: \"center\"});", element);
        }
    }
    //  @Step("<key> textbox alanına captcha değeri yazılır.")
    @Step({"<key> textbox alanına captcha değeri yazılır.",
            "The captcha value is written in the <key> textbox field."})
    public void handlingCAPTCHA(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement fileInput = driver.findElement(byElement);
        String captchaVal = JOptionPane.showInputDialog("Please enter the captcha value:");
        fileInput.sendKeys(captchaVal);
    }
    //  @Step("Ekran çözünürlüğünü <screenResolution> yap.")
    @Step({"Ekran çözünürlüğünü <screenResolution> yap.",
            "Set the screen resolution to <screenResolution>."})
    public void changeScreenResolution(String screenResolution) throws IOException {
        if (screenResolution.contains("1920x1080")) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else if (screenResolution.contains("1280x768")) {
            driver.manage().window().setSize(new Dimension(1280, 768));
        } else if (screenResolution.contains("1680x1050")) {
            driver.manage().window().setSize(new Dimension(1680, 1050));
        } else if (screenResolution.contains("1360x768")) {
            driver.manage().window().setSize(new Dimension(1360, 768));
        } else if (screenResolution.contains("1024x768")) {
            driver.manage().window().setSize(new Dimension(1024, 768));
        } else if (screenResolution.contains("800x600")) {
            driver.manage().window().setSize(new Dimension(800, 600));
        }
    }
    //Hepsifly used_public
    @Step({"Click on element with <key>",
            "<key> li elemente tıkla",
            "Click element with key <key>"})
    public void clickElement(String key) throws InterruptedException {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        swipeToElement(driver.findElement(byElement));
      Thread.sleep(1000);
        driver.findElement(byElement).click();

    }


    @Step({"<key>elementini <key2> elementinin text değeri <text> olana kadar tıkla"})
    public void clickElementUntilGetTextEqual(String key,String key2, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        swipeToElement(driver.findElement(byElement));
        WebElement element = driver.findElement(byElement);

        By byElement2 = ElementHelper.getElementInfoToBy(key2);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement2));
        swipeToElement(driver.findElement(byElement2));
        WebElement element2 = driver.findElement(byElement2);
        do {
            if(element2.getText().equals(text)){break;}
            element.click();

            System.out.println("element2.getText() = " + element2.getText());

        }while (!element2.getText().equals(text));
    }

   @Step({"<key> key li elemente <input> kere tıkla"})
    public void clickElementTimes(String key,String input) throws InterruptedException {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        swipeToElement(driver.findElement(byElement));
       int times=Integer.parseInt(input);

        for (int i = 1; i <= times ; i++) {
            driver.findElement(byElement).click();
            wait(2);

        }


   }
    //@Step({"Sayfanın title ı <title> mı?"})
    @Step({"Sayfanın title ı <title> mı?",
            "Is the page title <title>?"})
    public void checkTitle(String title) {
        try {
            waitingAction.waitUntil(ExpectedConditions.titleIs(title));

        } catch (Exception e) {
            Assert.assertTrue("Sayfanın title ı " + title + " değil", false);
        }
    }
    // @Step({"Sayfanın url i <url> mı?"})
    @Step({"Sayfanın url i <url> mı?",
            "Is the url of the page <url>?"})
    public void checkUrl(String url) {
        try {
            waitingAction.waitUntil(ExpectedConditions.urlToBe(url));

        } catch (Exception e) {
            Assert.assertTrue("Sayfanın url i " + url + " değil. Url: " + driver.getCurrentUrl(), false);
        }
    }
    //@Step({"Yeni açılan sayfaya geç"})
    @Step({"Yeni açılan sayfaya geç",
            "Switch to newly opened page"})
    public void navigateWindow() {
        try {
            winHandleBefore = driver.getWindowHandle();
            Set<String> winHandles = driver.getWindowHandles();
            for (String handle : winHandles) {
                if (!handle.equals(winHandleBefore)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step("Frame")
    //HF used, public
    @Step({"frame gir <key>", "Enter frame with key <key>"})
    public void goToFrame(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        driver.switchTo().frame(element);
    }
    //@Step({"<frame> isimli frame e geç"})
    @Step({"<frame> isimli frame e geç",
            "Switch to frame named <frame>"})
    public void navigateFrame(String frame) {
        try {
            driver.switchTo().frame(frame);
        } catch (Exception e) {
            System.out.println(e);
            Assert.assertTrue(false);

        }
    }
    //@Step({"Frameden ana sayfaya geç"})
    @Step({"Frameden ana sayfaya geç",
            "Switch from frame to home page"})
    public void navigateFrame() {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"Bulunduğun sayfada geri git"})
    @Step({"Bulunduğun sayfada geri git",
            "Go back to the current page"})
    public void backPage() {
        try {
            driver.navigate().back();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    // @Step({"Bulunduğun sayfada ileri git"})
    @Step({"Bulunduğun sayfada ileri git",
            "Move forward on the current page"})
    public void forwardPage() {
        try {
            driver.navigate().forward();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"Bulunduğun sayfayı yenile"})
    @Step({"Bulunduğun sayfayı yenile",
            "Refresh the current page"})
    public void refreshPage() {
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step("Beklemeleri kapat")
    @Step({"Beklemeleri kapat",
            "Close waits"})
    public void closeWait() {
        configuration.setWaitAjax(false);
        configuration.setWaitPageLoad(false);
        configuration.setWaitAngular(false);
    }
    //@Step("Beklemeleri ac")
    @Step({"Beklemeleri ac",
            "Open the waits"})
    public void openWait() {
        configuration.setWaitAjax(true);
        configuration.setWaitPageLoad(true);
        configuration.setWaitAngular(false);
    }
    //@Step("<key> li elementin içindekileri sil")
    @Step({"<key> li elementin içindekileri sil",
            "Delete the contents of the element with <key>"})
    public void deleteText(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        if (System.getProperty("os.name").contains("Mac"))
            element.sendKeys(Keys.COMMAND + "a");
        else if (System.getProperty("os.name").contains("Windows"))
            element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
    }
    //@Step("<key> li elementi temizle")
    @Step({"<key> li elementi temizle",
            "Clear element with <key>"})
    public void deleteText2(String key) {
        String s = "";
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        if (System.getProperty("os.name").contains("Mac"))
            s = Keys.chord(Keys.COMMAND, "a");
        else if (System.getProperty("os.name").contains("Windows"))
            s = Keys.chord(Keys.CONTROL, "a");
        element.sendKeys(s);
        element.sendKeys(Keys.DELETE);
    }
    //@Step("<key> Entere tıkla")
    @Step({"<key> Entere tıkla",
            "<key> Click Enter"})
    public void clickEnterWithElement(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.sendKeys(Keys.ENTER);
    }
    // @Step("Entere tıkla")
    @Step({"Entere tıkla",
            "Click enter"})
    public void clickEnter() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(200);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    // @Step("<key> sn bekle")
    //HF used, public
    @Step({"<key> sn bekle",
            "Wait for <key> sec"})
    public void waitSeconds(String key) throws InterruptedException {
        Thread.sleep(Integer.parseInt(key) * 2000);
        //driver.wait(Integer.parseInt(key) * 1000);
    }
    // @Step("<key> dakika bekle")
    @Step({"<key> dakika bekle",
            "wait <key> minutes"})
    public void waitMinutes(String key) throws InterruptedException {
        Thread.sleep(Integer.parseInt(key) * 1000 * 60);
    }
    // @Step("<key> li elementi görünür olana kadar bekle")
    // HF used_ public
    @Step({"<key> li elementi görünür olana kadar bekle",
            "Wait until element with <key> is visible"})
    public void waitVisible(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.visibilityOfAllElementsLocatedBy(byElement));
        } catch (TimeoutException t) {
            return;
        }
    }
    //HF used_public
    @Step({"Send keys to element with key <key> and text <text>",
            "<key> li elemente <text> degerini yaz"})
    public void sendKey(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        if (text.startsWith("Deger_")) {
            element.sendKeys(StoreHelper.getValue(text));
            System.out.println("text = " + StoreHelper.getValue(text));
        } else {
            element.sendKeys(text);
        }
    }
    @Step({"Wait <key> element",
            "<key> li elementi bekle"})
    public void waitElement(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        } catch (TimeoutException t) {
            return;
        }
    }
    @Step({"<key> li elementi invisible olana kadar bekle",
            "Wait until element with <key> becomes invisible"})
    public void waitInVisible(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.invisibilityOfElementLocated(byElement));
        } catch (TimeoutException t) {
            return;
        }
    }
    //  @Step({"<key> li elementin üzerine gel"})
    @Step({"<key> li elementin üzerine gel",
            "Hover over element with <key>"})
    public void hoverElement(String key) {
        Actions action = new Actions(driver);
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.visibilityOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        action.moveToElement(element).perform();
    }
    // @Step({"<key> li elementin <attr> attributesi <attrtext> mi"})
    @Step({"<key> li elementin <attr> attributesi <attrtext> mi",
            "Are the <attr> attributes of the element with <key> <attrtext>"})
    public void findElementWithTextAndCheckAttributeText(String key, String attr, String attrtext) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.attributeToBe(element, attr, attrtext));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    // @Step({"<key> li elementin <attr> attributesini <text> yap"})
    @Step({"<key> li elementin <attr> attributesini <text> yap",
            "Set the <attr> attributes of the <key> element to <text>"})
    public void setAttribute(String key, String attr, String text) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attr, text);
    }

    @Step({"<key> li elementlerden <text> değerine eşit olanı bul ve tıkla"})
    public void findElementWithTextsAndClicks(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element));
                System.out.println("element.getText() = " + element.getText());
                element.click();
                break;
            }
        }
    }

    @Step({"<key> li elementlerden <text> sırada olanı seç"})
    public void findElementInListAndClicks(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
       int count= Integer.parseInt(text)-1;
        elements.get(count).click();
    }


    //   @Step({"<key> li elementlerden <text> değerine eşit olana tıkla"})
    @Step({"<key> li elementlerden <text> değerine eşit olana tıkla",
            "Click on the element with <key> equal to <text>"})
    public void findElementWithTextsAndClick(String key, String text) {
        String elementText = null;
        boolean flag = true;
        int count = 1;
        while (flag) {
            try {
                By byElement = ElementHelper.getElementInfoToBy(key);
                waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
                List<WebElement> elements = driver.findElements(byElement);
                for (WebElement element : elements) {
                    swipeToElement(element);
                    if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                        if (!element.isDisplayed())
                            waitingAction.waitUntil(ExpectedConditions.visibilityOfElementLocated(byElement));
                        elementText = element.getText();
                        System.out.println("elementText = " + elementText);
                        element.click();
                        break;
                    }
                }
                flag = false;
                break;
            } catch (StaleElementReferenceException e) {
                count = count + 1;
                scrollDown();
                if (text.equals(elementText)) {
                    break;
                }
            }
        }
    }

    @Step({"Çocuğun yaşı için<key> li elementlerden <text> değerine eşit olanı seç",
            "Select the element with <key> equal to <text>"})
    public void kidsAgesFindElementWithTextsAndSelect(String key, String text) {
        int age= Integer.parseInt(text);
        LocalDate now= LocalDate.now();
        int year=now.getYear();
        int birthYear= year-age;

        text= String.valueOf(birthYear);

        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        Select list = new Select(element);
        if (text.startsWith("Deger_")) {
            list.selectByVisibleText(StoreHelper.getValue(text));
        } else {
            list.selectByVisibleText(text);
        }
    }




    @Step({"<key> li elementlerden <text> değerine eşit olanı seç",
            "Select the element with <key> equal to <text>"})
    public void findElementWithTextsAndSelect(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        Select list = new Select(element);
        if (text.startsWith("Deger_")) {
            list.selectByVisibleText(StoreHelper.getValue(text));
        } else {
            list.selectByVisibleText(text);
        }
    }
    //   @Step({"<key> li elementlerden <text> değerine eşit olanın üzerine gel"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın üzerine gel",
            "Hover over elements with <key> equal to <text>"})
    public void findElementWithTextsAndHover(String key, String text) {
        Actions action = new Actions(driver);
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                waitingAction.waitUntil(ExpectedConditions.visibilityOf(element));
                action.moveToElement(element).perform();
                break;
            }
        }
    }
    //  @Step({"<key> li elementin yanındaki <key2> elementine tikla"})
    @Step({"<key> li elementin yanındaki <key2> elementine tikla",
            "Click on the <key2> element next to the element with <key>"})
    public void clickElementNextTo(String key, String key2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        WebElement elementNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
        waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(elementNextTo));
        elementNextTo.click();
    }
    // @Step({"<key> li elementin yanındaki <key2> elementinin üzerine gel"})
    @Step({"<key> li elementin yanındaki <key2> elementinin üzerine gel",
            "Hover over the <key2> element next to the element with <key>"})
    public void hoverElementNextTo(String key, String key2) {
        Actions action = new Actions(driver);
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        WebElement elementNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
        waitingAction.waitUntil(ExpectedConditions.visibilityOf(elementNextTo));
        action.moveToElement(elementNextTo).perform();
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementine tıkla"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementine tıkla",
            "Click on the <key2> element next to the element with <key> equal to <text>"})
    public void findElementWithTextsAndClickNextTo(String key, String text, String key2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                WebElement elementsNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
                //waitingAction.waitUntil(ExpectedConditions.elementToBeClickable((elementNextTo)));
                elementsNextTo.click();
                break;
            }
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementini seç"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementini seç",
            "Select the <key2> element next to the element with <key> equal to <text>"})
    public void findElementWithTextsAndSelectNextTo(String key, String text, String key2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        WebElement elementNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
        Select list = new Select(elementNextTo);
        if (text.startsWith("Deger_")) {
            list.selectByVisibleText(StoreHelper.getValue(text));
        } else {
            list.selectByVisibleText(text);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin üzerine gel"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin üzerine gel",
            "Hover over the <key2> element next to the element with <key> equal to <text>"})
    public void findElementWithTextsAndHoverNextTo(String key, String text, String key2) {
        Actions action = new Actions(driver);
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                WebElement elementNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
                waitingAction.waitUntil(ExpectedConditions.visibilityOf(elementNextTo));
                action.moveToElement(elementNextTo).perform();
                break;
            }
        }
    }
    //@Step({"<key> li elementler var mı?"})
    @Step({"<key> li elementler var mı?",
            "Are there elements with <key>?"})
    public void checkElements(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        if (elements.isEmpty())
            Assert.fail("Elementler bulunamadı.");
    }
    //@Step({"<key> li element var mı?"})
    @Step({"<key> li element var mı?",
            "Is there an element with a <key>?"})
    public void checkElement(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        } catch (Exception e) {
            Assert.assertTrue("Element bulunamadı.", false);
        }
    }
    //@Step({"<key> li element görünür mü?"})
    // HF used_public
    @Step({"<key> li element görünür mü?",
            "Is element with <key> visible?"})
    public void checkElementVisible(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.visibilityOfAllElementsLocatedBy(byElement));
            WebElement element = driver.findElement(byElement);
            if (!element.isDisplayed())
                Assert.fail("Element görünür değil.");
        } catch (Exception e) {
            Assert.assertTrue("Element görünür değil.", false);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olan yok mu?"})
    @Step({"<key> li elementlerden <text> değerine eşit olan yok mu?",
            "Are there no elements with <key> equal to <text>?"})
    public void checkTextNotExistsElements(String key, String text) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            int size = elements.size();
            for (int i = 0; i < size; ) {
                if (elements.get(i).getText().equals(text))
                    Assert.assertFalse("Element bulundu.", true);
                if (i == size - 1) return;
                i++;
            }
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
    //@Step({"<key> li elementlerin <attr> attributesi <attrtext> mi"})
    @Step({"<key> li elementlerin <attr> attributesi <attrtext> mi",
            "Elements with <key> have <attr> attributes <attrtext>"})
    public void findElementWithCheckAttributeText(String key, String attr, String attrtext) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            for (WebElement element : elements) {
                waitingAction.waitUntil(ExpectedConditions.attributeToBe(element, attr, attrtext));
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }


    @Step({"<key> li elementlerinden <text1> ve <text2> içeriyorsa elementi tıkla"
            })
    public void CompareElementTextAndClick(String key,String text1,String text2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        List<WebElement> elements = driver.findElements(byElement);

        for (WebElement e:elements) {
            System.out.println("e.getText() = " + e.getText());
            if(text2.contains(text1+(e.getText()))){
                System.out.println("Hacı naber");
                e.click();
            }
        }
    }



    //@Step({"<key> li element tıklanabilir mi?"})
    @Step({"<key> li element tıklanabilir mi?",
            "Clickable:Is element with <key> clickable?"})
    public void checkClickable(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"<key> li elementin texti <text> mi?"})
    //HF used_public
    @Step({"<key> li elementin texti <text> mi?",
            "Is the text of the element with <key> <text>?"})
    public void checkTextEquals(String key, String text) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            if ((text.startsWith("Deger_") && element.getText().replaceAll("\\s+", "").contains(StoreHelper.getValue(text).replaceAll("\\s+", ""))) || element.getText().replaceAll("\\s+", "").equals(text.replaceAll("\\s+", ""))) {
                //   System.out.println("getElementText = " + element.getText());
            } else {
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olan tıklanabilir mi?"})
    @Step({"<key> li elementlerden <text> değerine eşit olan tıklanabilir mi?",
            "Are elements with <key> equal to <text> clickable?"})
    public void findElementWithTextsAndCheckClickable(String key, String text) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            for (WebElement element : elements) {
                if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                    waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element));
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("Element tıklanılabilir değil.", false);
        }
    }
    //@Step({"<key> li elementin yanındaki <key2> element var mı?"})
    @Step({"<key> li elementin yanındaki <key2> element var mı?",
            "Is there a <key2> element next to the element with <key>?"})
    public void checkElementNextTo(String key, String key2) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
        } catch (Exception e) {
            Assert.assertTrue("Yanındaki element yok.", false);
        }
    }

    // @Step({"<key> li elementin yanındaki <key2> elementinin texti var mı?"})
    @Step({"<key> li elementin yanındaki <key2> elementinin texti var mı?",
            "Does the <key2> element next to the element with <key> have text?"})
    public void checkTextNextTo(String key, String key2) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
            WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
            if (childElement.getText() == null) {
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            Assert.assertTrue("Yanındaki elementin texti yok.", false);
        }
    }
    //@Step({"<key> li elementin yanındaki <key2> elementi tıklanabilir mi?"})
    @Step({"<key> li elementin yanındaki <key2> elementi tıklanabilir mi?",
            "Is the <key2> element next to the <key> element clickable?"})
    public void checkClickableNextTo(String key, String key2) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
            WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
            waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(childElement));
        } catch (Exception e) {
            Assert.assertTrue("Yanındaki element tıklanılabilir değil.", false);
        }
    }
    //@Step({"<key> li elementin yanındaki <key2> elementinin texti <text> mi?"})
    @Step({"<key> li elementin yanındaki <key2> elementinin texti <text> mi?",
            "Is the text of the <key2> element next to the element with <key> <text>?"})
    public void checkTextEqualsNextTo(String key, String key2, String text) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
            WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
            waitingAction.waitUntil(ExpectedConditions.textToBePresentInElement(childElement, text));
        } catch (Exception e) {
            Assert.assertTrue("Yanındaki elementin texti " + text + " değil.", false);
        }
    }
    //@Step({"<key> li elementin yanındaki <key2> elementinin texti <text> içeriyor mi?"})
    @Step({"<key> li elementin yanındaki <key2> elementinin texti <text> içeriyor mi?",
            "Does the text of the element <key2> next to the element with <key> contain <text>?"})
    public void checkTextContainsNextTo(String key, String key2, String text) {
        try {
            Boolean var = false;
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
            WebElement element = driver.findElement(byElement);
            waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
            WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                var = true;
            }
            Assert.assertTrue(var);
        } catch (Exception e) {
            Assert.assertTrue("Yanındaki elementin texti " + text + " değerini içermiyor.", false);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olan elementin <attr> attributesi <attrtext> içeriyor mu?"})
    @Step({"<key> li elementlerden <text> değerine eşit olan elementin <attr> attributesi <attrtext> içeriyor mu?",
            "Does the element with <key> equal to <text> contain <attr> attributes <attrtext>?"})
    public void findElementWithTextAndCheckAttributeContainsTextNextTo(String key, String text, String attr, String
            attrtext) {
        try {

            Boolean var = false;
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            for (WebElement element : elements) {
                if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                    waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
                    if (element.getAttribute(attr).contains(attrtext))
                        break;
                }
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olan elementin <attr> attributesi <attrtext> mı?"})
    @Step({"<key> li elementlerden <text> değerine eşit olan elementin <attr> attributesi <attrtext> mı?",
            "Are the <attr> attributes of the element equal to <text> from <key> elements <attrtext>?"})
    public void findElementWithTextAndCheckAttributeTextNextTo(String key, String text, String attr, String
            attrtext) {
        try {
            Boolean var = false;
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            for (WebElement element : elements) {
                if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().contains(text)) {
                    waitingAction.waitUntil(ExpectedConditions.attributeToBe(element, attr, attrtext));
                    break;
                }
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin <attr> attributesi <attrtext> içeriyor mu?"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin <attr> attributesi <attrtext> içeriyor mu?",
            "Does the <attr> attribute of the <key2> element next to the element with <key> equal to <text> contain <attrtext>?"})
    public void findElementWithTextsAndCheckAttributeTextNextTo(String key, String text, String key2, String
            attr, String attrtext) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            swipeToElement(element);
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
                WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
                //waitingAction.waitUntil(ExpectedConditions.attributeToBe(childElement,attr,attrtext));
                String attrText = childElement.getAttribute(attr);
                System.out.println("Element attr: " + childElement.getAttribute(attr));
                System.out.println("Gönderilen attr: " + attrtext);
                if (childElement.getAttribute(attr).contains(attrtext))
                    return;
            }
        }
        Assert.fail(text + " li element değer içermiyor veya yanındaki elementin " + attr + " attributesine ait değer bulunamadı.");
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin <attr> attributesi <attrtext> mı?"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementinin <attr> attributesi <attrtext> mı?",
            "Is the <attr> attribute of the <key2> element <attrtext> next to the element with <key> equal to <text>?"})
    public void findElementWithTextAndCheckAttributeEqualsTextNextTo(String key, String text, String key2, String
            attr, String attrtext) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            swipeToElement(element);
            if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
                WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
                //waitingAction.waitUntil(ExpectedConditions.attributeToBe(childElement,attr,attrtext));
                String attrText = childElement.getAttribute(attr);
                System.out.println("Element attr: " + childElement.getAttribute(attr));
                System.out.println("Gönderilen attr: " + attrtext);
                if (childElement.getAttribute(attr).equals(attrtext))
                    return;
            }
        }
        Assert.fail(text + " li element değer içermiyor veya yanındaki elementin " + attr + " attributesine ait değer bulunamadı.");
    }
    //@Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementi tıklanabilir mi?"})
    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementi tıklanabilir mi?",
            "Can the <key2> element next to the <text> element equal to the <key> element clickable?"})
    public void findElementWithTextsAndCheckClickableNextTo(String key, String text, String key2) {
        try {
            Boolean variable = false;
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
            List<WebElement> elements = driver.findElements(byElement);
            for (WebElement element : elements) {
                if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                    System.out.println("Element Text: " + element.getText());
                    variable = true;
                    waitingAction.waitUntil(ExpectedConditions.presenceOfNestedElementLocatedBy(element, ElementHelper.getElementInfoToBy(key2)));
                    WebElement childElement = element.findElement(ElementHelper.getElementInfoToBy(key2));
                    waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(childElement));
                    break;
                }
            }
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    @Step({"Enter random email this <key>",
            "<key> li elemente random email yaz"})
    public void sentRandomEmail(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        Faker faker = new Faker();
        String fakeEmail = faker.name().firstName() + "@" + faker.lordOfTheRings().character() + ".com";
        System.out.println(fakeEmail);
        element.sendKeys(fakeEmail);
    }

    @Step({"<key> li elementi sürükle ve bırak",
            "Drag and drop element with <key>"})
    public void dragAndDropColumn(String key) {
        Actions action = new Actions(driver);
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.visibilityOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        action.moveToElement(element).perform();
        action.dragAndDropBy(element, 150, 0).perform();
    }

    @Step("<key> swipe element")
    public void swipeToElement(String key) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        for (int i = 0; i < 25; i++) {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            js.executeScript("arguments[0].scrollIntoView({behavior: \"auto\", block: \"center\", inline: \"center\"});", element);
        }
    }

    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 30; i++) {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }
    public void wait(int s) throws InterruptedException {
        Thread.sleep(s * 1000L);
    }
    // TRUE FUNCTION
    public void swipeJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 30; i++) {
            js.executeScript("arguments[0].scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", element);
        }
    }
    @Step({"<key> elementi secili mi?",
            "<key> element is selected?"})
    public void elementIsSelected(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        System.out.println("element.getAttribute(\"checked\") = " + element.getAttribute("checked"));
    }
    // Select class
    @Step({"Select <key> li elementlerinden <text> değerine eşit olanı seç (visibleText)"})
    public void findElementWithTextIneSelectWithVisibleName(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }
    public void scrollDown() {
        try {
            int i = 0;
            for (; i <= 30; i++) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
            for (; i > 0; i--) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
        } catch (WebDriverException wde) {
        } catch (Exception e) {
        }
    }
    @Step({"<key> li elementlerden <text> değerine eşit olan görünür mü?",
            "Is the element visible with <key> equal to <text>"})
    public void findElementWithTextsAndCheckVisible(String key, String text) {
        String elementText = null;
        boolean flag = true;
        int count = 1;
        while (flag) {
            try {
                By byElement = ElementHelper.getElementInfoToBy(key);
                waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
                List<WebElement> elements = driver.findElements(byElement);
                for (WebElement element : elements) {
                    swipeToElement(element);
                    if ((text.startsWith("Deger_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                        if (!element.isDisplayed())
                            waitingAction.waitUntil(ExpectedConditions.visibilityOfElementLocated(byElement));
                        elementText = element.getText();
                        Assert.assertTrue(element.isDisplayed());
                        break;
                    }
                }
                flag = false;
                break;
            } catch (StaleElementReferenceException e) {
                count = count + 1;
                scrollDown();
                if (text.equals(elementText)) {
                    break;
                }
            }
        }
    }

    @Step({"dropdown menüsünden <key> li elementlerinden <text> değerine eşit olanı seç",
            "Select <text> in dropdown menu with <key> li"})
    public void findElementWithTextInSelectMenu(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        Select select = new Select(element);
        select.selectByValue(text);
    }
    @Step({"<key> li elementinin alert mesajı içerdiğini kontrol et",
            "Verify <key> element alert message appeared"})
    public void checkAlertMessage(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        Assert.assertFalse(element.getAttribute("validationMessage").isEmpty());
        System.out.println("element.getAttribute(\"validationMessage\") = " + element.getAttribute("validationMessage"));
    }

    @Step("<key> li elemente <kez> tıkla")
    public void selectChildNumber(String key, String kez) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        for (int i = 0; i < Integer.parseInt(kez); i++) {
            waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element));
            element.click();
        }
    }
    //HF used, private for HF
    @Step({"Click element with key <key1> times until <key2> has <text>"})
    public void clickElementUntilgetText(String key1, String key2, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key1);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        if (text.startsWith("Deger_")) {
            text = StoreHelper.getValue(text);
        }
        By byElements = ElementHelper.getElementInfoToBy(key2);
        List<WebElement> fullNameOfCustomers = driver.findElements(byElements);
        int idx = 1;
        int numberOFCustomers = fullNameOfCustomers.size();
        while (idx < numberOFCustomers) {
            if (fullNameOfCustomers.get(idx).getAttribute("innerText").contains(text)) {
                WebElement customer = driver.findElement(By.xpath("//p[text()='" + text + "']/parent::div//span"));
                waitingAction.waitUntil(ExpectedConditions.visibilityOf(customer));
                customer.click();
                break;
            } else {
                if (!(idx < 3)) driver.findElement(byElement).click();
            }
            idx++;
        }
    }
    @Step({"<key> elementi <text> seçildi mi kontrolrol edilir.",
            "Is <key> element selected as <text>?"})
    public void elementIsSelected(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        if (element.getText().equals(text)) {
            Assert.assertTrue(element.isSelected());
        }
    }
    @Step({"<key> li elementlerden <attribute> değeri <text> değerine eşit olana tıkla"})
    public void findElementgetAttributeWithTextAndClick(String key, String attribute, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            swipeToElement(element);
            System.out.println("element.getAttribute(attribute) = " + element.getAttribute(attribute));
            if (element.getAttribute(attribute).equalsIgnoreCase(text)) {
                if (!element.isDisplayed())
                    waitingAction.waitUntil(ExpectedConditions.visibilityOfElementLocated(byElement));
                element.click();
                break;

            }
        }
    }

    // isDisplayed
    @Step({"<key> li elementinin degerinin görünür oldugunu kontrol et"})
    public void isElementNotVisible(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        System.out.println(element.getText());
        Assert.assertTrue(element.isDisplayed());
    }

    //not isDisplayed
    @Step({"<key> li element görünür olmadığını kontrol et?",
            "Is element with <key>  not visible?"})
    public void checkElementIsNotVisible(String key) {
        try {
            By byElement = ElementHelper.getElementInfoToBy(key);
            waitingAction.waitUntil(ExpectedConditions.visibilityOfAllElementsLocatedBy(byElement));
            WebElement element = driver.findElement(byElement);
            if (!element.isDisplayed())
                Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue("Element görünür değil.", false);
        }
    }

    @Step({"<key> li elementin texti veya value attribute'u <text> içeriyor mu?",
            "Does the text or attribute value of the element with <key> contains <text>?"})
    public void checkTextContains2(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        System.out.println("element.getText()= " + element.getText());
        System.out.println("element.getAttribute(\"value\") = " + element.getAttribute("value"));
        try {
            Assert.assertTrue(element.getText().contains(text));

        } catch (Exception e) {
            Assert.assertTrue(element.getAttribute("value").contains(text));
        }
    }

    //check Increasing Sorted
    @Step({"Are text of <key> , <key2> , <key3> elements sorted by increasing?"})
    public void checkIncreasingSorted(String key, String key2, String key3) throws InterruptedException {
        Double value1 = getDoubleValue(key);
        Double value2 = getDoubleValue(key2);
        Double value3 = getDoubleValue(key3);

        System.out.println("value1 = " + value1);
        System.out.println("value2 = " + value2);
        System.out.println("value3 = " + value3);
        Assert.assertTrue("Sorting is incorrect.", ((value1 < value2) && (value2 < value3)));
    }

    public double getDoubleValue(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        double value1 = Double.parseDouble(element.getText());
        return value1;
    }

    //click Until Down Of Page
    @Step({"Click <key> button while <key2> is smaller than <key3>"})
    public void clickUntilDownOfPage(String key, String key2, String key3) throws InterruptedException {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);

        int biggerValue = getINTValue(key2);
        System.out.println("biggerValue = " + biggerValue);

        int smallerValue = getINTValue(key3);
        System.out.println("smallerValue = " + smallerValue);

        while (biggerValue > smallerValue) {
            element.click();
        }
    }

    public int getINTValue(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        int value = Integer.parseInt(element.getText());
        System.out.println("value = " + value);
        return value;
    }

    // enter random mail(By Faker class)
    @Step({"Enter random email that <key>",
            "<key> li elemente random email yaz"})
    public void sendEmail(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        Faker faker = new Faker();
        String fakeEmail = faker.name().firstName() + "@" + faker.lordOfTheRings().character() + ".com";
        System.out.println(fakeEmail);
        element.sendKeys(fakeEmail);
    }

    // navigate to Window With Title
    @Step({"Switch to newly opened window which has <text> title"})
    public void navigateWindowWithTitle(String text) {
        Set<String> windowsHandles = driver.getWindowHandles();
        for (String handle : windowsHandles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(text)) {
                break;
            }
        }
    }

    public void scrollUp() {
        try {
            int i = 1;
            for (; i > -30; i--) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
            for (; i < 0; i++) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
        } catch (WebDriverException wde) {
        } catch (Exception e) {
        }
    }

    //scroll down
    @Step("Go to bottom of the page")
    public void goToBottom() throws InterruptedException {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i < 15; i++) {
            Thread.sleep(1000);
            jse.executeScript("window.scrollBy(0,350)");
        }
    }
    // HF used_ private for HF
    @Step("Select <option>")
    public void selectOneOption(String FlightClass) {
        WebElement economyOrBusiness = driver.findElement(By.xpath("//button[text()='" + FlightClass + "']"));
        economyOrBusiness.click();
    }
    // HF used_private for HF
    @Step("Select <text> and <SayiChild> and <SayiBaby>")
    public void selectOfPassengers(String sayiAdult, String sayiChild, String sayiBaby) {
        int numOfAdult = Integer.parseInt(sayiAdult);
        int numOfChild = Integer.parseInt(sayiChild);
        int numOfBaby = Integer.parseInt(sayiBaby);
        // yetişkin, çocuk ve bebek sayılarını artırmak ve azaltmak için locatorlar List'e eklendi.
        List<WebElement> passengers = driver.findElements(By.cssSelector("button[class='chakra-button css-11qjs01']"));
        while (numOfAdult > 1) {
            passengers.get(0).click();
            numOfAdult--;
        }
        while (numOfChild > 0) {
            passengers.get(1).click();
            numOfChild--;
        }
        while (numOfBaby > 0) {
            passengers.get(2).click();
            numOfBaby--;
        }
    }
    // verifying warning messages
    //HF used_public
    @Step("Verify <key> key contains <text> text")
    public void verifyMessage(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        String expectedMessage = text.replaceAll(" ", "");
        System.out.println("expectedMessage = " + expectedMessage);
        String actualMessage = element.getText().replaceAll(" ", "");
        System.out.println("actualMessage = " + actualMessage);
        Assert.assertTrue(actualMessage.contains(expectedMessage));

    }
    // liste halindeki web elemetlerin tüm text'leri için
    //HF, public
    @Step("Verify <key> key has <errorMessage> text of list")
    public void listOfTexts(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            String expectedMessage = text.replaceAll(" ", "");
            //   System.out.println("expectedMessage = " + expectedMessage);
            String actualMessage = element.getText().replaceAll(" ", "");
            //   System.out.println("actualMessage = " + actualMessage);
            Assert.assertEquals(expectedMessage, actualMessage);
        }

    }
    //HF used_private for HF
    @Step("Select the airport <airport>")
    public void selectAirport(String airport) {
        WebElement element = driver.findElement(By.xpath("//p[@class='chakra-text css-2i6bdx'][contains(text(),'"+airport+"')]/.."));
        waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    @Step("Select the airport2 <airport>")
    public void selectAirportDestination(String airport) {
        Actions actions = new Actions(driver);
        WebElement element2= driver.findElement(By.xpath("//div[@class='css-15vq24l']//p[@class='chakra-text css-2i6bdx'][contains(.,'"+airport+"')]/.."));
        waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(element2));
        actions.moveToElement(element2).click(element2).build().perform();

      /*  1. yöntem
      Actions actions = new Actions(driver);
      List<WebElement> elements= driver.findElements(By.xpath("//div[@role='group']//p[@class='chakra-text css-2i6bdx']"));
        for (WebElement webElement : elements) {
            if(webElement.getText().contains(airport)){
                actions.moveToElement(webElement).click(webElement).build().perform();
            }
        }
        */

        /*  2. yöntem
        Actions actions = new Actions(driver);
        List<WebElement> elements= driver.findElements(By.xpath("//div[@class='css-15vq24l']//p"));
        for (WebElement webElement : elements) {
            while (webElement.getText().contains(airport)) {
                actions.sendKeys(Keys.ARROW_DOWN).perform();
                actions.click(webElement).perform();
            }
        }
        */
    }


    @Step("Tarih olarak bu günden itibaren <text> gün sonra giriş <text2> gün sonra çıkış seç")
    public void selectVisitDate(String text,String text2) throws InterruptedException {
//.c-finder__flight-range
         WebElement elementMain=driver.findElement(By.xpath("(//div[@class='c-input__row'])[2]"));
           elementMain.click();
            int startDayFromToday = Integer.parseInt(text);
            int endDayFrom = Integer.parseInt(text2);

            selectDate(startDayFromToday, endDayFrom);
        }

        private void selectDate(int startDayFromToday, int endDayFrom) {
            LocalDate startDate = LocalDate.now().plusDays(startDayFromToday);
            int startDayOfMonth = startDate.getDayOfMonth();
            String locatorForStart="";
            if (!LocalDate.now().getMonth().toString().equals(startDate.getMonth().toString())){
                locatorForStart = "(//tbody)[2]//td[.='" + startDayOfMonth + "']";

            }else {
                locatorForStart = "(//tbody)[1]//td[.='" + startDayOfMonth + "']";
            }

            System.out.println("startDayOfMonth = " + startDayOfMonth);
            WebElement start = driver.findElement(By.xpath(locatorForStart));
            start.click();

            LocalDate endLocalDate = LocalDate.now().plusDays(startDayFromToday + endDayFrom);
            int endDayOfMonth =endLocalDate.getDayOfMonth();
            System.out.println("endDayOfMonth = " + endDayOfMonth);

            String locatorForEnd="";
            if (!LocalDate.now().getMonth().toString().equals(endLocalDate.getMonth().toString())){
                locatorForEnd = "(//tbody)[2]//td[.='" + endDayOfMonth + "']";

            }else {
                locatorForEnd = "(//tbody)[1]//td[.='" + endDayOfMonth + "']";
            }
            WebElement end = driver.findElement(By.xpath(locatorForEnd));
            end.click();



        }

    @Step("Ucus Tarihi olarak bu günden itibaren <text> gün sonra giriş <text2> gün sonra çıkış seç")
    public void selectFlightDate(String text,String text2) throws InterruptedException {

        WebElement elementMain=driver.findElement(By.xpath("//input[@class='c-input c-input--has-icon c-finder__flight-range flight-date-range']"));
        elementMain.click();
        Thread.sleep(3000);
        int startDayFromToday = Integer.parseInt(text);
        int endDayFrom = Integer.parseInt(text2);

        selectDate1(startDayFromToday, endDayFrom);
    }

    private void selectDate1(int startDayFromToday, int endDayFrom) {
        LocalDate startDate = LocalDate.now().plusDays(startDayFromToday);
        int startDayOfMonth = startDate.getDayOfMonth();
        String locatorForStart="";
        if (!LocalDate.now().getMonth().toString().equals(startDate.getMonth().toString())){
            locatorForStart = "(//tbody)[2]//td[.='" + startDayOfMonth + "']";

        }else {
            locatorForStart = "(//tbody)[1]//td[.='" + startDayOfMonth + "']";
        }

        System.out.println("startDayOfMonth = " + startDayOfMonth);
        WebElement start = driver.findElement(By.xpath(locatorForStart));
        start.click();

        LocalDate endLocalDate = LocalDate.now().plusDays(startDayFromToday + endDayFrom);
        int endDayOfMonth =endLocalDate.getDayOfMonth();
        System.out.println("endDayOfMonth = " + endDayOfMonth);

        String locatorForEnd="";
        if (!LocalDate.now().getMonth().toString().equals(endLocalDate.getMonth().toString())){
            locatorForEnd = "(//tbody)[2]//td[.='" + endDayOfMonth + "']";

        }else {
            locatorForEnd = "(//tbody)[1]//td[.='" + endDayOfMonth + "']";
        }
        WebElement end = driver.findElement(By.xpath(locatorForEnd));
        end.click();



    }




    @Step("Tarih olarak bu günden <text> gün sonrasını seç")
    public void selectHolidayDate(String text) throws InterruptedException {
        LocalDate now= LocalDate.now();
        System.out.println("now = " + now);
        int dayOfMonth =now.getDayOfMonth();
        LocalDate date = now.plusDays(Long.parseLong(text));
        System.out.println("date = " + date);
        String[] nows = (now + "").split("-");
        String[] dates = (date + "").split("-");
        wait(5);
        System.out.println("dates[2] = " + dates[2]);
      String chosenDate=Integer.parseInt(dates[2])+"";
      String nowDate=Integer.parseInt(dayOfMonth+"")+"";
        System.out.println("Integer.parseInt(dates[2]) = " + Integer.parseInt(dates[2]));
        System.out.println("nowDate = " + nowDate);
        WebElement elementdayOfWeek=driver.findElement(By.xpath("(//td[@class='today active start-date in-range available'])"));
        //WebElement elementdayOfWeekend=driver.findElement(By.xpath("(//td[@class='today weekend active start-date in-range available'])"));

        scrollToElement(elementdayOfWeek);
        elementdayOfWeek.click();
        if (nows[1].equals(dates[1])){
          WebElement element=driver.findElement(By.xpath("(//td[.="+chosenDate+"])[1]"));
          element.click();

            }
        else{
        WebElement element=driver.findElement(By.xpath("(//td[.="+chosenDate+"])[3]"));
        element.click();

        }
        }




    //HF used_ public/private
    // Gidiş tarihi girmek için yazıldı. Sadece tıklanabilen board'larda
    @Step("Tarih olarak bu günden <text> gün sonrasını seçiniz")
    public void selectTravelDepartureDate(String text) {

        String now=LocalDate.now().getMonth().toString();
        System.out.println(now);

        String departureDayString = getDepartureDate(text);
        String chosenday = departureDayString.substring(8);
        System.out.println("chosenday = " + chosenday);
        Integer day = Integer.valueOf(chosenday);
        System.out.println("chosenday = " + chosenday);


        String choosendayResult = null;
        if (day < 10) {
            choosendayResult = departureDayString.substring(9);
        } else {
            choosendayResult = departureDayString.substring(8);
        }
        System.out.println("choosendayResult = " + choosendayResult);
        Month currentMonth = LocalDate.now().getMonth();
        Month departureMonth = LocalDate.now().plusDays(Integer.parseInt(text)).getMonth();
        if (departureMonth.equals(currentMonth)) {
            List<WebElement> elementDate1 = driver.findElements(By.xpath("//div[@class='drp-calendar left']//div[@class='date-wrapper']"));
            for (WebElement e:elementDate1) {
                if(e.getText().equals(chosenday))
                    System.out.println("e.getText() = " + e.getText());
                    e.click();
            }

        } else {
            List<WebElement> elementDate2 = driver.findElements(By.xpath("//div[@class='drp-calendar right']//div[@class='date-wrapper']"));
            for (WebElement e:elementDate2) {
                if(e.getText().equals(chosenday))
                    System.out.println("e.getText() = " + e.getText());
                e.click();
            }
        }
    }

    // HF used
    //selectTravelDepartureDate() methodundan çağrılıyor.
    public String getDepartureDate(String day) {
        int nDate = Integer.parseInt(day);
        LocalDate date = LocalDate.now();
        LocalDate afterDays = date.plusDays(nDate);
        System.out.println("7days later: " + afterDays);
        return String.valueOf(afterDays);
    }

    //HF used_ public/private
    // Dönüş tarihi girmek için yazıldı. Sadece tıklanabilen board'larda
    @Step("Select <departureDate> after today and select return date <returnDate>")
    public void selectTravelReturnDate(String departureDate, String returnDate) {
        int travelReturnDate = Integer.parseInt(departureDate) + Integer.parseInt(returnDate);
        LocalDate date = LocalDate.now();
        LocalDate afterDays = date.plusDays(travelReturnDate);
        System.out.println("return days X days later after Y today: " + afterDays);
        String departureDayString = String.valueOf(afterDays);

        String chosenDay = departureDayString.substring(8);
        Integer day = Integer.valueOf(chosenDay);
        String chosenDayResult = null;
        if (day < 10) {
            chosenDayResult = departureDayString.substring(9);
        } else {
            chosenDayResult = departureDayString.substring(8);
        }

        Month currentMonth = date.getMonth();
        Month departureMonth = afterDays.getMonth();
        if (departureMonth.equals(currentMonth)) {
            WebElement elementDate1 = driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[3]//div[text()='" + chosenDayResult + "']"));
            elementDate1.click();
        } else {
            WebElement elementDate2 = driver.findElement(By.xpath("(//div[@class='react-datepicker__month'])[4]//div[text()='" + chosenDayResult + "']"));
            elementDate2.click();
        }
    }

    //HF used_public
    @Step({"<key> li elementlerin text değerleri <text> mi?",
            "Is all <key> key have <text> texts?"})
    public void classNameFromAllList(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            swipeToElement(element);
            Assert.assertTrue(element.getText().equalsIgnoreCase(text));
        }
    }
    //HF used_ private for HF
    @Step("Click select button according to package <text>")
    public void selectPackageOfFlight(String text) {
        WebElement secPackage = driver.findElement(By.xpath("//p[text()='" + text + "']"));
        waitingAction.waitUntil(ExpectedConditions.visibilityOf(secPackage));
        List<WebElement> secButtons = driver.findElements(By.xpath("//div[@class='css-g3besh']//button[@class='chakra-button css-1bggl2i']"));

        if (secPackage.getText().equals("EcoFly")) {
            secButtons.get(0).click();
        } else if (secPackage.getText().equals("ExtraFly")) {
            secButtons.get(1).click();
        } else if (secPackage.getText().equals("PrimeFly")) {
            secButtons.get(2).click();
        }

    }

    // iki ayrı element için iki ayrı text kontrolü
    //HF used, could be public
    @Step("Does the <key1> key and <key2> key elements have <text1> text and <text2> text?")
    public void verifyTwoKEysWithTwoTexts(String key1, String key2, String text1, String text2) {
        By byElement1 = ElementHelper.getElementInfoToBy(key1);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement1));
        WebElement element1 = driver.findElement(byElement1);
        Assert.assertEquals(text1, element1.getText());

        By byElement2 = ElementHelper.getElementInfoToBy(key2);
        WebElement element2 = driver.findElement(byElement2);
        Assert.assertEquals(text2, element2.getText());
    }

    //HF used_private for HF
    // anasayfada seçilen uçuş sonrası gelen uçuş bilgiler ile "sonraki adıma geç" butonuna tıkladıktan sonra açılan
    // iletişim sayfasında gelen bilgilerin aynı olduğu kontrol edildi.
    // Hepsifly ekibi aynı method içinde verify yapmayı onayladı.
    @Step("Verify information after click <key>")
    public void implementation2(String key) throws InterruptedException {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        String FlightNumberONHomePage = driver.findElement(By.cssSelector("p[class='chakra-text css-145dp8q']")).getText();
        String TakeOffTimeONHomePage = driver.findElement(By.xpath("(//p[@class='chakra-text css-1v0cd8t'])[1]")).getText();
        String LandingTimeONHomePage = driver.findElement(By.xpath("(//p[@class='chakra-text css-1v0cd8t'])[2]")).getText();
        String DepartureAirportONHomePage = driver.findElement(By.xpath("(//p[@class='chakra-text css-1v0cd8t'])[3]")).getText();
        String DestinationAirportONHomePage = driver.findElement(By.xpath("(//p[@class='chakra-text css-1v0cd8t'])[4]")).getText();
        String departureAndDestinationONHomePage = DepartureAirportONHomePage + "-" + DestinationAirportONHomePage;
        System.out.println("departureAndDestinationONHomePage = " + departureAndDestinationONHomePage);

        String ClassONHomePage = driver.findElement(By.xpath("//div[@class='css-1l7kiiv']//p[@class='chakra-text css-4h8r8a']")).getText();
        String PackageONHomePage = driver.findElement(By.xpath("//td[@class='css-42ly1l']")).getText();
        String classAndPackageONHomePage = ClassONHomePage + PackageONHomePage;
        System.out.println("classAndPackageONHomePage = " + classAndPackageONHomePage);

        String TotalPriceONHomePage = driver.findElement(By.xpath("//p[@class='chakra-text css-4tc3sx']")).getAttribute("innerHTML").replaceAll(" ", "");
        System.out.println("TotalPriceONHomePage = " + TotalPriceONHomePage);

        WebElement sonrakiAdımaGec = driver.findElement(byElement);
        sonrakiAdımaGec.click();
        wait(1);
        WebElement flightNumONContactInfoPage = driver.findElement(By.cssSelector("p[class='chakra-text css-1iw9q6j']"));
        waitingAction.waitUntil(ExpectedConditions.visibilityOf(flightNumONContactInfoPage));
        String FlightNumberONContactInfoPage = flightNumONContactInfoPage.getAttribute("innerHTML");
        String TakeOffTimeONContactInfoPage = driver.findElement(By.xpath("(//div[@class='css-uzwydv'])[1]")).getAttribute("innerHTML");
        String LandingTimeONContactInfoPage = driver.findElement(By.xpath("(//div[@class='css-uzwydv'])[2]")).getAttribute("innerHTML");

        WebElement classAndPackageInfo = driver.findElement(By.xpath("//div[@class='css-lgs8rh']"));
        String[] arrClass = classAndPackageInfo.getAttribute("innerText").split("kg");
        String classAndPackageContactInfoPage = arrClass[1];
        System.out.println("classAndPackageContactInfoPage = " + classAndPackageContactInfoPage);

        WebElement departureAndDestination = driver.findElement(By.xpath("(//p[@class='chakra-text css-1v0cd8t'])[1]"));
        String departureAndDestinationONContactInfoPage = departureAndDestination.getAttribute("innerHTML").replaceAll(" ", "");
        System.out.println("departureAndDestinationInfo = " + departureAndDestinationONContactInfoPage);

        String TotalPriceONContactInfoPage = driver.findElement(By.xpath("//p[@class='chakra-text css-12ufg2n']")).getAttribute("innerHTML").replaceAll(" ", "");

        Assert.assertEquals("Flight Numbers are not equal", FlightNumberONHomePage, FlightNumberONContactInfoPage);
        Assert.assertEquals("Take of times are not equal", TakeOffTimeONHomePage, TakeOffTimeONContactInfoPage);
        Assert.assertEquals("Landing times are not equal", LandingTimeONHomePage, LandingTimeONContactInfoPage);
        Assert.assertEquals("Airports are not equal", departureAndDestinationONHomePage, departureAndDestinationONContactInfoPage);
        Assert.assertEquals("Class and package are not equal", classAndPackageONHomePage, classAndPackageContactInfoPage);
        Assert.assertEquals("Total prices are not equal", TotalPriceONHomePage, TotalPriceONContactInfoPage);

    }

    // HF used_ private for HF
    @Step("<key1>,<key2>,<key3> match <key11>,<key22>,<key33> after click <SonrakiAdimaGec>")
    public void getText(String key1, String key2, String key3, String key11, String key22, String key33, String key) {
        String expectedTextYetişkin = getText(key1);
        String expectedTextCocuk = getText(key2);
        String expectedTextBebek = getText(key3);

        By byElement = ElementHelper.getElementInfoToBy(key);
        WebElement element = driver.findElement(byElement);
        element.click();

        String actualTextYetişkin = getText(key11);
        String actualTextCocuk = getText(key22);
        String actualTextBebek = getText(key33);

        Assert.assertTrue(actualTextYetişkin.equalsIgnoreCase(expectedTextYetişkin));
        Assert.assertTrue(actualTextCocuk.equalsIgnoreCase(expectedTextCocuk));
        Assert.assertTrue(actualTextBebek.equalsIgnoreCase(expectedTextBebek));
    }

    //HF used, üstteki method için, HF private
    public String getText(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        String[] arr = element.getText().split(" ");
        System.out.println("getText = " + arr[1]);
        return arr[1];
    }

    //HF used_public
    @Step("Check <key> inputBox filled by <text>")
    public void checkInputBoxes(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        String actualText = element.getAttribute("value");
        if (text.startsWith("Deger_")) {
            text = StoreHelper.getValue(text);
        }
        String expectedText = text;
        Assert.assertEquals(expectedText, actualText);
    }

    // button etkin mi? kontrolü
    //HF used, public
    @Step("Verify <key> key isEnable?")
    public void boxIsEnable(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        Assert.assertTrue(element.isEnabled());
    }

    // button etkin olmama kontrolü.
    //HF used, public
    @Step("Verify <key> key is NOT isEnable?")
    public void boxNOTisEnables(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);
        Assert.assertFalse(element.isSelected());
    }

    @Step("Verify <key2> key next to <key1> key is NOT displayed?")
    public void keyIsNOTDisplayed(String key1, String key2) {
        By byElement = ElementHelper.getElementInfoToBy(key1);
        waitingAction.waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        WebElement element = driver.findElement(byElement);

        By byElement2 = ElementHelper.getElementInfoToBy(key2);
        WebElement element2 = driver.findElement(byElement);
        Assert.assertFalse(element2.isDisplayed());
    }

    // +90 alan koduna kadar silsin, ya da alan kodu ne geliyorsa oraya kadar silsin
    //HF not used
    @Step("Send key before value clear key <key> and text <text>")
    public void denemePhoneNUmber(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        while (!element.getAttribute("value").equals("+90")) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        if (text.startsWith("Deger_")) {
          /*  JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].value = arguments[1]",element, "+90");*/
            element.sendKeys(StoreHelper.getValue(text));
            System.out.println("text = " + StoreHelper.getValue(text));
        } else {
            element.sendKeys(text);
        }
    }

    //HF not used
    @Step("Wait until clickable and click key <key>")
    public void waitAndClick(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        waitingAction.waitUntil(ExpectedConditions.elementToBeClickable(byElement));
        driver.findElement(byElement).click();
    }

    //HF used, private for HF
    @Step("Click element with dynamic key <text>")
    public void clickDynamicElement(String text) {
        WebElement element = driver.findElement(By.xpath("//span[normalize-space()='" + text + "']"));
        waitingAction.waitUntil(ExpectedConditions.visibilityOf(element));
        element.click();
    }


}


