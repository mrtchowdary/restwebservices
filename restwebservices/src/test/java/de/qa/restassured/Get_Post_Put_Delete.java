package de.qa.restassured;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.qa.base.Base;


//Open command prompt
//change directory to D:\Api test setup
//now run command "json-server --watch db.json"
//URL is - http://localhost:3000/posts
public class Get_Post_Put_Delete extends Base{
	
	String line = "=========================================================================";
	
	@Test(enabled=false)
	public void sampleDelete(){
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		
		Response response = request.delete("http://localhost:3000/posts/8");
		
		System.out.println(line);
		System.out.println("Post request response is \n"+response.asString());
		System.out.println(line);
		Assert.assertEquals(response.getStatusCode(), 200, "Delete request is not successful");
		System.out.println("Response status code is "+response.getStatusCode());
		System.out.println(line);
	}
	
	@Test(enabled=false)
	public void samplePut(){
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		json.put("id", "3");
		json.put("title", "tit3");
		json.put("author", "aut3");

		request.body(json.toString());

		Response response = request.put("http://localhost:3000/posts/3");
		
		System.out.println(line);
		System.out.println("Post request response is \n"+response.asString());
		System.out.println(line);
		Assert.assertEquals(response.getStatusCode(), 200, "Put (Update) request is not successful");
		System.out.println("Response status code is "+response.getStatusCode());
		System.out.println(line);
	}
	
	@Test(enabled=false)
	public void samplePost(){
		
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		json.put("id", "8");
		json.put("title", "tit");
		json.put("author", "aut");

		request.body(json.toString());

		Response response = request.post("http://localhost:3000/posts");
		
		System.out.println(line);
		System.out.println("Post request response is \n"+response.asString());
		System.out.println(line);
		Assert.assertEquals(response.getStatusCode(), 201, "Post (Create) request is not successful");
		System.out.println("Response status code is "+response.getStatusCode());
		System.out.println(line);
	}
	
	@Test(enabled=false)
	public void sampleGet(){
		
		RestAssured.baseURI = "https://reqres.in";
		RequestSpecification request  = RestAssured.given();
		request.header("Content-Type", "application/json");
		
		//Get command
		Response response = request
				.when()
				.get("/api/users?page=2");
		
		System.out.println(line);
		Assert.assertEquals(response.getStatusCode(), 200, "Get (retrive) request is not successful");
		System.out.println("Response status code is "+response.getStatusCode());
		System.out.println(line);
		System.out.println("Response body is (using asString()) \n"+response.asString());
		System.out.println(line);
		System.out.println("Response content types (using contentType())\n"+response.contentType());
		System.out.println(line);
		System.out.println("Get content types (using getContentType())\n"+response.getContentType());
		System.out.println(line);
		System.out.println("Status line of response is \n"+response.getStatusLine());
		System.out.println(line);
		System.out.println("Response time in milli seconds (default) using getTime() is "+response.getTime());
		System.out.println(line);
		System.out.println("Response time using getTime(timeunits) is "+response.getTimeIn(TimeUnit.SECONDS));
		System.out.println(line);
		System.out.println("Response body printing in pretty format \n"+response.prettyPrint());
		System.out.println(line);
		System.out.println("Response cookies are \n"+response.cookies());
		System.out.println(line);
		System.out.println("Response headers are \n"+response.getHeaders());
		System.out.println(line);
		System.out.println("Records per page are "+response.path("per_page"));
		System.out.println(line);
		
		//to get the data of the page
		System.out.println("Users are \n");
		String jsonString = response.asString();
		List<Map<String, String>> records = JsonPath.from(jsonString).get("data");
		records.forEach(rec -> System.out.println(rec));
		System.out.println(line);
	}

	//Normal use of Rest Assured
//	@Test
//	public void Get(){
//		int statusCode = RestAssured.get(prop.GetDataFromConfig("endpoint")+prop.GetDataFromConfig("apiurl")).getStatusCode();
//		System.out.println("Status code is "+statusCode);
//		Assert.assertEquals(statusCode, 200);
//	}	
	
	@Test(enabled=false)
	public void openWeatherCurrentWeatherData(){
		RestAssured.baseURI = "http://api.openweathermap.org/data/2.5/weather";		//Current weather
		RequestSpecification request = RestAssured.given();
		request.contentType("application/json");
		request.param("q","kakinada");
		request.param("appid", "43b411d6f2697237d343d0d715b678ff");  //c498089bbd5181899d23e08e6b9486d2
		
		Response response = request.get("");
		System.out.println("Response code is : "+response.getStatusCode());
		System.out.println("Response Time is : "+response.getTime());
		System.out.println("Status Line"+response.statusLine().toString());
		System.out.println("Response body"+response.body().prettyPrint());
		System.out.println("Response cookies"+response.cookies().toString());
		
		Headers headers = response.getHeaders();
		for(Header header: headers){
			System.out.println(header);
		}
	}
	
	//lat=17.69, lon=83.2093
	@Test(enabled=true)
	public void openWeatherFiveDayForecast(){
		RestAssured.baseURI="http://api.openweathermap.org/data/2.5/forecast";
		
		RequestSpecification request = RestAssured.given();
		request.contentType("application/json");
		request.param("q", "visakhapatnam");
		request.param("appid", "43b411d6f2697237d343d0d715b678ff");
		
		Response response = request.get();
		
		System.out.println("Status code : "+response.getStatusCode());
		int surise = response.path("city.sunrise");
		Date date = new Date(surise);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
		System.out.println(df.format(date));
		
//		System.out.println("Users are \n");
		String jsonString = response.asString();
		Map<String, String> records = JsonPath.from(jsonString).get("city");
		records.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(System.out::println);
	}
}
