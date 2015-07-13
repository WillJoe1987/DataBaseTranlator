package per.wlj.database.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.convert.impl.MysqlConvertorImpl;
import per.wlj.database.datasource.OracleDataSource;
import per.wlj.database.source.impl.OracleDescripCommond;
import per.wlj.files.Writer;

public class OracleBeanBuilder implements IBeanBuilder {
	
	OracleDataSource ods;
	OracleDescripCommond odc;
	Database oracle = new Database();
	
	
	public OracleBeanBuilder(OracleDataSource ods){
		this.ods = ods;
		odc = new OracleDescripCommond();
	}
	
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
			while(rs.next()){
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
					// TODO Auto-generated catch block
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
				
				//ORACLE LEGNTH PROCESS
				if(col.getLengthable() && (col.getPrecision()>col.getDataLength())){
					col.setDataLength(col.getPrecision());
				}
				
				table.addColumn(col);
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
	public static void main(String[] args) throws Exception {
		OracleBeanBuilder obb = new OracleBeanBuilder(new OracleDataSource());
//		Table cntMenu = obb.buildTableByName("CNT_MENU");
//		System.out.println(cntMenu);
		long start = System.currentTimeMillis();
		
		
		
		List<Table> tables = obb.buildAllTables();
		
		MysqlConvertorImpl mci = new MysqlConvertorImpl();
		
		String templatesDir = "E:\\files\\st\\";
		StringTemplateGroup stg = new StringTemplateGroup("mysql");
		
		Writer w = new Writer();
		w.createAndOpenFile();
		int i=0;
		for(Table table : tables){
			
			mci.convert(table);
			StringTemplate st1 = stg.getInstanceOf("Mysql");
			st1.setAttribute("table", table);
			w.writeLine(st1.toString());
			System.out.println("Writing THE【"+i+"】table,name:【"+table.getName()+"】");
			i++;
		}
		
		w.closeWriter();
		long end = System.currentTimeMillis();
		
		long sd = (end - start)/1000;
		System.out.println("user time : "+sd+" s;");
//		st1.setAttribute("table", cntMenu);
//		System.out.println(cntMenu.getColumns().size());
//		st1.setAttribute("columns", cntMenu.getColumns());
//		System.out.println(st1.toString());
	}
}
