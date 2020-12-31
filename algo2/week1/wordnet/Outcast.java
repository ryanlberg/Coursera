/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/27/2020
 *  Description: Outcast class described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class Outcast {

    private final WordNet castNet;

    public Outcast(WordNet wordnet) {
        this.castNet = wordnet;
    }

    public String outcast(String[] nouns) {
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                String wordOne = nouns[i];
                String wordTwo = nouns[j];
                int dist = this.castNet.distance(wordOne, wordTwo);
                if (!counts.containsKey(wordOne)) {
                    counts.put(wordOne, 0);
                }
                if (!counts.containsKey(wordTwo)) {
                    counts.put(wordTwo, 0);
                }
                counts.put(wordOne, counts.get(wordOne) + dist);
                counts.put(wordTwo, counts.get(wordTwo) + dist);
            }
        }

        int maxSeen = -1;
        String longWord = "";
        for (String word : counts.keySet()) {
            int d = counts.get(word);
            if (d > maxSeen) {
                maxSeen = d;
                longWord = word;
            }
        }
        return longWord;

    }

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
