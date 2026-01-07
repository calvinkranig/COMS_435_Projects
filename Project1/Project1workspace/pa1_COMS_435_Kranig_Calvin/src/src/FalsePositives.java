package src;

import java.util.HashSet;
import java.util.Random;

public class FalsePositives {
	
	private int setSize;
	private HashSet<String> inFilter;
	private int bitsPerElement;
	private int testStringNum;
	
	public FalsePositives(int setSize, int bitsPerElement, int testStringNum) {
		this.setSize = setSize;
		this.bitsPerElement = bitsPerElement;
		this.inFilter = new HashSet<String>();
		this.testStringNum = testStringNum;
		String filterStrings[] = this.generateStrings();
		for(String s : filterStrings){
			inFilter.add(s);
		}
	}
	
	private String[] generateStrings(){
		String[] ret = new String[this.setSize];
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
	private String[] generateStringsNonMatching(){
		String[] ret = new String[this.testStringNum];
		Random r = new Random();
		int index = 0;
		while(index < this.testStringNum){
			String s = "";
			int stringlength = r.nextInt(10)  + 5;
			for(int j = 0; j < stringlength; j++){
				char c = (char) (r.nextInt(26)+'a');
				s+=c;
			}
			boolean isinfilter = this.inFilter.contains(s);
			if(!isinfilter){
				ret[index] = s;
				index++;
			}
		}
		return ret;
	}
	
	public void filterTest(){
		BloomFilterFNV f1 = new BloomFilterFNV(this.setSize, this.bitsPerElement);
		DynamicFilter f2 = new DynamicFilter(this.bitsPerElement);
		BloomFilterRan f3 = new BloomFilterRan(this.setSize, this.bitsPerElement);
		Filter[] filters = {f1,f2,f3};
		filterTest(filters);
	}
	
	private void filterTest(Filter[] filters){
		String testStrings[] = this.generateStringsNonMatching();
		double expectedratio = Math.pow(0.618, (double)this.bitsPerElement);
		for(int i = 0; i<filters.length; i++){
			int falsepositives = 0;
			for(String s : this.inFilter){
				filters[i].add(s);
			}
			for(String s : testStrings){
				if(filters[i].appears(s)){
					falsepositives +=1;
				}
			}
			double ratio = ((double)falsepositives)/this.testStringNum;
			System.out.print(filters[i].name() +": "
					//+ "\n Total False Positives: " + falsepositives
					+ "\n Ratio: " + ratio + " Expected: " + expectedratio +"\n");
		}
	}
	
	
}
