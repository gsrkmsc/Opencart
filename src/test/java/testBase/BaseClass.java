package testBase;

import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {
	
    public static WebDriver driver;
    public Properties p;
    public Logger logger;   
	
    @BeforeClass(groups= {"Sanity","Regression","Master"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws Exception {
		
        // Loading config.properties file
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);
		
        logger = LogManager.getLogger(this.getClass());
		
        // ====== REMOTE ENVIRONMENT EXECUTION ======
        if(p.getProperty("execution_env").equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
				 
            // Operating System Setup
            if (os.equalsIgnoreCase("windows")) {
                capabilities.setPlatform(Platform.WINDOWS);
            } else if (os.equalsIgnoreCase("mac")) {
                capabilities.setPlatform(Platform.MAC);
            } else {
                System.out.println("No matching os found.");
                return;
            }
			     
            // Browser Setup
            switch(br.toLowerCase()) {
                case "chrome": capabilities.setBrowserName("chrome"); break;
                case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
                case "firefox": capabilities.setBrowserName("firefox"); break;
                default: System.out.println("No matching browser found."); return;
            }
            
            // Fixed: Cleaned up the malformed double "http://" url syntax error
            driver = new RemoteWebDriver(new URL("http://192.168.0.119:4444/"), capabilities);

        }
		
        // ====== LOCAL ENVIRONMENT EXECUTION ======
        if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
            switch(br.toLowerCase()) {
                case "chrome": driver = new ChromeDriver(); break;
                case "edge": driver = new EdgeDriver(); break;
                case "firefox": driver = new FirefoxDriver(); break;
                default: System.out.println("Invalid local browser name..."); return;
            }
        }
	
        // ====== COMMON DRIVER ACTIONS (Runs for BOTH Local & Remote contexts) ======
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get(p.getProperty("appURL")); 
            driver.manage().window().maximize();
        }
    }
    
    @AfterClass(groups= {"Sanity", "Regression", "Master"})
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
	
    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }
		
    public String randomNumber() {
        return RandomStringUtils.randomNumeric(5);
    }
		
    public String randomAlphaNumeric() {
        String generatedstring = RandomStringUtils.randomAlphabetic(5);
        String generatednumber = RandomStringUtils.randomNumeric(5);
        return (generatedstring + "@" + generatednumber);
    }
		
    public static String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        
        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);
        
        sourceFile.renameTo(targetFile);
        return targetFilePath;
    }
}
