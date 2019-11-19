package com.tutoralninja.testscript;


import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.tutoralninja.helper.ExcelReader;
import com.tutoralninja.utility.BaseClass;
import com.tutoralninja.utility.Constants;
import com.tutoralninja.utility.ValidationHelper;

public class TutorialNinjaTestScript extends BaseClass {
public	WebElement addToCartElement,productLink,inputElement,cartElement;
public	String actualProductName,actualQuenty,actualPrice,actualExTax,actualTotalPrice,avalability,actualGrandTotal,description;
public	String cartProductList[][];



public void SearchProductAndAddToCart(String expectedProductName,String expectedQuenty,String expectedPrice,String expectedExTax,String expectedAvalability,String expectedDescription) throws Exception {
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.tutorialNinjaPropertiesFilePath);
	inputElement=helper.getElement(driver, property, "searchinput");
	inputElement.clear();
	inputElement.sendKeys(expectedProductName);	
	log.info(" searched product name: " +expectedProductName);
	helper.getElement(driver, property, "searchButton").click();
	
	pageManager.scrollAndViewAtTop(driver, helper.getElement(driver, property, "reference"));
	helper.getElement(driver, property, "clickonProduct").click();
	actualPrice=helper.getElement(driver, property, "priceofproduct").getText();
	actualExTax=helper.getElement(driver, property, "taxOfproduct").getText();
	avalability=helper.getElement(driver, property, "avalability").getText();	
	
	ValidationHelper.validate(actualPrice, expectedPrice,"price not matched");
	log.info("unit price is validated:passed ");
	
	helper.getElement(driver, property, "quentityinput").clear();
	ValidationHelper.validate(actualExTax, expectedExTax,"tax not matched");	
	log.info("tax validated "+expectedExTax);
	
	ValidationHelper.validate(avalability, expectedAvalability,"avalability not matched");
	log.info(avalability);
	
	description=helper.getElement(driver, property, "description").getText();
	Assert.assertEquals(description, expectedDescription,"description not matched");
	log.info("description validation passed");
	
	helper.getElement(driver, property, "quentityinput").sendKeys(expectedQuenty);	
	addToCartElement=helper.getElement(driver, property, "addToCartButton");	
	addToCartElement.click();
	log.info(expectedProductName+" product added to the cart");
}




public void validateCartItems() throws Exception {
	excelReader=new ExcelReader(Constants.cartProductListExcelPath);
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.tutorialNinjaPropertiesFilePath);
	addToCartElement=waitHelper.fluentWaitHelper(driver, "cart", "staleelementexception", property);//shopingcart
	addToCartElement=waitHelper.explictWaitHelper(driver, "shopingcart", "clickable", property);
	pageManager.movieToPerticularElement(driver, addToCartElement);	
	addToCartElement.click();
	
	//reading expected product values through excel and storing in String[][].
	cartProductList=ExcelReader.getUserData(Constants.cartProductListExcelPath);
	
	actualProductName=helper.getElement(driver, property, "cartproductname1").getText();
	actualPrice=helper.getElement(driver, property, "cartproductname1").getText();	
	actualTotalPrice=(helper.getElement(driver, property, "totalprice1")).getText();
	
	//1st product validation in the cart.	
	ValidationHelper.validate(actualProductName, cartProductList[1][0]);
	//String data=excelReader.getCellData("Sheet1", "Name", 2);
	
	log.info("product addrd to the cart is correct "+actualProductName);	
	ValidationHelper.validate(actualTotalPrice, cartProductList[1][3],"total price not matched ");	
	log.info("total price in the cart is correct "+actualTotalPrice);
	log.info("1st product validation passed "+actualProductName);
	
	
	//2nd product validation in the cart.			
	actualProductName=helper.getElement(driver, property, "cartproductname2").getText();
	actualPrice=helper.getElement(driver, property, "cartproductname2").getText();	
	actualTotalPrice=(helper.getElement(driver, property, "totalprice2")).getText();
	ValidationHelper.validate(actualProductName, cartProductList[2][0],"product name not matched "+actualProductName);
	log.info("product addrd to the cart is correct "+actualProductName);	
	
	ValidationHelper.validate(actualTotalPrice, cartProductList[2][3],"price not matched "+actualPrice);	
	log.info("total price in the cart is correct "+actualTotalPrice);
	log.info("2nd product validation in the cart passed "+actualProductName);
	
	//grand totoal validated in the cart.
	actualGrandTotal=helper.getElement(driver, property, "grandtotal").getText();
	ValidationHelper.validate(actualGrandTotal, cartProductList[1][4],"grand totalprice not matched "+actualGrandTotal);
	log.info("grand totalprice in the cart is correct "+actualGrandTotal);
}


public void ValidateCartAfterRemovingItem() throws Exception {
	pageManager.manageTimeOuts(driver);
	property=pageManager.loadpropertyFile(Constants.tutorialNinjaPropertiesFilePath);
	
	//removing  2nd item from cart
	helper.getElement(driver, property, "removebutton").click();
	log.info("product is removed from the cart");
	
	//clicking on shoping cart
	addToCartElement=waitHelper.fluentWaitHelper(driver, "shopingcart", "staleelementexception", property);
	//pageManager.movieToPerticularElement(driver, addToCartElement);	
	addToCartElement.click();
	
	actualProductName=helper.getElement(driver, property, "cartproductname1").getText();
	actualTotalPrice=(helper.getElement(driver, property, "totalprice1")).getText();
	cartProductList=ExcelReader.getUserData(Constants.cartProductListExcelPath);	
	ValidationHelper.validate(actualProductName, cartProductList[1][0],"product name not matched");
	log.info("product addrd to the cart is correct "+actualProductName);
	
	ValidationHelper.validate(actualTotalPrice, cartProductList[1][3],"actual price not matched");	
	log.info("total price in the cart is correct "+actualTotalPrice);	
	
	//grand total validated in the cart after removing a item from the cart.
	actualGrandTotal=helper.getElement(driver, property, "grandtotal").getText();
	
	ValidationHelper.validate(actualGrandTotal, cartProductList[2][4],"grand totalprice not matched "+actualGrandTotal);
	log.info("grand totalprice in the cart is correct "+actualGrandTotal);
	
	log.info("product validation passed after removed one product from the cart");
}

}
