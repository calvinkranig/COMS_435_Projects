package src;

public final class Statistics {
	public static int estimateSetSize(BloomFilterFNV f){
		//Expect m(1-1/m)^(kn) to remain zero where 
		//m = filter size k = hash functions and n = number of elements
		int m = f.filterSize();
		int k = f.numHashes();
		int zeroes = numZeroes(f);
		//zeroes = m(1-1/m)^(kn) -> (zeroes/m) = (1-1/m)^kn
		//log(base = (1-1/m))(zeroes/m) = kn
		double a = 1-1/((double) m);
		double b = ((double) zeroes)/((double) m);
		double x = (Math.log(b)/Math.log(a));
		int n = (int) (x/k);
		return n;
	}
	
	public static int estimateIntersectSize(BloomFilterFNV f1, BloomFilterFNV f2){
		if(f1.filterSize()!= f2.filterSize()){
			//Returns -1 to show error if set sizes are not equal
			return -1;
		}
		int z1 = numZeroes(f1);
		int z2 = numZeroes(f2);
		int m = f1.filterSize();
		int k = f1.numHashes();
		int z = 0;
		for(int i = 0; i < f1.filterSize(); i++){
			//If not both 1 then zero
			if(!(f1.getBit(i)&&f2.getBit(i))){
				z+=1;
			}
		}
		double x = (((double)z1+(double)z2-(double)z))/((double)z1*(double)z2);
		double a1 = x*(double)m;
		double a = Math.log(a1);
		double c = 1-1/(double)m;
		double b = Math.log(c);
		int t = (int) -((a/b)/(double)k);
		return t;
	}
	
	private static int numZeroes(BloomFilterFNV f){
		int zeroes = 0;
		for(int i = 0; i < f.filterSize(); i++){
			if(!f.getBit(i)){
				zeroes+=1;
			}
		}
		return zeroes;
	}
}
