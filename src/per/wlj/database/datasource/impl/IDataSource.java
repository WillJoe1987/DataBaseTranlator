package per.wlj.database.datasource.impl;

import javax.sql.DataSource;

public interface IDataSource extends DataSource{

	public String getType();
	
	public void setType(String type);
	
	public String getDriverClassName();

	public void setDriverClassName(String driverClassName);

	public String getUrl();

	public void setUrl(String url) ;

	public String getUsername() ;

	public void setUsername(String username) ;

	public String getPassword() ;

	public void setPassword(String password) ;
	
}
