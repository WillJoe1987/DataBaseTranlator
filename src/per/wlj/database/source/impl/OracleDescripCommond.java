package per.wlj.database.source.impl;

import per.wlj.database.source.IDescriptionCommand;

public class OracleDescripCommond implements IDescriptionCommand {

	@Override
	public String getTableDescribCommand() {
		String sql = "select t.table_name , "
				+ "c.comments "
				+ "from user_tables t left join user_tab_comments c on t.TABLE_NAME = c.table_name";
		return sql;
	}

	@Override
	//return the oracle sql command which can search the structure of the table.
	public String getColumnsByTableName(String tableName) {
		if(null ==tableName)
			return null;
		
		//select col.TABLE_NAME,col.COLUMN_NAME, col.DATA_TYPE,col.DATA_LENGTH, col.DATA_PRECISION,col.DATA_SCALE, col.NULLABLE, com.comments from user_tab_columns col left join user_col_comments com on col.table_name = com.table_name and col.column_name = com.column_name where col.TABLE_NAME = 'CNT_MENU';
		return "select col.TABLE_NAME,col.COLUMN_NAME, col.DATA_TYPE,col.DATA_LENGTH, col.DATA_PRECISION,col.DATA_SCALE, col.NULLABLE, com.comments "
				+ "from user_tab_columns col "
				+ "left join user_col_comments com on col.table_name = com.table_name and col.column_name = com.column_name "
				+ "where col.TABLE_NAME = '"
				+ tableName
				+ "'";
	}
	
	public String getPKColumnCommand(String name){
		return "SELECT COL.column_name "
				+ "FROM USER_CONSTRAINTS CON "
				+ "LEFT JOIN USER_CONS_COLUMNS COL ON CON.table_name = COL.table_name AND CON.constraint_name = COL.constraint_name "
				+ "WHERE CON.constraint_type = 'P' "
				+ "and con.table_name = '"+name+"'";
	}
	
	@Override
	public String getVersion() {
		return "10.0-11.2";
	}

	@Override
	public String getTableDescribCommandByName(String name) {
		String sql = "select t.table_name , "
				+ "c.comments "
				+ "from user_tables t left join user_tab_comments c on t.TABLE_NAME = c.table_name "
				+ "where t.table_name='"+name+"'";
		return sql;
	}

	public static void main(String[] args) {
		OracleDescripCommond odc = new OracleDescripCommond();
		System.out.println(odc.getPKColumnCommand("ADMIN_LOG_INFO"));
	}
	
}
