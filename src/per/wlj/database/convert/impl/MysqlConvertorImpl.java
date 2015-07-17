package per.wlj.database.convert.impl;

import java.util.ArrayList;
import java.util.List;

import per.wlj.database.beans.Column;
import per.wlj.database.beans.Database;
import per.wlj.database.beans.Table;
import per.wlj.database.convert.IConvertor;

public class MysqlConvertorImpl implements IConvertor {
	
//	public static enum MysqlColumnType {
//		TINYINT,
//		SMALLINT,
//		MEDIUMINT,
//		INT,
//		INTEGER, //INTEGER
//		BIGINT,  //LONG
//		BIT,	 //C
//		REAL,
//		DOUBLE,
//		FLOAT,
//		DECIMAL,
//		NUMERIC,
//		CHAR,
//		VARCHAR,
//		DATE,
//		TIME,
//		YEAR,
//		TIMESTAMP,
//		DATETIME,
//		TINYBLOB,
//		BLOB,
//		MEDIUMBLOB,
//		LONGBLOB,
//		TINYTEXT,
//		TEXT,
//		MEDIUMTEXT,
//		LONGTEXT,
//		ENUM,
//		SET,
//		BINARY,
//		VARBINARY,
//		POINT,
//		LINESTRING,
//		POLYGON,
//		GEOMETRY,
//		MULTIPOINT,
//		MULTILINESTRING,
//		MULTIPOLYGON,
//		GEOMETRYCOLLECTION
//	}
	
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
		int oriType = column.getOriginType();
		switch (oriType) {
			case 1 :
				column.setType("VARCHAR");
				break;
			case 2 :
				column.setType("INTEGER");
				break;
			case 3 :
				column.setType("BIGINT");
				break;
			case 4 :
				column.setType("DECIMAL");
				break;
			case 5 :
				column.setType("DATETIME");
				break;
			case 6 : 
				column.setType("TIMESTAMP");
				break;
			case 7 : 
				column.setType("BLOB");
				break;
			case 8 : 
				column.setType("TEXT");
				break;
			default:
				column.setType("VARCHAR");
				break;
		}
		
		//整形主键默认自增
		if(column.getIsPK() && (column.getType().equals("BIGINT") || column.getType().equals("INTEGER"))){
			column.setAutoEncrease(true);
		}
		
	}
}
