/* **********************************************************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/24/2020
 *  Description: WordNet as described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 ************************************************************************************************************************ */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private final Digraph wordnet;
    private final HashMap<String, ArrayList<Integer>> nounToID;
    private final HashMap<Integer, ArrayList<String>> idToWords;
    private final SAP pathset;


    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        this.nounToID = new HashMap<String, ArrayList<Integer>>();
        this.idToWords = new HashMap<Integer, ArrayList<String>>();
        readSynsets(synsets);
        this.wordnet = new Digraph(idToWords.size());
        readHypernyms(hypernyms);
        this.pathset = new SAP(this.wordnet);
        int rootcounts = 0;
        for (int node : idToWords.keySet()) {
            if (this.wordnet.outdegree(node) == 0) {
                rootcounts++;
            }
        }
        if (rootcounts > 1) {
            throw new IllegalArgumentException();
        }
    }

    private void readSynsets(String synset) {
        In in = new In(synset);
        while (in.hasNextLine()) {
            String currentLine = in.readLine();
            String[] separated = currentLine.split(",");
            int id = Integer.parseInt(separated[0]);
            String[] words = separated[1].split(" ");
            for (String word : words) {
                if (!nounToID.containsKey(word)) {
                    nounToID.put(word, new ArrayList<Integer>());
                }
                nounToID.get(word).add(id);
                if (!idToWords.containsKey(id)) {
                    idToWords.put(id, new ArrayList<String>());
                }
                idToWords.get(id).add(word);
            }

        }

    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String curLine = in.readLine();
            String[] nums = curLine.split(",");
            int from = Integer.parseInt(nums[0]);
            for (int i = 1; i < nums.length; i++) {
                int to = Integer.parseInt(nums[i]);
                this.wordnet.addEdge(from, to);
            }

        }
    }


    public Iterable<String> nouns() {
        if (nounToID == null) {
            throw new NullPointerException();
        }
        return nounToID.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToID.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return this.pathset.length(this.nounToID.get(nounA),
                                   this.nounToID.get(nounB));

    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int id = this.pathset
                .ancestor(this.nounToID.get(nounA),
                          this.nounToID.get(nounB));
        return this.idToWords.get(id).get(0);
    }

    public static void main(String[] args) {
        WordNet test = new WordNet(args[0], args[1]);
        StdOut.print(test.distance("white_marlin", "mileage") + "\n");
        StdOut.print(test.distance("Black_Plague", "black_marlin") + "\n");
        StdOut.print(test.distance("American_water_spaniel", "histology") + "\n");
        StdOut.print(test.distance("Brown_Swiss", "barrel_roll") + "\n");
        StdOut.print(test.sap("individual", "edible_fruit") + "\n");
    }
}
