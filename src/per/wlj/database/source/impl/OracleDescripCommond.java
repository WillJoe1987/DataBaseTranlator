package per.wlj.database.source.impl;

import per.wlj.database.source.IDescriptionCommand;

public class OracleDescripCommond implements IDescriptionCommand {

	@Override
	public String getTableDescribCommand() {
		String sql = "select t.table_name , c.comments from user_tables t left join user_tab_comments c on t.TABLE_NAME = c.table_name";
		return sql;
	}

	@Override
	public String getColumnsByTableName(String tableName) {
		if(null ==tableName)
			return null;
		return "select * from user_tab_columns where table_name='"+tableName+"'";

	}

	@Override
	public String getVersion() {
		return "10.0-11.2";
	}

}
