package src;

import java.util.HashSet;
import java.util.Random;

public class StatisticsTest {
	private static final int BITSPERELEMENT = 8;
	
	private static String[] generateStrings(int setSize){
		String[] ret = new String[setSize];
		Random r = new Random();
		for(int i = 0; i < setSize; i++){
			String s = "";
			int stringlength = r.nextInt(10)  + 5;
			for(int j = 0; j < stringlength; j++){
				char c = (char) (r.nextInt(26)+'a');
				s+=c;
			}
			ret[i]= s;
		}
		return ret;
	}
	private static String[] generateStringsNonMatching(int testStringNum, HashSet<String> inFilter){
		String[] ret = new String[testStringNum];
		Random r = new Random();
		int index = 0;
		while(index < testStringNum){
			String s = "";
			int stringlength = r.nextInt(10)  + 5;
			for(int j = 0; j < stringlength; j++){
				char c = (char) (r.nextInt(26)+'a');
				s+=c;
			}
			boolean isinfilter = inFilter.contains(s);
			if(!isinfilter){
				ret[index] = s;
				index++;
			}
		}
		return ret;
	}
	
	public static void testEstimateSetSize(int setSize, int numRuns){
		double totalPercentOff = 0;
		for(int i = 0; i < numRuns; i++){
			BloomFilterFNV f = new BloomFilterFNV(setSize, BITSPERELEMENT);
			String[] elements = generateStrings(setSize);
			for(String s: elements){
				f.add(s);
			}
			int estimate = Statistics.estimateSetSize(f);
			totalPercentOff += Math.max((double)estimate, (double) setSize)/Math.min((double)estimate, (double) setSize)-1.0;
			//System.out.printf("Estimate of set size is: %d Actual set Size is: %d \n", estimate, setSize);
		}
		double percent = totalPercentOff/(double)numRuns;
		System.out.printf("Set Size Estimate was %f percent off from real set size after %d runs \n", percent, numRuns);
	}
	
	public static void testEstimateIntersectSize(int setSize, int numRuns){
		double totalPercentOff = 0;
		for(int i = 0; i < numRuns; i++){
			BloomFilterFNV f1 = new BloomFilterFNV(setSize, BITSPERELEMENT);
			BloomFilterFNV f2 = new BloomFilterFNV(setSize, BITSPERELEMENT);
			HashSet<String> infilter = new HashSet<>();
			int intersectSize = (int) setSize/2;
			String[] elements = generateStrings(intersectSize);
			for(String s: elements){
				f1.add(s);
				f2.add(s);
				infilter.add(s);
			}
			String[] extraelements1 = generateStringsNonMatching(intersectSize, infilter);
			for(String s: extraelements1){
				f1.add(s);
				infilter.add(s);
			}
			String[] extraelements2 = generateStringsNonMatching(intersectSize, infilter);
			for(String s: extraelements2){
				f2.add(s);
				infilter.add(s);
			}
			int estimate = Statistics.estimateIntersectSize(f1, f2);
			totalPercentOff += Math.max((double)estimate, (double) intersectSize)/Math.min((double)estimate, (double) intersectSize)-1.0;
			//System.out.printf("Estimate of intersect size is: %d Actual set Size is: %d \n", estimate, intersectSize);
		}
		double percent = totalPercentOff/(double)numRuns;
		System.out.printf("Intersect Size Estimate was %f percent off from real set size after %d runs \n", percent, numRuns);
	}
}
