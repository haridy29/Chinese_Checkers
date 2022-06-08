package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComputerPlayer {
	int difficulty;
	static private ArrayList<HashMap<String, Node>> listOfBord = new ArrayList<HashMap<String, Node>>();
	static private ArrayList<List<Node>> listOfNodes = new ArrayList<List<Node>>();
	static private ArrayList<Integer> heuristicLevelOne = new ArrayList<Integer>();
	static private HashMap<String, Node> currentState;
	static private List<Node> currentPCNodes;
	static private List<Node> currentHumanNodes;
	static private GameController gController;
	static private ArrayList<String> visitedJumps;
	static private int maximumHumanHueristic;
	static private int minimumPcHueristic;

	// Level Two
	static private ArrayList<Integer> heuristicLevelTwo = new ArrayList<Integer>();
	static private ArrayList<HashMap<String, Node>> listOfBordTwo = new ArrayList<HashMap<String, Node>>();
	static private ArrayList<List<Node>> listOfNodesTwo = new ArrayList<List<Node>>();
	static private ArrayList<String> visitedJumpsTwo;
	static private List<Node> modifiedPC;

	static private ArrayList<Integer> heuristicLevelThree = new ArrayList<Integer>();
	static private ArrayList<HashMap<String, Node>> listOfBordThree = new ArrayList<HashMap<String, Node>>();
	static private ArrayList<List<Node>> listOfNodesThree = new ArrayList<List<Node>>();
	static private ArrayList<String> visitedJumpsThree;
	static private boolean hardFlag = false;
	static private boolean medFlag = false;

	// List of visited states

	public ComputerPlayer(int lv, GameController obj) {
		difficulty = lv;
		gController = obj;
		if (difficulty == 3) {
			hardFlag = true;
		}
		if (difficulty == 2) {
			medFlag = true;
		}
	}

	public void computerMov(HashMap<String, Node> MapBord, List<Node> pcNodes, List<Node> humanNodes) {
		currentState = copyMap(MapBord);
		currentPCNodes = cloneList(pcNodes);
		currentHumanNodes = cloneList(humanNodes);

		// Level Two
		maximumHumanHueristic = -1;
		heuristicLevelTwo.clear();
		heuristicLevelTwo.ensureCapacity(0);
		listOfBordTwo.clear();
		listOfBordTwo.ensureCapacity(0);
		listOfNodesTwo.clear();
		listOfNodesTwo.ensureCapacity(0);
		visitedJumpsTwo = new ArrayList<String>();
		visitedJumpsTwo.clear();
		visitedJumpsTwo.ensureCapacity(0);

		// Level Three
		minimumPcHueristic = 99999;
		heuristicLevelThree.clear();
		heuristicLevelThree.ensureCapacity(0);
		listOfBordThree.clear();
		listOfBordThree.ensureCapacity(0);
		listOfNodesThree.clear();
		listOfNodesThree.ensureCapacity(0);
		visitedJumpsThree = new ArrayList<String>();
		visitedJumpsThree.clear();
		visitedJumpsThree.ensureCapacity(0);

		// LevelOne
		heuristicLevelOne.clear();
		heuristicLevelOne.ensureCapacity(0);
		visitedJumps = new ArrayList<String>();
		listOfBord.clear();
		listOfBord.ensureCapacity(0);
		listOfNodes.clear();
		listOfNodes.ensureCapacity(0);
		visitedJumps.clear();
		visitedJumps.ensureCapacity(0);

		allMoves(MapBord, pcNodes);
	}

	public void allMoves(HashMap<String, Node> MapBord, List<Node> pcNodes) {
		int counter = 0;
		for (Node nod : pcNodes) {

			// 14,7, 12 -> 15, 6, 12 -> up right
			if (checkValid(nod.getX() + 1, nod.getY() - 1, nod.getZ())) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");
				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
			
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);

					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}

			// 14,7, 12 -> 15, 7, 13 -> up left

			if (checkValid(nod.getX() + 1, nod.getY(), nod.getZ() + 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);

			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);
					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}

			////////// done //////////////

			// 14, 7, 12 -> 14, 6, 11 -> right

			///// done /////////////

			// 14,7, 12 -> 14, 8, 13 -> left
			if (checkValid(nod.getX(), nod.getY() + 1, nod.getZ() + 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);

					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}

			if (checkValid(nod.getX(), nod.getY() - 1, nod.getZ() - 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
				
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ() - 2);
					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);
					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}

			/// done ////////////////

			if (checkValid(nod.getX() - 1, nod.getY() + 1, nod.getZ()) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);
					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}

			////////// down////////////

			// 14, 7, 12 -> 13,7, 11 -> down right
			if (checkValid(nod.getX() - 1, nod.getY(), nod.getZ() - 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
				listOfBord.add(tmpState);
				listOfNodes.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() - 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelOne.add(17 - tmpPc.get(counter).getX());
					listOfBord.add(tmpState);
					listOfNodes.add(tmpPc);
					jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

				}
			}
			counter++;

		}

		if ((difficulty == 2 && hardFlag == false) || hardFlag == true) {
			// Iterate over list of bord -> send each state to make human movements on it
			for (int k = 0; k < listOfBord.size(); k++) {

	            modifiedPC = cloneList(listOfNodes.get(k));
				moveLevel2(listOfBord.get(k), currentHumanNodes);
				heuristicLevelOne.set(k, maximumHumanHueristic);
				maximumHumanHueristic = -1;
			}
		}

		int minimum = 9999999;
		int ct = 0;
		int index = 0;
		for (int heur : heuristicLevelOne) {
			if (heur < minimum) {
				minimum = heur;
				index = ct;
			}
			ct++;
		}
		currentPCNodes = listOfNodes.get(index);
		currentState = listOfBord.get(index);
		gController.draw(currentPCNodes, currentState);

	}

	public void moveLevel2(HashMap<String, Node> MapBord, List<Node> pcNodes) {
		int counter = 0;
		for (Node nod : pcNodes) {

			// 14,7, 12 -> 15, 6, 12 -> up right
			if (checkValid(nod.getX() + 1, nod.getY() - 1, nod.getZ())) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");
				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
		

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);

					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}

			// 14,7, 12 -> 15, 7, 13 -> up left

			if (checkValid(nod.getX() + 1, nod.getY(), nod.getZ() + 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);

			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);
					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}

			if (checkValid(nod.getX(), nod.getY() + 1, nod.getZ() + 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);

					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}

			if (checkValid(nod.getX(), nod.getY() - 1, nod.getZ() - 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
				
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ() - 2);
					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);
					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}

			/// done ////////////////
			if (checkValid(nod.getX() - 1, nod.getY() + 1, nod.getZ())) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);
					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}

			////////// down////////////

			// 14, 7, 12 -> 13,7, 11 -> down right
			if (checkValid(nod.getX() - 1, nod.getY(), nod.getZ() - 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelTwo.add(tmpPc.get(counter).getX());
				listOfBordTwo.add(tmpState);
				listOfNodesTwo.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() - 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelTwo.add(tmpPc.get(counter).getX());
					listOfBordTwo.add(tmpState);
					listOfNodesTwo.add(tmpPc);
					jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsTwo);

				}
			}
			counter++;
		}

		if (hardFlag == true) {
			for (int k = 0; k < listOfBordTwo.size(); k++) {
	            List<Node> currentNode = cloneList(modifiedPC);
	            moveLevel3(listOfBordTwo.get(k), currentNode);
				heuristicLevelTwo.set(k, minimumPcHueristic);
				minimumPcHueristic = 99999;
			}
		}
	

		for (int heur : heuristicLevelTwo) {
			if (heur > maximumHumanHueristic) {
				maximumHumanHueristic = heur;
			}
		}
	}

	public void moveLevel3(HashMap<String, Node> MapBord, List<Node> pcNodes) {
		int counter = 0;
		for (Node nod : pcNodes) {

			// 14,7, 12 -> 15, 6, 12 -> up right
			if (checkValid(nod.getX() + 1, nod.getY() - 1, nod.getZ())) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");
				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);
			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);

					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}

			// 14,7, 12 -> 15, 7, 13 -> up left

			if (checkValid(nod.getX() + 1, nod.getY(), nod.getZ() + 1)) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() + 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);

			} else {
				// jump
				if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);
					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}

			if (checkValid(nod.getX(), nod.getY() + 1, nod.getZ() + 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ() + 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ() + 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);

					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}

			if (checkValid(nod.getX(), nod.getY() - 1, nod.getZ() - 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 1) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);
					
					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
							+ Integer.toString(nod.getZ() - 2);
					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);
					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}

			/// done ////////////////

			if (checkValid(nod.getX() - 1, nod.getY() + 1, nod.getZ()) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY() + 1) + ","
						+ Integer.toString(nod.getZ());

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
							+ Integer.toString(nod.getZ());

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);
					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}

			////////// down////////////

			// 14, 7, 12 -> 13,7, 11 -> down right
			if (checkValid(nod.getX() - 1, nod.getY(), nod.getZ() - 1) && nod.getX() < 14) {
				HashMap<String, Node> tmpState = copyMap(MapBord);
				List<Node> tmpPc = cloneList(pcNodes);

				String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ());
				String keySearch2 = Integer.toString(nod.getX() - 1) + "," + Integer.toString(nod.getY()) + ","
						+ Integer.toString(nod.getZ() - 1);

				tmpState.get(keySearch1).setColor("w");
				tmpState.get(keySearch2).setColor("r");

				tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 1);
				tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 1);

				heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
				listOfBordThree.add(tmpState);
				listOfNodesThree.add(tmpPc);
			} else if (nod.getX() < 14) {
				// jump
				if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)) {
					HashMap<String, Node> tmpState = copyMap(MapBord);
					List<Node> tmpPc = cloneList(pcNodes);

					String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ());
					String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
							+ Integer.toString(nod.getZ() - 2);

					tmpState.get(keySearch1).setColor("w");
					tmpState.get(keySearch2).setColor("r");

					tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
					tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

					heuristicLevelThree.add(17 - tmpPc.get(counter).getX());
					listOfBordThree.add(tmpState);
					listOfNodesThree.add(tmpPc);
					jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumpsThree);

				}
			}
			counter++;

		}

		for (int heur : heuristicLevelThree) {
			if (heur < minimumPcHueristic) {
				minimumPcHueristic = heur;
			}
		}
	}

	public void jump(Node nod, HashMap<String, Node> tmpState, List<Node> tmpPc, int counter,
			List<String> visitedJumps) {
		String key1 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ());
		String key2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key3 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key4 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ() + 2);
		String key5 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ());
		String key6 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() + 2);

		if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())
				&& checkValid2(nod.getX() + 1, nod.getY() - 1, nod.getZ()) && !(visitedJumps.contains(key5))) // 14,7,
																												// 12 ->
																												// 15,
																												// 6, 12
																												// -> up
																												// right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)
				&& checkValid2(nod.getX() + 1, nod.getY(), nod.getZ() + 1) && !(visitedJumps.contains(key6))) 
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)
				&& checkValid2(nod.getX(), nod.getY() - 1, nod.getZ() - 1) && !(visitedJumps.contains(key3))
				&& nod.getX() < 14) // 14, 7, 12 -> 14, 6, 11 -> right
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)
				&& checkValid2(nod.getX(), nod.getY() + 1, nod.getZ() + 1) && !(visitedJumps.contains(key4))
				&& nod.getX() < 14) // 14,7, 12 -> 14, 8, 13 -> left
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())
				&& checkValid2(nod.getX() - 1, nod.getY() + 1, nod.getZ()) && !(visitedJumps.contains(key1))
				&& nod.getX() < 14) {// down left

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)
				&& checkValid2(nod.getX() - 1, nod.getY(), nod.getZ() - 1) && !(visitedJumps.contains(key2))
				&& nod.getX() < 14) // 14, 7, 12 -> 13,7, 11 -> down right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelOne.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBord.add(tmpState);
			listOfNodes.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		}

		return;
	}

	public void jump2(Node nod, HashMap<String, Node> tmpState, List<Node> tmpPc, int counter,
			List<String> visitedJumps) {
		String key1 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ());
		String key2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key3 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key4 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ() + 2);
		String key5 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ());
		String key6 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() + 2);

		if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())
				&& checkValid2(nod.getX() + 1, nod.getY() - 1, nod.getZ()) && !(visitedJumps.contains(key5))) // 14,7,
																												// 12 ->
																												// 15,
																												// 6, 12
																												// -> up
																												// right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)
				&& checkValid2(nod.getX() + 1, nod.getY(), nod.getZ() + 1) && !(visitedJumps.contains(key6))) // 14,7,
																												// 12 ->
																												// 15,
																												// 7, 13
																												// -> up
																												// left
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)
				&& checkValid2(nod.getX(), nod.getY() - 1, nod.getZ() - 1) && !(visitedJumps.contains(key3))
				&& nod.getX() < 14) // 14, 7, 12 -> 14, 6, 11 -> right
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)
				&& checkValid2(nod.getX(), nod.getY() + 1, nod.getZ() + 1) && !(visitedJumps.contains(key4))
				&& nod.getX() < 14) // 14,7, 12 -> 14, 8, 13 -> left
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())
				&& checkValid2(nod.getX() - 1, nod.getY() + 1, nod.getZ()) && !(visitedJumps.contains(key1))
				&& nod.getX() < 14) {// down left

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)
				&& checkValid2(nod.getX() - 1, nod.getY(), nod.getZ() - 1) && !(visitedJumps.contains(key2))
				&& nod.getX() < 14) // 14, 7, 12 -> 13,7, 11 -> down right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelTwo.add(tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordTwo.add(tmpState);
			listOfNodesTwo.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump2(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		}

		return;
	}

	public void jump3(Node nod, HashMap<String, Node> tmpState, List<Node> tmpPc, int counter,
			List<String> visitedJumps) {
		String key1 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ());
		String key2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key3 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ() - 2);
		String key4 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
				+ Integer.toString(nod.getZ() + 2);
		String key5 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
				+ Integer.toString(nod.getZ());
		String key6 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
				+ Integer.toString(nod.getZ() + 2);

		if (checkValid(nod.getX() + 2, nod.getY() - 2, nod.getZ())
				&& checkValid2(nod.getX() + 1, nod.getY() - 1, nod.getZ()) && !(visitedJumps.contains(key5))) // 14,7,
																												// 12 ->
																												// 15,
																												// 6, 12
																												// -> up
																												// right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() + 2, nod.getY(), nod.getZ() + 2)
				&& checkValid2(nod.getX() + 1, nod.getY(), nod.getZ() + 1) && !(visitedJumps.contains(key6))) // 14,7,
																												// 12 ->
																												// 15,
																												// 7, 13
																												// -> up
																												// left
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() + 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() - 2, nod.getZ() - 2)
				&& checkValid2(nod.getX(), nod.getY() - 1, nod.getZ() - 1) && !(visitedJumps.contains(key3))
				&& nod.getX() < 14) // 14, 7, 12 -> 14, 6, 11 -> right
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() - 2) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX(), nod.getY() + 2, nod.getZ() + 2)
				&& checkValid2(nod.getX(), nod.getY() + 1, nod.getZ() + 1) && !(visitedJumps.contains(key4))
				&& nod.getX() < 14) // 14,7, 12 -> 14, 8, 13 -> left
		{

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ() + 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() + 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY() + 2, nod.getZ())
				&& checkValid2(nod.getX() - 1, nod.getY() + 1, nod.getZ()) && !(visitedJumps.contains(key1))
				&& nod.getX() < 14) {// down left

			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY() + 2) + ","
					+ Integer.toString(nod.getZ());

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setY(tmpPc.get(counter).getY() + 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		} else if (checkValid(nod.getX() - 2, nod.getY(), nod.getZ() - 2)
				&& checkValid2(nod.getX() - 1, nod.getY(), nod.getZ() - 1) && !(visitedJumps.contains(key2))
				&& nod.getX() < 14) // 14, 7, 12 -> 13,7, 11 -> down right
		{
			String keySearch1 = Integer.toString(nod.getX()) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ());
			String keySearch2 = Integer.toString(nod.getX() - 2) + "," + Integer.toString(nod.getY()) + ","
					+ Integer.toString(nod.getZ() - 2);

			tmpState.get(keySearch1).setColor("w");
			tmpState.get(keySearch2).setColor("r");

			tmpPc.get(counter).setX(tmpPc.get(counter).getX() - 2);
			tmpPc.get(counter).setZ(tmpPc.get(counter).getZ() - 2);

			heuristicLevelThree.add(17 - tmpPc.get(counter).getX() + Math.abs((5 - tmpPc.get(counter).getY())));
			listOfBordThree.add(tmpState);
			listOfNodesThree.add(tmpPc);
			visitedJumps.add(keySearch1);
			jump3(tmpPc.get(counter), tmpState, tmpPc, counter, visitedJumps);

		}

		return;
	}

	public boolean checkValid(int x, int y, int z) {
		String keySearch = Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z);
		if (y >= 5 && y <= 13 && z >= 5 && z <= 13) {
			if (currentState.get(keySearch) != null && currentState.get(keySearch).getColor().equals("w")) {
				return true;
			}
		}

		return false;

	}

	public boolean checkValid2(int x, int y, int z) {
		String keySearch = Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z);
		if (currentState.get(keySearch) != null && !currentState.get(keySearch).getColor().equals("w"))
			return true;
		return false;

	}

	public static List<Node> cloneList(List<Node> nodeList) {
		List<Node> clonedList = new ArrayList<Node>(nodeList.size());
		for (Node nod : nodeList) {
			clonedList.add(new Node(nod));
		}
		return clonedList;
	}

	public HashMap<String, Node> copyMap(HashMap<String, Node> original) {
		HashMap<String, Node> second_Map = new HashMap<>();

		// Start the iteration and copy the Key and Value
		// for each Map to the other Map.
		for (HashMap.Entry<String, Node> entry : original.entrySet()) {

			// using put method to copy one Map to Other

			second_Map.put(entry.getKey(), new Node(entry.getValue()));
		}

		return second_Map;
	}

}