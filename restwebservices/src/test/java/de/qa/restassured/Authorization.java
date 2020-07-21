package de.qa.restassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.Test;

public class Authorization {

	@Test
	public void authorization(){
		RestAssured.baseURI = "http://restapi.demoqa.com/authentication/CheckForAuthentication";
		RequestSpecification request = RestAssured
										.given()
										.auth()
										.preemptive()
										.basic("ToolsQA", "TestPassword")
										.header("Content-Type", "application/json");
		Response response = request.get();
		
		System.out.println("Response code is "+ response.getStatusCode());
		System.out.println("Response body is "+response.asString());
		
	}
}
