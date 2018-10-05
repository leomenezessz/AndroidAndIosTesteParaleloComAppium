package com.menezes.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.menezes.screens.CalculatorScreen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

public class TestIosCalculator {
	
	private static CalculatorScreen calculatorScreen;
	private static AppiumDriver<MobileElement> appiumDriver;
	
	@BeforeClass
	public static void setup() throws MalformedURLException {
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability("app",new File("apps/SimpleCalculator.app"));
	    capabilities.setCapability("deviceName", "iPhone SE");
	 	capabilities.setCapability("automationName" , "XCUITest");
		appiumDriver = new IOSDriver<MobileElement>(new URL("http://localhost:4444/wd/hub") , capabilities);
		calculatorScreen = new CalculatorScreen(appiumDriver);
	}
	
	@Test
	public void should_sum() {
		calculatorScreen.fillFirstNumber("10").fillSecondNumber("10").closeKeyboard().buttonSum.click();
		assertTrue(calculatorScreen.operationResult().equals("20"));
	}
	
	@AfterClass
	public static void teardown() {
		calculatorScreen.quitDriver();
	}
}
