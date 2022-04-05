package week5.day1.assignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServiceNowAssignIncident extends ServiceNowProjectSpecificMethods{
	
	@Test(priority = 3, dependsOnMethods = "week5.day1.assignment.ServiceNowUpdateIncident.updateIncident")
	public void assignIncident() throws IOException, InterruptedException {
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
		
		//search existing incident number
		driver.switchTo().frame("gsft_main");
		WebElement selectListBox = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//select)[1]"));
		Select select = new Select(selectListBox);
		select.selectByVisibleText("Number");
		
		WebElement searchInc = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//input)[1]"));
		searchInc.sendKeys("INC0010013",Keys.ENTER);

		WebElement incLink = driver.findElement(By.xpath("(//table[@id='incident_table']//a[@class='linked formlink'])[1]"));
		String incidentResultNumber = incLink.getText();
		incLink.click();
		//wait until page loads
		WebElement updateButton = driver.findElement(By.id("sysverb_update"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sysverb_update")));
		
		//screenshot before updating the assignment group
		File screenshot1 = driver.getScreenshotAs(OutputType.FILE);
		File imageBefore = new File("./images/serviceNow/"+incidentResultNumber+"_beforeAssignGroup"+".jpg");
		FileUtils.copyFile(screenshot1, imageBefore);
		
		//click search icon in assignment group
		driver.findElement(By.xpath("//button[@id='lookup.incident.assignment_group']")).click();
		
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> list = new ArrayList<String>(windowHandles);
		driver.switchTo().window(list.get(1));
		
		
		WebElement selectAssignGroupName = driver.findElement(By.xpath("(//div[@class='input-group']//select)[1]"));
		Select select2 = new Select(selectAssignGroupName);
		select2.selectByVisibleText("Name");
		
		WebElement inputAssignGroupName = driver.findElement(By.xpath("(//div[@class='input-group']//input)[1]"));
		inputAssignGroupName.sendKeys("Software",Keys.ENTER);
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sys_user_group_table")));
		driver.findElement(By.xpath("//a[text()='Software']")).click();
		driver.switchTo().window(list.get(0));
		driver.switchTo().frame(0);
		String assignGroup = driver.findElement(By.id("sys_display.incident.assignment_group")).getAttribute("value");
		Assert.assertEquals("Software", assignGroup);
		
		driver.findElement(By.id("activity-stream-textarea")).sendKeys("assigned group as software");
		
		//click on update button
		updateButton.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='incident_table']")));
		//search the updated incident
		WebElement selectListBox1 = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//select)[1]"));
		Select select1 = new Select(selectListBox1);
		select1.selectByVisibleText("Number");
		
		WebElement searchInc1 = driver.findElement(By.xpath("(//span[@id='incident_hide_search']//input)[1]"));
		searchInc1.sendKeys("INC0010013",Keys.ENTER);
	
		WebElement incLink1 = driver.findElement(By.xpath("(//table[@id='incident_table']//a[@class='linked formlink'])[1]"));
		incLink1.click();
		
		//screenshot after the updating the assignment group
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sysverb_update")));
		File screenshot2 = driver.getScreenshotAs(OutputType.FILE);
		File imageAfter = new File("./snaps/serviceNow/"+incidentResultNumber+"_afterAssignGroup"+".jpg");
		FileUtils.copyFile(screenshot2, imageAfter);
}
}
