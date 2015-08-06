package per.wlj.database.source.impl;

import per.wlj.database.source.IDescriptionCommand;

public class MysqlDescripCommand implements IDescriptionCommand {
	
	@Override
	public String getVersion() {
		return "5.6";
	}
	
	
	@Override
	/**
	 * struct: table_name, table_comment
	 */
	public String getTableDescribCommand() {
		return "select table_name, table_comment from information_schema.tables where table_schema='dhgl' and table_type='BASE TABLE'";
	}

	@Override
	/**
	 * struct: table_name, table_comment
	 */
	public String getTableDescribCommandByName(String name) {
		if(null == name || "".equals(name)) 
			return null;
		return "select table_name, table_comment from information_schema.tables where table_schema='dhgl' and table_type='BASE TABLE' AND table_name='"+name+"'";
	}

	@Override
	/**
	 * struct: column_name, is_nullable, data_type, character_maximum_length, numeric_precision, numberic_scale, column_key, column_comment
	 */
	public String getColumnsByTableName(String tableName) {
		if(null == tableName || "".equals(tableName))
			return null;
		return "select column_name, is_nullable, data_type,character_maximum_length ,numeric_precision,numeric_scale,column_key, column_comment from information_schema.columns where table_schema='dhgl' and table_name='"+tableName+"'";
	}

}
