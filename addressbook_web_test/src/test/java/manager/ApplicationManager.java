package manager;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ApplicationManager {
    public static WebDriver driver;
    public LoginHelper session;
    public GroupHelper groups;

    public HelperBase helper;
    public ContactHelper contacts;
    public JdbcHelper jdbc;
    public HibernateHelper hbm;

    JavascriptExecutor js;
    private Map<String, Object> vars;

    private Properties properties;

    public void init(String browser, Properties properties) {
        this.properties = properties;
        if(driver == null) {
            if("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser %s", browser));
            }
            js = (JavascriptExecutor) driver;
            vars = new HashMap<String, Object>();
            driver.get(properties.getProperty("web.baseUrl"));
            driver.manage().window().setSize(new Dimension(1423, 993));
            session().login(properties.getProperty("web.username"), properties.getProperty("web.password"));
        }
    }

    public LoginHelper session() {
        if(session == null) {
            session = new LoginHelper(this);
        }
        return session;
    }

    public HelperBase helper() {
        if(helper == null) {
            helper = new HelperBase(this);
        }
        return helper;
    }

    public GroupHelper groups() {
        if(groups == null) {
            groups = new GroupHelper(this);
        }
        return groups;
    }

    public ContactHelper contacts() {
        if (contacts == null) {
            contacts = new ContactHelper(this);
        }
        return contacts;
    }

    public JdbcHelper jdbc() {
        if (jdbc == null) {
            jdbc = new JdbcHelper(this);
        }
        return jdbc;
    }

    public HibernateHelper hbm() {
        if (hbm == null) {
            hbm = new HibernateHelper(this);
        }
        return hbm;
    }

    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}
