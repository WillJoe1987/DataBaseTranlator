package per.wlj.database.beans;

import java.util.ArrayList;
import java.util.List;

public class Table {
	
	String name = null;
	String comment = null;
	List<Column> columns = new ArrayList<Column>();
	Database database = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Database getDatabase() {
		return database;
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	public void addColumn(Column column){
		column.setOwnerTable(this);
		this.columns.add(column);
	}
	
	public Column getColumn(String name){
		for(Column column : this.columns){
			if(column.getName().equals(name)){
				return column;
			}
		}
		return null;
	}
}
