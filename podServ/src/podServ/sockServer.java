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
	
	static double proteinTemp = 0;
	static double vegTemp = 0;
	static double bakeTemp = 0;
	
	static double truck1;
	static double truck2;
	static double truck3;
	
	DecimalFormat totalFormat = new DecimalFormat("#0.00");
	
	fileIO wrfile = new fileIO();
	
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
					
					}else if(clientString.contains("TRANSACTION")) {
						
						wrfile.wrTransactionData(clientString);
						String tokens[] = clientString.split("\\|");
						int amount = Integer.parseInt(tokens[4]);
						if(tokens[3].contains("-"))
							amount *= -1;
						if(users.containsKey(tokens[1]) == true) {
							
							//Order is MUFFIN|SALMON|ASPARAGUS|CHICKEN|SPROUTS|BREAD
							int c = Integer.parseInt(tokens[6]);
							proteinTemp += (4.99 * c);
							int s = Integer.parseInt(tokens[4]);
							proteinTemp += (10.99 * s);
							int a = Integer.parseInt(tokens[5]);
							vegTemp += (2.99 * a);
							int bs = Integer.parseInt(tokens[7]);
							vegTemp += (2.50 * bs);
							int b = Integer.parseInt(tokens[8]);
							bakeTemp += (1.99 * b);
							int m = Integer.parseInt(tokens[3]);
							bakeTemp += (3.69 * m);
							int t = Integer.parseInt(tokens[2]);
							int r = Integer.parseInt(tokens[9]);
							switch(t) {
							
								case 1:
									truck1 += proteinTemp + vegTemp + bakeTemp;
									server.tr1_total.setText("$" + totalFormat.format(truck1));
									break;
									
								case 2:
									truck2 += proteinTemp + vegTemp + bakeTemp;
									server.tr2_total.setText("$" + totalFormat.format(truck2));
									break;
								
								case 3:
									truck3 += proteinTemp + vegTemp + bakeTemp;
									server.tr3_total.setText("$" + totalFormat.format(truck3));
									break;
							
							}
							proteinTotal += proteinTemp;
							server.protein_total.setText("$" + totalFormat.format(proteinTotal));
							vegTotal += vegTemp;
							server.greens_total.setText("$" + totalFormat.format(vegTotal));
							bakeTotal += bakeTemp;
							server.bakery_total.setText("$" + totalFormat.format(bakeTotal));
							proteinTemp = 0;
							vegTemp = 0;
							bakeTemp = 0;
							
							server.t5.setText(String.valueOf("$" + totalFormat.format(proteinTotal + vegTotal + bakeTotal)));
							
							order o = new order(c, s, a, bs, b, m, t, r);
							server.transaction_log.appendText("USER: " + tokens[1] + "|CHICKEN: " + tokens[6] + "\nSALMON: " + tokens[4] + "|ASPARAGUS: " + tokens[5] + "\nSPROUTS: " + tokens[7] + "|BREAD: " + tokens[8] + "\nMUFFIN: " + tokens[3] + "|TRUCK: " + tokens[2] + "|TYPE: " + tokens[9] + "\n");
							(users.get(tokens[1])).addOrder(o);
							//(users.get(tokens[1])).displayOrders();
							
						}
						
					} else if(clientString.contains("LOGIN")) {
						
						String tokens[] = clientString.split("\\|");
						if(users.containsKey(tokens[1]) == true) {
							
							if((users.get(tokens[1])).getPassword() == tokens[2]) {
								
								//Successful login
								
							}else {
								
								//Bad login
								pstream.println("Login Failed. Invalid password.");
								
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
