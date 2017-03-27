/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.timely.login;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Inject;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import controller.LoginController;
import utility.DateTimeUtility;


public class TestPageLoad {
    
	
	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {      
		System.setProperty("webdriver.gecko.driver","c:\\geckodriver.exe");
    	driver = new FirefoxDriver();
	}
	
	@After
	public void tearDown() throws Exception{
		 // Close the driver
        if(driver!=null) {
			driver.close();
		}
	}
 
	@Test
    public void navigate() throws Exception {
    	//System.setProperty("webdriver.gecko.driver","c:\\geckodriver.exe");
    	//WebDriver driver = new FirefoxDriver();
		
        //Launch the Online Store Website
		driver.get("http://localhost:8080/Timely/");
 
        // Print a Log In message to the screen
        System.out.println("Successfully opened the website timely");
 
    	
    }

}
