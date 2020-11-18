package com.btb.hybrid.keywords;

public class AppKeywords extends WebGenericKeywords {
	
	public void validateLogin(){
		String expetedRes = data.get(dataKey);
		String actualRes = "";
		
		boolean res = isElementPresent("searchBox_xpath");
		if(res){
			actualRes = "LoginSuccess";
		}
		else{
			actualRes = "LoginFailure";
		}
		
		if(!expetedRes.equals(actualRes)){
			reportFailure("Got the result as :"+actualRes);
		}
	}
}
