package podServ;

import java.io.IOException;
import java.io.BufferedReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import java.io.InputStreamReader;
import java.io.PrintStream;

public class sockServer implements Runnable {
	
	//Socket and thread declarations
	Socket csocket;
	String ipstring;
	char threadType;
	
	//A vector to hold the five thread IDs.
	static Vector<String> vec = new Vector<String>(5);
	
	//A hash table detailing all of the users of the system.
	public static Hashtable<String, user> users = new Hashtable<String, user>();
	
	static final String newline = "\n";
	static int first_time = 1;

	static int port_num = 3333;
	   
	static int numOfConnections = 0;
	static int numOfMessages = 0;
	static int max_connections = 5;
	static int numOfTransactions = 0;
	
	static double proteinTotal = 0;
	static double vegTotal = 0;
	static double bakeTotal = 0;
	DecimalFormat totalFormat = new DecimalFormat("#0.00");
	
	sockServer(Socket csocket, String ip){
		this.csocket  = csocket;
		this.ipstring = ip;
	}
	
	public static void runSockServer() {
		
		boolean sessionDone = false;
		ServerSocket ssock = null;
		
		try{
			
			ssock = new ServerSocket(port_num);
		
		}catch(BindException e) {
			
			e.printStackTrace();
			
		}catch(IOException e) {
			
			e.printStackTrace();
			
		}
		try{
			InetAddress ipaddress = InetAddress.getLocalHost();
			server.users_log.appendText("IP Address: " + ipaddress.getHostAddress() + newline);
		}catch(UnknownHostException e1) {
			
			e1.printStackTrace();
			
		}
		
		server.users_log.appendText("Listening on port " + port_num + newline);
		
		//Hash Table of users.
		users.put("Zak", new user("Zak", "verysecurepassword"));
		users.put("Trung", new user("Trung", "ultrasecurepassword"));
		users.put("Abby", new user("Abby", "hahathisonesdifferent"));
				
		while(sessionDone == false) {
			
			Socket sock = null;
			try {
				
				sock = ssock.accept();
				
			}catch(IOException e) {
				
				e.printStackTrace();
				
			}
			
			server.users_log.appendText("Client Connected : " + sock.getInetAddress() + newline);
			new Thread(new sockServer(sock, sock.getInetAddress().toString())).start();
			
		}
		try {
			
			ssock.close();

		}catch(IOException e) {
			
			e.printStackTrace();
			
		}

	}
	
	//Takes a character representing the intended operation, finds the entry corresponding
	//to the given key, and calls the appropriate function to change the user's current
	//amount of the item.
	static synchronized void hashOperation(char type, String key, int val){
		
		switch (type){
		
			//Chicken
			case 'C':
				if (users.containsKey(key) == true){
					
					users.get(key).changeC(val);
		        }	
			break;
			
			//Salmon
			case 'S':
				if(users.containsKey(key) == true) {
					
					users.get(key).changeS(val);
					
				}
			break;
			
			//Asparagus
			case 'A':
				if(users.containsKey(key) == true) {
					
					users.get(key).changeA(val);
					
				}
			break;
			
			//Brussels Sprouts
			case 'R':
				if(users.containsKey(key) == true) {
					
					users.get(key).changeBS(val);
					
				}
			break;
			
			//Bread
			case 'B':
				if(users.containsKey(key) == true) {
					
					users.get(key).changeB(val);
					
				}
			break;
			
			//Muffins
			case 'M':
				if(users.containsKey(key) == true) {
					
					users.get(key).changeM(val);
					
				}
			break;
		}
	}
	
	//Individual thread code.
	public void run() {
		
		try {
			
			boolean sessionDone = false;
			long threadid;
			String clientString;
			String keyString = "";
			
			threadid = Thread.currentThread().getId();
			numOfConnections++;
			server.users_log.appendText("Num of Connections = " + numOfConnections + newline);
			server.t3.setText(String.valueOf(numOfConnections));
			keyString = ipstring + ":" + threadid;
			
			if(vec.contains(keyString) == false) {
				
				int counter = 0;
				vec.addElement(keyString);
				
				server.users_log.setText("");
	        	Enumeration<String> en = vec.elements();
	        	while (en.hasMoreElements()){
	        		server.users_log.appendText(en.nextElement() + newline);
	        		
	        		if (++counter >= 6)
	        		{
	        			server.users_log.appendText("\r\n");
	        			counter = 0;
	        		}
	        	}
				
			}
			
			PrintStream pstream = new PrintStream (csocket.getOutputStream());
			BufferedReader rstream = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
			
			while(sessionDone == false) {
				
				//If there is input from the client, fetch it and save it.
				if(rstream.ready()) {
					
					clientString = rstream.readLine();
					
					//Reads inputs from client in the form of OPERATION|username|ITEM|±|VALUE
					if(clientString.length() > 128) {
						
						sessionDone = true;
						continue;
						
					}
					if(clientString.contains("QUIT")) {
						
						sessionDone = true;
					
						//TODO: Probably change this system to accept the whole user cart at once.
						//TODO: Create an order class that stores the purchased items as an order.
					}else if(clientString.contains("TRANSACTION")) {
						
						String tokens[] = clientString.split("\\|");
						int amount = Integer.parseInt(tokens[4]);
						if(tokens[3].contains("-"))
							amount *= -1;
						if(users.containsKey(tokens[1]) == true) {
							
							if(tokens[2].contains("CHICKEN")) {
								
								hashOperation('C', tokens[1], amount);
								proteinTotal += (4.99 * amount);
								server.protein_total.setText("$" + totalFormat.format(proteinTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));
								//TODO: Color this text. No time right now.
								server.transaction_log.appendText("CHICKEN" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).cNum) + newline);
								
							}else if(tokens[2].contains("SALMON")) {
								
								hashOperation('S', tokens[1], amount);
								proteinTotal += (10.99 * amount);
								server.protein_total.setText("$" + totalFormat.format(proteinTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));								server.transaction_log.appendText("SALMON" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).sNum) + newline);
								
							}else if(tokens[2].contains("ASPARAGUS")) {
								
								hashOperation('A', tokens[1], amount);
								vegTotal += (2.99 * amount);
								server.greens_total.setText("$" + totalFormat.format(vegTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));								server.transaction_log.appendText("ASPARAGUS" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).aNum) + newline);
								
							}else if(tokens[2].contains("SPROUTS")) {
								
								hashOperation('R', tokens[1], amount);
								vegTotal += (2.50 * amount);
								server.greens_total.setText("$" + totalFormat.format(vegTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));								server.transaction_log.appendText("SPROUTS" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).bsNum) + newline);
								
							}else if(tokens[2].contains("BREAD")) {
								
								hashOperation('B', tokens[1], amount);
								bakeTotal += (1.99 * amount);
								server.bakery_total.setText("$" + totalFormat.format(bakeTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));								server.transaction_log.appendText("BREAD" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).bNum) + newline);
								
							}else if(tokens[2].contains("MUFFIN")) {
								
								hashOperation('M', tokens[1], amount);
								bakeTotal += (3.69 * amount);
								server.bakery_total.setText("$" + totalFormat.format(bakeTotal));
								server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));								server.transaction_log.appendText("MUFFIN" + tokens[3] + tokens[4]);
								server.transaction_log.appendText("|| NEW VALUE: " + String.valueOf(users.get(tokens[1]).mNum) + newline);
								
							}
							
						}
						
					}
					
				}
				
				Thread.sleep(500);
				
			}
			
			keyString = ipstring + ":" + threadid;
		      
	        if (vec.contains(keyString) == true)
	        {
	        	int counter = 0;
	        	vec.removeElement(keyString);
	        	
	        	server.users_log.setText("");
	        	Enumeration<String> en = vec.elements();
	        	while (en.hasMoreElements())
	        	{        		     		
                    server.users_log.appendText(en.nextElement() + " || ");
	        		
	        		if (++counter >= 6)
	        		{
	        			server.users_log.appendText("\r\n");
	        			counter = 0;
	        		}
	        	}
	        }
	      
	        numOfConnections--;
	        server.t3.setText(String.valueOf(numOfConnections));

	        // close client socket
	        csocket.close();
	       
	        // update the status text area to show progress of program
		     server.users_log.appendText("Child Thread : " + threadid + " : is Exiting!!!" + newline);
		     server.users_log.appendText("Num of Connections = " + numOfConnections);
		     			
		}catch(SocketException e) {
			
			server.users_log.appendText("ERROR: SOCKET EXCEPTION" + newline);
			
		}catch(UnknownHostException e) {
			
			server.users_log.appendText("ERROR: UNKNOWN HOST EXCEPTION" + newline);
			
		}catch(IOException e) {
			
			server.users_log.appendText("ERROR: I/O EXCEPTION" + newline);
			
		}catch(Exception e) {
			
			numOfConnections--;
			server.t3.setText(String.valueOf(numOfConnections));
			server.users_log.appendText("ERROR: GENERAL EXCEPTION" + newline);
			
		}
	}


}
