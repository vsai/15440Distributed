package hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public interface Writable extends Serializable{


	void write(DataOutput out) throws IOException;
	
	void readFields(DataInput in) throws IOException;
	
}
