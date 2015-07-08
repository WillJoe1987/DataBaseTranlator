package per.wlj.database.convert;

import per.wlj.database.beans.Table;

public interface IConvertor {
	
	public StringBuilder convertDDL(Table table);
	
}
