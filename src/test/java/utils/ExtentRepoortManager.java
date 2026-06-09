package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentRepoortManager {
	
	public static ExtentReports getInstance() {
	
	//where report will be saved
	ExtentSparkReporter report=new ExtentSparkReporter(
			//System.getProperty("user dir") -->project root folder 
			//"/reports/ExtentReports.html" -->report file location -->it will be autocreated
			System.getProperty("user.dir")+"/reports/ExtentReports.html");
	System.out.println("report path " +	System.getProperty("user.dir")+"/reports/ExtentReports.html");
	 
	//report setting 
	report.config().setTheme(Theme.STANDARD);
	report.config().setDocumentTitle("API Test Results");
	report.config().setReportName("ECommerce API Test");
	
	ExtentReports extentreports=new ExtentReports();   // main report object create 
	extentreports.attachReporter(report);
	//System info shown in report
	extentreports.setSystemInfo("Tester", "Roopa");
	extentreports.setSystemInfo("Environment", "QA");
	extentreports.setSystemInfo("Application", "ECommerce API Test");
	
	
	return extentreports ;
	
	
	 
	}

}
