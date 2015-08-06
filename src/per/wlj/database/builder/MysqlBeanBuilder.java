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
import per.wlj.database.source.impl.MysqlDescripCommand;
import per.wlj.database.ui.MainUI;

public class MysqlBeanBuilder implements IBeanBuilder {
	
	
	public static final String MYSQL_YES = "YES";
	public static final String MYSQL_NO = "NO";
	public static final String MYSQL_NULL = "NULL";
	public static final String MYSQL_KEY_FLAG = "PRI";
	
	public static List<String> nonLengthAble = new ArrayList<String>();
	static {
		nonLengthAble.add("date");
		nonLengthAble.add("timestamp");
		nonLengthAble.add("datetime");
		nonLengthAble.add("clob");
		nonLengthAble.add("long");
		nonLengthAble.add("blob");
	}
	
	public static Map<String ,Integer> typeMapping = new HashMap<String ,Integer>();
	
	static {
		typeMapping.put("tinyint",Column.TYPE_INTEGER);
		typeMapping.put("smallint",Column.TYPE_INTEGER);
		typeMapping.put("mediumint",Column.TYPE_INTEGER);
		typeMapping.put("int",Column.TYPE_INTEGER);
		typeMapping.put("bigint",Column.TYPE_LONG);
		typeMapping.put("bit",Column.TYPE_INTEGER);
		typeMapping.put("double",Column.TYPE_FLOAT);
		typeMapping.put("float",Column.TYPE_FLOAT);
		typeMapping.put("decimal",Column.TYPE_FLOAT);
		typeMapping.put("char",Column.TYPE_CHAR); 	
		typeMapping.put("varchar",Column.TYPE_STRING);
		typeMapping.put("date",Column.TYPE_DATE);
//		typeMapping.put("time",Column.TYPE_DATE);
//		typeMapping.put("year",Column.TYPE_LONG);
		typeMapping.put("timestamp",Column.TYPE_TIMESTAMP);
		typeMapping.put("datetime",Column.TYPE_DATE);
		typeMapping.put("tinyblob",Column.TYPE_BLOB);
		typeMapping.put("blob",Column.TYPE_BLOB);
		typeMapping.put("mediumblob",Column.TYPE_BLOB);
		typeMapping.put("longblob",Column.TYPE_BLOB);
		typeMapping.put("tinytext",Column.TYPE_CLOB);
		typeMapping.put("text",Column.TYPE_CLOB);
		typeMapping.put("mediumtext",Column.TYPE_CLOB);
		typeMapping.put("longtext",Column.TYPE_CLOB);
//		typeMapping.put("enum",Column.TYPE_LONG);
//		typeMapping.put("set",Column.TYPE_LONG);
//		typeMapping.put("binary",Column.TYPE_LONG);
//		typeMapping.put("varbinary",Column.TYPE_LONG);
//		typeMapping.put("point",Column.TYPE_LONG);
		typeMapping.put("linestring",Column.TYPE_STRING);
//		typeMapping.put("polygon",Column.TYPE_LONG);
//		typeMapping.put("geometry",Column.TYPE_LONG);
//		typeMapping.put("multipoint",Column.TYPE_LONG);
//		typeMapping.put("multilinestring",Column.TYPE_LONG);
//		typeMapping.put("multipolygon",Column.TYPE_LONG);
//		typeMapping.put("geometrycollection",Column.TYPE_LONG);
	}
	
	
	IDataSource mds;
	MysqlDescripCommand mdc;
	Database mysql = new Database();
	
	
	public MysqlBeanBuilder(){
		this.mds = DataSourceFactory.getInstance().createDataSource("mysql");
		mdc = new MysqlDescripCommand();
	}
	
	
	@SuppressWarnings("finally")
	@Override
	public List<Table> buildAllTables() {
		List<Table> tables = new ArrayList<Table>();
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultset = null;
		String tbsSql = mdc.getTableDescribCommand();
		
		try {
			conn = mds.getConnection();
			statement = conn.prepareStatement(tbsSql);
			resultset = statement.executeQuery();
			int i=0;
			while(resultset.next()){
				String tableName = resultset.getString("table_name");
				String tableComment = resultset.getString("table_comment");
				Table table = buildTable(tableName, tableComment);
				buildTableColumns(table, conn);
				MainUI.log("BUILD THE【"+i+"】table,name:【"+tableName+"】");
				tables.add(table);
				i++;
			}
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return tables;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public Table buildTableByName(String tableName) {
		Connection conn = null;
		PreparedStatement statement =  null;
		ResultSet resultset = null;
		Table table = null;
		String tbSql = this.mdc.getTableDescribCommandByName(tableName);
		try {
			conn = mds.getConnection();
			statement = conn.prepareStatement(tbSql);
			resultset = statement.executeQuery();
			while(resultset.next()){
				String tableComment = resultset.getString("table_comment");
				table = buildTable(tableName, tableComment);
			}
			buildTableColumns(table, conn);
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			return table;
		}
	}
	
	public Table buildTable(String tableName, String tableComment){
		Table table = new Table();
		table.setDatabase(mysql);
		table.setName(tableName);
		table.setComment(tableComment);
		return table;
	}
	
	public void buildTableColumns(Table table, Connection conn){
		
		String tableName = table.getName();
		PreparedStatement statement =  null;
		ResultSet resultset = null;
		String columnsSql = mdc.getColumnsByTableName(tableName);
		try {
			statement = conn.prepareStatement(columnsSql);
			resultset = statement.executeQuery();
			while(resultset.next()){
				Column column = new Column();
				column.setName(resultset.getString("column_name"));
				column.setNullable(MYSQL_YES.equals(resultset.getString("is_nullable")));
				String type = resultset.getString("data_type");
				column.setType(type);
				String characterMaximunLength =  resultset.getString("character_maximum_length");
				String numericPrecision = resultset.getString("numeric_precision");
				String numricScale = resultset.getString("numeric_scale");
				
				if(nonLengthAble.indexOf(column.getType())>=0){
					column.setLengthable(false);
				}else if(!MYSQL_NULL.equals(characterMaximunLength) && null != characterMaximunLength){
					column.setDataLength(Integer.valueOf(characterMaximunLength));
					column.setLengthable(true);
				}else if(!MYSQL_NULL.equals(numericPrecision) && null != numericPrecision){
					column.setDataLength(Integer.valueOf(numericPrecision));
					int scale = Integer.valueOf(numricScale);
					column.setScale(scale);
					column.setLengthable(true);
				}else {
					column.setLengthable(false);
				}
				String keyFlag = resultset.getString("column_key");
				if(MYSQL_KEY_FLAG.equals(keyFlag)){
					column.setPK(true);
				}
				column.setComment(resultset.getString("column_comment"));
				
				column.setOwnerTable(table);
				fixColumn(column);
				table.addColumn(column);
			}
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	private void fixColumn(Column column){
		String type = column.getType();
		int oriType = typeMapping.get(type);
		column.setOriginType(oriType);
		if("decimal".equals(type)){
			if(!column.getScalable()){
				column.setOriginType(Column.TYPE_LONG);
			}
		}
	}
}
