package per.wlj.database.beans;

import java.util.ArrayList;
import java.util.List;

public class Column {
	
	public static int TYPE_STRING = 1;
	public static int TYPE_INTEGER = 2;
	public static int TYPE_LONG = 3;
	public static int TYPE_FLOAT = 4;
	public static int TYPE_DATE = 5;
	public static int TYPE_TIMESTAMP = 6;
	public static int TYPE_BLOB = 7;
	public static int TYPE_CLOB = 8;
	public static int TYPE_CHAR = 9;
	
	
	public static List<String> nonLengthAble = new ArrayList<String>();
	static {
		nonLengthAble.add("DATE");
		nonLengthAble.add("TIMESTAMP");
		nonLengthAble.add("TIMESTAMP(6)");
		nonLengthAble.add("DATETIME");
		nonLengthAble.add("CLOB");
		nonLengthAble.add("LONG");
	}
	
	int originType = 0;
	
	String name = null;
	
	String type = null;
	
	String comment = null;
	
	boolean nullable = true;
	
	boolean isPK = false;
	
	int dataLength = 0;
	
	boolean lengthable = false;
	
	int precision = 0;
	
	int scale = 0;

	Table ownerTable ;
	
	public Table getOwnerTable() {
		return ownerTable;
	}

	public void setOwnerTable(Table ownerTable) {
		this.ownerTable = ownerTable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean ispk) {
		this.isPK = ispk;
	}

	public boolean getIsPK(){
		return isPK;
	}
	
	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean getScalable(){
		return this.scale>0;
	}
	
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean getLengthable() {
		return lengthable;
	}
	
	public int  getOriginType() {
		return originType;
	}

	public void setOriginType(int originType) {
		this.originType = originType;
	}

	public void setLengthable(boolean lengthable) {
		this.lengthable = lengthable;
	}
	
}
