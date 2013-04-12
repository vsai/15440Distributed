package messageProtocol;

import hadoop.OutputCollector;
import hadoop.Writable;

public class Map {

	public void map(Writable key, Writable value, OutputCollector output){
		Writable one = null;
		output.collect(value, one);
	}
}
