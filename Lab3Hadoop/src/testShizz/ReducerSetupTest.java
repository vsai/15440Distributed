package testShizz;


import fileIO.DirectoryHandler;

public class ReducerSetupTest {

	
	public static void main(String args[]) {		
		DirectoryHandler dh = new DirectoryHandler();
		String jobname = "adslj";
		dh.collectAllFiles("./CompressResults/"+ jobname, "./TempResults/" +jobname, ".txt");	
	}
	
}