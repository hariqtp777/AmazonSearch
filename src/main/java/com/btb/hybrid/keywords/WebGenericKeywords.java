package com.btb.hybrid.keywords;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.btb.hybrid.report.ExtentManager;

public class WebGenericKeywords {
	public Properties envprop;
	public Properties genprop;
	public String objKey;
	public String dataKey;
	public String descVal;
	public String proceedOnFail;
	public Hashtable<String, String> data;
	public WebDriver driver;
	public JavascriptExecutor jsDriver;
	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();
	public JavascriptExecutor js;

	public void setEnvprop(Properties envprop) {
		this.envprop = envprop;
	}

	public void setGenprop(Properties genprop) {
		this.genprop = genprop;
	}

	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setDescKey(String descVal) {
		this.descVal = descVal;
	}

	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}
	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}

	public String getProceedOnFail() {
		return proceedOnFail;
	}

	public Properties getEnvprop() {
		return envprop;
	}

	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}
	/**
	 * openBrowser -  Method opens the Respective browser - as per Data Sheet Browser value
	 */
	public void openBrowser(){
		try {
			String browser = data.get(dataKey);
			//test.log(Status.INFO, "Opening the browser: "+browser);
			test.log(Status.INFO, descVal + " : "+browser);
			if(browser.equalsIgnoreCase("mozilla")){
				System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+File.separator+"driver"+File.separator+"geckodriver");
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("chrome")){
				ChromeOptions ops = new ChromeOptions();
				//ops.addArguments("--headless");
				ops.addArguments("--disable-notifications");
				ops.addArguments("--disable-extensions");
				ops.setPageLoadStrategy(PageLoadStrategy.NORMAL);
				if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
					System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+File.separator+"driver"+File.separator+"chromedriver");
				}
				else {
					System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+File.separator+"driver"+File.separator+"chromedriver_win_86.exe");
				}
				//WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(ops);
			}
			driver.manage().window().maximize();
			//driver.manage().window().setSize(new Dimension(1600,700));
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		catch(Exception e) {
			test.log(Status.FAIL, "Browser did NOT open");

		}
	}

	public void openNewTab() throws InterruptedException {
		try {
			((JavascriptExecutor)driver).executeScript("window.open()");
			test.log(Status.INFO, descVal);
		}
		catch(Exception e) {
			reportFailure("openNewTab action failed");
		}
	}


	public void keyEnter() throws InterruptedException {
		try {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER).perform();
			test.log(Status.INFO, descVal);
		}
		catch(Exception e) {
			test.log(Status.INFO, "ENTER Keyboard action failed" );
		}
	}

	/**
	 * navigate - Method used to enter url and navigate to portal
	 */

	public void navigate(){
		try {
			WebElement html = driver.findElement(By.tagName("html"));
			html.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
			driver.get(genprop.getProperty(objKey));
			test.log(Status.INFO,descVal + " : "+genprop.getProperty(objKey));
		}
		catch(Exception e) {
			reportFailure("Navigate action failed");
		}
	}

	/**
	 * click - Method used to click on the WebElement
	 */
	public void click(){
		getObject(objKey).click();
		//test.log(Status.INFO, "Clicked on the webelement : "+envprop.getProperty(objKey));
		test.log(Status.INFO, descVal);
	}



	/**
	 * waitUntil - Method is used to wait until visibility of element (Ex: Scroll)
	 * @throws InterruptedException
	 */
	public void waitUntil() throws InterruptedException{
		takeScreenshot();
		WebDriverWait wait = new WebDriverWait(driver,60);
		wait.until(ExpectedConditions.visibilityOf(getObject(objKey)));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getObject(objKey));
		test.log(Status.INFO, descVal);
	}

	/**
	 * type - Method is used to enter values in a WebElement
	 */

	public void type(){
		getObject(objKey).sendKeys(data.get(dataKey));
		//test.log(Status.INFO, "Entered the Value as: "+data.get(dataKey));
		test.log(Status.INFO, descVal + " : "+data.get(dataKey));
	}


	/**
	 * quit - Method is used to quit the browser
	 */
	public void quit(){
		if(driver !=null){
			driver.quit();
			test.log(Status.INFO, "Browser Closed");
		}
	}



	public void validateElementPresent(){		
		boolean val = false;
		try{
			if(objKey.endsWith("_xpath")){
				val = driver.findElements(By.xpath(envprop.getProperty(objKey))).size()!=0;

			}
			else if(objKey.endsWith("_id")){
				val = driver.findElements(By.id(envprop.getProperty(objKey))).size()!=0;

			}
			else if(objKey.endsWith("_css")){
				val = driver.findElements(By.cssSelector(envprop.getProperty(objKey))).size()!=0;

			}
			else if(objKey.endsWith("_name")){
				val = driver.findElements(By.name(envprop.getProperty(objKey))).size()!=0;

			}
			else if(objKey.endsWith("_tagname")){
				val = driver.findElements(By.tagName(envprop.getProperty(objKey))).size()!=0;

			}
		}
		catch(Exception e) {
			reportFailure("Element NOT found");
		}

		if(val) {
			test.log(Status.INFO, descVal);
		}
		else {
			reportFailure("Element NOT found");
		}
	}


	public void switchNewTab(){
		try {
			ArrayList<String> newTab = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(newTab.get(1));
			test.log(Status.INFO,descVal);
		}
		catch(Exception e) {
			reportFailure("switchNewTab action failed ");
		}
	}


	public void switchOriginalTab(){		
		ArrayList<String> newTab = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(newTab.get(0));
	}

	public void switchOriginalTab1(){		
		ArrayList<String> newTab = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(newTab.get(1));
	}


	//---------------------------------------------

	public WebElement getObject(String objKey){
		WebElement e = null;
		try{
			if(objKey.endsWith("_xpath")){
				e = driver.findElement(By.xpath(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_id")){
				e = driver.findElement(By.id(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_css")){
				e = driver.findElement(By.cssSelector(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_name")){
				e = driver.findElement(By.name(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_tagname")){
				e = driver.findElement(By.tagName(envprop.getProperty(objKey)));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			takeScreenshot();
			reportFailure("Element NOT found "+objKey);
		}
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOf(e));
		wait.until(ExpectedConditions.elementToBeClickable(e));
		return e;
	}


	public WebElement getObjectXpath(String objKey){
		WebElement e = null;
		try{
			e = driver.findElement(By.xpath(objKey));	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			takeScreenshot();
			reportFailure("Element NOT found "+objKey);
		}
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOf(e));
		wait.until(ExpectedConditions.elementToBeClickable(e));
		return e;
	}

	public WebElement getObjectWithoutExplicit(String objKey){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		WebElement e = null;
		try{
			if(objKey.endsWith("_xpath")){
				e = driver.findElement(By.xpath(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_id")){
				e = driver.findElement(By.id(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_css")){
				e = driver.findElement(By.cssSelector(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_name")){
				e = driver.findElement(By.name(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_tagname")){
				e = driver.findElement(By.tagName(envprop.getProperty(objKey)));
			}
			else {
				e = driver.findElement(By.xpath(envprop.getProperty(objKey)));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			takeScreenshot();
			reportFailure("Element NOT found "+objKey);
		}
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return e;
	}

	public List<WebElement> getObjects(String objKey){
		List<WebElement> list=null;
		try{
			if(objKey.endsWith("_xpath")){
				list = driver.findElements(By.xpath(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_id")){
				list = driver.findElements(By.id(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_css")){
				list = driver.findElements(By.cssSelector(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_name")){
				list = driver.findElements(By.name(envprop.getProperty(objKey)));
			}
			else if(objKey.endsWith("_tagname")){
				list = driver.findElements(By.tagName(envprop.getProperty(objKey)));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			takeScreenshot();
			reportFailure("Element NOT found "+objKey);
		}
		return list;
	}

	public boolean isElementPresent(String objKey){
		List<WebElement> list=null;
		if (objKey.endsWith("_xpath")){
			list = driver.findElements(By.xpath(envprop.getProperty(objKey)));
		}
		else if(objKey.endsWith("_id")){
			list = driver.findElements(By.id(envprop.getProperty(objKey)));
		}
		else if(objKey.endsWith("_css")){
			list = driver.findElements(By.cssSelector(envprop.getProperty(objKey)));
		}
		else if(objKey.endsWith("_name")){
			list = driver.findElements(By.name(envprop.getProperty(objKey)));
		}


		if(list.size() == 0){
			return false;
		}
		else{
			return true;
		}
	}

	public boolean isElementPresentCustom(String objKey){
		List<WebElement> list=null;

		list = driver.findElements(By.xpath(objKey));

		if(list.size() == 0){
			return false;
		}
		else{
			return true;
		}
	}

	public boolean isElementVisible(String objKey){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getObject(objKey));
		if(getObject(objKey).isDisplayed()) {
			reportPass(descVal);
			return true;
		}
		else {
			reportFailure("Element NOT present "+objKey);
			return false;
		}
	}

	public void reportFailure(String failureMsg){
		test.log(Status.FAIL, failureMsg);//extent report failure
		takeScreenshot();
		if(proceedOnFail.equalsIgnoreCase("Y")){
			softAssert.fail(failureMsg);	
		}
		else{
			softAssert.fail(failureMsg);
			softAssert.assertAll();
		}
	}

	public void reportPass(String failureMsg){
		test.log(Status.PASS, failureMsg);//extent report failure
		takeScreenshot();
		if(proceedOnFail.equalsIgnoreCase("Y")){
			softAssert.fail(failureMsg);	
		}
		else{
			softAssert.fail(failureMsg);
			softAssert.assertAll();
		}
	}

	public void assertAll(){
		softAssert.assertAll();
	}

	public void takeScreenshot(){
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath+screenshotFile));
			test.log(Status.INFO,"Screenshot-> "+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+screenshotFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
