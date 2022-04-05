package week5.day1.assignment;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ServiceNowNewIncident extends ServiceNowProjectSpecificMethods{
	
	@Test(priority = 1, alwaysRun = true)
	public void createNewIncident() throws IOException, InterruptedException {
//		Step2: Enter username (Check for frame before entering the username)
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
		driver.findElement(By.id("user_name")).sendKeys("admin");
//		Step3: Enter password
		driver.findElement(By.id("user_password")).sendKeys("VR5f8EKhfLwi");
//		Step4: Click Login
		driver.findElement(By.xpath("//button[@id='sysverb_login']")).click();
		driver.switchTo().defaultContent();
//		Step5: Search “incident “ Filter Navigator
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys("incident");
		
//		Step6: Click “All”
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@id='filter']//following::span[text()='Incident'])[1]")));
		driver.findElement(By.xpath("(//span[text()='Incident']//following::div[text()='All'])[1]")).click();
//		Step7: Click New button
		driver.switchTo().frame("gsft_main");
		driver.findElement(By.xpath("//button[text()='New' and @id='sysverb_new']")).click();
//		Step8: Select a value for Caller and Enter value for short_description
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='New record']")));
		driver.findElement(By.id("lookup.incident.caller_id")).click();
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> list = new ArrayList<String>(windowHandles);
		driver.switchTo().window(list.get(1));
		WebElement firstValue = driver.findElement(By.xpath("(//a[@class='glide_ref_item_link'])[1]"));
		firstValue.click();
		driver.switchTo().window(list.get(0));
		driver.switchTo().frame(0);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("incident.short_description")));
		driver.findElement(By.id("incident.short_description")).sendKeys("checking service now");
//		Step9: Read the incident number and save it a variable
		String incidentNumber = driver.findElement(By.id("incident.number")).getAttribute("value");
		System.out.println("Incident number : "+incidentNumber);
//		Step10: Click on Submit button
		driver.findElement(By.id("sysverb_insert")).click();
//		Step 11: Search the same incident number in the next search screen as below
		WebElement selectListBox = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//select)[1]"));
		Select select = new Select(selectListBox);
		select.selectByVisibleText("Number");
		
		WebElement searchInc = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//input)[1]"));
		searchInc.sendKeys(incidentNumber,Keys.ENTER);
//		Step12: Verify the incident is created successful and take snapshot of the created incident
		
		WebElement incLink = driver.findElement(By.xpath("(//table[@id='incident_table']//a[@class='linked formlink'])[1]"));
		String incidentResultNumber = incLink.getText();
		Assert.assertEquals(incidentNumber, incidentResultNumber);
		
		incLink.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("incident.short_description")));
		
		File screenshot = driver.getScreenshotAs(OutputType.FILE);
		File image = new File("./images/serviceNow/"+incidentNumber+".jpg");
		FileUtils.copyFile(screenshot, image);
	}

	

}
