import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessDoc {
	private static final int THREADSPERPROCESS = 3;

	public ProcessDoc() {
	}

	private static void getFiles(String filename, ArrayList<String> docPaths, ArrayList<String> docNames) {
		File fileFolder = new File(filename);
		File[] files = fileFolder.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				docPaths.add(f.getPath());
				docNames.add(f.getName());
			}
		}
	}

	private static boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public int processDocsParallel(String folder, HashMap<String, ArrayList<PostingsListItem>> postingsList) {
		int processors = Runtime.getRuntime().availableProcessors();
		int NUMTHREADS = processors * THREADSPERPROCESS;
		HashSet<String> docs = new HashSet<>();
		List<Callable<Hashtable<String, HashMap<String, PostingsListItem>>>> tasks = new LinkedList<>();
		ArrayList<String> docPaths = new ArrayList<>();
		ArrayList<String> docNames = new ArrayList<>();
		getFiles(folder, docPaths, docNames);
		String[] pathsArr = new String[docPaths.size()];
		String[] namesArr = new String[docNames.size()];
		pathsArr = docPaths.toArray(pathsArr);
		namesArr = docNames.toArray(namesArr);
		int portion = pathsArr.length / NUMTHREADS;
		int remainder = pathsArr.length % NUMTHREADS;
		// Split up work
		for (int left = 0, end = portion; left < pathsArr.length; left = end, end += portion) {
			if (remainder > 0) {
				end++;
				remainder--;
			}
			String[] docPathsubarray = Arrays.copyOfRange(pathsArr, left, end);
			String[] docNamesubarray = Arrays.copyOfRange(namesArr, left, end);
			tasks.add(new Worker1(docPathsubarray, docNamesubarray));
		}

		ExecutorService exec = Executors.newFixedThreadPool(NUMTHREADS);
		try {
			List<Future<Hashtable<String, HashMap<String, PostingsListItem>>>> results = exec.invokeAll(tasks);
			// assemble results
			
			for (Future<Hashtable<String, HashMap<String, PostingsListItem>>> f : results) {
				for (Entry<String, HashMap<String, PostingsListItem>> workertermPostings : f.get().entrySet()) {
					for (Entry<String, PostingsListItem> docPosting : workertermPostings.getValue().entrySet()) {
						// add to postings
						docs.add(docPosting.getKey());
						ArrayList<PostingsListItem> postings = postingsList.get(workertermPostings.getKey());
						// If new term
						if (postings == null) {
							postings = new ArrayList<>();
							postingsList.put(workertermPostings.getKey(), postings);
						}
						postings.add(docPosting.getValue());
					}
				}
			}
			for (Entry<String, ArrayList<PostingsListItem>> e : postingsList.entrySet()) {
				Collections.sort(e.getValue(), PostingsListItem.Comparator());
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		exec.shutdownNow();
		return namesArr.length;
	}

	class Worker1 implements Callable<Hashtable<String, HashMap<String, PostingsListItem>>> {
		private String[] docPathssubarray;
		private String[] docNamesubarray;
		Hashtable<String, HashMap<String, PostingsListItem>> postingslist;

		public Worker1(String[] docPathssubarray, String[] docNamesubarray) {
			this.docPathssubarray = docPathssubarray;
			this.docNamesubarray = docNamesubarray;
			this.postingslist = new Hashtable<>();
		}

		@Override
		public Hashtable<String, HashMap<String, PostingsListItem>> call() {
			for (int i = 0; i < docPathssubarray.length; i++) {
				processDoc(docPathssubarray[i], docNamesubarray[i]);
			}
			return this.postingslist;
		}

		private void processDoc(String path, String docName) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(path)));
				String line;
				// read file line by line
				int docIndex = 0;
				while ((line = br.readLine()) != null) {
					String pline = processLine(line);
					String[] terms = pline.split("\\s+");
					for (String term : terms) {
						HashMap<String, PostingsListItem> posting = this.postingslist.get(term);
						if (posting == null) {
							posting = new HashMap<>();
							this.postingslist.put(term, posting);
						}
						PostingsListItem pli = posting.get(docName);
						if (pli == null) {
							pli = new PostingsListItem(docName);
							posting.put(docName, pli);
						}
						pli.positions().add(docIndex);
						docIndex++;
					}
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private String processLine(String line) {
			String ret = line.toLowerCase();
			String ret1 = ret.replaceAll("[,?\':;\\[\\](){}\"]", " ");
			StringBuilder s = new StringBuilder(ret1);

			int nextIndex = -1;
			while ((nextIndex = s.indexOf(".", nextIndex+1)) >= 0) {
				if (!isNumber("" + s.charAt(nextIndex + 1))
						&& (nextIndex == 0 || !isNumber("" + s.charAt(nextIndex - 1)))) {
					s.deleteCharAt(nextIndex);
				}
			}
			return s.toString();
		}

	}

}
