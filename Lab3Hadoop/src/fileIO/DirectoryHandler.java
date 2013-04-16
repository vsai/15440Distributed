package fileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;

public class DirectoryHandler {

	public DirectoryHandler() {
		
	}
	
	public void createDirectory (String directoryPath) {
		
	}

	public boolean collectSubsetFiles(String destFilename, ArrayList<String> filenames) throws IOException {
		FileWriter filestream = new FileWriter(destFilename, true); 
		BufferedWriter out = new BufferedWriter(filestream);
		
		for (String f : filenames) {
			FileInputStream fstream = new FileInputStream(f);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null)   {
				out.write(strLine);
				
			}	
		}
		//Close the output stream
		out.close();
		System.out.println("Wrote to: " + destFilename);
		return true;
	}
	
	public boolean collectAllFiles(String destDir, String sourceDir, String extension) {
		ArrayList<String> files = getAllFiles(sourceDir, extension);
		HashMap<String, ArrayList<String>> allMap = getKeys(files);
		
		for (String key : allMap.keySet()){
			try {
				collectSubsetFiles(destDir + "/" + key, allMap.get(key));
			} catch (IOException e) {
				System.err.println("Error collecting files for extensions of: " + key);
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static String extractKey(String filename){
		String[] parts = filename.split("/");
		String key = parts[parts.length - 1];
		return key;
	}
	
	public static HashMap<String, ArrayList<String>> getKeys(ArrayList<String> allFiles) {
		String key;
		HashMap <String, ArrayList<String>> keyFiles = new HashMap<String, ArrayList<String>>();
		
		for (String filename : allFiles) {
			key = extractKey(filename);
			if (keyFiles.containsKey(key)) {
				keyFiles.get(key).add(filename);
			} else {
				ArrayList<String> fs = new ArrayList<String>();
				fs.add(filename);
				keyFiles.put(key, fs);
			}
		}
		return keyFiles;
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