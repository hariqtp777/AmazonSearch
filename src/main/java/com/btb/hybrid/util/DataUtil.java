package com.btb.hybrid.util;

import java.util.Hashtable;

public class DataUtil {
	public static Object[][] getTestData(String testCaseName, Xls_Reader xls){
		int testStartRowCount = 1;
		while(!xls.getCellData(Constants.DATA_SHEET, 0, testStartRowCount).equals(testCaseName)){
			testStartRowCount++;
		}
		int colStartRowCount = testStartRowCount+1;
		int totalCols = 0;
		while(!xls.getCellData(Constants.DATA_SHEET, totalCols, colStartRowCount).equals("")){
			totalCols++;
		}
		int dataStartRowNum = testStartRowCount+2;
		int totalRows = 0;
		while(!xls.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum).equals("")){
			totalRows++;
			dataStartRowNum++;
		}
		dataStartRowNum = testStartRowCount+2;
		int finalRows = dataStartRowNum+totalRows;
		Hashtable<String, String> table=null;
		Object[][] myData = null;
		myData = new Object[totalRows][1];
		int i=0;
		for(int rNum=dataStartRowNum;rNum<finalRows;rNum++){
			table = new Hashtable<String, String>();
			for(int cNum=0;cNum<totalCols;cNum++){
				String dataVal=xls.getCellData(Constants.DATA_SHEET, cNum, rNum);
				String keyVal = xls.getCellData(Constants.DATA_SHEET, cNum, colStartRowCount);
				//System.out.println(keyVal+"------"+dataVal);
				table.put(keyVal, dataVal);
			}
			myData[i][0] = table;
			i++;
		}
		return myData;
	}
	//to check run mode of TC
	public static boolean isSkip(String testCaseName, Xls_Reader xls){
		int rows = xls.getRowCount(Constants.TESTCASE_SHEET);
		for(int rNum=2;rNum<rows;rNum++){
			String tcid = xls.getCellData(Constants.TESTCASE_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testCaseName)){
				String runmode = xls.getCellData(Constants.TESTCASE_SHEET, Constants.RUNMODE_COL, rNum);
				if(runmode.equals(Constants.RUNMODE_NO)){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return true;//in case test case not found

	}
}
