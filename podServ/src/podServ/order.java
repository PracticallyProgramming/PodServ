package podServ;

public class order {

	int cNum;
	int sNum;
	int aNum;
	int bsNum;
	int bNum;
	int mNum;
	
	int truck;
	
	int type;
	
	order(int c, int s, int a, int bs, int b, int m, int t, int r) {
		
		cNum = c;
		sNum = s;
		aNum = a;
		bsNum = bs;
		bNum = b;
		mNum = m;
		truck = t;
		type = r;
		
	}
	
	void display() {
		
		server.transaction_log.appendText(cNum + "|" + sNum + "|" + aNum + "|" + bsNum + "|" + bNum + "|" + mNum + "|" + truck + "|" + type);
		
	}
	
}
