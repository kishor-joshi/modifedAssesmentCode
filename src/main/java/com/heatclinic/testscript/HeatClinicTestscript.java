package com.heatclinic.testscript;

import java.util.Iterator;

import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebElement;

import com.tutoralninja.helper.ExcelReader;
import com.tutoralninja.utility.BaseClass;
import com.tutoralninja.utility.Constants;
import com.tutoralninja.utility.ValidationHelper;
/**
 * HeatClinic Website test script.
 * @author kishor.joshi
 *
 */
public class HeatClinicTestscript extends BaseClass{
	String menuText,actualcontentText,expectedcontentText,parentWindowHandler,subWindowHandler,locator,
	actualColour,actualSize,actualProductname,actualperonalizeName,actualTotalPrice,unitPrice,updatedTotalPrice;
	WebElement menuElement,menElement,cartElement,totalPriceElement;
	String[][] menuContent;
	Set<String> handles ;
	Iterator<String> iterator;
	Properties userProperties;
	
	
/**
 * navigate to each menu and validate content inside the each menu.	
 * @throws Exception
 */
public void navigateToEachMenuAndValidate() throws Exception {
	
	menuContent=ExcelReader.getUserData(Constants.menuContentExcelPath);
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.heatClinicPropertiesFilePath);
	
	for(int index=1;index<menuContent.length;index++) {
		menuElement=helper.getElement(driver, property, "eachmenu", "###", index+"");
		menuText=menuElement.getText();
		log.info("clicking on "+menuText);
		menuElement.click();
		
		log.info("navigated to: "+menuText);
		locator=menuContent[index][1];
		
		actualcontentText=helper.getElement(driver, locator).getText();
		expectedcontentText=menuContent[index][2];
		ValidationHelper.validate(actualcontentText, expectedcontentText,"actualcontent text not matched");
		
		log.info("content validated in: "+menuText+" content is: "+actualcontentText);
		
	}
}
/**
 * 
 * @throws Exception
 */
public void validateContentInMensMerchandise() throws Exception {
	pageManager.manageTimeOuts(driver);
	menuContent=ExcelReader.getUserData(Constants.menuContentExcelPath);
	property=pageManager.loadpropertyFile(Constants.heatClinicPropertiesFilePath);
	
	menuElement=helper.getElement(driver, property, "MerchandiseLink");
	pageManager.movieToPerticularElement(driver, menuElement);
	menElement=helper.getElement(driver, property, "menLink");
	pageManager.movieToPerticularElement(driver, menElement);
	menElement.click();
	
	actualcontentText=helper.getElement(driver, property,"menContent").getText();
	expectedcontentText=property.getProperty("expectedcontentinMenLink");
	ValidationHelper.validate(actualcontentText, expectedcontentText,"content text not matched");
	log.info("content validated in:Mens. content is: "+actualcontentText);
	
	helper.getElement(driver, property, "buynow_button").click();
	
}
public void switchToProductWindowAndValidate() throws Exception {
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.heatClinicPropertiesFilePath);
	userProperties=pageManager.loadpropertyFile(Constants.dressDetailsPath);
	
	parentWindowHandler = driver.getWindowHandle(); // Store your parent window
	subWindowHandler = null;
	
	handles = driver.getWindowHandles(); // get all window handles		
	iterator = handles.iterator();
	while (iterator.hasNext()){
		
	    subWindowHandler = iterator.next();
	    helper.getElement(driver, property, "colourLink").click();
	    log.info("colour selected");
	    
	    helper.getElement(driver, property, "size_link").click();
	    log.info("size selected");
	    
	    helper.getElement(driver, property, "personalName_input").sendKeys(userProperties.getProperty("PersonalizedName"));
	    log.info("name sent");
	    
	    helper.getElement(driver, property, "buynowbutton").click();
	    log.info("product added to cart");
	    
	}
	driver.switchTo().window(subWindowHandler); // switch to popup window

	// Now you are in the popup window, perform necessary actions here

	driver.switchTo().window(parentWindowHandler);  // switch back to parent window 
	waitHelper.explictWaitHelper(driver, "cartLink", "clickable", property).click();
	log.info("navigated to cart");
}

public void ValidateCartItems() throws Exception {
	
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.heatClinicPropertiesFilePath);
	userProperties=pageManager.loadpropertyFile(Constants.dressDetailsPath);	
	
	   actualSize=  helper.getElement(driver, property, "sizeincart").getText();	  
	   actualperonalizeName= helper.getElement(driver, property, "personalnameincart").getText();
	   actualProductname= helper.getElement(driver, property, "productnameincart").getText();
	   actualColour= helper.getElement(driver, property, "colorinpart").getText();
	   
	   ValidationHelper.varifyProduct(actualSize,userProperties.getProperty("ShirtSize") );
	   log.info("size validated:passed "+actualSize);
	   
	   ValidationHelper.varifyProduct(actualperonalizeName,userProperties.getProperty("PersonalizedName") );
	   log.info("perosnalName validated:passed "+actualperonalizeName);
	   
	   ValidationHelper.varifyProduct(actualProductname,userProperties.getProperty("ITEMName") );
	   log.info("Item Name validated:passed "+actualProductname);
	   
	   ValidationHelper.varifyProduct(actualColour,userProperties.getProperty("ShirtColor") );
	   log.info("color validated:passed "+actualColour);	   
	   
	   actualTotalPrice= helper.getElement(driver, property, "totalprice").getText();
	   ValidationHelper.varifyProduct(actualTotalPrice,userProperties.getProperty("TotalPrice") );
	   log.info("total price validated:passed "+actualTotalPrice);
		 
		 unitPrice=helper.getElement(driver, property, "price").getText();
		 ValidationHelper.varifyProduct(unitPrice,userProperties.getProperty("Price") );
		 log.info("unit price validated:passed "+unitPrice);
		 
		 //updating quentity
		 helper.getElement(driver, property, "quentyinput").clear();
		 helper.getElement(driver, property, "quentyinput").sendKeys(userProperties.getProperty("increaseQuenty"));
		 helper.getElement(driver, property, "update").click();	   
		 log.info("quenty updated");
		 
		 
		 waitHelper.fluentWaitHelper(driver, "productimg","staleelementexception", property);
		 waitHelper.explictWaitHelper(driver, "productimg","visbility", property);
		 log.info("product image visible");
		
		
		 totalPriceElement=waitHelper.fluentWaitHelper(driver, "totalprice","staleelementexception", property);
		 updatedTotalPrice=totalPriceElement.getText();
		
		 ValidationHelper.validate(updatedTotalPrice,userProperties.getProperty("updatedTotalPrice"),"updated price not matched" );
		 log.info("updated total price validated:passed "+updatedTotalPrice);
	
}

}
