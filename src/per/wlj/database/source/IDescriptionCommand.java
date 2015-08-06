package per.wlj.database.source;

public interface IDescriptionCommand {
	
	public String getVersion();
	
	/**
	 * RETURN A SQL THAT CAN EXCUSE ON DB TO GET ALL USER'S TABLE NAMES AND COMMENTS.
	 * @return
	 */
	public String getTableDescribCommand();
	
	public String getTableDescribCommandByName(String name);
	
	public String getColumnsByTableName(String tableName);
	
}
