package per.wlj.database.convert.impl;

import java.util.ArrayList;
import java.util.List;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.convert.IConvertor;

public class MysqlConvertorImpl implements IConvertor {
	
	public static final List<String> errorNames = new ArrayList<String>();
	static {
		errorNames.add("LIMIT");
		errorNames.add("KEY");
		errorNames.add("CONDITION");
	}
	
	@Override
	public Table convert(Table table) {
		Database target = new Database();
		target.setType("mysql");
		target.setVersion("5.6");
		table.setDatabase(target);
		for(Column column : table.getColumns()){
			converColumnFromOracle(column);
		}
		return null;
	}
	
	private void converColumnFromOracle(Column column){
		String type = column.getType();
		if(type.equals("DATE")){
			column.setType("DATETIME");
		}else if(type.equals("NUMBER")){
			if(column.getScale() == 0){
				//BIGINT LENGTH ALWAYS 19.IT WILL BE NO EFFACT IF YOU SET A LENGTH NUMBER.
				column.setType("BIGINT");
			}else{
				column.setType("DECIMAL");
			}
		}else if(type.equals("VARCHAR2")){
			column.setType("VARCHAR");
		}else if(type.equals("LONG")){
			column.setType("BIGINT");
		}else if(type.equals("CLOB")){
			column.setType("TEXT");
		}
		String name = column.getName();
		if(errorNames.indexOf(name.toUpperCase())>=0){
			column.setName("_"+name);
			System.out.println("【ERROR】：the column Name【"+name+"】 is invalidate in mysql, it has bean changed into 【_"+name+"】");
		}
	}
}
