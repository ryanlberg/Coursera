/* **********************************************************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/24/2020
 *  Description: WordNet as described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 ************************************************************************************************************************ */

public class WordNet {

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
    }

    public Iterable<String> nouns() {
    }

    public boolean isNoun(String word) {
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {

    }
}
