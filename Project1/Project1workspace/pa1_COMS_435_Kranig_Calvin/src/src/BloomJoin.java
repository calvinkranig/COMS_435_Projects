package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;

public class BloomJoin {
	private String f1;
	private String f2;
	private static final int BITSPERELEMENT = 8;
	
	public BloomJoin(String f1, String f2) {
		this.f1 = f1;
		this.f2 = f2;
	}
	
	public void join(String f3){
		//Get keys from file 
		HashSet<String> joinattrs = this.getKeys();
		Filter filter = this.createFilter(joinattrs);
		//Send over filter and return pairs
		LinkedList<String> pairs= this.getPairs(filter);
		//Send over new partial table to first process 
		writeJoin(f3, pairs);
	}
	
	private void writeJoin(String f3, LinkedList<String> pairs){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f1));
			FileWriter fileWriter = new FileWriter(f3);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
			String line;
			while((line = reader.readLine())!=null){
				//Join attribute is first thing in line
				String[] temp = line.trim().split("\\s+");
				for(String p: pairs){
					String[] pair = p.split("\\s+");
					if(pair[0].equals(temp[0])){
						printWriter.printf("%s %s \n", temp[1], p);
					}
				}		
			}
			reader.close();
			printWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private LinkedList<String> getPairs(Filter filter){
		LinkedList<String> pairs = new LinkedList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f2));
			String line;
			while((line = reader.readLine())!=null){
				//Join attribute is first thing in line
				String joinattr = line.trim().split("\\s+")[0];
				if(filter.appears(joinattr)){
					pairs.add(line.trim());
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pairs;
	}
	
	private Filter createFilter(HashSet<String> keys){
		Filter ret = new BloomFilterRan(keys.size(), this.BITSPERELEMENT);
		for(String s : keys){
			ret.add(s);
		}
		return ret;
	}
	
	private HashSet<String> getKeys(){
		HashSet<String> ret = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f1));
			String line;
			while((line = reader.readLine())!=null){
				//Join attribute is first thing in line
				String joinattr = line.trim().split("\\s+")[0];
				ret.add(joinattr);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
