package commonFuncrions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;

	public static WebDriver startBrowser() throws Throwable
	{
		conpro=new Properties();

		conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));

		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome")){
			driver=new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("FireFox"))

		{
			driver=new FirefoxDriver();
		}
		else {
			Reporter.log("Browser value is not matching",true);
		}
		return driver;

	}

	public static void openUrl() {
		driver.get(conpro.getProperty("Url"));
	}

	public static void waitForElement(String LocatorType,String LocatorValue,String TestData) {
		WebDriverWait mywait= new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("id")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}

		if(LocatorType.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath")) {
			mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(LocatorValue)));
		}
	}

	public static void typeAction(String LocatorType,String LocatorValue,String TestData) {
		if(LocatorType.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);

		}
		if(LocatorType.equalsIgnoreCase("xpath")) 
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
	}
	public static void clickAction (String LocatorType,String LocatorValue) {
		if(LocatorType.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	public static void validateTitle(String Expected_Title)  {
		String Actual_Title=driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title,"Title is not matching");
		}
		catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	}


	public static void closeBrowser() {
		driver.quit();
	}
	public static String generateDate() {

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_DD  hh_mm_ss");
		return df.format(date);

	}

	public static void dropDownAction(String LocatorType,String LocatorValues,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("id")) {
			int value=Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocatorValues)));
			element.selectByIndex(value);
		}

		if(LocatorType.equalsIgnoreCase("name")) {
			int value=Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocatorValues)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value=Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValues)));
			element.selectByIndex(value);
		}
	}


	public static void captuserStock(String LocatorType,String LocatorValues) throws Throwable {
		String StockNum= "";

		if(LocatorType.equalsIgnoreCase("name")) {
			StockNum=driver.findElement(By.name(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			StockNum=driver.findElement(By.id(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath")) {
			StockNum=driver.findElement(By.xpath(LocatorValues)).getAttribute("value");

		}
		FileWriter fw= new FileWriter("./CaptureData/stockNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StockNum);
		bw.flush();
		bw.close();
	}

	public static void stockTable(String LocatorType,String LocatorValues) throws Throwable {
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data=br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-pannel"))).click();
		
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data,"Stock number should not match");
		}
		catch (AssertionError a) {
			Reporter.log(a.getMessage(),true);
		}

	}
	public static void captSuppNum(String LocatorType,String LocatorValues)throws Throwable {
		String SuppNum= "";

		if(LocatorType.equalsIgnoreCase("name")) {
			SuppNum=driver.findElement(By.name(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			SuppNum=driver.findElement(By.id(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath")) {
			SuppNum=driver.findElement(By.xpath(LocatorValues)).getAttribute("value");

		}
		FileWriter fw= new FileWriter("./CaptureData/suppliersNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(SuppNum);
		bw.flush();
		bw.close();

	}

	public static void supplierTable() throws Throwable{

		FileReader fr = new FileReader("./CaptureData/suppliersNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data =br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-pannel"))).click();
		
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();	
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data+"        "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data,"Supplier number Should not Match");
		}catch(AssertionError a)
		{
			Reporter.log(a.getMessage(),true);
		}
	}

	public static void captCusNum(String LocatorType,String LocatorValues) throws Throwable {
		String CustomerNum= "";

		if(LocatorType.equalsIgnoreCase("name")) {
			CustomerNum=driver.findElement(By.name(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			CustomerNum=driver.findElement(By.id(LocatorValues)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath")) {
			CustomerNum=driver.findElement(By.xpath(LocatorValues)).getAttribute("value");

		}
		FileWriter fw= new FileWriter("./CaptureData/customerNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(CustomerNum);
		bw.flush();
		bw.close();

	}

	public static void custTable() throws Throwable {
		FileReader fr = new FileReader("./CaptureData/customerNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data=br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-pannel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+Exp_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data,"Customer number should not match");
		}
		catch (AssertionError a) {
			Reporter.log(a.getMessage(),true);


		}
	}
}

