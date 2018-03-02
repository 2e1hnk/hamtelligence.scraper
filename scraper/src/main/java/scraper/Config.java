package scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
	private String filename;
	
	public Config() {
		
	}
	
	public Config(String filename) {
		this.filename = filename;
		this.load(filename);
	}
	
	public void load(String filename) {
		// Load properties
		try {
			ClassLoader classloader = getClass().getClassLoader();
			File file = new File(classloader.getResource(this.filename).getFile());
			FileInputStream fileInput = new FileInputStream(file);
			super.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void save() throws IOException {
		if ( this.filename == null || this.filename.equals("") ) {
			throw new IOException();
		}
		this.save(this.filename);
	}
	
	public void save(String filename) {
		try {
			ClassLoader classloader = getClass().getClassLoader();
			this.store(new FileOutputStream(classloader.getResource(this.filename).getFile()), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPropertyAsInt(String key) {
		return Integer.parseInt(this.getProperty(key));
	}
	
	public void setProperty(String key, Object value) {
		this.setProperty(key, String.valueOf(value));
	}
}
