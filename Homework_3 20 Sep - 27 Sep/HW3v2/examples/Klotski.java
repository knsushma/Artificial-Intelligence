import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.crypto.CipherInputStream;
import javax.swing.text.AsyncBoxView.ChildLocator;

import java.util.stream.Collectors;

// implement the class of block if necessary
class Block {
    
}

class GameState {      
    protected int[][] board = new int[5][4];
    protected GameState parent = null;
    protected int cost = 0;
    protected int gn = 0;
    protected int fn = 0;
    protected int h = 0;
    protected int steps = 0;

    public GameState() {
     
    }
    
    public GameState(GameState parent) {
    	this.parent = parent;
    }
    
    public GameState(int [][] inputBoard) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
    }

    public GameState(int [][] inputBoard, int steps) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
        this.steps = steps;
    }

    
    public GameState(int [][] inputBoard, GameState parent) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
        this.parent = parent;
    }
    
    public GameState(int [][] inputBoard, GameState parent, int cost, int h) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
        this.parent = parent;
        this.cost = cost;
        this.h = h;
        this.gn = parent.gn + this.cost;
        this.fn = this.gn + this.h;
    }

 // return the 20-digit number as ID
    protected String getStateID() {  
        String s = "";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++)
                s += this.board[i][j];
        }
        return s;
    }

    protected void printBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++)
                System.out.print(this.board[i][j]);
            System.out.println();
        }
    }
    
    // check whether the current state is the goal
    protected boolean goalCheck() {        
    	int[][] board = this.board;
    	if (board[3][1] == 1 && board[3][2] == 1 && board[4][1] == 1 && board[4][2] == 1)
    		return true;
        return false;
    }
            
    // get all successors and return them in sorted order
    protected List<GameState> getNextStates() {
    	
    	//System.out.println("### Computing Next States started. ###");

    	List<GameState> successors = new ArrayList<>();        
        
    	List<GameState> state4 = getStatesById4(this).stream().filter(state-> state!=null).distinct().collect(Collectors.toList());
    	List<GameState> state3 = getStatesById3(this).stream().filter(state-> state!=null).distinct().collect(Collectors.toList());
    	List<GameState> state2 = getStatesById2(this).stream().filter(state-> state!=null).distinct().collect(Collectors.toList());
    	List<GameState> state1 = getStatesById1(this).stream().filter(state-> state!=null).distinct().collect(Collectors.toList());

    	successors = Stream.concat(Stream.concat(Stream.concat(state4.stream(), state3.stream()),state2.stream()),state1.stream()).distinct().collect(Collectors.toList());
        return successors;
    }

    protected List<GameState> sortSuccessorStates(List<GameState> unorderedStates) {
    	
    	List<GameState> orderedStates = new ArrayList<GameState>();
    	List<String> stateList = new ArrayList<String>();
    	
    	unorderedStates.forEach(state -> {
    		stateList.add(state.getStringByBoard());
    	});
    	String[] sortedStates = stateList.stream().toArray(String[]::new);
    	Arrays.sort(sortedStates);
    	for (String s : sortedStates){
          	GameState newState = new GameState(this.board, this, 1, 0);
  			newState.board = getBoardByString(s);
  			orderedStates.add(newState);
        }
    	return orderedStates;
    }
    
    public static List<GameState> getStatesById4(GameState initialState) {
    	
    	//System.out.println("### NextStates computation by State ID 4. ###");
    	List<GameState> states = new ArrayList<>();
    	
    	String stateBoard = initialState.getStringByBoard();
    	String text = stateBoard;
    	int[] indicesOf4 = IntStream.range(0, stateBoard.length()).filter(i-> text.charAt(i)=="4".charAt(0)).toArray();
    	int[] indicesOf0 = IntStream.range(0, stateBoard.length()).filter(i-> text.charAt(i)=="0".charAt(0)).toArray();

    	//System.out.println("Indexes are  " + indicesOf4.length + indicesOf0.length);
    	for (int i=0; i<indicesOf4.length; i++) {
    		for (int j=0; j<indicesOf0.length; j++) {
    			//System.out.println("new State: " + indicesOf4[i] + " " + indicesOf0[j] + " " + newStateBoard);
    			String newStateStr = GameState.swapCharByIndex(stateBoard, indicesOf4[i], indicesOf0[j]);
    			GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
    			states.add(state);
    		}
    	}
//    	System.out.println();
//		states.forEach(s -> s.printBoard());
    	
    	return states;
    	
    }
    
    public static List<GameState> getStatesById3(GameState initialState) {
    	
    	//System.out.println("### NextStates computation by State ID 3. ###");
    	List<GameState> states = new ArrayList<>();
    	String stateBoard = initialState.getStringByBoard();

		String subPattern1 = "(?<zeroes>00)\\d\\d(?<threes>33)";
		Pattern p1 = Pattern.compile(subPattern1);
		Matcher match1 = p1.matcher(stateBoard);
		String subPattern2 = "(?<threes>33)\\d\\d(?<zeroes>00)";
		Pattern p2 = Pattern.compile(subPattern2);
		Matcher match2 = p2.matcher(stateBoard);
		if (match1.find() || match2.find()) {
			String newStateStr = GameState.swapSubstrings(stateBoard, "00", "33");
			GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
			states.add(state);
		} else {
			//System.out.println("Inside row split ");
			String[] arrOfString = GameState.stringSplitByNumber(stateBoard,4);
			for (int i=0; i<arrOfString.length; i++) {
				//System.out.println("each row string: " + arrOfString[i]);
				String newStateStr = stateBoard;
				if ( arrOfString[i].contains("033") ) {
					newStateStr = stateBoard.replace("033", "330");
					GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
	    			states.add(state);
				} 
				if ( arrOfString[i].contains("330") ) {
					newStateStr = stateBoard.replace("330", "033");
					GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
	    			states.add(state);
				}
			}
		}
//		System.out.println();
//		states.forEach(s -> s.printBoard());
    	return states;
    	
    }
    
    public static List<GameState> getStatesById2(GameState initialState) {
    	
    	//System.out.println("### NextStates computation by State ID 2. ###");
    	List<GameState> states = new ArrayList<>();
    	String stateBoard = GameState.getStringByBoardVertical(initialState.board);
    	
		String[] arrOfString = GameState.stringSplitByNumber(stateBoard,5);
		for (int i=0; i<arrOfString.length; i++) {
			if ( arrOfString[i].contains("220") ) {
				//System.out.println("contain 220 in" + " " +i + " : " + stateBoard + " column is " + arrOfString[i] );
				String[] temp = arrOfString.clone();
				temp[i] = temp[i].replace("220", "022");
				GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
    			states.add(state);
			}
			if ( arrOfString[i].contains("022") ) {
				//System.out.println("contain 022 in" + " " +i + " : " + stateBoard + " column is " + arrOfString[i] );
    			String[] temp = arrOfString.clone();
				temp[i] = temp[i].replace("022", "220");
				GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
    			states.add(state);
			}
		}

		arrOfString = GameState.stringSplitByNumber(stateBoard,5);
		for (int i=1; i<arrOfString.length; i++) {
			arrOfString = GameState.stringSplitByNumber(stateBoard,5);
			String pre = arrOfString[i-1];
			String cur = arrOfString[i];
			if (pre.contains("2222") && cur.contains("00") ) {
				String[] temp = arrOfString.clone();
				String[] states2 = indentifyStatesFor2222(cur, pre);
				if (states2[0]!=null && states2[1]!=null) { 
					temp[i-1] = states2[1];
					temp[i] = states2[0];
					GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
	    			states.add(state);
				}
	    	} 
			else if (pre.contains("00") && cur.contains("2222")) {
	    		String[] temp = arrOfString.clone();
	    		String[] states2 = indentifyStatesFor2222(pre, cur); 
	    		if (states2[0]!=null && states2[1]!=null) { 
					temp[i-1] = states2[0];
					temp[i] = states2[1];
					GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
	    			states.add(state);
	    		}
	    	} else {
	    		pre = arrOfString[i-1];
				cur = arrOfString[i];
	    		if (pre.contains("00") && cur.contains("22")) {
	    			String[] temp = arrOfString.clone();
		    		String[] states2 = indentifyStatesFor22(pre, cur);
					if (states2[0]!=null && states2[1]!=null) { 
			    		temp[i-1] = states2[0];
						temp[i] = states2[1];
						GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
		    			states.add(state);
		    			System.out.println(temp[1]);
					}
				}
				if (cur.contains("00") && pre.contains("22") ) {
					String[] temp = arrOfString.clone();
					String[] states2 = indentifyStatesFor22(cur, pre);
					if (states2[0]!=null && states2[1]!=null) { 
						temp[i-1] = states2[1];
						temp[i] = states2[0];
						GameState state = GameState.getGameStateByColumnString(initialState, String.join("", temp));
						states.add(state);
					}
				}
			}

		}
//		System.out.println();
//		states.forEach(s -> s.printBoard());
    	return states;
    	     	
    }
    
    
    public static String[] indentifyStatesFor22(String strZero, String strTwo) {

    	String[] result = new String[2];
    	String[] zero = strZero.split("");
    	String[] two = strTwo.split("");
    	boolean flag = false;
    	for (int i=strTwo.indexOf("22"); i<strTwo.length()-1; i=i+1) {
    		//System.out.println(two[i] + two[i+1] + zero[i] + zero[i+1]);
    		if ("2".equalsIgnoreCase(two[i]) && "2".equalsIgnoreCase(two[i+1]) && "0".equalsIgnoreCase(zero[i]) && "0".equalsIgnoreCase(zero[i+1])) {
    			two[i]="0";
    			two[i+1]="0";
    			zero[i]="2";
    			zero[i+1]="2";
    			flag = true;
    			break;
    		}
    	}
    	
    	if(flag) {
    		result[0] = String.join("", zero);
    		result[1] = String.join("", two);
    	}
    	return result;
    }
    
    public static String[] indentifyStatesFor2222(String strZero, String strTwo) {
    	
    	String[] result = new String[2];
    	String[] zero = strZero.split("");
    	String[] two = strTwo.split("");
    	
    	for (int i=strTwo.indexOf("2222"); i<strTwo.length()-1; i=i+2) {
    		//System.out.println(two[i] + two[i+1] + zero[i] + zero[i+1]);
    		if ("2".equalsIgnoreCase(two[i]) && "2".equalsIgnoreCase(two[i+1]) && "0".equalsIgnoreCase(zero[i]) && "0".equalsIgnoreCase(zero[i+1])) {
    			two[i]="0";
    			two[i+1]="0";
    			zero[i]="2";
    			zero[i+1]="2";
    			break;
    		}
    	}
    	
    	result[0] = String.join("", zero);
    	result[1] = String.join("", two);
    	
    	return result;
    }
    
    public static List<GameState> getStatesById1(GameState initialState) {
    	
    	//System.out.println("### NextStates computation by State ID 1. ###");
    	List<GameState> states = new ArrayList<>();
    	String stateBoard = initialState.getStringByBoard();

    	String subPattern = "(?<zeroes>00)";
		Pattern p = Pattern.compile(subPattern);
		Matcher parentMatch1 = p.matcher(stateBoard);
		
		subPattern = "(0\\d\\d\\d0)";
		p = Pattern.compile(subPattern);
		Matcher parentMatch2 = p.matcher(stateBoard);
		
		subPattern = "(11\\d\\d11\\d\\d00)";
		p = Pattern.compile(subPattern);
		Matcher childMatch1 = p.matcher(stateBoard);
		
		subPattern = "(00\\d\\d11\\d\\d11)";
		p = Pattern.compile(subPattern);
		Matcher childMatch2 = p.matcher(stateBoard);
		
		subPattern = "(110\\d110)";
		p = Pattern.compile(subPattern);
		Matcher childMatch3 = p.matcher(stateBoard);
		
		subPattern = "(011\\d011)";
		p = Pattern.compile(subPattern);
		Matcher childMatch4 = p.matcher(stateBoard);
		
		if (parentMatch1.find()) {
			if (childMatch1.find()) {
				String newStateStr = GameState.swapSubstrings(stateBoard, "11", "00");
				GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
				states.add(state);
			} else if (childMatch2.find()) {
				String newStateStr = stateBoard.replaceFirst("11", "yy").replace("00", "xx").replace("11", "00").replace("xx", "11").replace("yy", "11");
				GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
				states.add(state);
			}
		} else if (parentMatch2.find()) {
			if (childMatch3.find()) {
				String newStateStr = stateBoard.replaceAll("110", "011");
				GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
				states.add(state);
			} else if (childMatch4.find()) {
				String newStateStr = stateBoard.replaceAll("011", "110");
				GameState state = GameState.getGameStateByRowString(initialState, newStateStr);
				states.add(state);
			}
		}
		
//		System.out.println();
//		states.forEach(s -> s.printBoard());
    	return states;
    	
    }
    
    public static GameState getGameStateByRowString(GameState parent, String stateBoard) {
    	GameState state = new GameState(parent.board, parent, 1, 0);
		state.board = state.getBoardByString(stateBoard);
		return state;
    }
    
    public static GameState getGameStateByColumnString(GameState parent, String stateBoard) {
    	GameState state = new GameState(parent.board, parent, 1, 0);
		state.board = GameState.getBoardByStringVertical(stateBoard);
		return state;
    }
    
    public static String[] stringSplitByNumber(String str, int size) {
        return (size<1 || str==null) ? null : str.split("(?<=\\G.{"+size+"})");
    }
    
    public static String swapCharByIndex(String str, int index1, int index2) {
    	
    	if( index1 < 0 || index2 < 0  || index1 >= str.length() || index2 >= str.length())
    	    return "Invalid index for charSwap";
	    char charArr[] = str.toCharArray();
	    char tmp = charArr[index1];
	    charArr[index1] = charArr[index2];
	    charArr[index2] = tmp;
	    //System.out.println("resulted state: " + index1 + " " + index2 +  " " + str + " " + new String(charArr) );
	    
	    return new String(charArr);
    	
    }
    
    public static String swapSubstrings(String originalString, String subString1, String subString2) {
    	String result = originalString;
    	if ( originalString.isEmpty() || ( !originalString.contains(subString1) && !originalString.contains(subString2))) {
    		//System.out.println("swap returns with : " + subString1 + " " + subString2  + " in original " + originalString + originalString.contains(subString1));
    		return result;
    	}
    	result = originalString.replaceFirst(subString1, "xx").replace(subString2, subString1).replace("xx", subString2);
    	return result;
    }
    
    protected String getStringByBoard() {	
    	StringBuilder state = new StringBuilder();
    	String boardState = new String();
    	for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++)
            	state.append(Integer.toString(this.board[i][j]));
        }
    	boardState = state.toString();
    	//System.out.println("String for the board: " + boardState);
    	
    	return boardState;
    }
    
    public static String getStringByBoardVertical(int [][] board) {	
    	StringBuilder state = new StringBuilder();
    	String boardState = new String();
    	for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 5; i++)
            	state.append(Integer.toString(board[i][j]));
        }
    	boardState = state.toString();
    	//System.out.println("String for the board: " + boardState);
    	
    	return boardState;
    }
    
    protected int[][] getBoardByString(String boardState) {
    	int[][] board = new int[5][4];
    	String[] state = boardState.split("");
    	for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
            	board[i][j] = Integer.parseInt(state[i * 4 + j]);
    	
    	return board;
    }
    
    public static int[][] getBoardByStringVertical(String boardState) {
    	int[][] board = new int[5][4];
    	String[] state = boardState.split("");
    	int count = 0;
    	for (int j = 0; j < 4; j++)
    		for (int i = 0; i < 5; i++) {
            	board[i][j] = Integer.parseInt(state[count]);
            	count = count + 1;
   		}
    	return board;
    }
    
    // add new methods for the GameState if necessary        

}



class AStarSearch{
    Queue<GameState> openSet;
    Set<GameState> closedSet;

    //Comparator for the GameState
    public Comparator<GameState> stateComparator = new Comparator<GameState>() {
        @Override
        public int compare(GameState o1, GameState o2) {
        	if (o1.fn - o2.fn != 0 )
            	return o1.fn - o2.fn;
            else
                return o1.getStateID().compareTo(o2.getStateID());
        }
    };   

    // print the states of board in open set
    public static void printOpenList(Queue<GameState> openSet) {
        
    	System.out.println("OPEN");
    	openSet.forEach(state -> {
            System.out.println(state.getStringByBoard());
            state.printBoard();
            System.out.println(state.fn + " " + state.gn + " " + state.h);
            System.out.println(state.parent!=null ? state.parent.getStringByBoard() : state.parent);
    	});  
    }

    public static void printClosedList(Set<GameState> closedSet) {
        
    	System.out.println("CLOSED");
        closedSet.forEach(state -> {
        	System.out.println(state.getStringByBoard());
            state.printBoard();
            System.out.println(state.fn + " " + state.gn + " " + state.h);
            System.out.println( state.parent!=null ? state.getStringByBoard() : state.parent);
        });
        
    }
    
    public static void printToBeRemovedState(GameState state) {
        
    	System.out.println(state.getStringByBoard());
        state.printBoard();
        System.out.println( state.fn + " " + state.gn + " " + state.h);
        String str = (state.parent!=null) ? state.parent.getStringByBoard() : null ;
        System.out.println(str);
    }
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    
    
    public static boolean doesQueueContain(Queue<GameState> stateQueue, GameState state ) {
    	
    	for (GameState s : stateQueue) {
    		if ( !s.getStateID().isEmpty() && !state.getStateID().isEmpty() && s.getStateID().equalsIgnoreCase(state.getStateID().toString()))
    			return true;
    	}
    	return false;
    }
    
    public static boolean isStatePresent(Set<GameState> stateSet, GameState state ) {
    	
    	for (GameState s : stateSet) {
    		if ( !s.getStateID().isEmpty() && !state.getStateID().isEmpty() && s.getStateID().equalsIgnoreCase(state.getStateID().toString()))
    			return true;
    	}
    	return false;
    }
    
    public static void removeFromPriorityQueue(Queue<GameState> stateQueue, GameState state) {
    	GameState removeState = new GameState();
    	for (GameState s : stateQueue) {
    		if ( s.getStateID().equalsIgnoreCase(state.getStateID())) {
				removeState = s;
				break;
			}
    	}
    	stateQueue.remove(removeState);
    }
    
    

    // implement the A* search
    public GameState aStarSearch(int flag, GameState initialState) {   
        // feel free to using other data structures if necessary
        openSet = new PriorityQueue<>(stateComparator);
        closedSet = new HashSet<>();
        int goalCheckTime = 0;
        int maxOPEN = 0;
        int maxCLOSED = 0;
        int steps = 0;
        int iterations = 1;
        GameState currentState = initialState;
        boolean goalFound = false;
        
        	if (flag == 400 || flag == 500) {
        		currentState = addManhattanDisToInitialState(currentState);
        	}
        	openSet.add(currentState);
        	while ( !openSet.isEmpty()) {	
        		
        		//for Falg = 300
        		if (openSet.size()>maxOPEN) {
        			maxOPEN = openSet.size();
        		}
        		if (flag == 200 || flag == 400) {
        			System.out.println("iteration " + (iterations++));
        		}
	        	currentState = openSet.poll();
	        	closedSet.add(currentState);
        		
        		//for Falg = 300
        		goalCheckTime += 1;
	        	if (currentState.goalCheck()) {
	        		if (flag == 200 || flag == 400) {
	        			AStarSearch.printToBeRemovedState(currentState);
	        		}
	        		break;
	        	}
	        	
	        	//for Falg = 300
        		if (closedSet.size()>maxCLOSED) {
        			maxCLOSED = closedSet.size();
        		}
	        	List<GameState> successorStates = currentState.getNextStates();
	        	
	        	if (flag == 400 || flag == 500) {
	        		successorStates = addManhattanDisToSuccessor(successorStates);
	        	}
	        	
	        	for (GameState state : successorStates) {
	        		
	        		boolean presentAnywhere = false;
	        		
	        		for (GameState s : openSet) {
	            		if (s.getStateID().equals(state.getStateID())) {
	            			presentAnywhere = true;
	            			if (s.gn > state.gn) {
	            				openSet.remove(s);
	            				openSet.add(state);
	            				break;
	            			}
	            		}
	            	}
	        		for (GameState s : closedSet) {
	            		if (s.getStateID().equals(state.getStateID())) {
	            			presentAnywhere = true;
	            			if (s.gn > state.gn) {
	            				openSet.add(state);
	            			}
	            		}
	            	}
	        		if(!presentAnywhere) {
	        			openSet.add(state);
	        		}

	        	}
	        	
	        	if (flag == 200 || flag == 400) {
	        		AStarSearch.printToBeRemovedState(currentState);
	        		AStarSearch.printOpenList(openSet);
	        		AStarSearch.printClosedList(closedSet);
	        		
	        	}

	        }
        	
        if (flag == 300 || flag == 500) {
        	int noOfStates = getAStarGoalPath(currentState);
        	System.out.println("goalCheckTimes " + goalCheckTime);
            System.out.println("maxOPENSize " + maxOPEN);
            System.out.println("maxCLOSEDSize " + maxCLOSED);
            System.out.println("steps " + (noOfStates-1)); 
        }
        
        return currentState;
    }    

    // add more methods for the A* search if necessary
    public static int getAStarGoalPath(GameState goalState) {
    	List<GameState> states = new ArrayList<GameState>();
    	int noOfStates = 1;
    	states.add(goalState);
    	while (goalState.parent!=null) {
    		goalState = goalState.parent;
    		states.add(goalState);
    		noOfStates += 1;
    	}
    	Collections.reverse(states);
    	printAStarGoalPath(states);
    	return noOfStates;
    }
    
    public static void printAStarGoalPath(List<GameState> goalState) {
    	if (!goalState.isEmpty()) {
    		goalState.forEach(state -> {
    			state.printBoard();
    			System.out.println();
    		});
    	}
    }
    
    public static GameState addManhattanDisToInitialState(GameState state) {
    	 state.h = findManhattanDisFromGoal(state);
    	 state.fn = state.gn + state.h; 
    	return state;
    }
    
    public static List<GameState> addManhattanDisToSuccessor(List<GameState> states) {
    	for (GameState s : states) {
    		int h = findManhattanDisFromGoal(s);
    		s.h = h;
    		s.fn = s.gn + h;
    	}   	
    	return states;
    }
    
    public static int findManhattanDisFromGoal(GameState curState) {
    	
    	int sum = 0;
    	int[] indeces = findIndecesOfGoalBlockInCur(curState);
    	int index1 = Math.abs((3-indeces[0]))+Math.abs((1-indeces[1]));
    	int index2 = Math.abs((3-indeces[2]))+Math.abs((2-indeces[3]));
    	int index3 = Math.abs((4-indeces[4]))+Math.abs((1-indeces[5]));
    	int index4 = Math.abs((4-indeces[6]))+Math.abs((2-indeces[7]));	
    	sum = (index1 + index2 + index3 + index4)/4;
    	
    	return sum;	
    }
    
    public static int[] findIndecesOfGoalBlockInCur(GameState state) {
    	
    	int count = 0;
    	int[] indeces = new int[8];
    	int[][] board = state.board;
    	for(int i=0; i<5; i++) {
    		for(int j=0; j<4; j++) {
    			if(board[i][j]==1) {
    				indeces[count++] = i;
    				indeces[count++] = j;
    			}
    		}
    	}
    	return indeces;
    } 
}



public class Klotski {   
    public static void printNextStates(GameState s) {
            	
    	List<GameState> states = s.getNextStates();

    	//System.out.println("### printNextStates computation started. ###");
        states = s.sortSuccessorStates(states);
    	for (GameState state: states) {
            state.printBoard();
            System.out.println();
        }
    }
    

    public static void main(String[] args) {
    	//System.out.println("input value: "+args.length + args[0] + args[1]);
        if (args == null || args.length < 21) {
            System.out.println("No proper input, enter 20 numbers\n");
        	return;
        }
        String[] inputState = Arrays.copyOfRange(args, 1, args.length);
        System.out.println("java Klotski " + args[0] + " " + String.join(" ", inputState));
        int flag = Integer.parseInt(args[0]);
        int[][] board = new int[5][4];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = Integer.parseInt(args[i * 4 + j + 1]);
            }                
        }        
        GameState s = new GameState(board, 0);
        //s.printBoard();

        if (flag == 100) {
            printNextStates(s);
            return;
        } else {
        	AStarSearch search = new AStarSearch();        
	         search.aStarSearch(flag, s); 
        }     
        
    }

}
