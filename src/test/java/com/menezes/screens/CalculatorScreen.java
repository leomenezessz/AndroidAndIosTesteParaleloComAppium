package com.menezes.screens;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class CalculatorScreen {
	
	private AppiumDriver<MobileElement> driver;
	
	public CalculatorScreen(AppiumDriver<MobileElement> driver)  {
		PageFactory.initElements( new AppiumFieldDecorator(driver), this);
		this.driver = driver;
	}
	
	@AndroidFindBy(id = "android_button_sum")
	@iOSFindBy(accessibility = "apple-sum-button")
	public MobileElement buttonSum;
  
	@AndroidFindBy(id = "android_result_text")
	@iOSFindBy(accessibility = "apple_result_text")
	private MobileElement resultText;
  
	@AndroidFindBy(id = "android_field_first_number")
	@iOSFindBy(accessibility = "apple_first_input")
	private MobileElement inputFirstNumber;
	
	@AndroidFindBy(id = "android_field_second_number")
	@iOSFindBy(accessibility = "apple_second_input")
	private MobileElement inputSecondNumber;

	public CalculatorScreen fillFirstNumber(String number) {
		inputFirstNumber.clear();
		inputFirstNumber.sendKeys(number);
		return this;
	}
	
	public CalculatorScreen fillSecondNumber(String number) {
		inputSecondNumber.clear();
		inputSecondNumber.sendKeys(number);
		return this;
	}
	
	public CalculatorScreen closeKeyboard() {
		driver.hideKeyboard();
		return this;
	}
	
	public String operationResult() {
		return resultText.getText().toString().trim();
	}
	
	public void quitDriver() {
		driver.quit();
	}
}
