public class MinHashTime {
	public static void timer(String folder, int numPermutations){
		System.out.println("TIMER TEST");
		StopWatch watch = new StopWatch();
		watch.start();
		MinHashSimilarities mhs = new MinHashSimilarities(folder, numPermutations);
		watch.stop();
		System.out.printf("Milliseconds taken to construct MinHashSimilarities: %d\n",watch.getElapsedTime());
		watch.start();
		for(int i = 0; i < mhs.allDocs().length;i++){
			String f1 = mhs.allDocs()[i];
			for(int j = i+1; j < mhs.allDocs().length;j++){
				String f2 = mhs.allDocs()[j];
				mhs.exactJaccard(f1, f2);
			}
		}
		watch.stop();
		System.out.printf("Milliseconds taken to compute Exact Jaccard Similarity: %d\n",watch.getElapsedTime());
		watch.start();
		mhs.recomputeMinHashMatrix();
		for(int i = 0; i < mhs.allDocs().length;i++){
			String f1 = mhs.allDocs()[i];
			for(int j = i+1; j < mhs.allDocs().length;j++){
				String f2 = mhs.allDocs()[j];
				mhs.approximateJaccard(f1, f2);
			}
		}
		watch.stop();
		System.out.printf("Milliseconds taken to construct MinHash matrix and compute Approximate Jaccard Similarity: %d\n",watch.getElapsedTime());
	}
}
