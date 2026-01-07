
public class Prime {
	public static int nextPrime(int n){
		int ret = n;
		while(!isPrime(ret)){
			ret+=1;
		}
		return ret;
	}
	
	private static boolean isPrime(int n){
		for(int i = 2; i < (int) Math.sqrt(n); i++){
			if(n%i ==0){
				return false;
			}
		}
		return true;
	}
}
