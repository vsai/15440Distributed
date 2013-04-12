package hadoop;

import java.util.AbstractMap;
import java.util.ArrayList;

public class OutputCollector<K, V> {

	ArrayList<AbstractMap.SimpleEntry<K,V>> collector;
	
	
	public OutputCollector () {
		collector = new ArrayList<AbstractMap.SimpleEntry<K, V>>();
	}
	
	public void collect(AbstractMap.SimpleEntry<K,V> entry) {
		collector.add(entry);
	}
	
}
