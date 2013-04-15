package fileIO;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class RecordReader {

	LineNumberReader lnr;
	String filename;
	int lineNumber;
	
	public RecordReader(String filename) {
		this.filename = filename;
		lineNumber = 0;
	}
	
	public int fileNumberOfLines() throws IOException {
		lnr = new LineNumberReader(new FileReader(filename));
		lnr.skip(Long.MAX_VALUE);
		int maxNumLine = lnr.getLineNumber();
		lnr.close();
		return maxNumLine;
	}
	
	public String readRecord() throws IOException {
		lnr = new LineNumberReader(new FileReader(filename));
		lnr.setLineNumber(lineNumber);
		String line = lnr.readLine();
		lnr.close();
		return line;
	}
	
}
