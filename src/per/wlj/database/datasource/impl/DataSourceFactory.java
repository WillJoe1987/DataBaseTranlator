package per.wlj.database.datasource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import per.wlj.database.ui.MainUI;
import per.wlj.files.PropertiesReader;

public class DataSourceFactory {

	private static DataSourceFactory instance;
	
	public static DataSourceFactory getInstance(){
		if(null == instance){
			instance = new DataSourceFactory(new PropertiesReader().getProperties());
		}
		return instance;
	}

	Properties properties = null;

	protected Map<String ,IDataSource> DBconfigurations = new HashMap<String, IDataSource>();
	
	private DataSourceFactory(){
		//DataSourceFactory();
	}
	
	private DataSourceFactory(Properties properties){
		this.properties = properties;
	}
	
	public IDataSource createDataSource(){
		return createDataSource("oracle");
	}
	public IDataSource createDataSource(String type){
		if(this.DBconfigurations.keySet().contains(type)){
			return this.DBconfigurations.get(type);
		}
		MainUI.log("type = " + type);
		String driver = this.properties.getProperty(type+".driver");
		String url = this.properties.getProperty(type+".url");
		String username = this.properties.getProperty(type+".username");
		String password = this.properties.getProperty(type+".password");
		return  buildDataSource(type, driver, url, username, password);
	}
	
	public IDataSource createDataSource(String type, String driver, String url, String username, String password){
		IDataSource dataSource = null;
		if(this.DBconfigurations.keySet().contains(type)){
			dataSource = this.DBconfigurations.get(type);
		}else{
			dataSource = buildDataSource(type, driver, url, username, password);
		}
		return dataSource;
	}
	
	public IDataSource buildDataSource(String type, String driver, String url, String username, String password){
		IDataSource dataSource = new DTDataSource();
		dataSource.setType(type);
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.DBconfigurations.put(type, dataSource);
		return dataSource;
	}
	
}
