import java.util.*;

public class successor {
    public static class JugState {
        int[] Capacity = new int[]{0,0};
        int[] Content = new int[]{0,0};
        public JugState(JugState copyFrom)
        {
            this.Capacity[0] = copyFrom.Capacity[0];
            this.Capacity[1] = copyFrom.Capacity[1];
            this.Content[0] = copyFrom.Content[0];
            this.Content[1] = copyFrom.Content[1];
        }
        public JugState()
        {
        }
        public JugState(int A,int B)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
        }
        public JugState(int A,int B, int a, int b)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
            this.Content[0] = a;
            this.Content[1] = b;
        }
 
        public void printContent()
        {
            System.out.println(this.Content[0] + " " + this.Content[1]);
        }
 
        public ArrayList<JugState> getNextStates(){
            ArrayList<JugState> successors = new ArrayList<>();

            // TODO add all successors to the list
            int A = this.Capacity[0];
            int B = this.Capacity[1];
            int a = this.Content[0];
            int b = this.Content[1];
            
            
            if ( A<0 || B<0 || a<0 || b<0 ) {
               System.out.println("Negative numbers cannot be value for capacity/content(s) of water jar\n Please correct your input values."); 
            } else if ( A==0 || B==0 ) {
                System.out.println("Capacity of water jar(s) cannot be Zero. Please check your input values");
            } else if ( a>A || b>B) {
                System.out.println("Content cannot be greater than capacity of water jar. Please check your input values.");
            } else {
                if (a>0) {
                    JugState j = new JugState(A, B, 0, b);
                    successors.add(j);
                }
                if (b>0) {
                    JugState j = new JugState(A, B, a, 0);
                    successors.add(j);
                }
                if (a<A) {
                    JugState j = new JugState(A, B, a+(A-a), b);
                    successors.add(j);
                }
                if (b<B) {
                    JugState j = new JugState(A, B, a, b+(B-b));
                    successors.add(j);
                }
                if ( a>0 && (B-b)>0) {
                    if ( a>=(B-b) ) {
                        JugState j = new JugState(A, B, a-(B-b), B);
                        successors.add(j);
                    } else if ( a<(B-b) ) {
                        JugState j = new JugState(A, B, 0, a+b);
                        successors.add(j);
                    }
                }
                if ( b>0 && (A-a)>0) {
                    if ( b>=(A-a) ) {
                        JugState j = new JugState(A, B, A, b-(A-a));
                        successors.add(j);
                    } else if ( b<(A-a) ) {
                        JugState j = new JugState(A, B, a+b, 0);
                        successors.add(j);
                    }
                }
            }
            
            // Use this snippet to sort the successors
            Collections.sort(successors, (s1, s2) -> {
                if (s1.Content[0] < s2.Content[0]) {
                    return -1;
                } else if (s1.Content[0] > s2.Content[0]) {
                    return 1;
                } else return Integer.compare(s1.Content[1], s2.Content[1]);
            });

            return successors;
        }

    }

    public static void main(String[] args) {
        if( args.length != 4 )
        {
            System.out.println("Usage: java successor [A] [B] [a] [b]");
            return;
        }

        // parse command line arguments
        JugState a = new JugState();
        a.Capacity[0] = Integer.parseInt(args[0]);
        a.Capacity[1] = Integer.parseInt(args[1]);
        a.Content[0] = Integer.parseInt(args[2]);
        a.Content[1] = Integer.parseInt(args[3]);

        // Implement this function
        ArrayList<JugState> asist = a.getNextStates();

        // Print out generated successors
        for(int i=0;i< asist.size(); i++)
        {
            asist.get(i).printContent();
        }

        return;
    }
}