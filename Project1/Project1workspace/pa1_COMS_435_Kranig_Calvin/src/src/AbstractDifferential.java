package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public abstract class AbstractDifferential {
	protected String database;
	protected String diffFile;
	protected String gramFile;
	protected HashSet<String> keys;
	protected static final int GRAMNUM = 4;
	public AbstractDifferential(String database, String diffFile, String grams) {
		this.database = database;
		this.diffFile = diffFile;
		this.gramFile = grams;
		this.keys = new HashSet<>();
		//create a hashset with the grams in order to check entered keys
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.gramFile));
			String line;
			while((line = reader.readLine())!=null){
				String key = this.getKey(line);
				this.keys.add(key);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public abstract String retreiveRecord(String key);
	
	protected String getKey(String line){
		String arr[] = line.trim().split("\\s+");
		String key = "";
		for(int i = 0; i < Math.min(this.GRAMNUM, arr.length); i++){
			key += arr[i] + " ";
		}
		return key;
	}
	
	protected String getRecordFromFile(String key, String filename){
		String ret = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine())!=null){
				String diffkey = this.getKey(new String(line));
				if(key.equalsIgnoreCase(diffkey)){
					ret = line;
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	
	
	
}
