package Base;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.openqa.selenium.By;
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

	static WebDriver d;
	boolean present;
	int time;
	static String id;
	static String pass;

	@Parameters({ "employeeId", "password" })
	@BeforeClass
	public void d(@Optional("762") String employeeId, @Optional("prem2180%") String password) {
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
		LocalDateTime dd = LocalDateTime.now();
		DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern("MMMM yyyy");
		DateTimeFormatter sdf3 = DateTimeFormatter.ofPattern("HHmm");
		DateTimeFormatter sdf2 = DateTimeFormatter.ofPattern("MM");
		// To check if it is Sunday or Saturday if it is WH then else will execute
		if (dd.getDayOfWeek().getValue() == 0 || dd.getDayOfWeek().getValue() == 6) {
			d.quit();
			System.exit(0);
		} else {
			// This will loop until the employeeId are over
			d.findElement(By.id("txtDomain")).sendKeys("Apalya");
			d.findElement(By.id("txtUserName")).sendKeys(id);
			d.findElement(By.id("txtPassword")).sendKeys(pass);
			d.findElement(By.id("btnLogin")).click();
	// To check the button is swiped in or swiped out if it is already swiped in then if will execute
			Thread.sleep(10000);
			if (d.findElement(By.xpath("//*[@ng-click='applAttendance()'][1]")).getAttribute("class")
					.equals("newpaytimein ng-binding ng-hide")) { // LOgout code
				String lastSwiped = d.findElement(By.xpath("//*[@ng-if='lastSwipedStatus']//p")).getText();
				String[] lastSwipedSplited = lastSwiped.split("-");
				String time2 = lastSwipedSplited[1].replace(" ", "").replace(":", "");
				int time3 = Integer.parseInt(time2);
				time = time3 + 900;
				int presentTime = Integer.parseInt(sdf3.format(dd));
		// This will check if the 9h are completed are not if 9h are completed then if will execute
				if (time <= presentTime) {
					d.findElement(By.xpath("//*[@ng-click='applAttendance()'][2]")).click();
				} else {
					Random randomGenerator = new Random();
					int num = randomGenerator.nextInt(60) + 1;
					long pause = (long) ((time - presentTime) + num);
					System.out.println("Time paused: "+ pause * 60 * 1000);
					Thread.sleep(pause * 60 * 1000);
					d.findElement(By.xpath("//*[@ng-click='applAttendance()'][2]")).click();
				}
			} else { // Login code
				d.findElement(By.xpath("//div[3]/div/div/div[2]//td[1]")).click();
				d.findElement(By.xpath("//*[text()='Calendar View']")).click();
				String mouth = d.findElement(By.xpath("//*[@id='calendar']//h2")).getText();
				if (mouth.equals(sdf1.format(dd))) {
					try {
						d.findElement(By.xpath("//*[@class='fc-content']//*[text()='A']"));
						present = true;
					} catch (NoSuchElementException e) {
						present = false;
					}
				} else {
					String calender = d.findElement(By.xpath("//*[@id='calendar']//div[3]/div[1]//td[4]"))
							.getAttribute("data-date");
					String[] calenderSplit = calender.split("-");
					int calenderMounth = Integer.parseInt(calenderSplit[1]);
					int presentMonth = Integer.parseInt(sdf2.format(dd));
					while (true) {
						if (presentMonth > calenderMounth) {
							d.findElement(By.xpath("//*[@class='fc-right']/button")).click();
							calender = d.findElement(By.xpath("//*[@id='calendar']//div[3]/div[1]//td[4]"))
									.getAttribute("data-date");
							calenderSplit = calender.split("-");
							calenderMounth = Integer.parseInt(calenderSplit[1]);
							if (presentMonth == calenderMounth) {
								try {
									d.findElement(By.xpath("//*[@class='fc-content']//*[text()='A']"));
									present = true;
									break;
								} catch (NoSuchElementException e) {
									break;
								}
							}
						} else if (presentMonth < calenderMounth) {
							d.findElement(By.xpath("//*[@class='fc-left']/button")).click();
							calender = d.findElement(By.xpath("//*[@id='calendar']//div[3]/div[1]//td[4]"))
									.getAttribute("data-date");
							calenderSplit = calender.split("-");
							calenderMounth = Integer.parseInt(calenderSplit[1]);
							if (presentMonth == calenderMounth) {
								try {
									d.findElement(By.xpath("//*[@class='fc-content']//*[text()='A']"));
									present = true;
									break;
								} catch (NoSuchElementException e) {
									break;
								}
							}
						} else {
							try {
								d.findElement(By.xpath("//*[@class='fc-content']//*[text()='A']"));
								present = true;
								break;
							} catch (NoSuchElementException e) {
								break;
							}
						}

					}
				}
				if (present == true) {
					d.findElement(By.xpath("//*[@ng-click='switchversion()']")).click();
					Random randomGenerator = new Random();
					int time = randomGenerator.nextInt(60) + 1;
					time = time * 60000;
					Thread.sleep(time);
					d.findElement(By.xpath("//*[@ng-click='applAttendance()'][1]")).click();
				}
			}
		}

	}

	@AfterClass
	public void exit() {
		d.quit();
	}
	
	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup();
		d = new ChromeDriver();
		d.get("https://apps.paybooks.in/mylogin.aspx");
		d.manage().deleteAllCookies();
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
		id = "762";
		pass = "prem2180%";
		d.findElement(By.id("txtDomain")).sendKeys("Apalya");
		d.findElement(By.id("txtUserName")).sendKeys(id);
		d.findElement(By.id("txtPassword")).sendKeys(pass);
		d.findElement(By.id("btnLogin")).click();
		d.findElement(By.id("menu1")).click();
		d.findElement(By.xpath("//*[@id='topNavigation']//a[@ng-click='logout()']//span")).click();
	}
}
