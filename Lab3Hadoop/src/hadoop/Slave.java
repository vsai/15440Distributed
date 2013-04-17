package hadoop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import messageProtocol.InitiateConnection;
import messageProtocol.Job.InputType;
import messageProtocol.MapMessage;
import messageProtocol.MapResult;
import messageProtocol.ReduceMessage;
import messageProtocol.ReduceResult;

import fileIO.ConfigReader;
import fileIO.RecordReader;
import fileIO.DirectoryHandler;

public class Slave extends Thread {
	
	public final String tempFileDirectory="";
	String ipAddress;
	int portnum;
	
	public Slave (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
	}

	public MapResult mapper(MapMessage mapMessage) {
		try {
		URL classUrl;
    	classUrl = new URL(mapMessage.getclassURL());
    	URL[] classUrls = { classUrl };
    	URLClassLoader ucl = new URLClassLoader(classUrls);
    	Class c = ucl.loadClass(mapMessage.getClassName());
    	Constructor cotr = c.getConstructors()[0];
    	Method [] methods = c.getMethods();
    	Method mapMethod=null;
    	for(Method m :methods){
    		if(m.getName().equals("map"))
    			mapMethod=m;
    	}
    	int startLine = mapMessage.getStartSeek();
    	int numLines= mapMessage.getNumLines();
    	OutputCollector output = new OutputCollector();
    	InputType type = mapMessage.getInputType();
    	String fileName=mapMessage.getInputFile();
    	LineNumberReader lnr = new LineNumberReader(new FileReader(fileName));
    	lnr.setLineNumber(startLine);
    	String line=null;
    	Object inst = cotr.newInstance();
    	String key,value;
    	for(int i=0; i <numLines;i++){
    		line=lnr.readLine();
    		//
    		if(type == InputType.KEYVALUE){
    			String [] keyValuePair = line.split("/t");
    			key = keyValuePair[0];
    			value = keyValuePair[1];
    		}
    		else {
    			 Integer num = i + startLine;
    			key=fileName+"_"+num.toString();
    			value=line;
    		}
    			Object [] args = {key,value,output};
    			mapMethod.invoke(inst, args);
    		
    	}
    	//Writing mapped key values to temp files
    	String jobName =mapMessage.getJobName();
    	int partitionNum = mapMessage.getPartitionNum();
    	String filePath = mapMessage.getJobDirectory()+"/"+partitionNum;
    	boolean success = (new File(filePath)).mkdirs();
    	
    	//Making the dir failed
    	if(!success){
    		return (new MapResult(null,false,-1,null));
    	}
    	
    	ArrayList<Tuple<String,String>> data = output.getData();
    	for(Tuple<String,String> tup: data){
    		key= tup.getX();
    		value= tup.getY();
    		
    		File file =new File(filePath+"/"+key+".txt");
    		 //if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    		
    		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(value+"/n");
	        bufferWritter.close();
    	}
    	return (new MapResult(filePath,true,partitionNum,jobName));
		}
		catch(Exception e){
			return (new MapResult(null,false,-1,null));
		}
    	//file = jobName/partitionNum/key.txt
    	
    	//write to files from outputcollector
    	//then return mapResult
    	
	}
	
	

	public ReduceResult reducer(ReduceMessage reduceMessage){
		try{
			URL classUrl;
	    	classUrl = new URL(reduceMessage.getClassDirectory());
	    	URL[] classUrls = { classUrl };
	    	URLClassLoader ucl = new URLClassLoader(classUrls);
	    	Class c = ucl.loadClass(reduceMessage.getClassName());
	    	Constructor cotr = c.getConstructors()[0];
	    	Method [] methods = c.getMethods();
	    	Method reduceMethod=null;
	    	for(Method m :methods){
	    		if(m.getName().equals("reduce"))
	    			reduceMethod=m;
	    	}
			
			ArrayList<String> fileNames = DirectoryHandler.getAllFiles(reduceMessage.getJobTmpFileDirectory(), ".txt");
			String key;
			String val;
			DataInputStream in;
			BufferedReader br;
			OutputCollector output = new OutputCollector();
			ArrayList<String> values = new ArrayList<String>();
			for(String fileName : fileNames){
				key = DirectoryHandler.extractKey(fileName);
				FileInputStream fstream = new FileInputStream(fileName);
				// Get the object of DataInputStream
				in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				
				val="";
				values.clear();
				while ((val = br.readLine()) != null)   {
					values.add(val);
				}
				Object [] args = {key,values.iterator(),output};
				reduceMethod.invoke(cotr, args);
				
			}
			
			writeOutputToFile(output,reduceMessage.getOutputFile());
			return (new ReduceResult(true,reduceMessage.getOutputFile(), reduceMessage.getJobName()));
		}
		catch(Exception e){
			return (new ReduceResult(true,null,null));
		}
	}
	
	public void writeOutputToFile(OutputCollector output, String outputFile) throws IOException{
		FileWriter fileWritter = new FileWriter(outputFile);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		String key,value;
		ArrayList<Tuple<String, String>> data = output.getData();
		for(Tuple<String, String> tup : data){
			key=tup.getX();
			value=tup.getY();
			bufferWritter.write(key+"/t"+value);
		}
        bufferWritter.close();
		
	}
	public void run() {
		ConfigReader cread = new ConfigReader();
		MasterWrapper m = cread.readMaster();
		Socket toMaster;
		InitiateConnection initConn = new InitiateConnection(ipAddress, portnum);
		ObjectInputStream in;
		ObjectOutputStream out;
		Object inobj;
		try {
			toMaster = new Socket(m.ipAddress, m.portnum);
			in = new ObjectInputStream(toMaster.getInputStream());
			out = new ObjectOutputStream(toMaster.getOutputStream());
			out.writeObject(initConn);
			
			while (true) {
				try {
					inobj = in.readObject();
					if (inobj instanceof MapMessage) {
						MapResult mr = mapper((MapMessage) inobj); 
						out.writeObject(mr);
					} else if (inobj instanceof ReduceMessage) {
						ReduceResult rr = reducer((ReduceMessage) inobj);
						out.writeObject(rr);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
