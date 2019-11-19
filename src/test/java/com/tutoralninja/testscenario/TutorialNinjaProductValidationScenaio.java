package com.tutoralninja.testscenario;

import java.io.IOException;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.tutoralninja.helper.DataProviderClass;
import com.tutoralninja.testscript.TutorialNinjaTestScript;
import com.tutoralninja.utility.Constants;

public class TutorialNinjaProductValidationScenaio  {
TutorialNinjaTestScript script=new TutorialNinjaTestScript();

/**
 * this method sets the specified browser,taking driverType from suite files.
 * @param browserType
 * @throws IOException
 */
@Parameters({"browserType"})
@BeforeTest
public void setBrowser(String browserType) throws IOException {
	script.openBrowser(browserType);
	
}



/**
 * this method launches specified URL taking URL from specific config properties file.
 * @throws IOException
 */
@Test(priority = 1)
public void setURL() throws IOException {
	script.launchURL(Constants.tutorialNinjaConfigPropertiesFilePath);
	
}

/**
 * 
 * @param productName
 * @param quenty
 * @param price
 * @param exTax
 * @param expectedAvalability
 * @param expectedDescription
 * @throws Exception
 */

@Test(priority=2,dataProvider = "productDetails",dataProviderClass = DataProviderClass.class)
public void scenario(String productName,String quenty,String price,String exTax,String expectedAvalability,String expectedDescription) throws Exception {
	script.SearchProductAndAddToCart(productName,quenty,price,exTax,expectedAvalability,expectedDescription);
}

/**
 * 
 * @throws Exception
 */
@Test(priority = 3)
public void validateItemsInCart() throws Exception {
	script.validateCartItems();
	script.ValidateCartAfterRemovingItem();
}
}
