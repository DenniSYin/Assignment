package eightpuzzle;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;



public class EightPuzzle {

    
    static final byte [] goalTiles = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    // A* priority queue.
    final PriorityQueue <State> queue = new PriorityQueue<State>(100, new Comparator<State>() {
        @Override
        public int compare(State a, State b) { 
            return a.priority() - b.priority();
        }
    });

    // The closed state set.
    final HashSet <State> closed = new HashSet <State>();

    // State of the puzzle including its priority and chain to start state.
    class State {
        final byte [] tiles;    
        final int spaceIndex;   
        final int g;            
        final int h;            
        final State prev;       

        int priority() {
            return g + h;
        }

        
        State(byte [] initial) {
            tiles = initial;
            spaceIndex = index(tiles, 8);
            g = 0;
            h = heuristic(tiles);
            prev = null;
        }

        
        State(State prev, int slideFromIndex) {
            tiles = Arrays.copyOf(prev.tiles, prev.tiles.length);
            tiles[prev.spaceIndex] = tiles[slideFromIndex];
            tiles[slideFromIndex] = 8;
            spaceIndex = slideFromIndex;
            g = prev.g + 1;
            h = heuristic(tiles);
            this.prev = prev;
        }

        
        boolean isGoal() {
            return Arrays.equals(tiles, goalTiles);
        }

        
        State moveS() { return spaceIndex > 2 ? new State(this, spaceIndex - 3) : null; }       
        State moveN() { return spaceIndex < 6 ? new State(this, spaceIndex + 3) : null; }       
        State moveE() { return spaceIndex % 3 > 0 ? new State(this, spaceIndex - 1) : null; }       
        State moveW() { return spaceIndex % 3 < 2 ? new State(this, spaceIndex + 1) : null; }

        // Print this state.
        void print() {
            System.out.println("p = " + priority() + " = g+h = " + g + "+" + h);
           
           // String[] s ;
            
           // myarraylist.add(s);
            
            
		for (int a = 0; a<9 ; a++)
           //s = tiles[1] ;
            	if (tiles[a]==8)
            		 tiles[a]= 8;
			
           //	a = "#" ;tiles[j]
			for (int i = 0; i < 9; i += 3)
                System.out.println(tiles[i] + " " + tiles[i+1] + " " + tiles[i+2]);
            			
            
        }

        
        void printAll() {
            if (prev != null) prev.printAll();
            System.out.println();
            print();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof State) {
                State other = (State)obj;
                return Arrays.equals(tiles, other.tiles);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(tiles);
        }
    }

    
    void addSuccessor(State successor) {
        if (successor != null && !closed.contains(successor)) 
            queue.add(successor);
    }

    
    void solve(byte [] initial) {

        queue.clear();
        closed.clear();

        
        //long start = System.currentTimeMillis();

        
        queue.add(new State(initial));

        while (!queue.isEmpty()) {

            
            State state = queue.poll();

           
            if (state.isGoal()) {
                
                state.printAll();
                return;
            }

            
            closed.add(state);

            
            addSuccessor(state.moveS());
            addSuccessor(state.moveN());
            addSuccessor(state.moveW());
            addSuccessor(state.moveE());
        }
    }

    
    static int index(byte [] a, int val) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == val) return i;
        return -1;
    }

    
    static int manhattanDistance(int a, int b) {
        return Math.abs(a / 3 - b / 3) + Math.abs(a % 3 - b % 3);
    }

   
    static int heuristic(byte [] tiles) {
        int h = 0;
        for (int i = 0; i < tiles.length; i++)
            if (tiles[i] != 0)
                h = Math.max(h, manhattanDistance(i, tiles[i]));
        return h;
    }

    public static void main(String[] args) {
    	      
       // byte[] initial = input.getBytes(StandardCharsets.UTF_8);
    	//byte [] initial = { 8, 0, 6, 5, 4, 7, 2, 3, 1 };

        // This is taken from the SO example.
    	Scanner sc = new Scanner(System.in);
        System.out.print("Please input a initial state(# or 8 represents Empty Tile)");
        String input = sc.nextLine();
        sc.close();    
      
        //char initial = input.charAt(0);
        byte [] initial = input.getBytes();        
        for(int i =0 ; i<initial.length; i++)
        	{initial[i]-=48;if (initial[i]==-13) initial[i]=8;}
        
        new EightPuzzle().solve(initial);
    }
}