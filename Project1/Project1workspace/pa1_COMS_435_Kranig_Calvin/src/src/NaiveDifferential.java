package src;

public class NaiveDifferential extends AbstractDifferential{

	public NaiveDifferential(String database, String diffFile, String grams) {
		super(database, diffFile, grams);
	}

	@Override
	public String retreiveRecord(String key) {
		String ret;
		if(!this.keys.contains(key)){
			return "";
		}
		ret = this.getRecordFromFile(key, this.diffFile);
		if(!ret.equals("")){
			return ret;
		}else{
			return this.getRecordFromFile(key, this.database);
		}
	}

}
