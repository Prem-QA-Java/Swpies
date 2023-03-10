package Base;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class runner {

	WebDriver d;
	boolean present;
	int time;
	String id;
	String pass;

	@Parameters({ "employeeId", "password" })
	@BeforeClass
	public void d(@Optional("762") String employeeId,@Optional("prem2180%") String password) {
		WebDriverManager.chromedriver().setup();
		d = new ChromeDriver();
		d.get("https://apps.paybooks.in/mylogin.aspx");
		d.manage().deleteAllCookies();
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
		id = employeeId;
		pass = password;
	}

	@Test
	public void login() throws InterruptedException {
		Date dd = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM yyyy");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HHmm");
		// To check if it is Sunday or Saturday if it is WH then else will execute
		if (dd.getDay() == 0 || dd.getDay() == 6) {
			System.exit(0);
		} else {
			// This will loop until the employeeId are over
			d.findElement(By.id("txtDomain")).sendKeys("Apalya");
			d.findElement(By.id("txtUserName")).sendKeys(id);
			d.findElement(By.id("txtPassword")).sendKeys(pass);
			d.findElement(By.id("btnLogin")).click();
			// To check the button is swiped in or swiped out if it is already swiped in ten
			// if will execute
			if (d.findElement(By.xpath("//*[@ng-click='applAttendance()'][1]")).getAttribute("class")
					.contains("newpaytimein ng-binding ng-hide")) {
				String lastSwiped = d.findElement(By.xpath("//*[@ng-if='lastSwipedStatus']//p")).getText();
				String[] lastSwipedSplited = lastSwiped.split("-");
				String time2 = lastSwipedSplited[1].replace(" ", "").replace(":", "");
				int time3 = Integer.parseInt(time2);
				time = time3 + 900;
				System.out.println("Login time: " + time3);
				System.out.println("Added 9 hours to login time: " + time);
				int presentTime = Integer.parseInt(sdf3.format(dd));
				System.out.println("Present time: "+presentTime);
				// This will check if the 9h are completed are not if 9h are completed then if
				// will execute
				if (time <= presentTime) {
						d.findElement(By.xpath("//*[@ng-click='applAttendance()'][2]")).click();
				} else {
					Random randomGenerator = new Random();
					int num = randomGenerator.nextInt(60) + 1;
					long pause = (long) ((time - presentTime) + num);
					System.out.println("Random genarated number: " + num);
					System.out.println("Time to pause: " + pause);
					TimeUnit.HOURS.sleep(pause);
					d.findElement(By.xpath("//*[@ng-click='applAttendance()'][2]")).click();
					d.findElement(By.id("menu1")).click();
					d.findElement(By.xpath("//*[@id='topNavigation']//a[@ng-click='logout()']//span")).click();
				}
			} else {
				d.findElement(By.xpath(
						"//*[@id='abc1']/div/div/div/div[1]/div/div[1]/div[2]/div[3]/div/div/div[2]/table/tbody/tr/td[1]"))
						.click();
				d.findElement(By.xpath("//*[text()='Calendar View']")).click();
				String mouth = d.findElement(By.xpath("//*[@id='calendar']//h2")).getText();
				System.out.println(mouth);
				System.out.println(sdf1.format(dd));
				if (mouth.equals(sdf1.format(dd))) {
					try {
						d.findElement(By.xpath("//*[@class='fc-content']//*[text()='A']"));
						present = true;
					} catch (NoSuchElementException e) {
						present = false;
					}
				} else {
					try {
						d.findElement(By.xpath("//*[@class='fc-right']/button")).click();
						if (mouth.equals(sdf1.format(dd))) {
							present = true;
						}
					} catch (ElementClickInterceptedException e) {
						d.findElement(By.xpath("//*[@class='fc-left']/button")).click();
					}
				}
				if (present == true) {
					d.findElement(By.xpath("//*[@ng-click='switchversion()']")).click();
					Random randomGenerator = new Random();
					int time = randomGenerator.nextInt(60) + 1;
					time = time * 1000;
					System.out.println(time);
					Thread.sleep(time);
					int presentTime = Integer.parseInt(sdf3.format(dd));
					System.out.println("present" + presentTime);
					d.findElement(By.xpath("//*[@ng-click='applAttendance()'][1]")).click();
					d.findElement(By.id("menu1")).click();
					d.findElement(By.xpath("//*[@id='topNavigation']//a[@ng-click='logout()']//span")).click();
				}
			}
		}

	}

	@AfterClass
	public void exit() {
		d.close();
	}
}
