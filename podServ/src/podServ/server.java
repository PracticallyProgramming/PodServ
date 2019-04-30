//Zachary Dulac, Trung Nguyen, Abby Tse
//TODO: DATE
//GUI Pea Pod Server Clone 

package podServ;

//TODO: Remove all the unnecessary packages when we're done.
import java.util.*;
import java.awt.EventQueue;
import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font; 

import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.beans.value.ChangeListener;

public class server extends Application {

	Text clock;
	static Text t3;
	static Text t5;
	static Text protein_total;
	static Text greens_total;
	static Text bakery_total;
	static Text tr1_total;
	static Text tr2_total;
	static Text tr3_total;
	Stage window;
	
	int height = 1000;
	int width = 800;
	
	//TODO: Probably remove all of these values that are declared in sockServer.java
	static int num_user = 0; //number of users
	double total_profit = 0.00; //total profit
	DecimalFormat totalFormat = new DecimalFormat("#0.00");

	static TextArea transaction_log = new TextArea();
	static TextArea users_log = new TextArea();
	
	double tr1_ttl = 0.00;
	double tr2_ttl = 0.00;
	double tr3_ttl = 0.00;
	double p_ttl = 0.00;
	double g_ttl = 0.00;
	double b_ttl = 0.00;
	
	private void startSockServer(){

		Thread servThread = new Thread(){
			
			public void run(){
				
				  sockServer.runSockServer();
			}
		};

		servThread.start();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		window = primaryStage;
		window.setTitle("PeaPod Clone");

//		Navbar 
		VBox navbar = new VBox();

//		Top of the navbar
		HBox topMenu = new HBox();
		topMenu.setPadding(new Insets(20));
		topMenu.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");

		Text title = new Text("Peapod");
		title.setFont(Font.font("Verdana", 40));
		title.setStyle("-fx-font-weight: bold");

		Image logo = new Image("/resources/logo.png");
		ImageView logoV = new ImageView(logo);
		logoV.setFitWidth(100);
		logoV.setPreserveRatio(true);
		logoV.setSmooth(true);
		logoV.setCache(true);

		Region r1 = new Region();
		HBox.setHgrow(r1, Priority.ALWAYS);

//		TOP MIDDLE
		StackPane clockSP = new StackPane();
		clock = new Text();
		clock.setFont(Font.font("Verdana", 20));
		clock.setTextAlignment(TextAlignment.CENTER);
		clockSP.getChildren().add(clock);
		StackPane.setAlignment(clock, Pos.CENTER);
		StackPane.setMargin(clock, new Insets(5, 0, 0, 0));

		Region r2 = new Region();
		HBox.setHgrow(r2, Priority.ALWAYS);

		topMenu.getChildren().addAll(title, logoV, r1, clockSP, r2);

//		Bottom of the navbar
		HBox bottomMenu = new HBox();
		bottomMenu.setStyle("-fx-font-weight: bold");

//		TODO: MAKE BUTTONS DYNAMICALLY CHANGE ACCORDING TO SCENE RATIO
		//Was Total Profits; changed because I needed a button for testing purposes.
		Button btn1 = new Button("EXIT");
		btn1.setMinSize(.333*height, 50);
		btn1.setStyle("-fx-background-radius: 0;" + "-fx-background-color: white;" + "-fx-border-color: black;" + "-fx-border-width: 3;");
		btn1.setFont(Font.font("Verdana", 17));
		btn1.setOnAction(new EventHandler <ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				
				System.exit(0);
				
			}
			
		});
		Button btn2 = new Button("Users Logged In");
		btn2.setMinSize(.334*height, 50);
		btn2.setStyle("-fx-background-radius: 0;" + "-fx-background-color: white;" + "-fx-border-color: black;" + "-fx-border-width: 3;");
		btn2.setFont(Font.font("Verdana", 17));
		Button btn3 = new Button("Food Transaction Log");
		btn3.setMinSize(.333*height, 50);
		btn3.setStyle("-fx-background-radius: 0;" + "-fx-background-color: white;" + "-fx-border-color: black;" + "-fx-border-width: 3;");
		btn3.setFont(Font.font("Verdana", 17));
//		THIS IS TO TEST THE TRANSACTION LOG
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				transaction_log.appendText("CHICKEN+ || NEW: 1");
			}
		});

		bottomMenu.getChildren().addAll(btn1, btn2, btn3);

		navbar.getChildren().addAll(topMenu, bottomMenu);
		
//		Sidebar
		VBox sidebar = new VBox();
		sidebar.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;");
		sidebar.setMinWidth(.333*height);
		
		VBox top_sb = new VBox();
		top_sb.setPadding(new Insets(10));
		top_sb.setMinHeight(220);
		top_sb.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		top_sb.setAlignment(Pos.CENTER);
		Text t1 = new Text("Total Number of");
		t1.setFont(Font.font("Verdana", 30));
		Text t2 = new Text("Users Logged In:");
		t2.setFont(Font.font("Verdana", 30));
		//CHANGED: Declaration moved to global space for use in sockServer.java
		/*Text*/ t3 = new Text(String.valueOf(num_user));
		t3.setFont(Font.font("Verdana", 60));
		top_sb.getChildren().addAll(t1, t2, t3);
		
		VBox bottom_sb = new VBox();
		bottom_sb.setPadding(new Insets(10));
		bottom_sb.setMinHeight(220);
		bottom_sb.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		bottom_sb.setAlignment(Pos.CENTER);
		Text t4 = new Text("Total Profit Today");
		t4.setFont(Font.font("Verdana", 30));
		/*Text*/ t5 = new Text("$" + totalFormat.format(total_profit));
		t5.setFont(Font.font("Verdana", 60));
		bottom_sb.getChildren().addAll(t4, t5);
		
		sidebar.getChildren().addAll(top_sb, bottom_sb);
	
		ScrollPane sp1 = new ScrollPane();
//		Transaction Log Tracker - right side
		transaction_log.setPrefWidth(.333*height);
		transaction_log.setEditable(false);
		transaction_log.setStyle("-fx-font-weight: bold");
		transaction_log.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		sp1.setContent(transaction_log);
		sp1.setFitToWidth(true);
		sp1.setFitToHeight(true);
		
		ScrollPane sp2 = new ScrollPane();
//		User Logged In - center
		users_log.setPrefWidth(.3*height);
		users_log.setEditable(false);
		users_log.setStyle("-fx-font-weight: bold");
		users_log.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		sp2.setContent(users_log);
		sp2.setFitToWidth(true);
		sp2.setFitToHeight(true);
		
//		Bottom
		HBox bottom = new HBox();
		bottom.setPrefHeight(200);
		Image truck = new Image("/resources/truck.png");
		
//		Bottom-left
		
		VBox bottom_left = new VBox();
		bottom_left.setPrefWidth(.5*height);
		bottom_left.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		Text t6 = new Text("Trucks Out For Delivery:");
		t6.setFont(Font.font("Verdana", 30));
		t6.setUnderline(true);
		
		HBox left = new HBox();
		
		ImageView tr1 = new ImageView(truck);
		tr1.setFitWidth(130);
		tr1.setPreserveRatio(true);
		tr1.setSmooth(true);
		tr1.setCache(true);
		
		ImageView tr2 = new ImageView(truck);
		tr2.setFitWidth(130);
		tr2.setPreserveRatio(true);
		tr2.setSmooth(true);
		tr2.setCache(true);
		
		ImageView tr3 = new ImageView(truck);
		tr3.setFitWidth(130);
		tr3.setPreserveRatio(true);
		tr3.setSmooth(true);
		tr3.setCache(true);
		
		VBox truck1 = new VBox();
		truck1.setAlignment(Pos.CENTER);
		/*Text*/ tr1_total = new Text("$" + totalFormat.format(tr1_ttl));
		tr1_total.setFont(Font.font("Verdana", 20));
		truck1.getChildren().addAll(tr1, tr1_total);
		
		VBox truck2 = new VBox();
		truck2.setAlignment(Pos.CENTER);
		/*Text*/ tr2_total = new Text("$" + totalFormat.format(tr2_ttl));
		tr2_total.setFont(Font.font("Verdana", 20));
		truck2.getChildren().addAll(tr2, tr2_total);
		
		VBox truck3 = new VBox();
		truck3.setAlignment(Pos.CENTER);
		/*Text*/ tr3_total = new Text("$" + totalFormat.format(tr3_ttl));
		tr3_total.setFont(Font.font("Verdana", 20));
		truck3.getChildren().addAll(tr3, tr3_total);
		
		Region br1 = new Region();
		HBox.setHgrow(br1, Priority.ALWAYS);
		
		Region br2 = new Region();
		HBox.setHgrow(br2, Priority.ALWAYS);
		
		left.getChildren().addAll(br1,truck1, truck2, truck3, br2);
		bottom_left.getChildren().addAll(t6, left);
		
//		Bottom-right
		
		VBox bottom_right = new VBox();
		bottom_right.setPrefWidth(.5*height);
		bottom_right.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;" + "-fx-background-color: white;");
		Text t7 = new Text("Category Totals:");
		t7.setFont(Font.font("Verdana", 30));
		t7.setUnderline(true);
		
		HBox right = new HBox();
		right.setSpacing(20);
		right.setPadding(new Insets(10));
		
		Image protein = new Image("/resources/protein.jpg");
		Image greens = new Image("/resources/greens.jpg");
		Image bakery = new Image("/resources/bakery.png");
		
		ImageView proteinV = new ImageView(protein);
		proteinV.setFitWidth(115);
//		proteinV.setPreserveRatio(true);
		proteinV.setFitHeight(90);
		proteinV.setSmooth(true);
		proteinV.setCache(true);
		
		ImageView greensV = new ImageView(greens);
		greensV.setFitWidth(115);
//		greensV.setPreserveRatio(true);
		greensV.setFitHeight(90);
		greensV.setSmooth(true);
		greensV.setCache(true);
		
		ImageView bakeryV = new ImageView(bakery);
		bakeryV.setFitWidth(115);
//		bakeryV.setPreserveRatio(true);
		bakeryV.setFitHeight(90);
		bakeryV.setSmooth(true);
		bakeryV.setCache(true);
		
		VBox proteinVB = new VBox();
		proteinVB.setAlignment(Pos.CENTER);
		/*Text*/ protein_total = new Text("$" + totalFormat.format(p_ttl));
		protein_total.setFont(Font.font("Verdana", 20));
		proteinVB.getChildren().addAll(proteinV, protein_total);
		
		VBox greensVB = new VBox();
		greensVB.setAlignment(Pos.CENTER);
		/*Text*/ greens_total = new Text("$" + totalFormat.format(g_ttl));
		greens_total.setFont(Font.font("Verdana", 20));
		greensVB.getChildren().addAll(greensV, greens_total);
		
		VBox bakeryVB = new VBox();
		bakeryVB.setAlignment(Pos.CENTER);
		/*Text*/ bakery_total = new Text("$" + totalFormat.format(b_ttl));
		bakery_total.setFont(Font.font("Verdana", 20));
		bakeryVB.getChildren().addAll(bakeryV, bakery_total);
		
		Region br3 = new Region();
		HBox.setHgrow(br3, Priority.ALWAYS);
		
		Region br4 = new Region();
		HBox.setHgrow(br4, Priority.ALWAYS);
		
		right.getChildren().addAll(br3, proteinVB, greensVB, bakeryVB, br4);
		bottom_right.getChildren().addAll(t7, right);
		
		bottom.getChildren().addAll(bottom_left, bottom_right);
		
//		Border Pane
		BorderPane bp = new BorderPane();
		bp.setTop(navbar);
		bp.setLeft(sidebar);
		bp.setRight(sp1); //CHANGED
		bp.setCenter(sp2);
		bp.setBottom(bottom);
		bp.setStyle("-fx-border-color: black;" + "-fx-border-width: 3;");
		
		startSockServer();
		refreshClock();
		
//		Scene Setup
		Scene scene = new Scene(bp, height, width);
		window.setScene(scene);
		window.show();
	}
	
	// Clock - thread code
    private void refreshClock()
    {
    	Thread refreshClock = new Thread()
		   {  
			  public void run()
			  {	 
				while (true)
				{
					Date dte = new Date();
		
					String topMenuStr = "       " + dte.toString();					      
				    clock.setText(topMenuStr); 
				    clock.setStyle("-fx-padding:150;");
			               
				    try
				    {
					   sleep(1000L);
				    }
				    catch (InterruptedException e) 
				    {
					   e.printStackTrace();
				    }
				  
	            }  // end while ( true )
				 
		    } // end run thread
		 };

	     refreshClock.start();
    }

	public static void main(String[] args){				
				launch(args);

		
	}

