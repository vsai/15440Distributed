package hadoop;


import java.util.ArrayList;

public class OutputCollector {
	
	ArrayList<Tuple<Writable, Writable>>  data = new ArrayList<Tuple<Writable, Writable>>();
	
	public void collect(Writable key, Writable value){
		
		data.add(new Tuple<Writable, Writable>(key,value));
	}
	
	public ArrayList<Tuple<Writable, Writable>> getData(){return data;}
}
