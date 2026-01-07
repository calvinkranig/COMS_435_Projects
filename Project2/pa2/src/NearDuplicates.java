import java.util.ArrayList;

public class NearDuplicates {
	private LSH lsh;
	
	public NearDuplicates(String folderName, int numPermutations, double simularitythreshold) {
		MinHash mh = new MinHash(folderName,numPermutations);
		int b = initB(numPermutations, simularitythreshold);
		lsh = new LSH(mh.minHashMatrix(),mh.allDocs(),b);
	}
	
	private int initB(int numPermutations, double s){
		//need to find b and r such that s = (1/b)^(1/r) where b = numbands and r = rows perband
		//r = numPermutations/b so s = (1/b)^(1/(numPermutations/b)) = (1/b)^(b/numPermutations)
		int b = 1;
		double estimate = Math.pow((1.0/(double)b), ((double)b/(double)numPermutations));
		while(s < estimate){
			b+=1;
			estimate = Math.pow((1.0/(double)b), ((double)b/(double)numPermutations));
		}
		double high = Math.abs(s - Math.pow((1.0/(double)b), ((double)b/(double)numPermutations)));
		double low = Math.abs(s - Math.pow((1.0/(double)(b-1)), ((double)(b-1)/(double)numPermutations)));
		int ret =  high < low ? (int) b: (int)b-1;
		return ret;
	}
	
	public ArrayList<String> nearDuplciateDetector(String docName){
		return lsh.nearDuplicates(docName);
	}
	
	protected String[] docNames(){
		return this.lsh.docNames();
	}
	
}
