package src;

import java.util.BitSet;
import java.lang.Math;

public class BloomFilterFNV extends AbstractFilter{
	
	private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 0x100000001b3L;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
		super(setSize, bitsPerElement);
		this.name = "BloomFilterFNV";
		long b = FNV1_64_INIT;
		long a = FNV1_PRIME_64;
	}
    
	@Override
	public void add(String s) {
		String temp = s;
		temp.toLowerCase();
		
		//Each iteration shift the string by one character putting the first character at the back
		for(int i = 0; i < this.numHashes(); i++){
			long h = FNV1_64_INIT;
			//shift string for every iteration
			for(int j = i; j < temp.length(); j++){
				h = getHash(temp.charAt(j),h);
			}
			for(int k = 0; k<i; k++){
				h = getHash(temp.charAt(k),h);
			}
			h = Math.abs(h%this.filterSize());
			int a = this.filterSize();
			this.setBit((int)h);
		}
		this.datasize++;
		//% result by filter size and select next hash
	}
	
	private static long getHash(char c, long h){
			//h = h XOR c
			h = h ^ c;
			//h = (h*FNV1_PRIME_64)%2^64 can't do 2^64
			h = (h*FNV1_PRIME_64);	
			return h;
	}

	@Override
	public boolean appears(String s) {
		String temp = s;
		temp.toLowerCase();
		for(int i = 0; i < this.numHashes(); i++){
			long h = FNV1_64_INIT;
			//shift string for every iteration
			for(int j = i; j < temp.length(); j++){
				h = getHash(temp.charAt(j),h);
			}
			for(int k = 0; k<i; k++){
				h = getHash(temp.charAt(k),h);
			}
			h = Math.abs(h%this.filterSize());
			if(!this.getBit((int) h)){
				return false;
			}
		}
		return true;
	}
}
