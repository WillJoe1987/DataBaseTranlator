package per.wlj.database.data.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import per.wlj.database.datasource.impl.DTDataSource;
import per.wlj.database.datasource.impl.DataSourceFactory;
import per.wlj.database.datasource.impl.IDataSource;
import per.wlj.database.source.impl.OracleDescripCommond;
import per.wlj.files.Writer;

public class DataExporter {
	
	String dataFileName = "data.sql";
	String dataFilePath = "E:/files/";
	String[] exludeTables = {"ACRM_F_CI_DEP_TOP",
			"OCRM_F_WP_WORK_REPORT_COM",
			"OCRM_F_WP_WORK_REPORT_PER",
			"ACRM_F_CI_GRT_INFO"};
	int limit = 400;
	int start = 0;
	
	public void buildData(){
		Writer writer = new Writer();
		writer.setFileName(dataFilePath+dataFileName);
		OracleDescripCommond odc = new OracleDescripCommond();
		String allTable = odc.getTableDescribCommand();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			writer.createAndOpenFile();
			IDataSource ods = DataSourceFactory.getInstance().createDataSource("oracle");
			conn = ods.getConnection();
			ps = conn.prepareStatement(allTable);
			rs = ps.executeQuery();
			int index = 0;
			while(rs.next() &&  index < limit){
				if(!Arrays.asList(this.exludeTables).contains(rs.getString("TABLE_NAME")) && index >= start){
					writer.writeLine("-- --------------------------------------------------------------");
					writer.writeLine("-- index : "+index);
					String comment = rs.getString("COMMENTS");
					writer.writeLine("-- 导出表：【"+rs.getString("TABLE_NAME")+"】；（"+((null==comment)?null:comment.replace('\n', ' '))+"）");
					writer.writeLine("-- --------------------------------------------------------------");
					writer.writeLine("truncate table "+rs.getString("TABLE_NAME")+";");
					buildTable(rs.getString("TABLE_NAME"), conn, writer);
				}
				System.out.println("TABLE INDDEX 【"+index+"】");
				index ++ ;
			}
			writer.closeWriter();
			ps.close();
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void buildData(String tablename){
		Writer writer = new Writer();
		writer.setFileName(dataFilePath+tablename+".sql");
		Connection conn = null;
		try {
			writer.createAndOpenFile();
			writer.writeLine("truncate table "+tablename+";");
			DTDataSource ods = new DTDataSource();
			conn = ods.getConnection();
			DataExporter de = new DataExporter();
			de.buildTable(tablename, conn, writer);
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				writer.closeWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private void buildTable(String name, Connection conn, Writer writer){
		String sql = "SELECT * FROM "+name;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			insertBuilder(name, rs, writer);
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertBuilder (String tableName ,ResultSet rs , Writer writer){
		StringBuilder insertHead = new StringBuilder("INSERT INTO ");
		System.out.println("Start building table  【"+tableName+"】...");
		long start = System.currentTimeMillis();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			insertHead.append(tableName);
			insertHead.append("(");
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String columnName = rsmd.getColumnName(i);
				insertHead.append("`"+columnName+"`");
				if(i<rsmd.getColumnCount() )
					insertHead.append(", ");
			}
			insertHead.append(" ) values ");
			
			int lineNumber= 0;
			while(rs.next()){
				StringBuilder valuesSql = new StringBuilder("(");
				for(int i=1;i<=rsmd.getColumnCount();i++){
					int type = rsmd.getColumnType(i);
					switch (type) {
						case Types.ARRAY :
							valuesSql.append("null");
							break;
						case Types.BIGINT :
						case Types.INTEGER :
						case Types.TINYINT :
						case Types.LONGNVARCHAR :
						case Types.FLOAT : 
						case Types.NUMERIC : 
							valuesSql.append(rs.getString(i));
							break;
						case Types.BOOLEAN : 
							valuesSql.append(rs.getBoolean(i));
							break;
						case Types.DATE :
						case Types.TIME :
						case Types.TIMESTAMP :
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							Date valueDate= rs.getDate(i);
							if(null == valueDate){
								valuesSql.append("null");
							}else{
								valuesSql.append("'"+df.format(valueDate)+"'");
							}
							break;
						case Types.CHAR :
						case Types.VARCHAR : 
							String valueString = rs.getString(i);
							if(null == valueString) {
								valuesSql.append("null");
							}else{
								valuesSql.append("'"+valueString.replaceAll("'", "''")+"'");
							}
							break;
						default :
							valuesSql.append("null");
							break;
					}
					if(i<rsmd.getColumnCount() )
						valuesSql.append(", ");
				}
				valuesSql.append(" ); \r\n ");
				writer.writeLine(insertHead.toString()+valuesSql.toString());
				lineNumber ++;
			}
			long end = System.currentTimeMillis();
			long second = (end - start)/10;
			System.out.println("TABLE 【"+tableName+"】 HAS BEAN BUILT, WITH 【"+lineNumber+"】 LINES, used 【"+second+"】 seconds!");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getDataFilePath() {
		return dataFilePath;
	}

	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}	
	
}
