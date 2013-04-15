package hadoop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    	classUrl = new URL(mapMessage.getclassDirectory());
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
    		
    		File file =new File(filePath+"/"+key);
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
	
	
	//PROBALY DONT NEED ANYMORE
	private Object[] getArgsForMapMethod(InputType inputType, String inputFile,int startSeek, int numLine) throws IOException {
		Object [] objArr = new Object [3];
		String partitionText = RecordReader.readPartition(startSeek,numLine,inputFile);
		
		return null;
	}

	public ReduceResult reducer(ReduceMessage reduceMessage) {
		//TODO
		return null;
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
