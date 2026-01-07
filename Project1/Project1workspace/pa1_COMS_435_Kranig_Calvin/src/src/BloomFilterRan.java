package src;

import java.util.Random;

public class BloomFilterRan extends AbstractFilter{
	
	protected Pair[] hashes;

	public BloomFilterRan(int setSize, int bitsPerElement) {
		super(setSize, bitsPerElement);
		this.hashes = generateHashes(this.numHashes());
		this.name = "BloomFilterRan";
	}
	
	private Pair[] generateHashes(int numberofHashes){
		Pair[] ret = new Pair[numberofHashes];
		Random r = new Random();
		for(int i = 0; i < numberofHashes; i++){
			int a = r.nextInt(this.filterSize());
			int b = r.nextInt(this.filterSize());
			ret[i] = new Pair(a,b);
		}
		return ret;
	}

	@Override
	public void add(String s) {
		String temp = s;
		temp.toLowerCase();
		int hashS = temp.hashCode();
		for(int i = 0; i <this.numHashes(); i++){
			long h = Math.abs((this.hashes[i].a*hashS+this.hashes[i].b)%this.filterSize());
			this.setBit((int)h);;
		}
		this.datasize++;
	}

	@Override
	public boolean appears(String s) {
		String temp = s;
		temp.toLowerCase();
		int hashS = temp.hashCode();
		for(int i = 0; i <this.numHashes(); i++){
			long h = Math.abs((this.hashes[i].a*hashS+this.hashes[i].b)%this.filterSize());
			if(!this.getBit((int)h)){
				return false;
			}
		}
		return true;
	}
	
	
	private class Pair{
		private int a,b;
		Pair(int a, int b){
			this.a = b;
			this.a = b;
		}
	}
	

}


