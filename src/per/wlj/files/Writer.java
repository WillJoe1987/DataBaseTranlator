package per.wlj.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
		
	String fileName= "E:/files/1.sql";
	String lineSeporat = "\r\n";//System.lineSeparator();
	File file = null;
	FileWriter fw = null;
	
	public void createAndOpenFile() throws IOException{
		file = new File(fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		
		if(!file.exists()){
			file.createNewFile();
		}
		fw = new FileWriter(file);
	}
	
	public void closeWriter() throws IOException{
		if(null != fw) fw.close();
		fw = null;
		file = null;
	}
	
	public void writeLine(String string) throws IOException{
		if(null == fw ) return ;
		fw.write(string);
		fw.write(lineSeporat);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
