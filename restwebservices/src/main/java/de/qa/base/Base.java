package de.qa.base;

import org.testng.annotations.BeforeMethod;

import de.qa.util.ReadPropertiesFile;

public class Base {
	public ReadPropertiesFile prop;
	
	@BeforeMethod
	public void setUp(){
		prop = new ReadPropertiesFile();
	}

}
