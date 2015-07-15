package per.wlj.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	
	private static Properties properties;
	
	public Properties getProperties(){
		if(properties == null){
			InputStream is  = ClassLoader.getSystemResourceAsStream("util.properties");
			properties = new Properties();
			try {
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

}
