package per.wlj.database.convert.impl;

import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

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
		
		//Database source = table.getDatabase();
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
	
	
	public static void main(String[] args) {
		String templatesDir = "E:\\files\\st\\";
		StringTemplateGroup stg = new StringTemplateGroup("mysql",templatesDir);
		StringTemplate st1 = stg.getInstanceOf("Mysql");
	//	st1.
		Table t = new Table();
		t.setName("table1");
		t.setDatabase(new Database());
		t.setComment("234");
		Column c1 = new Column();
		Column c2 = new Column();
		c1.setName("1");
		c2.setName("2");
		c1.setType("varchar2");
		c2.setType("bigint");
		
		t.addColumn(c1);
		t.addColumn(c2);
		
		st1.setAttribute("table", t);
		System.out.println(st1.toString());
	}
	
}
