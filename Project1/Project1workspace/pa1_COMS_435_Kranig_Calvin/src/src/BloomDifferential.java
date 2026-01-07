package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BloomDifferential extends AbstractDifferential{
	private Filter filter;
	private static final int BITSPERELEMENT = 8;
	
	public BloomDifferential(String database, String diffFile, String grams) {
		super(database, diffFile, grams);
		this.filter = this.createFilter();
	}

	public Filter createFilter(){
		Filter ret = new BloomFilterRan(this.keys.size(), BITSPERELEMENT);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.diffFile));
			String line;
			while((line = reader.readLine())!=null){
				String key = this.getKey(line);
				ret.add(key);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public String retreiveRecord(String key) {
		if(!this.keys.contains(key)){
			return "";
		}else if(this.filter.appears(key)){//Key in diff file
			return this.getRecordFromFile(key, this.diffFile);
		}else{//Key in database
			return this.getRecordFromFile(key, this.database);
		}
	}
}
