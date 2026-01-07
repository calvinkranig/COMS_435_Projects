package src;

import java.util.BitSet;

public abstract class AbstractFilter implements Filter{
	private BitSet filter;
	protected int datasize;
	private int numHashes;
	protected String name;
	private int filtersize;
	
	public AbstractFilter(int setSize, int bitsPerElement){
		//set filter size to next prime greater than setSize*bitsPerElement
		this.filtersize = nextPrime(setSize *bitsPerElement);
		filter = new BitSet(this.filtersize);
		//For debugging
		int a = filter.size();
		numHashes = (int)(Math.log(2)*filter.size()/setSize);
		datasize =0;
		name = "";
	}
	
	private static int nextPrime(int n){
		while(!isPrime(n)){
			n+=1;
		}
		return n;
	}
	
	private static boolean isPrime(int n){
		for(int i = 2; i < (int) Math.sqrt(n); i++){
			if(n%i ==0){
				return false;
			}
		}
		return true;
	}
	
	public abstract void add(String s);
	
	public abstract boolean appears(String s);
	
	public int filterSize(){
		return this.filtersize;
	}
	
	public int dataSize(){
		return datasize;
	}
	
	public int numHashes(){
		return numHashes;
	}
	
	public boolean getBit(int j){
		return this.filter.get(j);
	}
	
	protected void setBit(int j){
		this.filter.set(j);
	}
	
	public String name(){
		return this.name;
	}
}
