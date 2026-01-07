
import java.util.ArrayList;
import java.util.Comparator;

public class PostingsListItem implements Comparable<PostingsListItem> {
	private String DocName;
	private ArrayList<Integer> positions;
	private static final Comparator<PostingsListItem> comparator = new Comparator<PostingsListItem>() {
		@Override
		public int compare(PostingsListItem o1, PostingsListItem o2) {
			return o1.compareTo(o2);
		}   
    };

	public PostingsListItem(String docName) {
		DocName = docName;
		this.positions = new ArrayList<Integer>();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("<" + DocName + ":");
		for (int i : positions) {
			s.append(i + ",");
		}
		s.setCharAt(s.lastIndexOf(","), '>');
		String ret = s.toString();
		return ret;
	}

	public String DocName() {
		return DocName;
	}

	public ArrayList<Integer> positions() {
		return positions;
	}
	
	public static Comparator<PostingsListItem> Comparator(){
		return comparator;
	}

	@Override
	public int compareTo(PostingsListItem o) {
		return this.DocName.compareTo(o.DocName);
	}

}