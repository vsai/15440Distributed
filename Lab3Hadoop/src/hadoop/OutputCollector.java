package hadoop;

import java.util.ArrayList;

public class OutputCollector {
	
	ArrayList<Tuple<String, String>>  data = new ArrayList<Tuple<String, String>>();
	
	public void collect(String key, String value){	
		data.add(new Tuple<String, String>(key,value));
	}
	
	public ArrayList<Tuple<String, String>> getData(){ 
		return data;
	}
}
