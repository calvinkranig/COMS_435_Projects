package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;

public class EmpericalComparison {
	private static final String databasefile = "./database.txt";
	private static final String difffile = "./diffFile.txt" ;
	private static final String gramsfile = "./grams.txt";
	private static final int GRAMNUM = 4;
	
	
	
	public static void compare(int numRunsDiffFile, int numRunsDatabase){
		BloomDifferential bd = new BloomDifferential(databasefile,difffile,gramsfile);
		NaiveDifferential nd = new NaiveDifferential(databasefile,difffile,gramsfile);
		HashSet<String> diffKeys = getKeysFromFile(difffile);
		HashSet<String> databaseKeys = getDatabaseKeys(gramsfile, diffKeys);
		//Test some strings in the diff file
		System.out.println("Testing BloomDifferential key in DiffFile");
		testRetreiveKey(numRunsDiffFile, bd, diffKeys);
		System.out.println("Testing BloomDifferential key in Database");
		testRetreiveKey(numRunsDatabase, bd, databaseKeys);
		System.out.println("Testing NaiveDifferential key in DiffFile");
		testRetreiveKey(numRunsDiffFile, nd, diffKeys);
		System.out.println("Testing NaiveDifferential key in Database");
		testRetreiveKey(numRunsDatabase, nd, databaseKeys);
		//Test some strings in the database not in diff file
		
	}
	
	public static void generateTestFiles(int numKeys, double percentInDiffFile){
		if(percentInDiffFile > 1.0){
			return;
		}
		String keys[] = generateRandomKeys(numKeys);
		generateFile(keys, gramsfile);
		generateFile(keys, databasefile);
		String diffKeys[] = selectRandomKeys(keys, (int)(numKeys*percentInDiffFile));
		generateFile(diffKeys, difffile);
		System.out.println("Done Generating Files");
	}
	
	private static String[] selectRandomKeys(String keys[], int numRandom){
		String arr[] = new String[numRandom];
		Random r = new Random();
		HashSet<Integer> selected = new HashSet<>();
		for(int i = 0; i < arr.length; i++){
			int index = r.nextInt(arr.length);
			while(selected.contains(index)){
				index = r.nextInt(arr.length);
			}
			selected.add(index);
			arr[i] = keys[index];
		}
		return arr;
	}
	
	private static void generateFile(String arr[], String fileName){
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for( String s : arr){
				printWriter.printf("%s\n", s);
			}
			printWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private static String[] generateRandomKeys(int numKeys){
		String arr[] = new String[numKeys];
		Random r = new Random();
		for(int i = 0; i < arr.length; i++){
			String key = "";
			for(int j = 0; j < GRAMNUM; j++){
				int stringlength = r.nextInt(5)  + 2;
				for(int k = 0; k < stringlength; k++){
					char c = (char) (r.nextInt(26)+'a');
					key +=c;
				}
				key += " ";	
			}
			arr[i] = key;
		}
		return arr;
 	}
	
	
	
	private static void testRetreiveKey(int numRuns, AbstractDifferential ad, HashSet<String> keys){
		int i = 0;
		StopWatch s = new StopWatch();
		s.start();
		for(String key: keys){
			ad.retreiveRecord(key);
			i++;
			if(i >= numRuns){
				break;
			}
		}
		s.stop();
		System.out.printf("%d keys were found in %d milliseconds \n", i, s.getElapsedTime());
	}
	
	
	private static String getKey(String line){
		String arr[] = line.trim().split("\\s+");
		String key = "";
		for(int i = 0; i < GRAMNUM && i < arr.length; i++){
			key += arr[i] + " ";
		}
		return key;
	}
	
	private static HashSet<String> getDatabaseKeys(String filename, HashSet<String> diffkeys){
		HashSet<String> ret = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine())!=null){
				String diffkey = getKey(new String(line));
				//if not in diffkeys add it to hashset
				if(!diffkeys.contains(diffkey)){
					ret.add(diffkey);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private static HashSet<String> getKeysFromFile(String filename){
		HashSet<String> ret = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine())!=null){
				String diffkey = getKey(new String(line));
				ret.add(diffkey);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}


