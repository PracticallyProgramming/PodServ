//Zachary Dulac, Trung Nguyen, Abby Tse
//Implementation of user class, which represents a user of the system, including that user's cart.
package podServ;

import java.util.Vector;
import java.util.Collections;

public class user {

	private static String uname;
	private static String password;
	
	public Vector<order> orders= new Vector<order>();
	
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
	
	void addOrder(order o) {
		
		orders.addElement(o);
		
	}
	
	void displayOrders() {
		
		for(int i = 0; i < orders.size(); i++) {
			
			order temp = orders.get(i);
			temp.display();
			
		}
		
	}
	
	Vector<order> getOrders(){
		
		return orders;
		
	}
}
