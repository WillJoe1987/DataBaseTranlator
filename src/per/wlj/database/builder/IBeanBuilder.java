package per.wlj.database.builder;

import java.util.List;

import per.wlj.database.beans.Table;

public interface IBeanBuilder {
	
	public List<Table> buildAllTables();
	
	public Table buildTableByName(String tableName);
}
