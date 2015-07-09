package per.wlj.database.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import per.wlj.database.beans.Table;
import per.wlj.database.source.impl.OracleDescripCommond;

public class OracleDataSource {
	
	String driverClassName = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@114.215.170.78:1521:orcl";
	String username = "crm";
	String password = "crm";

	public OracleDataSource() throws Exception{
		Class.forName(driverClassName);
	}
	
	public Connection getConnection() throws Exception{
		Class.forName(driverClassName);
		return DriverManager.getConnection(url, username, password);
	}
	
	public static void main(String[] args)  {
		
		
		
		OracleDataSource ods;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ods = new OracleDataSource();
		
			OracleDescripCommond odc = new OracleDescripCommond();
			
			String sql = odc.getTableDescribCommand();
			conn = ods.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			Table t = null;
			String templatesDir = "E:\\files\\st\\";
			StringTemplateGroup stg = new StringTemplateGroup("mysql",templatesDir);
			StringTemplate st1 = stg.getInstanceOf("Mysql");
			while(rs.next()){
				System.out.println(rs.getString("TABLE_NAME"));
				System.out.println(rs.getString("comments"));
				t = new Table();
				t.setName(rs.getString("TABLE_NAME"));
				t.setComment(rs.getString("comments"));
				st1 = stg.getInstanceOf("Mysql");
				st1.setAttribute("table", t);
				System.out.println(st1.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
}
