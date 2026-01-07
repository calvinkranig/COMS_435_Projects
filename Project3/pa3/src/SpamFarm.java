import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SpamFarm {
	
	private String edgeFile;
	private int target;
	private int numSpamPages;
	
	public SpamFarm(String edgeFile, int target, int numSpamPages) {
		this.edgeFile = edgeFile;
		this.target = target;
		this.numSpamPages = numSpamPages;
	}
	
	public void CreateSpam(String fileName){
		ProcessEdgeFile.writeEdgeFile(this.edgeFile, fileName,this.target,this.numSpamPages);
	}
	
	
}
