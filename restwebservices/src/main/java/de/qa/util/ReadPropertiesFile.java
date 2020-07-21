package de.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import de.qa.base.Base;

public class ReadPropertiesFile extends Base{
	Properties prop;
	public ReadPropertiesFile(){
		prop = new Properties();
		File excel = new File("./src/main/java/de/qa/config/config.properties");
		try {
			FileInputStream fis = new FileInputStream(excel);
			prop.load(fis);
		} catch (Exception e) {
			System.out.println("Invalid data file");
		}
	}
	
	public String GetDataFromConfig(String key){
		return prop.getProperty(key);
	}
}
