package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFuncrions.FunctionLibrary;
import utilites.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
	String inputpath="./FileInput/DataEnginee.xlsx";
	String outputpath="./FileOutput/HybridResult.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String TCSheet="MasterTestCases";

	public void startTest() throws Throwable
	{
		String Module_status="";
		String Module_new="";

		ExcelFileUtil xl = new ExcelFileUtil(inputpath);

		for(int i=1;i<=xl.rowCount(TCSheet);i++)
		{
			if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
				String TCModule=xl.getCellData(TCSheet, i, 1);
				report = new ExtentReports("./target/ExtentReports/"+TCModule+FunctionLibrary.generateDate()+".html");
				logger= report.startTest(TCModule);
				logger.assignAuthor("Jaydeep");
				for(int j=1;j<=xl.rowCount(TCModule);j++) 
				{
					String Description =xl.getCellData(TCModule, j, 0);
					String ObjectType =xl.getCellData(TCModule, j, 1);
					String Ltype=xl.getCellData(TCModule, j, 2);
					String Lvalue=xl.getCellData(TCModule, j, 3);
					String TestData=xl.getCellData(TCModule, j, 4);

					try {
						if(ObjectType.equalsIgnoreCase("startBrowser"))
						{
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captuserStock")) {
							FunctionLibrary.captuserStock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						
						if(ObjectType.equalsIgnoreCase("captSuppNum")) {
							FunctionLibrary.captSuppNum(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
							
						}
						if(ObjectType.equalsIgnoreCase("supplierTable")) {
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captCusNum")) {
							FunctionLibrary.captCusNum(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("custTable")) {
							FunctionLibrary.custTable();
							logger.log(LogStatus.INFO, Description);
						}

						xl.setCellData(TCModule, j, 5,"Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_status="True";
					}
					catch(Throwable t)
					{
						System.out.println(t.getMessage());
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_new="False";
						File screen =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/Screenshot/"+Description+FunctionLibrary.generateDate()+".png"));
					}
					if(Module_status.equalsIgnoreCase("True"))
					{
						xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
					}
					if(Module_new.equalsIgnoreCase("False"))
					{
						xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
					}
					report.endTest(logger);
					report.flush();
				}
			}
			else {
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}

}
