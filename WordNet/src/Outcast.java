/******************************************************************************
 *  Compilation:  javac-algs4 Outcast.java
 *  Execution:    java-algs4 Outcast synsets.txt hypernyms.txt outcast.txt
 *  Dependencies: WordNet.java SAP.java DeluxeBFS.java In StdOut
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnetCtxt;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordnetCtxt = wordnet;
    }  

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int maxDist = -1;

        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                dist += wordnetCtxt.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}