package src;

import java.io.File;

public class MainTest {

	final static int NUMSTRINGS = 1000;
	final static int DATABASESIZE = 1000;
	final static double PERCENTDIFFILE = 0.1;
	final static String f1 = "./src/src/jointestf1";
	final static String f2 = "./src/src/jointestf2";
	final static String f3 = "./src/src/jointestf3";
	
	public static void main(String[] args) {
		
		//System.out.println("Filter Test");
		//filterTest();
		//System.out.println("\n\nStatistics Test");
		//StatisticsTest.testEstimateIntersectSize(NUMSTRINGS,100);
		//StatisticsTest.testEstimateSetSize(NUMSTRINGS,100);
		
		System.out.println("\n\nBloomDifferential Test");
		EmpericalComparison.generateTestFiles(DATABASESIZE, PERCENTDIFFILE);
		int toTest = 1000;
		int difFileruns = (int) (toTest*PERCENTDIFFILE);
		EmpericalComparison.compare(difFileruns, toTest-difFileruns);
	}
	
	private static void BloomJoinTest(){
		BloomJoin bj = new BloomJoin(f1, f2);
		bj.join(f3);
	}
	
	public static void filterTest(){
		System.out.printf("Testing Filters of size %d with bits per element %d \n",NUMSTRINGS,4);
		FalsePositives test = new FalsePositives(NUMSTRINGS, 4, NUMSTRINGS);
		test.filterTest();
		System.out.printf("\nTesting Filters of size %d with bits per element %d \n",NUMSTRINGS,8);
		test = new FalsePositives(NUMSTRINGS, 8, NUMSTRINGS);
		test.filterTest();
		System.out.printf("\nTesting Filters of size %d with bits per element %d \n",NUMSTRINGS,10);
		new FalsePositives(NUMSTRINGS, 10, NUMSTRINGS);
		test.filterTest();
	}
	
	public static void statisticsTest(){
		
	}

}
