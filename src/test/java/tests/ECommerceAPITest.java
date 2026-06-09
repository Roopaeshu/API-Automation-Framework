package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;
import utils.BaseTest;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ECommerceAPITest extends BaseTest
{

	
		@Test
		public void loginTest()
		{
			test.info("Sending login request");
		//Login		
		LoginRequest loginRequest=new LoginRequest();
		loginRequest.setUserEmail("Deshu@gmail.com");
		loginRequest.setUserPassword("Hello123@");
		
		//SSL certificate : relaxedHTTPSValidation() -->invalid also its bypass
		
	LoginResponse loginResponse=given().relaxedHTTPSValidation().log().all()
			.spec(req)
			.body(loginRequest)
			.when().post("/api/ecom/auth/login")
			.then().log().all().extract().response().as(LoginResponse.class);
	
	token=loginResponse.getToken();
	 userId=loginResponse.getUserId();
	
	test.info("Token Recieved: " +token);
	test.info("UserId :"+ userId);
	
	Assert.assertNotNull(token);
    Assert.assertNotNull(userId);
	   
	     
		}
	     
	     
	   // --------Create Product------
		
		@Test(dependsOnMethods="loginTest")
		public void createProductTest() {
			
			test.info("Creating product with userId: "+userId);
	     RequestSpecification reqAddProduct=given().spec(getSpec())
	    		 .log().all()
	     .param("productName", "Laptop")
	     .param("productAddedBy", userId)
	     .param("productCategory","System")
	     .param("productSubCategory", "Software")
	     .param("productPrice", 15000).param("productDescription", "HP")
	     .param("productFor", "ALL")
	     .multiPart("productImage",new File("C:\\Users\\HP\\Postman\\files\\laptop.png"));
	     
	     String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
	     .then().log().all().extract().response().asString();
	     JsonPath js= new JsonPath(addProductResponse);
	      productId=js.get("productId");
	      test.info("Product created with ID : "+productId);
	     Assert.assertNotNull(productId);
	    
		}

	     //Create Order
	     @Test(dependsOnMethods="createProductTest()")
		public void createOrderTest() 
		{
	    	 //Before creating order
	    	 test.info("Creating product with Id: "+productId);
	     OrderDetail orderDetail=new OrderDetail();
	     
	     orderDetail.setCountry("India");
	     orderDetail.setProductOrderedId(productId);
	     
	     List<OrderDetail> orderDetailList=new ArrayList<OrderDetail>();
	     orderDetailList.add(orderDetail);     //list data that y we are try to store create one list and push all order detaillsit
	     
	     Orders orders=new Orders();
	     orders.setOrders(orderDetailList);
	     
	     //what values JSON sending 
	     try {
	    	 ObjectMapper mapper =new ObjectMapper();
	    	 System.out.println("Json Sending : "+mapper.writeValueAsString(orders));
	     }catch(Exception e)
	     {
	     e.printStackTrace();
	     }
	     String createOrderResponse=given().log().all().spec(getSpecJson())
	     .body(orders)
	     .when().post("/api/ecom/order/create-order")
	     .then().log().all().extract().response().asString();
	     
	      orderId=new JsonPath(createOrderResponse).getString("orders[0]");
	     test.info("Order created  with Id: "+orderId);
	      Assert.assertNotNull(orderId);
	     
		}
	     
	     
	     //View Order details
	     @Test(dependsOnMethods="createOrderTest()")
	     public void viewOrderTest()
	    	 {
	    test.info("Viewing Order with ID : "+orderId);
		 String orderDetailsResponse =given().spec(getSpecJson())
	            .param("id",orderId)
	            .when().get("/api/ecom/order/get-orders-details")
	            .then().log().all()
	            .extract().response().asString();
	    String message =new JsonPath(orderDetailsResponse).getString("message");
	    test.info("Order details fetched successfully");
	   
	    
	    	 }
	     
	     //Delete Order
	     
	     @Test(dependsOnMethods ="createProductTest()")
	    	 public void deleteProductTest()
	    	 {
	    	 test.info("Deleting order with Id : "+productId);
	     String deleteResponse =given().spec(getSpecJson())
	     .pathParam("productId", productId)
	     .when().delete("/api/ecom/product/delete-product/{productId}")
	     .then().log().all()
	     .extract().response().asString();
	     String message=new JsonPath(deleteResponse).getString("message");
	     
	     test.info("Product Deleted Successfully");
	     
	    	 }
	     
	      
	     
	


}
