package hadoop;

//import java.util.AbstractMap;
//import java.util.ArrayList;
//
//public class OutputCollector<K, V> {
//
//	ArrayList<AbstractMap.SimpleEntry<K,V>> collector;
//	
//	
//	public OutputCollector () {
//		collector = new ArrayList<AbstractMap.SimpleEntry<K, V>>();
//	}
//	
//	public void collect(AbstractMap.SimpleEntry<K,V> entry) {
//		collector.add(entry);
//	}

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
