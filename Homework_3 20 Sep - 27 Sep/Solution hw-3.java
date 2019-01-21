import java.util.*;
//0:empty block, 1:2*2 block, 2:2*1 block, 3:1*2 block, 4:1*1 block

public class Klotski {
    public static class GameState {
        private static class Block {

            public int xpos;
            public int ypos;
            public int type;
            public int xrange;
            public int yrange;

            Block() {}

            Block(int xpos, int ypos, int type) {
                this.xpos = xpos;
                this.ypos = ypos;
                this.type = type;
                switch (type) {
                    case 1: xrange = 2; yrange = 2; break;
                    case 2: xrange = 2; yrange = 1; break;
                    case 3: xrange = 1; yrange = 2; break;
                    case 4: xrange = 1; yrange = 1; break;
                }
            }
        }

        public int[][] board = new int[5][4];
        public GameState parent = null;
        public int cost = 0;
        public int steps = 0;

        public GameState() {}

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

        @Override
        public boolean equals(Object e_o) {
            GameState e = (GameState) e_o;
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 4; j++)
                    if (this.board[i][j] == -1 || e.board[i][j] == -1)
                        continue;
                    else if (this.board[i][j] != e.board[i][j])
                        return false;

            return true;
        }

        private ArrayList<Block> boardScan() {
            ArrayList<Block> blockPos = new ArrayList<>();
            int [][] boardCopy = getBoardCopy(this.board);

            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 4; j++) {
                    int type = boardCopy[i][j];
                    if (type != 0 && type != -1) {
                        Block b = new Block(i, j, type);
                        blockPos.add(b);
                        for (int m = 0; m < b.xrange; m++)
                            for (int n = 0; n < b.yrange; n++)
                                boardCopy[i+m][j+n] = -1;
                    }
                }

            return blockPos;
        }

        private int[][] getBoardCopy(int [][] board) {
            int [][] boardCopy = new int[5][4];
            for(int i = 0; i < 5; i++)
                for(int j = 0; j < 4; j++)
                    boardCopy[i][j] = board[i][j];

            return boardCopy;
        }

        private int[][] moveBlock(Block b, char dir) {
            int dx = 0, dy = 0;
            switch (dir) {
                case 'u': dx = -1; dy = 0; break;
                case 'd': dx = 1; dy = 0; break;
                case 'l': dx = 0; dy = -1; break;
                case 'r': dx = 0; dy = 1; break;
            }

            int [][] boardCopy = getBoardCopy(this.board);
            for (int i = 0; i < b.xrange; i++)
                for (int j = 0; j < b.yrange; j++)
                    boardCopy[b.xpos+i][b.ypos+j] = 0;
            for (int i = 0; i < b.xrange; i++)
                for (int j = 0; j < b.yrange; j++)
                    boardCopy[b.xpos+dx+i][b.ypos+dy+j] = b.type;

            return boardCopy;
        }

        private boolean checkBlockMove(Block b, char dir) {
            if (dir == 'u') {
                if (b.xpos == 0)
                    return false;
                else {
                    for (int i = 0; i < b.yrange; i++)
                        if (this.board[b.xpos-1][b.ypos+i] != 0)
                            return false;
                    return true;
                }
            }
            else if (dir == 'd') {
                if (b.xpos >= 5-b.xrange)
                    return false;
                else {
                    for (int i = 0; i < b.yrange; i++)
                        if (this.board[b.xpos+b.xrange][b.ypos+i] != 0)
                            return false;
                    return true;
                }
            }
            else if (dir == 'l') {
                if (b.ypos == 0)
                    return false;
                else {
                    for (int i = 0; i < b.xrange; i++)
                        if (this.board[b.xpos+i][b.ypos-1] != 0)
                            return false;
                    return true;
                }
            }
            else if (dir == 'r') {
                if (b.ypos == 4-b.yrange)
                    return false;
                else {
                    for (int i = 0; i < b.xrange; i++)
                        if (this.board[b.xpos+i][b.ypos+b.yrange] != 0)
                            return false;
                    return true;
                }
            }
            return true;
        }

        public ArrayList<GameState> getNextStates() {
            ArrayList<GameState> successors = new ArrayList<>();

            ArrayList<Block> blockPos = boardScan();

            GameState s;
            for (int i = 0; i < blockPos.size(); i++) {
                Block b = blockPos.get(i);
                if (b.type == 4) {
                    for (int m = 0; m < 5; m++)
                        for (int n = 0; n < 4; n++)
                            if (this.board[m][n] == 0) {
                                int[][] boardCopy = getBoardCopy(this.board);
                                boardCopy[m][n] = b.type;
                                boardCopy[b.xpos][b.ypos] = 0;
                                s = new GameState(boardCopy, this.steps+1);
                                s.parent = this;
                                if (!successors.contains(s))
                                    successors.add(s);
                            }
                    continue;
                }

                char[] dirList = new char[]{'u', 'd', 'l', 'r'};
                for (int j = 0; j < dirList.length; j++)
                    if (checkBlockMove(b, dirList[j])) {
                        s = new GameState(moveBlock(b, dirList[j]), this.steps+1);
                        s.parent = this;
                        if (!successors.contains(s))
                            successors.add(s);
                    }
            }

            return successors;
        }

        public String getBoardString() {
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

        public void printStateInfo(CostCal calC, GameState g) {
            System.out.println(getBoardString());

            printBoard();

            int fVal = calC.cal(this, g);
            int gVal = this.steps;
            int hVal = fVal - gVal;
            System.out.println(fVal+" "+gVal+" "+hVal);

            if (parent != null)
                System.out.println(parent.getBoardString());
            else
                System.out.println("null");
        }

        @Override
        public int hashCode() {
            return getBoardString().hashCode();
        }

    }

    interface CostCal {
        int cal(GameState c, GameState g);
    }

    public static class UniformCostCal implements CostCal {
        @Override
        public int cal(GameState c, GameState g) {
            return c.steps;
        }
    }

    public static class HeruisticCostCal implements CostCal {
        @Override
        public int cal(GameState c, GameState g) {
            int cx = 0, cy = 0, gx = 0, gy = 0;
            outer_loop_c:
            for (cx = 0; cx < 5; cx++)
                for (cy = 0; cy < 4; cy++)
                    if (c.board[cx][cy] == 1)
                        break outer_loop_c;

            outer_loop_g:
            for (gx = 0; gx < 5; gx++)
                for (gy = 0; gy < 4; gy++)
                    if (g.board[gx][gy] == 1)
                        break outer_loop_g;

            return Math.abs(cx-gx) + Math.abs(cy-gy) + c.steps;
        }
    }

    public static Comparator<GameState> stateComparator = new Comparator<GameState>() {
        @Override
        public int compare(GameState o1, GameState o2) {
            if (o1.cost - o2.cost != 0)
                return o1.cost - o2.cost;
            else
                return o1.getBoardString().compareTo(o2.getBoardString());
        }
    };

    public static class AStarSearch{

        public PriorityQueue<GameState> openList = new PriorityQueue<>(11, stateComparator);
        public HashSet<GameState> closedList = new HashSet<>();
        public HashMap<String, Integer> stateCostR = new HashMap<>();
        public int maxQueueLength = 0, goalCheckTime = 0;
        private boolean printFlag = false;
        public int maxClosedLength = 0;
        public int iterations = 0;

        public void setPrintFlag(boolean f) {
            this.printFlag = f;
        }

        public void printOpenList(CostCal calC, GameState goal) {
            System.out.println("OPEN");

            for (GameState p: this.openList)
                p.printStateInfo(calC, goal);
        }

        public void printClosedList(CostCal calC, GameState goal) {
            System.out.println("CLOSED");

            for (GameState p: this.closedList)
                p.printStateInfo(calC, goal);
        }

        public GameState aStarSearch(GameState s, GameState goal, CostCal calC) {
            openList.clear();
            closedList.clear();

            openList.add(s);
            stateCostR.put(s.getBoardString(), calC.cal(s, goal));
            while (!openList.isEmpty()) {
                GameState n = openList.poll();
                iterations += 1;
                if (this.printFlag)
                    System.out.println("iteration " + iterations);
                if (this.printFlag)
                    n.printStateInfo(calC, goal);
                this.goalCheckTime++;
                closedList.add(n);
                if (closedList.size() > maxClosedLength)
                    this.maxClosedLength = closedList.size();
                if (n.equals(goal))
                    return n;

                ArrayList<GameState> nextStates = n.getNextStates();
                for (int i = 0; i < nextStates.size(); i++) {
                    GameState ns = nextStates.get(i);
                    ns.cost = calC.cal(ns, goal);

                    String id = ns.getBoardString();
                    if (stateCostR.containsKey(id)) {
                        int oldCost = stateCostR.get(id);
                        if (oldCost > ns.cost) {
                            if (openList.contains(ns)) {
                                openList.remove(ns);
                            }
                            if (closedList.contains(ns)) {
                                closedList.remove(ns);
                            }
                            openList.add(ns);
                        }
                    }
                    else {
                        stateCostR.put(id, ns.cost);
                        openList.add(ns);
                    }
                }

                if (openList.size() > maxQueueLength)
                    this.maxQueueLength = openList.size();

                if (this.printFlag) {
                    printOpenList(calC, goal);
                    printClosedList(calC, goal);
                }
            }

            return null;
        }
    }

    public static void printNextStates(GameState s) {
        ArrayList<GameState> stateList = s.getNextStates();
        stateList.sort(stateComparator);
        for (int i = 0; i < stateList.size(); i++) {
            stateList.get(i).printBoard();
            System.out.println();
        }
    }

    public static void main(String[] args) {

        int flag = Integer.parseInt(args[0]);
        int[][] board = new int[5][4];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
                board[i][j] = Integer.parseInt(args[i*4+j+1]);
        GameState s = new GameState(board, 0);

        for (int i = 0; i < 5; i++)
            for(int j = 0; j < 4; j++)
                board[i][j] = -1;
        board[3][1] = 1; board[3][2] = 1;
        board[4][1] = 1; board[4][2] = 1;
        GameState g = new GameState(board, 0);

        if (flag == 100) {
            printNextStates(s);
            return;
        }

        AStarSearch searchP = new AStarSearch();
        GameState f = null;
        if (flag == 200){
            searchP.setPrintFlag(true);
            searchP.aStarSearch(s, g, new UniformCostCal());
            return;
        }
        else if (flag == 300)
            f = searchP.aStarSearch(s, g, new UniformCostCal());
        else if (flag == 400) {
            searchP.setPrintFlag(true);
            searchP.aStarSearch(s, g, new HeruisticCostCal());
            return;
        }
        else if (flag == 500)
            f = searchP.aStarSearch(s, g, new HeruisticCostCal());

        Stack<GameState> tempStore = new Stack<>();
        while (f != null) {
            tempStore.push(f);
            f = f.parent;
        }

        int steps = tempStore.size() - 1;
        while (!tempStore.isEmpty()) {
            tempStore.pop().printBoard();
            System.out.println();
        }

        System.out.println("goalCheckTimes " + searchP.goalCheckTime);
        System.out.println("maxOPENSize " + searchP.maxQueueLength);
        System.out.println("maxCLOSEDSize " + searchP.maxClosedLength);
        System.out.println("steps " + steps);
    }

}