package com.btb.hybrid.amazon;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.btb.hybrid.base.BaseTest;
import com.btb.hybrid.util.Constants;
import com.btb.hybrid.util.DataUtil;

public class AmazonSearch extends BaseTest{
	@Test(dataProvider="getData")
	public void searchInAmazon(Hashtable<String,String> data) throws Exception{
		try {
		test.log(Status.INFO, "Starting "+ testCaseName+" - Test");
		System.out.println("Running "+testCaseName+" Test");		
		if(DataUtil.isSkip(testCaseName, xls) || data.get(Constants.RUNMODE_COL).equalsIgnoreCase(Constants.RUNMODE_NO)){
			test.log(Status.SKIP, "RunMode Set to No - TC Skipped");
			throw new SkipException("RunMode Set to No");
		}
		ds.executeKeywords(testCaseName, xls, data);
		}
		catch(Exception e) {
			test.log(Status.INFO, "Script Execution Skipped");
		}
	}
}