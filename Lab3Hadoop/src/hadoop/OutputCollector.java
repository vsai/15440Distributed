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
	
	ArrayList<Tuple<Writable, Writable>>  data = new ArrayList<Tuple<Writable, Writable>>();
	
	public void collect(Writable key, Writable value){	
		data.add(new Tuple<Writable, Writable>(key,value));
	}
	
	public ArrayList<Tuple<Writable, Writable>> getData(){ 
		return data;
	}
}
