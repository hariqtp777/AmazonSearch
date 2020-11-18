package com.btb.hybrid.base;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.btb.hybrid.driver.DriverScript;
import com.btb.hybrid.report.ExtentManager;
import com.btb.hybrid.util.Constants;
import com.btb.hybrid.util.DataUtil;
import com.btb.hybrid.util.Xls_Reader;

public class BaseTest {
	public Properties envprop;
	public Properties genprop;
	public Xls_Reader xls;
	public String testCaseName;
	public DriverScript ds;
	public ExtentReports rep ;
	public ExtentTest test ;


	@BeforeTest
	public void init(){
		try {
		testCaseName = this.getClass().getSimpleName();
		envprop=new Properties();
		genprop=new Properties();
		FileInputStream fs;
		try {
			fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//env.properties");
			envprop.load(fs);
			String propName=envprop.getProperty("env");
			fs =  new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+propName+".properties");
			genprop.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//xls = new Xls_Reader(System.getProperty("user.dir") + "//TestScripts//"+Constants.FILE_NAME);
		String[] arr  = this.getClass().getPackage().getName().split("\\.");
		String suiteName = arr[arr.length-1];
		//xls = new Xls_Reader(genprop.getProperty(suiteName+"_xls"));
		//xls = new Xls_Reader(System.getProperty("user.dir") + "//TestScripts//"+Constants.FILE_NAME);
		xls = new Xls_Reader(System.getProperty("user.dir") + "//TestScripts//"+suiteName+".xlsx");
		ds = new DriverScript();
		ds.setEnvprop(envprop);
		ds.setGenprop(genprop);
		}
		catch(Exception e) {
			test.log(Status.FAIL, "Init Method Failed");
		}
	}

	@DataProvider
	public Object[][] getData(Method method){
		return DataUtil.getTestData(testCaseName, xls);
	}

	@BeforeMethod
	public void initReport(){
		
		//rep = ExtentManager.getInstance(envprop.getProperty("reportPath"));
		rep = ExtentManager.getInstance(Constants.REPORT_PATH);
		test = rep.createTest(testCaseName);
		ds.setExtentTest(test);
	}

	@AfterMethod
	public void quit(){
		if(ds!=null){
			ds.quit();
		}

		if(rep!=null){
			rep.flush();
		}
	}
}
