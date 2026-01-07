import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

/**
 * @author pols_ckranig
 *
 */
public class LSH {
	private int[][] minHashMatrix;
	private String[] docNames;
	//Keeps track of index for docname in each table
	private Hashtable<String, int[]> docIndex;
	private Hashtable<Integer,ArrayList<String>>[] tables;
	private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 0x100000001b3L;
	
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		this.minHashMatrix = minHashMatrix;
		this.docNames = docNames;
		this.initDocIndex(bands);
		this.tables = (Hashtable<Integer,ArrayList<String>>[])new Hashtable[bands];
		this.createTables(bands);
	}
	
	
	private void initDocIndex(int bands){
		this.docIndex = new Hashtable<>(this.docNames.length);
		for(String docName : docNames){
			this.docIndex.put(docName, new int[bands]);
		}
	}
	
	private void createTables(int bands){
		int size = this.minHashMatrix[0].length/bands;
		int remainder = this.minHashMatrix[0].length%bands;
		int curleft = 0;
		for(int i = 0; i < bands; i++){
			int bandsize = size;
			if(i < remainder){
				bandsize +=1;
			}
			this.createTable(curleft, curleft +bandsize, i);
			curleft = curleft+bandsize;
		}
	}
	
	private void createTable(int left, int right, int tableNum){
		this.tables[tableNum] = new Hashtable<>();
		for(int i = 0; i < this.docNames.length;i++){
			long h = FNV1_64_INIT;
			for(int j = left; j <right; j++){
				h = getHash((long)this.minHashMatrix[i][j],h);
			}
			h = Math.abs((int)h);
			int index = (int) h;
			this.docIndex.get(this.docNames[i])[tableNum] = index;
			if(this.tables[tableNum].containsKey(index)){
				this.tables[tableNum].get(index).add(this.docNames[i]);
			}else{
				ArrayList<String> docs = new ArrayList<String>();
				docs.add(this.docNames[i]);
				this.tables[tableNum].put(index, docs);
			}
		}
	}
	
	private static long getHash(long c, long h){
		//h = h XOR c
		h = h ^ c;
		//h = (h*FNV1_PRIME_64)%2^64 can't do 2^64
		h = (h*FNV1_PRIME_64);	
		return h;
	}
	
	public String[] docNames(){
		return this.docNames;
	}
	
	public ArrayList<String> nearDuplicates(String docName){
		ArrayList<String> ret = new ArrayList<>();
		HashSet<String> simDocs = new HashSet<>();
		int[] indexes = this.docIndex.get(docName);
		for(int i = 0; i < indexes.length; i++){
			ArrayList<String> nearDups = this.tables[i].get(indexes[i]);
			for(String s: nearDups){
				if(!simDocs.contains(s)){
					simDocs.add(s);
					ret.add(s);
				}
			}
		}
		return ret;
	}
	

}


