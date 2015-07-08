package per.wlj.database.beans;

public class Column {
	
	String name = null;
	
	String type = null;
	
	String comment = null;
	
	boolean nullable = true;
	boolean idPK = false;
	int length = 0;
	
	

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
	
	
	
	
}
