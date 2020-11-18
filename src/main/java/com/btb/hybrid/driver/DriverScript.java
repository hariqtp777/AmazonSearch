package com.btb.hybrid.driver;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.btb.hybrid.keywords.WebGenericKeywords;
import com.btb.hybrid.util.Constants;
import com.btb.hybrid.util.Xls_Reader;

public class DriverScript {

	public Properties envprop;
	public Properties genprop;
	WebGenericKeywords app;
	public ExtentTest test;


	public void executeKeywords(String testCaseName, Xls_Reader xls, Hashtable<String, String> testData) throws Exception{
		try {
		int rowCount = xls.getRowCount(Constants.KEYWORDS_SHEET);
		app = new WebGenericKeywords();
		app.setEnvprop(envprop);
		app.setGenprop(genprop);
		app.setData(testData);
		app.setExtentTest(test);
		for (int rNum=2;rNum<=rowCount;rNum++){
			String tcid = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equalsIgnoreCase(testCaseName)){
				String descVal = null;
				String keyword = null;
				String objKey = null;
				String dataKey = null;
				String proceedOnFail = null;
				
				descVal = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DESRIPTION_COL, rNum);
				keyword = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				objKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.Object_COL, rNum);
				dataKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_SHEET, rNum);
				proceedOnFail = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.PROCEED_COL, rNum);
				//String data = testData.get(dataKey);
				app.setDataKey(dataKey);
				app.setObjKey(objKey);
				app.setDescKey(descVal);
				app.setProceedOnFail(proceedOnFail);

				Method method;
					method = app.getClass().getMethod(keyword);
					method.invoke(app);
			}
		}
		app.assertAll();
		}
		catch(Exception e) {
			test.log(Status.FAIL, "MisMatch in Data(Locator or Excel) : Data Read Failed");
		}
	}

	public void quit(){
		if(app!=null){
			app.quit();
		}
	}

	public Properties getEnvprop() {
		return envprop;
	}


	public void setEnvprop(Properties envprop) {
		this.envprop = envprop;
	}


	public Properties getGenprop() {
		return genprop;
	}


	public void setGenprop(Properties genprop) {
		this.genprop = genprop;
	}

	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}
}
