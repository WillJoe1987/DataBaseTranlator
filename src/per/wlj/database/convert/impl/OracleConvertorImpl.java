package per.wlj.database.convert.impl;

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
		// TODO Auto-generated method stub
		return null;
	}

}
