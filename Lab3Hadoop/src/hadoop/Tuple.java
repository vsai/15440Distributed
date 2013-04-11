package hadoop;

public class Tuple<X,Y> {

	public X x; 
	public Y y; 
	public Tuple(X x, Y y) { 
		this.x = x; 
		this.y = y; 
	} 
	public X getX() {return x;}
	public Y getY() {return y;}
		
}
