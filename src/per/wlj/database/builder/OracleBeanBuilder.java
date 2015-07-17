package per.wlj.database.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.datasource.impl.DataSourceFactory;
import per.wlj.database.datasource.impl.IDataSource;
import per.wlj.database.source.impl.OracleDescripCommond;

public class OracleBeanBuilder implements IBeanBuilder {
	
	IDataSource ods;
	OracleDescripCommond odc;
	Database oracle = new Database();
	
	public OracleBeanBuilder(){
		this.ods = DataSourceFactory.getInstance().createDataSource("oracle");
		odc = new OracleDescripCommond();
	}
	
	public static List<String> nonLengthAble = new ArrayList<String>();
	static {
		nonLengthAble.add("DATE");
		nonLengthAble.add("TIMESTAMP");
		nonLengthAble.add("TIMESTAMP(6)");
		nonLengthAble.add("DATETIME");
		nonLengthAble.add("CLOB");
		nonLengthAble.add("LONG");
	}
	
	public static Map<String ,Integer> typeMapping = new HashMap<String ,Integer>();
	
	static {
		typeMapping.put("LONG", Column.TYPE_LONG);
		typeMapping.put("NCLOB", Column.TYPE_BLOB);
		typeMapping.put("NUMBER", Column.TYPE_FLOAT);
		typeMapping.put("NVARCHAR2", Column.TYPE_STRING);
		typeMapping.put("RAW", Column.TYPE_STRING);
		typeMapping.put("TIMESTAMP(6)", Column.TYPE_TIMESTAMP);
		typeMapping.put("VARCHAR2", Column.TYPE_STRING);
		typeMapping.put("BINARY_DOUBLE", Column.TYPE_FLOAT);
		typeMapping.put("BINARY_FLOAT", Column.TYPE_FLOAT);
		typeMapping.put("BLOB", Column.TYPE_BLOB);
		typeMapping.put("CLOB", Column.TYPE_CLOB);
		typeMapping.put("CHAR", Column.TYPE_CHAR);
		typeMapping.put("DATE", Column.TYPE_DATE);
	}
	
	
	@SuppressWarnings("finally")
	public List<Table> buildAllTables(){
		Connection conn = null;
		List<Table> tables = new ArrayList<Table>();		
		String allTables = odc.getTableDescribCommand();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = ods.getConnection();
			ps = conn.prepareStatement(allTables);
			rs = ps.executeQuery();
			int i = 0;
			while(rs.next() ){
				String tableName =  rs.getString("TABLE_NAME");
				System.out.println("BUILD THE【"+i+"】table,name:【"+tableName+"】");
				Table table = buildTableByName(tableName, conn);
				buildTableColumns(table, conn);
				tables.add(table);
				i++;
			}
		}catch(Exception e){
		}finally{
			return tables;
		}
	}
	
	public Table buildTableByName(String tableName){
		Connection conn = null;
		Table table = null;
		try{
			conn = ods.getConnection();
			table = buildTableByName(tableName, conn);
			buildTableColumns(table, conn);
			return table;
		}catch(Exception e){
			return null;
		}finally{
			if(null != conn)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	@SuppressWarnings("finally")
	private Table buildTableByName(String tableName, Connection con){
		String tableDescribe = odc.getTableDescribCommandByName(tableName);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Table table = null;
		try{
			ps = con.prepareStatement(tableDescribe);
			rs = ps.executeQuery();
			while(rs.next()){
				table = new Table();
				table.setName(rs.getString("TABLE_NAME"));
				table.setComment(rs.getString("COMMENTS"));
			}
			table.setDatabase(oracle);
		}catch(Exception e){
			e.printStackTrace();
			table = null;
		}finally{
			try {
				if(null!=rs) rs.close();
				if(null!=ps) ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				return table;
			}
		}
	}
	private void buildTableColumns(Table table, Connection con){
		String columnSql = odc.getColumnsByTableName(table.getName());
		String columnPK = odc.getPKColumnCommand(table.getName());
		PreparedStatement columnPS = null;
		PreparedStatement PKPS = null;
		ResultSet columnRS = null;
		ResultSet PKRS = null;
		try{
			columnPS = con.prepareStatement(columnSql);
			columnRS = columnPS.executeQuery();
			while(columnRS.next()){
				Column col = new Column();
				col.setName(columnRS.getString("COLUMN_NAME"));
				col.setType(columnRS.getString("DATA_TYPE"));
				col.setDataLength(columnRS.getInt("DATA_LENGTH"));
				col.setPrecision(columnRS.getInt("DATA_PRECISION"));
				col.setScale(columnRS.getInt("DATA_SCALE"));
				col.setNullable("Y".equals(columnRS.getString("NULLABLE")));
				col.setComment(columnRS.getString("COMMENTS"));				
				table.addColumn(col);
				preFixColumn(col);
			}
			PKPS = con.prepareStatement(columnPK);
			PKRS = PKPS.executeQuery();
			while(PKRS.next()){
				String pkColumn = PKRS.getString("COLUMN_NAME");
				Column pk = table.getColumn(pkColumn);
				if(null != pk) pk.setPK(true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(null != PKRS) PKRS.close();
				if(null != columnRS) columnRS.close();
				if(null != PKPS) PKPS.close();
				if(null != columnPS) columnPS.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void preFixColumn(Column col){
		String type = col.getType();
		int oriType = typeMapping.get(type);
		
		if(type.equals("NUMBER")){
			if(col.getScale() == 0){
				oriType=Column.TYPE_LONG;
			}else{
				oriType=Column.TYPE_FLOAT;
			}
		}
		col.setOriginType(oriType);
		if(nonLengthAble.indexOf(col.getType())<0){
			col.setLengthable(true);
		}
		if(col.getLengthable() && (col.getPrecision()>col.getDataLength())){
			col.setDataLength(col.getPrecision());
		}
	}
}
