package testForEcommerse;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.testng.annotations.Test;

import excleReader.UtilityClass;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import pojo.GetterAndSetterForOrderClass;
import pojo.GetterAndSetterForOrderDetails;
import pojo.GetterAndSetterForTheUserNamee;

public class TestCaseForTheEcom{
	String tokenValue;
	String userIdValue;
	String messageValue;
	String productId;
	UtilityClass utility=new UtilityClass();

	

@Test(priority=1)
public void testForAPI() throws EncryptedDocumentException, IOException {
	 RequestSpecification req=new RequestSpecBuilder().setBaseUri(utility.excleReader("Sheet1",0,1)).setContentType(ContentType.JSON).build();
	 GetterAndSetterForTheUserNamee gs=new GetterAndSetterForTheUserNamee();
	 gs.setUserEmail(utility.excleReader("Sheet1",1,1));
	 gs.setUserPassword(utility.excleReader("Sheet1",2,1));
	 RequestSpecification request= given().spec(req).body(gs);
     ResponseSpecification  responce=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
     String data= request.when().post("/api/ecom/auth/login").then().spec(responce).extract().response().asString();
     
     JsonPath js=new JsonPath(data);
     tokenValue=js.get(utility.excleReader("Sheet1",3,1));
 
     userIdValue=js.get(utility.excleReader("Sheet1",4,1));
  
     
}
@Test(priority=2)
public void createProduct() throws EncryptedDocumentException, IOException {

	RequestSpecification resForAddProduct=new RequestSpecBuilder().setBaseUri(utility.excleReader("Sheet1",0,1)).addHeader("authorization",tokenValue)
			.build();
	RequestSpecification requestForAddProduct=given().spec(resForAddProduct).param("productName","Iphone").param("productAddedBy", userIdValue).
	param("productCategory","fashion").param("productSubCategory","shirts").param("productPrice","11500").
	param("productDescription","Addias Originals")
	.param("productFor","women")
	.multiPart("productImage",new java.io.File(System.getProperty("user.dir")+"//1280px-Mercedes_R121_190SL_(1960)_lVA_100kb.jpg"));
	 String responceForAddApi=requestForAddProduct.when().post("/api/ecom/product/add-product"). 
	then().log().all().extract().response().asString();
	 System.out.println(responceForAddApi);
	JsonPath js1=new JsonPath(responceForAddApi);
	productId=js1.get("productId");

}
@Test(priority=3)
public void placeOrder() throws EncryptedDocumentException, IOException {
	RequestSpecification resForAddProduct=new RequestSpecBuilder().setBaseUri(utility.excleReader("Sheet1",0,1)).addHeader("authorization",tokenValue)
			.build();
	 GetterAndSetterForOrderDetails og=new  GetterAndSetterForOrderDetails();
	og.setCountry("India");
	og.setProductOrderedId(productId);
	List<GetterAndSetterForOrderDetails> orderPlace=new ArrayList<GetterAndSetterForOrderDetails>();
	orderPlace.add(og);
	GetterAndSetterForOrderClass order=new  GetterAndSetterForOrderClass();
	 order.setOrders(orderPlace);
	RequestSpecification request=given().spec(resForAddProduct).body(order);
	String responce=request.when().post("/api/ecom/order/create-order").
	then().extract().response().asString();
	System.out.println(responce);
 
  
}
@Test(priority=4)
public void delete() throws EncryptedDocumentException, IOException {
	RequestSpecification resForAddProduct=new RequestSpecBuilder().setBaseUri(utility.excleReader("Sheet1",0,1)).addHeader("authorization",tokenValue)
			.build();
	RequestSpecification request=given().spec(resForAddProduct).pathParams("productId",productId);
	String responce=request.delete("/api/ecom/product/delete-product/{productId}").
	then().assertThat().statusCode(200).extract().response().asString();
//	System.out.println(responce);
}
}
