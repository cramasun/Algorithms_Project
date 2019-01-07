/******************************************************************************
 *  Compilation:  javac-algs4 SAP.java DeluxeBFS.java
 *  Execution:    java-algs4 SAP digraph1.txt
 *  Dependencies: Digraph.java Queue.java Bag.java In StdIn StdOut
 ******************************************************************************/
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final DeluxeBFS bfsCtxt;
    private final int numGraphVertices;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        // SAP is an immutable interface. Hence clone the input digraph and 
        // make it immutable type
        bfsCtxt = new DeluxeBFS(G);
        numGraphVertices = G.V();
    }

    // Validate vertex sets for invalid vertices
    private void validateVertices(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Invalid Vertex Set");
        for (Integer curV : v) {
            if (curV == null || curV < 0 || curV >= numGraphVertices) {
                throw new IllegalArgumentException("Invalid Vertex");
            }
        }

        for (Integer curW : w) {
            if (curW == null || curW < 0 || curW >= numGraphVertices) {
                throw new IllegalArgumentException("Invalid Vertex");
            }
        }
    }

    // Check if any vertex in set v is same as that in set w
    private int findCommonVertex(Iterable<Integer> v, Iterable<Integer> w) {
        for (int curV : v) {
            for (int curW : w) {
                if (curV == curW) {
                    return curV;
                }
            }
        }
        return -1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Bag<Integer> vList = new Bag<>();
        Bag<Integer> wList = new Bag<>();
        vList.add(v);
        wList.add(w);
        validateVertices(vList, wList);
        // If any vertex in set v is same as that in set w, then
        // length is 0
        if (findCommonVertex(vList, wList) != -1) return 0;
        return bfsCtxt.length(vList, wList);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Bag<Integer> vList = new Bag<>();
        Bag<Integer> wList = new Bag<>();
        vList.add(v);
        wList.add(w);
        validateVertices(vList, wList);
        // If any vertex in set v is same as that in set w, then
        // ancestor is the common vertex
        int commonVertex = findCommonVertex(vList, wList);
        if (commonVertex != -1) return commonVertex;
        return bfsCtxt.ancestor(vList, wList);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v, w);
        // If any vertex in set v is same as that in set w, then
        // length is 0
        if (findCommonVertex(v, w) != -1) return 0;
        return bfsCtxt.length(v, w);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v, w);
        // If any vertex in set v is same as that in set w, then
        // ancestor is the common vertex
        int commonVertex = findCommonVertex(v, w);
        if (commonVertex != -1) return commonVertex;
        return bfsCtxt.ancestor(v, w);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}