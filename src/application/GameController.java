package application;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.Event;
import javafx.event.EventHandler;
import java.net.*;

public class GameController extends Application {

	private int player; // 0 means Human turn, 1 means Computer turn
	private int difficulty;
	private int movementCounter;
	private List<Event> click;
	private ArrayList<Node> Bord;
	static private HashMap<String, Node> MapBord;
	static private List<Node> humanNodes;
	static private List<Node> pcNodes;
	static private ComputerPlayer computer;
	static private Scene scene;
	static private ArrayList<String> visitedJumps;
	static private boolean found;
	static private List<String>playerWin,PcWin;
	static private List<String>checkPcWin;
	static private ArrayList<Node> winMoves;
	static int finalStateCounter;
	static int winCounterHuman;
	static int winCounterPc;
	static int myLevel;
	static Stage exitStage;

	public GameController() {
		playerWin = new LinkedList<String>();
		PcWin = new LinkedList<String>();
		//////
		player = 0;
		movementCounter = 0;
		finalStateCounter = 0;
		click = new ArrayList<Event>();
		MapBord = new HashMap();
		Bord = new ArrayList<Node>();
		humanNodes = new ArrayList<Node>();
		pcNodes = new ArrayList<Node>();
		winMoves = new ArrayList<Node>();
		visitedJumps = new ArrayList<String>();
		found = false;
		winCounterHuman = 0;
		winCounterPc = 0;
		INIT();
	}

	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("main.fxml"));
			scene = new Scene(root, 750, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			exitStage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDifficulty(int level) {
		difficulty = level;
		computer = new ComputerPlayer(difficulty, this);
	}

	public void draw(List<Node> currentPCNodes, HashMap<String, Node> currentState) {
		for (int i = 0; i < pcNodes.size(); i++) {
			if (!(currentPCNodes.get(i).getKey().equals(pcNodes.get(i).getKey()))) {

				// New Node
				String keySearch1 = Integer.toString(currentPCNodes.get(i).getX()) + ","
						+ Integer.toString(currentPCNodes.get(i).getY()) + ","
						+ Integer.toString(currentPCNodes.get(i).getZ());
				// Old Node
				String keySearch2 = Integer.toString(pcNodes.get(i).getX()) + ","
						+ Integer.toString(pcNodes.get(i).getY()) + "," + Integer.toString(pcNodes.get(i).getZ());
							
				MapBord = currentState;

				pcNodes.get(i).setX(currentPCNodes.get(i).getX());
				pcNodes.get(i).setY(currentPCNodes.get(i).getY());
				pcNodes.get(i).setZ(currentPCNodes.get(i).getZ());
				
				Circle tmp = (Circle) scene.lookup("#" + keySearch1);
				Circle tmp1 = (Circle) scene.lookup("#" + keySearch2);
				Paint c1 = tmp.getFill();
				Paint c2 = tmp1.getFill();			
				tmp.setFill(c2);
				tmp1.setFill(c1);
				checkWinPc(pcNodes.get(i));

				break;
			}
		}
	}

	public void checkWinPc(Node nod) {
		for(int i  = 0; i < PcWin.size(); i++) {
			
			if(PcWin.get(i).equals(nod.getKey()))
			{
				PcWin.remove(i);
			}
		}
		
		for(int i = 0; i < checkPcWin.size();i++) {
			if(MapBord.get(checkPcWin.get(i)).getColor().equals("r") ) {
				finalStateCounter++;
			}
		}
		
		if(finalStateCounter == 10) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("AI has taken over the world");
			alert.setContentText("Humanity will keep fighting");
			alert.showAndWait();
 		    exitStage.close();
		}else {
			finalStateCounter = 0;
		}
	}
	
	
	public void checkWinHuman(String key) {
		
		for(int i = 0; i < playerWin.size();i++) {
			if(MapBord.get(playerWin.get(i)).getColor().equals("g") ) {
				winCounterHuman++;
			}
		}
		
		
		if(winCounterHuman == 10) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Humanity Won");
			alert.setContentText("We will keep fighting for another day!");
			alert.showAndWait();
 		    exitStage.close();
		}
		else {
			winCounterHuman = 0;
		}
	}
	
	@FXML // clicked
	protected void clicked(Event evt) {
		boolean f = false;
		boolean pcTurn = false;
		if (player == 0 && movementCounter < 1) {
			click.add(evt);
			movementCounter++;
		} else if (player == 0 && movementCounter == 1) {
			click.add(evt);
			// Call check Movement if right movement recolor - recalculate built tree
			// "Heuristic else print wrong movement
			while (true) {
				if (f != true && checkMovementHuman(click)) {
					Paint color1 = ((Circle) click.get(0).getSource()).getFill();
					Paint color2 = ((Circle) click.get(1).getSource()).getFill();
					((Circle) click.get(0).getSource()).setFill(color2);
					((Circle) click.get(1).getSource()).setFill(color1);
					checkWinHuman(((Circle) click.get(1).getSource()).getId());
					pcTurn = true;
					break;
				} else if(f == false){
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Wrong Movement");
					alert.showAndWait();
					click = new ArrayList<Event>();
					movementCounter = 0;
					f = true;
				}
				else {
 
					break;
				}
			}
 
			if(pcTurn == true) {
				click = new ArrayList<Event>();
				movementCounter = 0;
				player = 1;
				// Play Computer		
				computer.computerMov(MapBord, pcNodes, humanNodes);
				player = 0;
 
			}
		}
	}

	public boolean checkMovementHuman(List<Event> pl) {

		// get IDs
		String id1 = ((Circle) pl.get(0).getSource()).getId();
		String id2 = ((Circle) pl.get(1).getSource()).getId();
		// Search HashMap
		Node obj1 = MapBord.get(id1);
		Node obj2 = MapBord.get(id2);

		// Base cases
		if (!(obj2.getColor().equals("w")) || obj1.getColor().equals("w")) {
			return false;
		}

		if (player == 0 && (obj1.getColor().equals("r"))) {
			return false;
		}

		// x y z red,yellow,orange
		// 14, 7, 12 -> 13, 8, 12 -> down left
		// 14, 7, 12 -> 13,7, 11 -> down right
		// 14, 7, 12 -> 14, 6, 11 -> right
		// 14,7, 12 -> 14, 8, 13 -> left
		// 14,7, 12 -> 15, 6, 12 -> up right
		// 14,7, 12 -> 15, 7, 13 -> up left
		if (obj1.getX() == obj2.getX() - 1 && obj1.getY() == obj2.getY() + 1 && obj1.getZ() == obj2.getZ()) { // up right
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
			return true;
		} else if (obj1.getX() == obj2.getX() - 1 && obj1.getY() == obj2.getY() && obj1.getZ() == obj2.getZ() - 1) { // up left
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
		
			return true;
		} else if (obj1.getX() == obj2.getX() && obj1.getY() == obj2.getY() - 1 && obj1.getZ() == obj2.getZ() - 1) { // left
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
			return true;
		} else if (obj1.getX() == obj2.getX() && obj1.getY() == obj2.getY() + 1 && obj1.getZ() == obj2.getZ() + 1) { // right
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
			return true;
		} else if (obj1.getX() == obj2.getX() + 1 && obj1.getY() == obj2.getY() - 1 && obj1.getZ() == obj2.getZ()) { //down left
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
			return true;
		} else if (obj1.getX() == obj2.getX() + 1 && obj1.getY() == obj2.getY() && obj1.getZ() == obj2.getZ() + 1) { // down right
			obj2.setColor(obj1.getColor());
			obj1.setColor("w");
			return true;
		}

		// Jump
		visitedJumps.clear();
		visitedJumps.ensureCapacity(0);
		Node tmp1 = new Node(obj1);
		Node tmp2 = new Node(obj2);

		HashMap<String, Node> tempMap = copyMap(MapBord);
		jump(tmp1, tmp2, visitedJumps, tempMap, obj1, obj2);
		if (found == false) {
			return false;
		} else {
			found = false;
			return true;
		}

	}

	public void jump(Node nod1, Node nod2, List<String> visitedJumps, HashMap<String, Node> tempMap, Node finalObj, Node finalObj2) {
		if(nod2.getKey().equals(nod1.getKey())) {
			finalObj2.setColor(nod1.getColor());
			finalObj.setColor("w");
			found = true;
			MapBord = copyMap(tempMap);
			return;
		}
      
        String key1 = Integer.toString(nod1.getX() - 2) + "," + Integer.toString(nod1.getY()+2) + "," + Integer.toString(nod1.getZ());
        String key2 = Integer.toString(nod1.getX()-2) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ()-2);
        String key3 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()-2) + "," + Integer.toString(nod1.getZ()-2);
        String key4 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()+2) + "," + Integer.toString(nod1.getZ()+2);
        String key5 = Integer.toString(nod1.getX()+2) + "," + Integer.toString(nod1.getY()-2) + "," + Integer.toString(nod1.getZ());
        String key6 = Integer.toString(nod1.getX()+2) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ()+2);


		if(checkValid(nod1.getX()-2, nod1.getY()+2, nod1.getZ()) && checkValid2(nod1.getX()-1, nod1.getY()+1, nod1.getZ())  && !(visitedJumps.contains(key1))) {// down left
	        String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
	        String keySearch2 = Integer.toString(nod1.getX() - 2) + "," + Integer.toString(nod1.getY() + 2) + "," + Integer.toString(nod1.getZ());
	
	        tempMap.get(keySearch1).setColor("w");
	        tempMap.get(keySearch2).setColor("g");
	        nod1.setX(nod1.getX()-2);
	        nod1.setY(nod1.getY()+2);
	        nod1.setZ(nod1.getZ());
	        visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
	        tempMap.get(keySearch1).setColor("g");
	        tempMap.get(keySearch2).setColor("w");
	        nod1.setX(nod1.getX()+2);
	        nod1.setY(nod1.getY()-2);
	        nod1.setZ(nod1.getZ());
	   
        }
	    if(checkValid(nod1.getX()-2, nod1.getY(), nod1.getZ()-2) && checkValid2(nod1.getX()-1, nod1.getY(), nod1.getZ()-1) && !(visitedJumps.contains(key2)))  {//14, 7, 12 -> 13,7, 11 -> down right
	          
	        String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
	        String keySearch2 = Integer.toString(nod1.getX() - 2) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ()-2);
	
	        tempMap.get(keySearch1).setColor("w");
	        tempMap.get(keySearch2).setColor("g");
	
	        nod1.setX(nod1.getX()-2);
	        nod1.setY(nod1.getY());
	        nod1.setZ(nod1.getZ()-2);
	        visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
	        tempMap.get(keySearch1).setColor("g");
	        tempMap.get(keySearch2).setColor("w");
	
	        nod1.setX(nod1.getX()+2);
	        nod1.setY(nod1.getY());
	        nod1.setZ(nod1.getZ()+2);

        }
	    if(checkValid(nod1.getX(), nod1.getY()-2, nod1.getZ()-2) && checkValid2(nod1.getX(), nod1.getY()-1, nod1.getZ()-1) && !(visitedJumps.contains(key3)))  //14, 7, 12 -> 14, 6, 11 -> right
	    {  
		   
	        String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
	        String keySearch2 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()-2) + "," + Integer.toString(nod1.getZ()-2);
	
			tempMap.get(keySearch1).setColor("w");
			tempMap.get(keySearch2).setColor("g");
	        nod1.setX(nod1.getX());
	        nod1.setY(nod1.getY()-2);
	        nod1.setZ(nod1.getZ()-2);
	  
	        visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
	        tempMap.get(keySearch1).setColor("g");
			tempMap.get(keySearch2).setColor("w");
	        nod1.setX(nod1.getX());
	        nod1.setY(nod1.getY()+2);
	        nod1.setZ(nod1.getZ()+2);
	 
	   }
	   if(checkValid(nod1.getX(), nod1.getY()+2, nod1.getZ()+2)&& checkValid2(nod1.getX(), nod1.getY()+1, nod1.getZ()+1) && !(visitedJumps.contains(key4)))  //14,7, 12 -> 14, 8, 13 -> left
	   {
	        String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
	        String keySearch2 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()+2) + "," + Integer.toString(nod1.getZ()+2);
	
	        
	        tempMap.get(keySearch1).setColor("w");
	        tempMap.get(keySearch2).setColor("g");
	        nod1.setX(nod1.getX());
	        nod1.setY(nod1.getY()+2);
	        nod1.setZ(nod1.getZ()+2);
	        visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
	        tempMap.get(keySearch1).setColor("g");
	        tempMap.get(keySearch2).setColor("w");
	        nod1.setX(nod1.getX());
	        nod1.setY(nod1.getY()-2);
	        nod1.setZ(nod1.getZ()-2);
	            
	     
        }
	    if(checkValid(nod1.getX()+2, nod1.getY()-2, nod1.getZ()) && checkValid2(nod1.getX()+1, nod1.getY()-1, nod1.getZ()) && !(visitedJumps.contains(key5)))  //14,7, 12 -> 15, 6, 12 -> up right
	    {
	          
            String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
            String keySearch2 = Integer.toString(nod1.getX()+2) + "," + Integer.toString(nod1.getY()-2) + "," + Integer.toString(nod1.getZ());
            
            
            tempMap.get(keySearch1).setColor("w");
            tempMap.get(keySearch2).setColor("g");
            nod1.setX(nod1.getX()+2);
            nod1.setY(nod1.getY()-2);
            nod1.setZ(nod1.getZ());
            visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
            tempMap.get(keySearch1).setColor("g");
            tempMap.get(keySearch2).setColor("w");
            nod1.setX(nod1.getX()-2);
            nod1.setY(nod1.getY()+2);
            nod1.setZ(nod1.getZ());	      
	   }
       if(checkValid(nod1.getX()+2, nod1.getY(), nod1.getZ()+2) && checkValid2(nod1.getX()+1, nod1.getY(), nod1.getZ()+1) && !(visitedJumps.contains(key6)))  //14,7, 12 -> 15, 7, 13 -> up left
       {
	            
	        String keySearch1 = Integer.toString(nod1.getX()) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ());
	        String keySearch2 = Integer.toString(nod1.getX()+2) + "," + Integer.toString(nod1.getY()) + "," + Integer.toString(nod1.getZ()+2);
	
	        tempMap.get(keySearch1).setColor("w");
	        tempMap.get(keySearch2).setColor("g");
	        nod1.setX(nod1.getX()+2);
	        nod1.setY(nod1.getY());
	        nod1.setZ(nod1.getZ()+2);
	        visitedJumps.add(keySearch1);
	        jump(nod1, nod2, visitedJumps, tempMap, finalObj, finalObj2);
	        tempMap.get(keySearch1).setColor("g");
	        tempMap.get(keySearch2).setColor("w");
	        nod1.setX(nod1.getX()-2);
	        nod1.setY(nod1.getY());
	        nod1.setZ(nod1.getZ()-2);

	    }	
		return;
	}

    public HashMap<String, Node>
    copyMap(HashMap<String, Node> original)
    {
        HashMap<String, Node> second_Map = new HashMap<>();

        // Start the iteration and copy the Key and Value
        // for each Map to the other Map.
        for (HashMap.Entry<String, Node> entry : original.entrySet()) {

            // using put method to copy one Map to Other
        	

            second_Map.put(entry.getKey(), new Node(entry.getValue()));
        }

        return second_Map;
    }
	public boolean checkValid(int x, int y, int z) {
		String keySearch = Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z);
		if (MapBord.get(keySearch) != null && MapBord.get(keySearch).getColor().equals("w")) {
			return true;
		}
		return false;

	}

	public boolean checkValid2(int x, int y, int z) {
		String keySearch = Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z);
		if (MapBord.get(keySearch) != null && !MapBord.get(keySearch).getColor().equals("w"))
			return true;
		return false;

	}

	
	//knowledge base
	public static void INIT() {

		// Start Green
		Node tmpNode = new Node("g", 17, 5, 13);
		MapBord.put("17,5,13", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("17,5,13");

		tmpNode = new Node("g", 16, 6, 13);
		MapBord.put("16,6,13", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("16,6,13");
		
		tmpNode = new Node("g", 16, 5, 12);
		MapBord.put("16,5,12", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("16,5,12");

		tmpNode = new Node("g", 15, 7, 13);
		MapBord.put("15,7,13", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("15,7,13");
		
		tmpNode = new Node("g", 15, 5, 11);
		MapBord.put("15,5,11", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("15,5,11");
		
		tmpNode = new Node("g", 15, 6, 12);
		MapBord.put("15,6,12", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("15,6,12");
		
		tmpNode = new Node("g", 14, 7, 12);
		MapBord.put("14,7,12", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("14,7,12");
		
		tmpNode = new Node("g", 14, 8, 13);
		MapBord.put("14,8,13", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("14,8,13");

		
		tmpNode = new Node("g", 14, 6, 11);
		MapBord.put("14,6,11", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("14,6,11");
		
		tmpNode = new Node("g", 14, 5, 10);
		MapBord.put("14,5,10", tmpNode);
		humanNodes.add(tmpNode);
		PcWin.add("14,5,10");
		// End Green

		// Start Blue
		MapBord.put("13,13,1", new Node("w", 13, 13, 17));

		MapBord.put("13,12,16", new Node("w", 13, 12, 16));
		MapBord.put("12,13,16", new Node("w", 12, 13, 16));

		MapBord.put("13,11,15", new Node("w", 13, 11, 15));
		MapBord.put("12,12,15", new Node("w", 12, 12, 15));
		MapBord.put("11,13,15", new Node("w", 11, 13, 15));

		MapBord.put("13,10,14", new Node("w", 13, 10, 14));
		MapBord.put("12,11,14", new Node("w", 12, 11, 14));
		MapBord.put("11,12,14", new Node("w", 11, 12, 14));
		MapBord.put("10,13,14", new Node("w", 10, 13, 14));
		// End Blue

		// Start orange
		MapBord.put("5,5,1", new Node("w", 5, 5, 1));

		MapBord.put("6,5,2", new Node("w", 6, 5, 2));
		MapBord.put("5,6,2", new Node("w", 5, 6, 2));

		MapBord.put("7,5,3", new Node("w", 7, 5, 3));
		MapBord.put("6,6,3", new Node("w", 6, 6, 3));
		MapBord.put("5,7,3", new Node("w", 5, 7, 3));

		MapBord.put("8,5,4", new Node("w", 8, 5, 4));
		MapBord.put("7,6,4", new Node("w", 7, 6, 4));
		MapBord.put("6,7,4", new Node("w", 6, 7, 4));
		MapBord.put("5,8,4", new Node("w", 5, 8, 4));
		// End orange

		// Start Red
		

		tmpNode = new Node("r", 1, 13, 5);
		MapBord.put("1,13,5", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("1,13,5");
		
		tmpNode = new Node("r", 2, 13, 6);
		MapBord.put("2,13,6", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("2,13,6");
		
		tmpNode = new Node("r", 2, 12, 5);
		MapBord.put("2,12,5", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("2,12,5");
	

		tmpNode = new Node("r", 3, 13, 7);
		MapBord.put("3,13,7", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("3,13,7");

		tmpNode = new Node("r", 3, 12, 6);
		MapBord.put("3,12,6", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("3,12,6");

		tmpNode = new Node("r", 3, 11, 5);
		MapBord.put("3,11,5", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("3,11,5");

		
		
		tmpNode = new Node("r", 4, 13, 8);
		MapBord.put("4,13,8", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("4,13,8");

		tmpNode = new Node("r", 4, 12, 7);
		MapBord.put("4,12,7", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("4,12,7");

		tmpNode = new Node("r", 4, 11, 6);
		MapBord.put("4,11,6", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("4,11,6");

		tmpNode = new Node("r", 4, 10, 5);
		MapBord.put("4,10,5", tmpNode);
		pcNodes.add(tmpNode);
		playerWin.add("4,10,5");
		
		// End Red

		// Start white
		MapBord.put("13,9,13", new Node("w", 13, 9, 13));
		MapBord.put("13,8,12", new Node("w", 13, 8, 12));
		MapBord.put("13,7,11", new Node("w", 13, 7, 11));
		MapBord.put("13,6,10", new Node("w", 13, 6, 10));
		MapBord.put("13,5,9", new Node("w", 13, 5, 9));

		MapBord.put("12,10,13", new Node("w", 12, 10, 13));
		MapBord.put("12,9,12", new Node("w", 12, 9, 12));
		MapBord.put("12,8,11", new Node("w", 12, 8, 11));
		MapBord.put("12,7,10", new Node("w", 12, 7, 10));
		MapBord.put("12,6,9", new Node("w", 12, 6, 9));
		MapBord.put("12,5,8", new Node("w", 12, 5, 8));

		MapBord.put("11,11,13", new Node("w", 11, 11, 13));
		MapBord.put("11,10,12", new Node("w", 11, 10, 12));
		MapBord.put("11,9,11", new Node("w", 11, 9, 11));
		MapBord.put("11,8,10", new Node("w", 11, 8, 10));
		MapBord.put("11,7,9", new Node("w", 11, 7, 9));
		MapBord.put("11,6,8", new Node("w", 11, 6, 8));
		MapBord.put("11,5,7", new Node("w", 11, 5, 7));

		MapBord.put("10,12,13", new Node("w", 10, 12, 13));
		MapBord.put("10,11,12", new Node("w", 10, 11, 12));
		MapBord.put("10,10,11", new Node("w", 10, 10, 11));
		MapBord.put("10,9,10", new Node("w", 10, 9, 10));
		MapBord.put("10,8,9", new Node("w", 10, 8, 9));
		MapBord.put("10,7,8", new Node("w", 10, 7, 8));
		MapBord.put("10,6,7", new Node("w", 10, 6, 7));
		MapBord.put("10,5,6", new Node("w", 10, 5, 6));

		MapBord.put("9,13,13", new Node("w", 9, 13, 13));
		MapBord.put("9,12,12", new Node("w", 9, 12, 12));
		MapBord.put("9,11,11", new Node("w", 9, 11, 11));
		MapBord.put("9,10,10", new Node("w", 9, 10, 10));
		MapBord.put("9,9,9", new Node("w", 9, 9, 9));
		MapBord.put("9,8,8", new Node("w", 9, 8, 8));
		MapBord.put("9,7,7", new Node("w", 9, 7, 7));
		MapBord.put("9,6,6", new Node("w", 9, 6, 6));
		MapBord.put("9,5,5", new Node("w", 9, 5, 5));

		MapBord.put("8,13,12", new Node("w", 8, 13, 12));
		MapBord.put("8,12,11", new Node("w", 8, 12, 11));
		MapBord.put("8,11,10", new Node("w", 8, 11, 10));
		MapBord.put("8,10,9", new Node("w", 8, 10, 9));
		MapBord.put("8,9,8", new Node("w", 8, 9, 8));
		MapBord.put("8,8,7", new Node("w", 8, 8, 7));
		MapBord.put("8,7,6", new Node("w", 8, 7, 6));
		MapBord.put("8,6,5", new Node("w", 8, 6, 5));

		MapBord.put("7,13,11", new Node("w", 7, 13, 11));
		MapBord.put("7,12,10", new Node("w", 7, 12, 10));
		MapBord.put("7,11,9", new Node("w", 7, 11, 9));
		MapBord.put("7,10,8", new Node("w", 7, 10, 8));
		MapBord.put("7,9,7", new Node("w", 7, 9, 7));
		MapBord.put("7,8,6", new Node("w", 7, 8, 6));
		MapBord.put("7,7,5", new Node("w", 7, 7, 5));

		MapBord.put("6,13,10", new Node("w", 6, 13, 10));
		MapBord.put("6,12,9", new Node("w", 6, 12, 9));
		MapBord.put("6,11,8", new Node("w", 6, 11, 8));
		MapBord.put("6,10,7", new Node("w", 6, 10, 7));
		MapBord.put("6,9,6", new Node("w", 6, 9, 6));
		MapBord.put("6,8,5", new Node("w", 6, 8, 5));

		MapBord.put("5,13,9", new Node("w", 5, 13, 9));
		MapBord.put("5,12,8", new Node("w", 5, 12, 8));
		MapBord.put("5,11,7", new Node("w", 5, 11, 7));
		MapBord.put("5,10,6", new Node("w", 5, 10, 6));
		MapBord.put("5,9,5", new Node("w", 5, 9, 5));

		// End white

		// start purple assume white :"
		MapBord.put("5,17,13", new Node("w", 5, 17, 13));
		MapBord.put("5,16,12", new Node("w", 5, 16, 12));
		MapBord.put("5,15,11", new Node("w", 5, 15, 11));
		MapBord.put("5,14,10", new Node("w", 5, 14, 10));

		MapBord.put("6,16,13", new Node("w", 6, 16, 13));
		MapBord.put("6,15,12", new Node("w", 6, 15, 12));
		MapBord.put("6,14,11", new Node("w", 6, 14, 11));

		MapBord.put("7,15,13", new Node("w", 7, 15, 13));
		MapBord.put("7,14,12", new Node("w", 7, 14, 12));

		MapBord.put("8,14,13", new Node("w", 8, 14, 13));

		// end purple white

		// start yellow assume white :"
		MapBord.put("13,1,5", new Node("w", 13, 1, 5));
		MapBord.put("13,2,6", new Node("w", 13, 2, 6));
		MapBord.put("13,3,7", new Node("w", 13, 3, 7));
		MapBord.put("13,4,8", new Node("w", 13, 4, 8));

		MapBord.put("12,2,5", new Node("w", 12, 2, 5));
		MapBord.put("12,3,6", new Node("w", 12, 3, 6));
		MapBord.put("12,4,7", new Node("w", 12, 4, 7));

		MapBord.put("11,3,5", new Node("w", 11, 3, 5));
		MapBord.put("11,4,6", new Node("w", 11, 4, 6));

		MapBord.put("10,4,5", new Node("w", 10, 4, 5));
		
		checkPcWin = cloneList2(PcWin);

		// end yellow white
	}
	
	public static List<String> cloneList2(List<String> nodeList) {
		List<String> clonedList = new ArrayList<String>(nodeList.size());
		for (String nod : nodeList) {
			clonedList.add(new String(nod));
		}
		return clonedList;
	}

}