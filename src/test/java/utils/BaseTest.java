package utils;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
	
	public static String token;
	public static String userId;
	public static String productId;
	public static String orderId;
	
	//Extent Report variables
	public static ExtentReports extentreports;
	public static ExtentTest test;
	
	//Specs
	
	//Base URL spec -- for login
	public static RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
			.setContentType(ContentType.JSON).build();
	
//	//Add product 
//	public static  RequestSpecification addProductBaseReq() 
//	{
//		return new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
//			.addHeader("Authorization",token)
//			.build();
//	}
	
	//Create product 
		public static  RequestSpecification getSpec() 
		{
			return new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				
				.addHeader("Authorization",token)
				.build();
		}
		
		//View Order
		public static  RequestSpecification getSpecJson() 
		{
			return new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
					.setContentType(ContentType.JSON)
				.addHeader("Authorization",token)
				.build();
		}
//		
//		//Delete Order
//		public static  RequestSpecification deletOrderBaseReq() 
//		{
//			return new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
//					.setContentType(ContentType.JSON)
//				.addHeader("Authorization",token)
//				.build();
//		}
		
		
		
		//runs before all test
	
		
		@BeforeSuite
		public void beforeSuite() {
			extentreports =ExtentRepoortManager.getInstance();
		}
		
		//runs before every test method
		@BeforeMethod
		public void beforeMethod(java.lang.reflect.Method method)
		{
			test=extentreports.createTest(method.getName());
			test.info("Starting test : " + method.getName());
		}
		
		//runs after each test method 
		@AfterMethod
		public void afterMethods(ITestResult result)
		{
			if(result.getStatus() == ITestResult.SUCCESS)
				test.pass("Test Passed");
			else if(result.getStatus() == ITestResult.FAILURE)
				test.fail("Test Failed: "+result.getThrowable());
			else if(result.getStatus() == ITestResult.SKIP)
				test.skip("Test Skipped : "+ result.getName());
		}
		
		//runs once after all test-- saves report 
		@AfterSuite
		public void afterSuite() {
			System.out.println("After Suite Excetued ");
			extentreports.flush(); //without this report will be empty after adding all test data wriiten in html file 
			System.out.println("Report Saved at : "+System.getProperty("user.dir")
			+"/reports/ExtentReport.html");
			
			
		}

}
