package per.wlj.database.convert.impl;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.convert.IConvertor;

public class MysqlConvertorImpl implements IConvertor {
	
	@Override
	public StringBuilder convertDDL(Table table) {
		
		StringBuilder builder = new StringBuilder();
		
		return null;
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
