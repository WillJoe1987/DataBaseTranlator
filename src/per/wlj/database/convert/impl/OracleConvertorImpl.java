package per.wlj.database.convert.impl;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.convert.IConvertor;

public class OracleConvertorImpl implements IConvertor {

	public static enum oracleColumnType{
		BINARY_DOUBLE,
		BINARY_FLOAT,
		BLOB,
		CLOB,
		CHAR,
		DATE,
		INTERVAL_DAY_TO_SECOND,
		INTERVAL_YEAR_TO_MONTH,
		LONG,
		LONG_RAW,
		NCLOB,
		NUMBER,
		NVARCHAR2,
		RAW,
		TIMESTAMP,
		TIMESTAMP_WITH_LOCAL_TIME_ZONE,
		ITMESTAMP_WITH_TIME_ZONE,
		VARCHAR2
	}
	
	
	
	@Override
	public Table convert(Table table) {
		Database target = new Database();
		target.setType("oracle");
		target.setVersion("10.0.2");
		table.setDatabase(target);
		for(Column column : table.getColumns()){
			convertColumnToOracle(column);
		}
		return null;
	}
	
	private void convertColumnToOracle(Column column){
		int origenType = column.getOriginType();
		switch(origenType){
		case 1 :
			column.setType("VARCHAR2");
			break;
		case 2 :
			column.setType("INTEGER");
			break;
		case 3 :
			column.setType("LONG");
			break;
		case 4 :
			column.setType("NUMBER");
			break;
		case 5 :
			column.setType("DATE");
			break;
		case 6 : 
			column.setType("TIMESTAMP");
			break;
		case 7 : 
			column.setType("BLOB");
			break;
		case 8 : 
			column.setType("CLOB");
			break;
		default:
			column.setType("VARCHAR2");
			break;
		}
	}
	

}
