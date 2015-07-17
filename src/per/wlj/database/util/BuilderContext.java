package per.wlj.database.util;

import java.util.concurrent.ConcurrentHashMap;

public class BuilderContext {
	
	private static BuilderContext instance;
	
	private ConcurrentHashMap<String, Object> contexts = new ConcurrentHashMap<String, Object>();
	
	private BuilderContext(){};
	
	public static BuilderContext getInstance(){
		if(null == instance) instance = new BuilderContext();
		instance.putParams(Constance.KEY_DDL_PATH, "E:/files/");
		instance.putParams(Constance.KEY_DDL_FILENAME, "ddl.sql");
		instance.putParams(Constance.KEY_DATA_PATH, "E:/files/");
		instance.putParams(Constance.KEY_DATA_FILENAME, "data.sql");
		return instance;
	}
	
	public void putParams(String key, Object value){
		this.contexts.put(key, value);
	}
	
	public Object getParams(String key){
		return this.contexts.get(key);
	}
	
}
