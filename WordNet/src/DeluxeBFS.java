/******************************************************************************
 *  Compilation:  javac-algs4 DeluxeBFS.java
 *  Execution:    java-algs4 DeluxeBFS digraph.txt
 *  Dependencies: Digraph.java Queue.java Bag.java
 ******************************************************************************/

import java.util.Arrays;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Bag;

public class DeluxeBFS {
    private final Digraph inGraph;    // Digrah on which shorest ancestral path is to be found 
    private int length;         // length of shortest ancestral path between v and w
    private int ancestor;       // a common ancestor of v and w that participates in a shortest ancestral path
    private int[] distFromV;    // Array maintaining the shortest distance of each vertex from v  in digraph
    private int[] distFromW;    // Array maintaining the shortest distance of each vertex from w in digraph

    DeluxeBFS(Digraph graph) {
        this.inGraph = new Digraph(graph);
        length = -1;
        ancestor = -1;
        distFromV = new int[graph.V()];
        distFromW = new int[graph.V()];
        Arrays.fill(distFromV, -1);
        Arrays.fill(distFromW, -1);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return ancestor;
    }

    // Lockstep BFS from multiple sources in set v and set w
    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();
        Bag<Integer> traversedV = new Bag<>();
        Bag<Integer> traversedW = new Bag<>();
        boolean bfsComplete = false;

        length = -1;
        ancestor = -1;

        // Add all the source vertices in set v to the queue and set the distance to be 0
        for (int s : v) {
            vQueue.enqueue(s);
            traversedV.add(s);
            distFromV[s] = 0;
        }

        // Add all the source vertices in set w to the queue and set the distance to be 0
        for (int s : w) {
            wQueue.enqueue(s);
            traversedW.add(s);
            distFromW[s] = 0;
        }

        while (!bfsComplete && (!vQueue.isEmpty() || !wQueue.isEmpty())) {
            if (!vQueue.isEmpty()) {
                int curV = vQueue.dequeue();
                // System.out.println("curV: " + curV);
                for (int neighbor : inGraph.adj(curV)) {
                    // System.out.println("curV: " + curV + " neighbor: " + neighbor);
                    // No path currently exists from v to neighbor
                    if (distFromV[neighbor] == -1) {
                        distFromV[neighbor] = distFromV[curV] + 1;
                        traversedV.add(neighbor);
                        // If a path from w to the neighbor exists, then
                        // the neighbor is a common ancestor
                        if (distFromW[neighbor] != -1) {
                            if (length == -1 || ((distFromW[neighbor] + distFromV[neighbor]) < length)) {
                                length = distFromW[neighbor] + distFromV[neighbor];
                                ancestor = neighbor;
                            }
                            else if (distFromV[neighbor] > length) {
                                bfsComplete = true;
                                break;
                            }
                        }
                        vQueue.enqueue(neighbor);
                    }
                }
            }
            if (!wQueue.isEmpty()) {
                int curW = wQueue.dequeue();
                // System.out.println("curW: " + curW);
                for (int neighbor : inGraph.adj(curW)) {
                    // System.out.println("curW: " + curW + " neighbor: " + neighbor);
                    // No path currently exists from w to neighbor
                    if (distFromW[neighbor] == -1) {
                        distFromW[neighbor] = distFromW[curW] + 1;
                        traversedW.add(neighbor);
                        // If a path from v to the neighbor exists, then
                        // the neighbor is the shortest common ancestor
                        if (distFromV[neighbor] != -1) {
                            if (length == -1 || ((distFromW[neighbor] + distFromV[neighbor]) < length)) {
                                length = distFromW[neighbor] + distFromV[neighbor];
                                ancestor = neighbor;
                            }
                            else if (distFromW[neighbor] > length) {
                                bfsComplete = true;
                                break;
                            }
                        }
                        wQueue.enqueue(neighbor);
                    }
                }
            }
        }

        // Reset the distFromV for the vertices traversed
        for (int cur : traversedV) {
            distFromV[cur] = -1;
        }

        // Reset the distFromW for the vertices traversed
        for (int cur : traversedW) {
            distFromW[cur] = -1;
        }
    }
}