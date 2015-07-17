package per.wlj.database.runnable;

import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import per.wlj.database.beans.Table;
import per.wlj.database.builder.OracleBeanBuilder;
import per.wlj.database.convert.impl.MysqlConvertorImpl;
import per.wlj.database.data.impl.DataExporter;
import per.wlj.database.util.BuilderContext;
import per.wlj.database.util.Constance;
import per.wlj.files.Writer;

public class Entrance {
	
	public void runDdl() throws Exception{
		
		String filePath = (String)BuilderContext.getInstance().getParams(Constance.KEY_DDL_PATH);
		String fileName = (String)BuilderContext.getInstance().getParams(Constance.KEY_DDL_FILENAME);
		
		if(null == filePath || "".equals(filePath)){
			filePath = "E:/files";
		}
		if(null == fileName || "".equals(fileName)){
			fileName = "ddl.sql";
		}
		
		
		OracleBeanBuilder obb = new OracleBeanBuilder();
		long start = System.currentTimeMillis();
		List<Table> tables = obb.buildAllTables();
		
		MysqlConvertorImpl mci = new MysqlConvertorImpl();
		StringTemplateGroup stg = new StringTemplateGroup("mysql");
		Writer w = new Writer();
		w.setFileName(filePath+fileName);
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
	}
	
	
	public void runDataExport(){
		long start = System.currentTimeMillis();
		DataExporter de = new DataExporter();
		
		String filePath = (String)BuilderContext.getInstance().getParams(Constance.KEY_DATA_PATH);
		String fileName = (String)BuilderContext.getInstance().getParams(Constance.KEY_DATA_FILENAME);
		
		if(null != filePath && !"".equals(filePath)){
			de.setDataFilePath(filePath);
		}
		if(null != fileName && !"".equals(fileName)){
			de.setDataFileName(fileName);
		}
		de.buildData();
		long end = System.currentTimeMillis();
		System.out.println("Time used total: ["+((end - start)/1000)+"]");
	}
	
	public static void main(String[] args) throws Exception {
		
		String runpar = "ddl";//data
		
		Entrance en = new Entrance();
		if("ddl".equals(runpar)){
			en.runDdl();
		} else {
			en.runDataExport();
		}
	}
	
}
