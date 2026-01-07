
public class Prime {
	public static int nextPrime(int n){
		while(!isPrime(n)){
			n+=1;
		}
		return n;
	}
	
	public static boolean isPrime(int n){
		for(int i = 2; i < (int) Math.sqrt(n); i++){
			if(n%i ==0){
				return false;
			}
		}
		return true;
	}
}
