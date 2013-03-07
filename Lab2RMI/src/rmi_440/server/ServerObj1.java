package rmi_440.server;

class ServerObj1 extends ServerObjects implements ServerObj1Intf{

	String message;
	int score;
	
	public ServerObj1(String objName) {
		super(objName);
		this.message = "Leggo";
		this.score = 10;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void increment() {
		score++;
	}
	
	@Override
	public String returnSameString(String str, int [] ar,int num) {
		String strargs = "";
		for (int i=0; i<ar.length; i++) {
			strargs += ar[i] + " ";
		}
		return str + " ; " + strargs + "; " + num + " ;";
	}

	@Override
	public void setScore(int newScore) {
		this.score = newScore;
	}		
}