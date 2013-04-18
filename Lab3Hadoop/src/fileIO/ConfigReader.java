package fileIO;

import hadoop.MasterWrapper;
import hadoop.SlaveWrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigReader {
	
	final static String tempMapFiles = "./TempMapFiles/";
	final static String resultFiles = "./ResultFiles/";
	final static String configFile = "./src/config.txt";
	
	public ConfigReader() {
		
	}
	
	public ArrayList<SlaveWrapper> readSlaves() {
		BufferedReader in;
		String line;
		String ip;
		String role;
		int runport;
		ArrayList<SlaveWrapper> s = new ArrayList<SlaveWrapper>();
		try {
			in = new BufferedReader(new FileReader(configFile));	
			while ((line = in.readLine()) != null) {
				String[] linears = line.split("\t");
				if (linears.length > 2) {
					ip = linears[0];
					role = linears[1];
					runport = Integer.parseInt(linears[2]);
					if (role.equals("MASTER")) {
						s.add(new SlaveWrapper(ip, runport));
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public MasterWrapper readMaster() {
		BufferedReader in;
		String line;
		String ip;
		String role;
		int runport;
		MasterWrapper m = null;
		
		try {
			in = new BufferedReader(new FileReader(configFile));	
			while ((line = in.readLine()) != null) {
//				System.out.println(line);
				String[] linears = line.split("\t");
				if (linears.length > 2) {
					ip = linears[0];
					role = linears[1];
//					System.out.println(ip);
//					System.out.println(role);
					runport = Integer.parseInt(linears[2]);
//					System.out.println(runport);
					if (role.equals("MASTER")) {
						System.out.println("YES GOT A MASTER");
						m = new MasterWrapper(ip, runport);
						return m;
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public static String getTempmapfiles() {
		return tempMapFiles;
	}

	public static String getResultfiles() {
		return resultFiles;
	}
}