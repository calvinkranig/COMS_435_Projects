import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProcessEdgeFile {
	
	@SuppressWarnings("resource")
	public static ArrayList<Integer>[] getEdges(String filename, HashMap<String, Integer>pages){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			//first line is number of nodes
			String line = br.readLine();
			String[] words = line.split("\\s+");
			int vertices = Integer.parseInt(words[0]);
			ArrayList<Integer>[] edges = initLists(vertices);
			//check to see if ints or string vertices
			line = br.readLine();
			if(line == null){return edges;}
			words = line.split("\\s+");
			if(isNumber(words[0]) && isNumber(words[1])){
				int src = Integer.parseInt(words[0]);
				int dst = Integer.parseInt(words[1]);
				edges[src-1].add(dst-1);
				getEdgesIntegers(br,edges);
			}else{
				if(pages == null){
					pages = new HashMap<>();
				}
				int i0, i1;
				i0 = 1;
				pages.put(words[0], i0);
				i1 = 2;
				pages.put(words[1], i1);
				edges[i0-1].add(i1-1);
				getEdgesStrings(br,edges,pages);
			}
			br.close();
			return edges;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static void getEdgesIntegers(BufferedReader br, ArrayList<Integer>[] edges){
		String line;
		try {
			while((line = br.readLine())!=null){
				String[] words = line.split("\\s+");
				edges[Integer.parseInt(words[0])-1].add(Integer.parseInt(words[1])-1);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getEdgesStrings(BufferedReader br,ArrayList<Integer>[] edges, HashMap<String, Integer> indexes){
		String line;
		
		try {
			while((line = br.readLine())!=null){
				String[] words = line.split("\\s+");
				int i0, i1;
				if(!indexes.containsKey(words[0])){
					i0 = indexes.size()+1;
					indexes.put(words[0], i0);
				}else{
					i0 = indexes.get(words[0]);
				}
				if(!indexes.containsKey(words[1])){
					i1 = indexes.size()+1;
					indexes.put(words[1], i1);
				}else{
					i1 = indexes.get(words[1]);
				}
				edges[i0-1].add(i1-1);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<Integer>[] initLists(int vertices) {
		ArrayList<Integer>[] ret = new ArrayList[vertices];
		for(int i = 0; i < vertices; i++){
			ret[i] = new ArrayList<Integer>();
		}
		return ret;
	}

	private static boolean isNumber(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(NumberFormatException  e){
			return false;
		}
	}
	
	

	public static void writeEdgeFile(String src, String dst,int target,int numSpamPages) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(src)));
			BufferedWriter out = new BufferedWriter(new FileWriter(dst, false)); 
			
			//first line is number of nodes
			String line = br.readLine();
			String[] words = line.split("\\s+");
			int vertices = Integer.parseInt(words[0]);
			int newVertices = vertices+numSpamPages;
			out.write(""+ newVertices +"\n");
			//copy file
			while((line = br.readLine())!=null){
				out.write(line +"\n");
			}
			for(int i = 1; i<=numSpamPages; i++){
				int vertex = vertices + i;
				out.write(""+target + " " + vertex + "\n");
				out.write(""+vertex + " " + target + "\n");
			}
			br.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeEdgeFile(ArrayList<Integer>[] edges, String dst) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dst, true)); 
			out.write(edges.length + "\n");
			for(int i = 0; i < edges.length; i++){
				for(Integer edge : edges[i]){
					out.write(""+ i + " " + edge + "\n");
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
