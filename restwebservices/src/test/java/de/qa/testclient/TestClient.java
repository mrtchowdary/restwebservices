package de.qa.testclient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.qa.base.Base;
import de.qa.client.Client;
import de.qa.data.Users;
import de.qa.util.ReadJSON;

public class TestClient extends Base{
	
	String endPoint, apiUrl;
	Client client;
	CloseableHttpResponse response;
	
	@BeforeMethod
	public void SetUp(){
		endPoint = prop.GetDataFromConfig("endpoint");
		apiUrl = prop.GetDataFromConfig("apiurl");
	}
	
	@Test(priority = 1,enabled=false)
	public void GetApiWithoutHeader(){
		client = new Client();
		response = client.GetRequest(endPoint+apiUrl);
		
		//getting status code
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("Status code for url "+ endPoint+apiUrl+ " is "+ statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not 200");
		
		//getting JSON string
		String responseString = null;
		try {
			responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(responseString);
		
		JSONObject jsonResponse = new JSONObject(responseString);
		System.out.println("Response JSON from API --- "+jsonResponse);
		
		//getting content values
		String perPageCount = ReadJSON.getValueFromJSONPath(jsonResponse, "per_page");
		System.out.println("Per page count is --> "+perPageCount);
		
		//getting all headers
		Header[] headerArray = response.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for(Header h:headerArray){
			allHeaders.put(h.getName(), h.getValue());
		}
		System.out.println(allHeaders);
	}
	
	@Test(priority=2,enabled=false)
	public void GetApiWithHeader(){
		System.out.println("With header");
		client = new Client();
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		
		response = client.GetRequest(endPoint+apiUrl, headerMap);
		
		//getting status code
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("Status code for url "+ endPoint+apiUrl+ " is "+ statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not 200");
		
		//getting JSON string
		String responseString = null;
		try {
			responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(responseString);
		
		JSONObject jsonResponse = new JSONObject(responseString);
		System.out.println("Response JSON from API --- "+jsonResponse);
		
		//getting content values
		String perPageCount = ReadJSON.getValueFromJSONPath(jsonResponse, "per_page");
		System.out.println("Per page count is --> "+perPageCount);
		
		//getting all headers
		Header[] headerArray = response.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for(Header h:headerArray){
			allHeaders.put(h.getName(), h.getValue());
		}
		System.out.println(allHeaders);
	}
	
	@Test
	public void postAPITest(){
		client = new Client();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		
		//jackson API:
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader"); //expected users obejct
		
		//object to json file:
		try {
			mapper.writeValue(new File(".\\src\\main\\java\\de\\qa\\data\\users.json"), users);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//java object to json in String:
		String usersJsonString = null;
		try {
			usersJsonString = mapper.writeValueAsString(users);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(usersJsonString);
		
		response = client.post(endPoint+apiUrl, usersJsonString, headerMap); //call the API
		
		//validate response from API:
		//1. status code:
		int statusCode = response.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, 201);
		
		//2. JsonString:
		String responseString = null;
		try {
			responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("The response from API is:"+ responseJson);
		
		//json to java object:
		Users usersResObj = null;
		try {
			usersResObj = mapper.readValue(responseString, Users.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //actual users object
		System.out.println(usersResObj.getName());
		System.out.println(usersResObj.getJob());
		
		Assert.assertTrue(users.getName().equals(usersResObj.getName()));
		Assert.assertTrue(users.getJob().equals(usersResObj.getJob()));
		
		System.out.println(usersResObj.getId());
		System.out.println(usersResObj.getCreatedAt());
		
	}

}
