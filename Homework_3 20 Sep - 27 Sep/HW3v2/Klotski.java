/** 
 * This skeleton is just an exmple.
 * Feel free to change this skeleton or using better ideas to implement.  
 */

import java.util.*;

// implement the class of block if necessary
class Block {
    
}

class GameState {      
    public int[][] board = new int[5][4];
    public GameState parent = null;
    public int cost = 0;
    public int steps = 0;

    public GameState(int [][] inputBoard, int steps) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
        this.steps = steps;
    }

    public GameState(int [][] inputBoard) {
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                this.board[i][j] = inputBoard[i][j];
    }

            
    // get all successors and return them in sorted order
    public List<GameState> getNextStates() {
        List<GameState> successors = new ArrayList<>();        

        return successors;
    }

    // return the 20-digit number as ID
    public String getStateID() {  
        String s = "";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++)
                s += this.board[i][j];
        }
        return s;
    }

    public void printBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++)
                System.out.print(this.board[i][j]);
            System.out.println();
        }
    }

    // check whether the current state is the goal
    public boolean goalCheck() {        

        return false;
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
            if (o1.cost - o2.cost != 0)
                return o1.cost - o2.cost;
            else
                return o1.getStateID().compareTo(o2.getStateID());
        }
    };   

    // print the states of board in open set
    public void printOpenList(int flag, GameState state) {
        System.out.println("OPEN");
        
        
    }

    public void printClosedList(int flag, GameState state) {
        System.out.println("CLOSED");
        
    }

    // implement the A* search
    public GameState aStarSearch(int flag, GameState state) {   
        // feel free to using other data structures if necessary
        openSet = new PriorityQueue<>(stateComparator);
        closedSet = new HashSet<>();
        int goalCheck = 0;
        int maxOPEN = -1;
        int maxCLOSED = -1;
        int steps = 0;       

        System.out.println("goalCheckTimes " + goalCheck);
        System.out.println("maxOPENSize " + maxOPEN);
        System.out.println("maxCLOSEDSize " + maxCLOSED);
        System.out.println("steps " + steps);
        return state;
    }    

    // add more methods for the A* search if necessary
}

public class Klotski {   
    public static void printNextStates(GameState s) {
        List<GameState> states = s.getNextStates();
        for (GameState state: states) {
            state.printBoard();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length < 21) {
            return;
        }
        int flag = Integer.parseInt(args[0]);
        int[][] board = new int[5][4];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = Integer.parseInt(args[i * 4 + j + 1]);
            }                
        }        
        GameState s = new GameState(board, 0);

        if (flag == 100) {
            printNextStates(s);
            return;
        }

        AStarSearch search = new AStarSearch();        
        search.aStarSearch(flag, s);  
        
    }

}
