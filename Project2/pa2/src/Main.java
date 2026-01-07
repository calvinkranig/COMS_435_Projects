
public class Main {

	private static final String folder0 = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project2/PA2Data/F17PA2";
	private static final String folder1 = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project2/PA2Data/space";
	private static final String folder2 = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project2/PA2Data/test";
	private static final String folder3 = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project2/PA2Data/test2";
	private static final int numPermutations = 800;
	private static final double simularity = 0.8;
	
	public static void main(String[] args) {
		//mhsTest(); 
		mhaTest();
		//mhtTest();
		//neardupTest();
	}
	
	private static void neardupTest(){
		System.out.printf("Testing with s = %f and Number of Permutations: %d \n",simularity,numPermutations );
		NearDuplicates nd = new NearDuplicates(folder0, numPermutations, simularity);
		int jump = (nd.docNames().length-1)/10;
		for(int i = 0; i < 10; i++){
			String doc = nd.docNames()[i*jump];
			System.out.println("Similar documents to " + doc+":");
			for(String s: nd.nearDuplciateDetector(doc)){
				System.out.println(s);
			}
			System.out.println();
		}
	}
	private static void mhaTest(){
		int[] perms = {400,600,800};
		double[] errors = {0.04,0.07, 0.09};
		for(int i = 0; i <perms.length; i++){
			for(int j = 0; j < errors.length; j++){
				System.out.printf("%d pairs while using %d permutations and %f error parameter\n",MinHashAccuracy.accuracy(folder1, perms[i], errors[j]),perms[i], errors[j]);
			}
		}
	}
	
	private static void mhtTest(){
		MinHashTime.timer(folder1, 600);
	}

	private static void mhsTest(){
		//MinHash mh = new MinHash(folder2, numPermutations);
		MinHashSimilarities mhs = new MinHashSimilarities(folder3, numPermutations);
		String f1 = mhs.allDocs()[0];
		String f2 = mhs.allDocs()[mhs.allDocs().length-1];
		System.out.println(f1);
		System.out.println(f2);
		System.out.println(mhs.approximateJaccard(f1, f2));
		System.out.println(mhs.exactJaccard(f1, f2));
		System.out.println(mhs.numTerms());
	}
}
