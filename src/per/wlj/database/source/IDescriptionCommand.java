package per.wlj.database.source;

public interface IDescriptionCommand {
	
	public String getVersion();
	
	public String getTableDescribCommand();
	
	public String getTableDescribCommandByName(String name);
	
	public String getColumnsByTableName(String tableName);
	
}
