package hadoop;
import hadoop.SlaveWrapper.Status;


import java.util.Iterator;
import java.util.List;

public class SOList implements Iterable {

    private  List<SlaveWrapper> arrayList;

    public SOList(List<SlaveWrapper> newArray) {
        this.arrayList = newArray;
    }

    @Override
    public Iterator<SlaveWrapper> iterator() {
        Iterator<SlaveWrapper> it = new Iterator<SlaveWrapper>() {


            @Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}
            
            @Override
            public SlaveWrapper next() {
            	while(true){
            		for(SlaveWrapper s : arrayList){
            			if(s.status == Status.CHILLIN)
            				return s;
            		}
            	}
            }

            public void removeSlave(SlaveWrapper s) {
            	arrayList.remove(s);
                // TODO Auto-generated method stub
            }

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}

			
        };
        return it;
    }
}