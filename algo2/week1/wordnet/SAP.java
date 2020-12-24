/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/24/2020
 *  Description: Sap as described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
    }

    public int length(int v, int w) {

        return -1;
    }

    public int ancestor(int v, int w) {
        return -1;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {

    }

    public int acestor(Iterable<Integer> v, Iterable<Integer> w) {
        `
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
