package per.wlj.database.beans;

import java.util.ArrayList;
import java.util.List;

public class Column {
	
	public static List<String> nonLengthAble = new ArrayList<String>();
	static {
		nonLengthAble.add("DATE");
		nonLengthAble.add("TIMESTAMP");
		nonLengthAble.add("TIMESTAMP(6)");
		nonLengthAble.add("DATETIME");
		nonLengthAble.add("CLOB");
		nonLengthAble.add("LONG");
	}
	
	String name = null;
	
	String type = null;
	
	String comment = null;
	
	boolean nullable = true;
	
	boolean isPK = false;
	
	int dataLength = 0;
	
	boolean lengthable = false;
	
	int precision = 0;
	
	int scale = 0;

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
		if(nonLengthAble.indexOf(this.type)>=0){
			return;
		}
		this.lengthable = true;
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
	
}
