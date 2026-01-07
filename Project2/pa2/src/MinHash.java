import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author pols_ckranig
 *
 */
public class MinHash {
	private String folder;
	private String[] docNames;
	private String[] paths;
	private int numTerms;
	private int numPermutations;
	
	
	public MinHash(String folder, int numPermutations) {
		this.folder = folder;
		this.numPermutations = numPermutations;
		getFiles();
		this.numTerms = -1;
	}
	
	protected int[][][] createBothMatrixs(){
		Hashtable<String,Term> terms = new Hashtable<>();
		DocumentProcessor Dp = new DocumentProcessor();
		//Process Documents in Parallel or on a single thread
		//Hashtable<String,Integer>[] multisets = Dp.processDocs(paths,terms);
		Hashtable<String,Integer>[] multisets = Dp.processDocsParallel(paths, terms);
		this.numTerms = terms.size();
		int totalTerms = this.getTotalTerms(terms);
		int p = Prime.nextPrime(totalTerms);
		ArrayList<Integer>[] binaryTermMultiSets = (ArrayList<Integer>[])new ArrayList[this.docNames.length];
		createBinaryTermMultiset(multisets, terms, binaryTermMultiSets);
		int[][] minhashmatrix = createminHashMatrixHelper(binaryTermMultiSets, p);
		int[][] termDocMatrix = this.createTermDocMatrixHelper(multisets, terms);
		int[][][] ret = {minhashmatrix, termDocMatrix};
		return ret;
	}
	
	private int[][] createMinHashMatrix(){
		Hashtable<String,Term> terms = new Hashtable<>();
		DocumentProcessor Dp = new DocumentProcessor();
		//Process Documents in Parallel or on a single thread
		//Hashtable<String,Integer>[] multisets = Dp.processDocs(paths,terms);
		Hashtable<String,Integer>[] multisets = Dp.processDocsParallel(paths, terms);
		this.numTerms = terms.size();
		int totalTerms = this.getTotalTerms(terms);
		int p = Prime.nextPrime(totalTerms);
		ArrayList<Integer>[] binaryTermMultiSets = (ArrayList<Integer>[])new ArrayList[this.docNames.length];
		createBinaryTermMultiset(multisets, terms, binaryTermMultiSets);
		return createminHashMatrixHelper(binaryTermMultiSets, p);
	}

	private int getTotalTerms(Hashtable<String,Term> terms){
		int total = 0;
		for(Entry<String, Term> t: terms.entrySet()){
			t.getValue().setBinaryIndex(total);
			total += t.getValue().getMaxFrequency();
		}
		return total;
	}
	
	private int[][] createminHashMatrixHelper(ArrayList<Integer>[] binaryTermMultiSets, int p) {
		int[][] minHashMatrix = new int[docNames.length][this.numPermutations];
		Random r = new Random();
		for(int i = 0; i < this.numPermutations; i++){
			//Create random permutation
			int a = r.nextInt(p);
			int b = r.nextInt(p);
			for(int j = 0; j < this.docNames.length; j++){
				int minhash = getMin(binaryTermMultiSets[j],a,b,p);
				minHashMatrix[j][i]=minhash;
			}
		}
		return minHashMatrix;
	}


	private int getMin(ArrayList<Integer> binaryTermMultiSet, int a, int b, int p) {
		int min = p;
		for(Integer i : binaryTermMultiSet){
			int permutation = (a*i+b)%p;
			min = Math.min(permutation, min);
		}
		return min;
	}
	
	private void createBinaryTermMultiset(Hashtable<String, Integer>[] multisets, Hashtable<String,Term> terms,ArrayList<Integer>[] binaryTermMultiSets){
		for(int i = 0; i < binaryTermMultiSets.length; i++){
			binaryTermMultiSets[i] = new ArrayList<Integer>();
		}
		for(int i = 0; i < multisets.length; i++){
			for(Entry<String, Integer> term: multisets[i].entrySet()){
				Term t = terms.get(term.getKey());
				int termindex = t.getIndex();
				int occurrences = term.getValue();
				for(int j = 0; j < occurrences; j++){
					binaryTermMultiSets[i].add(t.getBinaryIndex()+j);
				}
			}
		}
	}
	
	private int[][] createTermDocMatrix(){
		Hashtable<String,Term> terms = new Hashtable<>();
		DocumentProcessor Dp = new DocumentProcessor();
		//Process Documents in Parallel or on a single thread
		//Hashtable<String,Integer>[] multisets = Dp.processDocs(paths,terms);
		Hashtable<String,Integer>[] multisets = Dp.processDocsParallel(paths, terms);
		return createTermDocMatrixHelper(multisets, terms);
	}

	private int[][] createTermDocMatrixHelper(Hashtable<String, Integer>[] multisets, Hashtable<String,Term> terms) {
			int [][] termDocMatrix = new int[docNames.length][terms.size()];
			for(int i = 0; i < multisets.length; i++){
				for(Entry<String, Integer> term: multisets[i].entrySet()){
					Term t = terms.get(term.getKey());
					int termindex = t.getIndex();
					int occurrences = term.getValue();
					termDocMatrix[i][termindex] = occurrences;
				}
			}
			return termDocMatrix;
	}

	private void getFiles(){
		File fileFolder = new File(this.folder);
		File[] files = fileFolder.listFiles();
		ArrayList<String> docs = new ArrayList<>();
		ArrayList<String> docpaths = new ArrayList<>();
		for(File f: files){
			if(f.isFile()){
				docs.add(f.getName());
				docpaths.add(f.getPath());
			}
		}
		this.docNames = new String[docs.size()];
		this.docNames = docs.toArray(this.docNames);
		this.paths = new String[docpaths.size()];
		this.paths = docpaths.toArray(this.paths);
	}
	
	public String[] allDocs(){
		return docNames;
	}
	
	public int[][] minHashMatrix(){
		return this.createMinHashMatrix();
	}
	
	public int[][] termDocumentMatrix(){
		return this.createTermDocMatrix();
	}
	
	public int numTerms(){
		if(this.numTerms==-1){
			this.createMinHashMatrix();
		}
		return this.numTerms;
	}
	
	public int numPermutations(){
		return this.numPermutations;
	}
}
