import java.util.Hashtable;

/**
 * @author pols_ckranig
 *
 */
public class MinHashSimilarities {
	
	private MinHash mh;
	private Hashtable<String, Integer> docIndex;
	private int[][] termDocMatrix;
	private int[][] minHashMatrix;
	
	
	public MinHashSimilarities(String folder, int numPermutations){
		mh = new MinHash(folder ,numPermutations);
		docIndex = new Hashtable<>();
		this.createDocIndex();
		int[][][] matrixs = mh.createBothMatrixs();
		this.minHashMatrix = matrixs[0];
		this.termDocMatrix = matrixs[1];
	}
	
	private void createDocIndex(){
		for(int i = 0; i < mh.allDocs().length; i++){
			docIndex.put(mh.allDocs()[i], i);
		}
	}
	
	protected void recomputeMinHashMatrix(){
		this.minHashMatrix = mh.minHashMatrix();
	}
	
	protected String[] allDocs(){
		return mh.allDocs();
	}
	
	protected int numTerms(){
		return this.mh.numTerms();
	}
	
	public double exactJaccard(String file1, String file2){
		if(!docIndex.containsKey(file1)||!docIndex.containsKey(file2)){
			System.out.println("Enter valid file name");
			return -1;
		}
		int f1 = docIndex.get(file1);
		int f2 = docIndex.get(file2);
		int intersection = 0;
		int union = 0;
		for(int i = 0; i < this.termDocMatrix[f1].length; i++){
			intersection += Math.min(this.termDocMatrix[f1][i], this.termDocMatrix[f2][i]);
			union += Math.max(this.termDocMatrix[f1][i], this.termDocMatrix[f2][i]);
		}
		return ((double) intersection)/((double)union);
	}
	
	public double approximateJaccard(String file1, String file2){
		if(!docIndex.containsKey(file1)||!docIndex.containsKey(file2)){
			System.out.println("Enter valid file name");
			return -1;
		}
		int f1 = docIndex.get(file1);
		int f2 = docIndex.get(file2);
		int l = 0;
		for(int i = 0; i < mh.numPermutations(); i++){
			if(this.minHashMatrix[f1][i] == this.minHashMatrix[f2][i]){
				l+=1;
			}
		}
		return  (double) l/this.mh.numPermutations();
	}
	
	public int[] minHashSig(String fileName){
		return this.minHashMatrix[docIndex.get(fileName)];
	}
}

