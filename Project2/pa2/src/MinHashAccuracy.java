/**
 * @author pols_ckranig
 *
 */
public class MinHashAccuracy {
	public static int accuracy(String folder, int numPermutations, double errorparam){
		MinHashSimilarities mhs = new MinHashSimilarities(folder, numPermutations);
		int pairs = 0;
		for(int i = 0; i < mhs.allDocs().length;i++){
			String f1 = mhs.allDocs()[i];
			for(int j = i+1; j < mhs.allDocs().length;j++){
				String f2 = mhs.allDocs()[j];
				double jacSim = mhs.exactJaccard(f1, f2);
				double aproxJacSim = mhs.approximateJaccard(f1, f2);
				if(Math.abs(jacSim-aproxJacSim)>errorparam){
					pairs+=1;
				}
			}
		}
		return pairs;
	}
}
