//Zachary Dulac, Trung Nguyen, Abby Tse
//Implementation of user class, which represents a user of the system, including that user's cart.
package podServ;

public class user {

	private static String uname;
	private static String password;
	int cNum;
	int sNum;
	int aNum;
	int bsNum;
	int bNum;
	int mNum;
	
	
	user(String u, String p){
		
		uname = u;
		password = p;
		
	}
	
	String getUname() {
		
		return uname;
		
	}
	
	String getPassword() {
		
		return password;
		
	}
	
	void setUname(String u) {
		
		uname = u;
		
	}
	void setPassword(String p) {
		
		password = p;
		
	}

	//Functions to modify values of the items in the user's cart.
	//TODO: Make functions return something instead of just doing nothing when the operation is invalid.
	void changeC(int val) {
		
		if(cNum + val >= 0)
			cNum += val;
		
	}
	
	void changeS(int val) {
		
		if(sNum + val >= 0)
			sNum += val;
		
	}
	
	void changeA(int val) {
		
		if(aNum + val >= 0)
			aNum += val;
		
	}
	
	void changeBS(int val) {
		
		if(bsNum + val >= 0)
			bsNum += val;
		
	}
	
	void changeB(int val) {
		
		if(bNum + val >= 0)
			bNum += val;
		
	}
	
	void changeM(int val) {
		
		if(mNum + val >= 0)
			mNum += val;
		
	}
}
