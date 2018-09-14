package iFramesDemo;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import lib.BrowserDriverUtility;
import lib.EmailWithAttachmentUtility;
import lib.ExtentReportUtility;
import lib.ScreenshotUtility;

public class IFrameHandling {
	WebDriver dr = BrowserDriverUtility.InvokeBrowser("webdriver.chrome.driver",
			"C:\\Chetan\\SeleniumSuite\\WebDrivers\\chromedriver.exe",
			System.getProperty("user.dir") + "\\iFramesHTML_Source.html");
	ExtentReports report = ExtentReportUtility.InvokeExtentReport();
	ExtentTest logger = report.createTest("File Upload Test");
	WebElement ele;
	String path1, path2, path3, path4;

	@BeforeTest
	public void InvokeBrowser() {
		try {
			path1 = ScreenshotUtility.CaptureScreenshot(dr, "1_MainPage");
			logger.pass("Main Page - Screenshot taken.", MediaEntityBuilder.createScreenCaptureFromPath(path1).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void IFrameHandle() {
		try {
			int total_frames = dr.findElements(By.tagName("iframe")).size();
			System.out.println("Total frames on page are: " + total_frames);
			
			// Switch to 1st Frame
			dr.switchTo().frame(0);
			ele = dr.findElement(By.xpath("//a[contains(text(),'Register')]"));
			JavascriptExecutor jse = (JavascriptExecutor) dr;
			jse.executeScript("arguments[0].scrollIntoView(true);", ele);
			path2 = ScreenshotUtility.CaptureScreenshot(dr, "2_EbayPage");
			logger.pass("Ebay Page - Screenshot taken.", MediaEntityBuilder.createScreenCaptureFromPath(path2).build());

			// Switch from 1st Frame to HTML Body before going to 2nd Frame
			dr.switchTo().defaultContent();

			// Switch to 2nd Frame
			dr.switchTo().frame(1);
			ele = dr.findElement(By.xpath("//input[@id='searchBox']"));
			jse.executeScript("arguments[0].scrollIntoView(true);", ele);
			ele.sendKeys("Passport");
			dr.findElement(By.xpath("//input[@id='search']")).click();
			path3 = ScreenshotUtility.CaptureScreenshot(dr, "3_PassportIndiaPage");
			logger.pass("www.passportingia.gov.in Page - Screenshot taken.",
					MediaEntityBuilder.createScreenCaptureFromPath(path3).build());

			// Switch from 2st Frame to HTML Body before going to 3nd Frame
			dr.switchTo().defaultContent();

			dr.switchTo().frame(2);
			ele = dr.findElement(By.xpath("//a[contains(text(),'Terms and conditions')]"));
			jse.executeScript("arguments[0].scrollIntoView(true);", ele);
			ele.click();
			path4 = ScreenshotUtility.CaptureScreenshot(dr, "4_cicPage");
			logger.pass("www.cic.gc.ca Page - Screenshot taken.",
					MediaEntityBuilder.createScreenCaptureFromPath(path4).build());
			Thread.sleep(2000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() {
		try {
			EmailWithAttachmentUtility.SendEmail("Test Case for IFrames Handling is Passed - File is uploaded successfully...!!!",
					"Congratulations...Bro!!!", path1, "Screenshot of Main page which is working fine...!!!",
					path2, "Screenshot of www.ebay.in page which is working fine...!!!",
					path3, "Screenshot of www.passportindia.gov.in page which is working fine...!!!",
					path4, "Screenshot of www.cic.gc.ca page which is working fine...!!!");
			report.flush();
			Thread.sleep(1000);
			dr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
