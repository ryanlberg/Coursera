/* **********************************************************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/24/2020
 *  Description: WordNet as described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 ************************************************************************************************************************ */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private Digraph wordnet;
    private HashMap<String, Word> nounToWord;
    private HashMap<Integer, Word> idToWord;
    private HashMap<Integer, Word> realidToWord;
    private SAP pathset;

    private class Word {

        private String noun;
        private int id;
        private HashSet<Integer> ids;

        public Word(String noun, int id) {
            this.noun = noun;
            this.id = id;
            this.ids = new HashSet<Integer>();
        }

        public void addToIds(int newId) {
            this.ids.add(newId);
        }

        public Iterable<Integer> getIds() {
            return this.ids;
        }

        public String getNoun() {
            return this.noun;
        }

        public int getWordId() {
            return this.id;
        }

    }

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        this.realidToWord = new HashMap<Integer, Word>();
        this.nounToWord = new HashMap<String, Word>();
        this.idToWord = new HashMap<Integer, Word>();
        readSynsets(synsets);

        this.wordnet = new Digraph(nounToWord.size());
        readHypernyms(hypernyms);
        this.pathset = new SAP(this.wordnet);


    }

    private void readSynsets(String synset) {
        In in = new In(synset);
        int seenids = 0;
        while (in.hasNextLine()) {
            String currentline = in.readLine();
            String[] separated = currentline.split(",");
            int id = Integer.parseInt(separated[0]);
            String[] words = separated[1].split(" ");
            for (String word : words) {
                if (!nounToWord.containsKey(word)) {
                    Word currentWord = new Word(word, seenids);
                    currentWord.addToIds(seenids);
                    this.realidToWord.put(seenids, currentWord);
                    this.nounToWord.put(word, currentWord);
                    this.idToWord.put(id, currentWord);
                    seenids++;
                }
                else {
                    this.idToWord.put(id, this.nounToWord.get(word));
                    this.nounToWord.get(word).addToIds(this.idToWord.get(id).getWordId());

                }
            }

        }

    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String curline = in.readLine();
            String[] nums = curline.split(",");
            int from = Integer.parseInt(nums[0]);
            Word toadd = this.idToWord.get(from);
            for (int i = 1; i < nums.length; i++) {
                int to = Integer.parseInt(nums[i]);
                Word togo = this.idToWord.get(to);
                for (int value : togo.getIds()) {
                    this.wordnet.addEdge(toadd.getWordId(), value);
                }

            }
        }
    }


    public Iterable<String> nouns() {
        if (nounToWord == null) {
            throw new NullPointerException();
        }
        return nounToWord.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounToWord.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return this.pathset.length(this.nounToWord.get(nounA).getWordId(),
                                   this.nounToWord.get(nounB).getWordId());

    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int id = this.pathset
                .ancestor(this.nounToWord.get(nounA).getWordId(),
                          this.nounToWord.get(nounB).getWordId());
        return this.realidToWord.get(id).getNoun();
    }

    public static void main(String[] args) {
        WordNet test = new WordNet(args[0], args[1]);
        //StdOut.print(test.nounToWord.get("bird").getIds());
        StdOut.print(test.distance("white_marlin", "mileage") + "\n");
        StdOut.print(test.distance("Black_Plague", "black_marlin") + "\n");
        StdOut.print(test.distance("American_water_spaniel", "histology") + "\n");
        StdOut.print(test.distance("Brown_Swiss", "barrel_roll") + "\n");
        StdOut.print(test.sap("individual", "edible_fruit") + "\n");
    }
}
