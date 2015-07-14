package per.wlj.database.beans;

public class Database {
	
	String type = "oracle";
	String version = "11.0";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
