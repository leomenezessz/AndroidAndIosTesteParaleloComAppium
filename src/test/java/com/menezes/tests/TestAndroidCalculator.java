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
import io.appium.java_client.android.AndroidDriver;

public class TestAndroidCalculator {
	
	private static CalculatorScreen calculatorScreen;
	private static AppiumDriver<MobileElement> appiumDriver;
	
	@BeforeClass
	public static void setup() throws MalformedURLException {
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    	capabilities.setCapability("app",new File("apps/app-android-calculator.apk"));
	    	capabilities.setCapability("platformName","Android" );
	    	capabilities.setCapability("deviceName","Android Emulator API 22");
	    	capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("disableAndroidWatchers" , true);
	    	appiumDriver = new AndroidDriver<MobileElement>(new URL("http://localhost:4444/wd/hub") , capabilities);
	    	calculatorScreen = new CalculatorScreen(appiumDriver);
	}
	
	@Test
	public void shouldSum() {
		calculatorScreen.fillFirstNumber("10").fillSecondNumber("10").buttonSum.click();
		assertTrue(calculatorScreen.operationResult().equals("20"));
	}
	
	@AfterClass
	public static void teardown() {
		calculatorScreen.quitDriver();
	}
}
