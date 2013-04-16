package testShizz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fileIO.DirectoryHandler;

public class ReducerSetupTest {

	
	public static void main(String args[]) {
//		ArrayList<String> files = getAllFiles(".", ".txt");
//		for (String f : files) {
//			System.out.println(f);
//		}
//		 
//		Set<String> keys = getKeys(files);
//		for (String k : keys) {
//			System.out.println(k);
//		}
//		
		
		DirectoryHandler dh = new DirectoryHandler();
		dh.collectAllFiles(".", ".", ".txt");
		
		
		
		
	}

	
	public static String extractKey(String filename){
		String[] parts = filename.split("/");
		String key = parts[parts.length - 1];
		return key;
	}
	
	public static Set<String> getKeys(ArrayList<String> allFiles) {
		String key;
		Set<String> s = new HashSet<String>();
		for (String filename : allFiles) {
			key = extractKey(filename);
			s.add(key);
		}
		return s;
	}
	
	public static ArrayList<String> getAllFiles (String path, String extension) {
		ArrayList<String> filenames = new ArrayList<String>();
		File[] allFiles = new File(path).listFiles();
		  for(File file: allFiles){
			  if (file.getName().endsWith(extension)){
				  System.out.println(file.getAbsolutePath());
				  filenames.add(file.getAbsolutePath());
			  } else if (file.isDirectory()) {
				  filenames.addAll(getAllFiles(file.getAbsolutePath(), extension));
			  }			 
		  }
		return filenames;
	}	
	
}
