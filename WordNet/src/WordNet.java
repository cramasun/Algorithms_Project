/******************************************************************************
 *  Compilation:  javac-algs4 WordNet.java
 *  Execution:    java-algs4 WordNet synsets.txt hypernyms.txt
 *  Dependencies: SAP.java DeluxeBFS.java Digraph.java ST.java Bag.java In StdIn
 ******************************************************************************/
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
public class WordNet {
    private final ST<String, Bag<Integer>> nounToSynsetIDMap;
    private final ST<Integer, String> synsetIDToSynsetMap;
    private final SAP sapCtxt;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        int numSynsets = 0;
        nounToSynsetIDMap = new ST<>();
        synsetIDToSynsetMap = new ST<>();

        // Read the synsets file line by line and populate 
        // nounToSynsetIDMap and sysetIDToSynsetMap DS
        while (!inSynsets.isEmpty()) {
            String[] tokens = inSynsets.readLine().split(",");
            String[] nouns = tokens[1].split(" ");
            int synsetID = Integer.parseInt(tokens[0]);

            numSynsets++;
            for (String noun : nouns) {
                Bag<Integer> idList = nounToSynsetIDMap.get(noun);
                if (idList == null) {
                    idList = new Bag<>();
                    nounToSynsetIDMap.put(noun, idList);
                }
                idList.add(synsetID);
            }
            synsetIDToSynsetMap.put(synsetID, tokens[1]);
        }

        // Build the WordNet graph from the Hypernyms File
        Digraph wordNetGraph = new Digraph(numSynsets);
        while (!inHypernyms.isEmpty()) {
            String[] tokens = inHypernyms.readLine().split(",");
            int vertex = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                wordNetGraph.addEdge(vertex, Integer.parseInt(tokens[i]));
            }
        }

        DirectedCycle graphCycle = new DirectedCycle(wordNetGraph);
        // Throw an Illegal Argument exception when the WordNet Graph has cycles
        if (graphCycle.hasCycle()) throw new IllegalArgumentException("WordNet Graph has a Cycle");

        // Throw an Illegal Argument exception when the WordNet Graph has more than one root
        int numRoots = 0;
        for (int v = 0; v < numSynsets; v++) {
            if (wordNetGraph.outdegree(v) == 0) numRoots++;
        } 

        if (numRoots > 1) throw new IllegalArgumentException("WordNet Graph contains multiple roots");

        sapCtxt = new SAP(wordNetGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsetIDMap.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Word is NULL");
        return nounToSynsetIDMap.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Word is not a noun");
        return sapCtxt.length(nounToSynsetIDMap.get(nounA), nounToSynsetIDMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("Word is not a noun");
        return synsetIDToSynsetMap.get(sapCtxt.ancestor(nounToSynsetIDMap.get(nounA), 
            nounToSynsetIDMap.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wNet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String word1 = StdIn.readString();
            String word2 = StdIn.readString();
            StdOut.println("Distance between " + word1 + " and " + word2 + " = " + 
                wNet.distance(word1, word2));
            StdOut.println("SCA of " + word1 + " and " + word2 + " = " + 
                wNet.sap(word1, word2));
        }
    }
}