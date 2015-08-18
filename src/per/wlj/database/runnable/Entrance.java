package per.wlj.database.runnable;

import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.builder.IBeanBuilder;
import per.wlj.database.builder.MysqlBeanBuilder;
import per.wlj.database.builder.OracleBeanBuilder;
import per.wlj.database.convert.IConvertor;
import per.wlj.database.convert.impl.MysqlConvertorImpl;
import per.wlj.database.convert.impl.OracleConvertorImpl;
import per.wlj.database.data.impl.DataExporter;
import per.wlj.database.ui.MainUI;
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
		
		//DB_INFORMATIONS
		Map<String, String> dbinfos = (Map<String, String>)BuilderContext.getInstance().getParams("DB_INFORMATIONS");
		
		String sourceType = dbinfos.get("SOURCE_TYPE");
		String targetType = dbinfos.get("TARGET_TYPE");
		
		Database sourceDb = new Database();
		sourceDb.setType(sourceType);
		Database targetDb = new Database();
		targetDb.setType(targetType);
		
		IBeanBuilder builder = sourceDb.getBeanBuilder();
		long start = System.currentTimeMillis();
		List<Table> tables = builder.buildAllTables();
		IConvertor convertor = targetDb.getConverter();
				
		StringTemplateGroup stg = new StringTemplateGroup(targetType);
		Writer w = new Writer();
		w.setFileName(filePath+fileName);
		w.createAndOpenFile();
		int i=0;
		for(Table table : tables){
			if(!sourceType.equals(targetType))
				convertor.convert(table);
			StringTemplate st1 = stg.getInstanceOf(targetType);
			st1.setAttribute("table", table);
			w.writeLine(st1.toString());
			System.out.println("Writing THE【"+i+"】table,name:【"+table.getName()+"】");
			MainUI.log("Writing THE【"+i+"】table,name:【"+table.getName()+"】");
			i++;
		}
		w.closeWriter();
		long end = System.currentTimeMillis();
		long sd = (end - start)/1000;
		System.out.println("user time : "+sd+" s;");
		MainUI.log("user time : "+sd+" s;");
		MainUI.log("writing in : "+ w.getFileName());	
	}
	
	public void runDdl(int f) throws Exception{
		
		String filePath = (String)BuilderContext.getInstance().getParams(Constance.KEY_DDL_PATH);
		String fileName = (String)BuilderContext.getInstance().getParams(Constance.KEY_DDL_FILENAME);
		
		if(null == filePath || "".equals(filePath)){
			filePath = "E:/files";
		}
		if(null == fileName || "".equals(fileName)){
			fileName = "ddl.sql";
		}
		
		IBeanBuilder obb = new MysqlBeanBuilder();
		long start = System.currentTimeMillis();
		List<Table> tables = obb.buildAllTables();
		
		IConvertor mci = new OracleConvertorImpl();
		StringTemplateGroup stg = new StringTemplateGroup("oracle");
		Writer w = new Writer();
		w.setFileName(filePath+fileName);
		w.createAndOpenFile();
		int i=0;
		for(Table table : tables){
			mci.convert(table);
			StringTemplate st1 = stg.getInstanceOf("oracle");
			st1.setAttribute("table", table);
			w.writeLine(st1.toString());
			System.out.println("Writing THE【"+i+"】table,name:【"+table.getName()+"】");
			MainUI.log("Writing THE【"+i+"】table,name:【"+table.getName()+"】");
			i++;
		}
		w.closeWriter();
		long end = System.currentTimeMillis();
		long sd = (end - start)/1000;
		System.out.println("user time : "+sd+" s;");
		MainUI.log("user time : "+sd+" s;");
		MainUI.log("writing in : "+ w.getFileName());	
	}
	
	public void runDdl(String name) throws Exception{
		
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
		Table table = obb.buildTableByName(name);
		
		MysqlConvertorImpl mci = new MysqlConvertorImpl();
		StringTemplateGroup stg = new StringTemplateGroup("mysql");
		Writer w = new Writer();
		w.setFileName(filePath+fileName);
		w.createAndOpenFile();
		mci.convert(table);
		StringTemplate st1 = stg.getInstanceOf("mysql");
		st1.setAttribute("table", table);
		w.writeLine(st1.toString());
		String logStr = new String("Writing THE table, name:【"+table.getName()+"】");
		System.out.println(logStr);
		MainUI.log(logStr);
		w.closeWriter();
		long end = System.currentTimeMillis();
		long sd = (end - start)/1000;
		System.out.println("user time : "+sd+" s;");
		MainUI.log("user time : "+sd+" s;");
		MainUI.log("writing in : "+ w.getFileName());	
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

//	public static void main(String[] args) throws Exception {
//		String runpar = "ddl";//data
//		Entrance en = new Entrance();
//		if("ddl".equals(runpar)){
//			en.runDdl();
//		} else {
//			en.runDataExport();
//		}
//	}
	
}
