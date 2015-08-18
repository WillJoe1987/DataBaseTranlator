package per.wlj.database.beans;

import per.wlj.database.builder.IBeanBuilder;
import per.wlj.database.builder.MysqlBeanBuilder;
import per.wlj.database.builder.OracleBeanBuilder;
import per.wlj.database.convert.IConvertor;
import per.wlj.database.convert.impl.MysqlConvertorImpl;
import per.wlj.database.convert.impl.OracleConvertorImpl;
import per.wlj.database.source.IDescriptionCommand;
import per.wlj.database.source.impl.MysqlDescripCommand;
import per.wlj.database.source.impl.OracleDescripCommand;

public class Database {
	
	public static final String TYPE_ORACLE = "oracle";
	public static final String TYPE_MYSQL = "mysql";
	
	String type = "oracle";
	String version = "11.0";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
    public IConvertor getConverter(){
    	if(TYPE_ORACLE.equals(type)){
    		return new OracleConvertorImpl();
    	}else if(TYPE_MYSQL.equals(type)){
    		return new MysqlConvertorImpl();
    	}else return null;
    }
    public IBeanBuilder getBeanBuilder(){
    	if(TYPE_ORACLE.equals(type)){
    		return new OracleBeanBuilder();
    	}else if(TYPE_MYSQL.equals(type)){
    		return new MysqlBeanBuilder();
    	}return null;
    }
    public IDescriptionCommand getCommands(){
    	if(TYPE_ORACLE.equals(type)){
    		return new OracleDescripCommand();
    	}else if(TYPE_MYSQL.equals(type)){
    		return new MysqlDescripCommand();
    	}else return null;
    	
    }
}
