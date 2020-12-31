/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 12/24/2020
 *  Description: Sap as described at https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SAP {

    private final Digraph G;
    private final HashMap<Integer, HashMap<Integer, Integer>> commonAncestors;
    private final HashMap<Integer, HashMap<Integer, Integer>> shortestPathLengths;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.G = new Digraph(G);
        this.commonAncestors = new HashMap<Integer, HashMap<Integer, Integer>>();
        this.shortestPathLengths = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    // 2 simultaneous bfs to find the common ancestor of between b and w if there is one. otherwise return -1
    private void biDFS(int v, int w) {
        HashMap<Integer, Integer> vseen = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> wseen = new HashMap<Integer, Integer>();
        Queue<int[]> vqueue = new Queue<>();
        Queue<int[]> wqueue = new Queue<>();
        vqueue.enqueue(new int[] { v, 0 });
        wqueue.enqueue(new int[] { w, 0 });
        int minSeen = -1;
        int spot = -1;
        while (!vqueue.isEmpty() && !wqueue.isEmpty()) {

            int[] curv = vqueue.dequeue();
            int[] curw = wqueue.dequeue();

            vseen.put(curv[0], curv[1]);
            wseen.put(curw[0], curw[1]);

            if (vseen.containsKey(curw[0])) {
                int value = curw[1] + vseen.get(curw[0]);
                if (minSeen != -1) {
                    if (value < minSeen) {
                        minSeen = value;
                        spot = curw[0];
                    }
                }
                else {
                    minSeen = value;
                    spot = curw[0];
                }
            }

            if (wseen.containsKey(curv[0])) {
                int value = curv[1] + wseen.get(curv[0]);
                if (minSeen != -1) {
                    if (value < minSeen) {
                        minSeen = value;
                        spot = curv[0];
                    }
                }
                else {
                    minSeen = value;
                    spot = curv[0];
                }
            }

            for (Integer edge : this.G.adj(curv[0])) {
                if (!vseen.containsKey(edge)) {
                    vqueue.enqueue(new int[] { edge, curv[1] + 1 });
                    vseen.put(edge, curv[1] + 1);
                }
            }

            for (Integer edge : this.G.adj(curw[0])) {
                if (!wseen.containsKey(edge)) {
                    wqueue.enqueue(new int[] { edge, curw[1] + 1 });
                    wseen.put(edge, curw[1] + 1);
                }
            }


        }

        while (!wqueue.isEmpty()) {
            int[] curw = wqueue.dequeue();
            wseen.put(curw[0], curw[1]);
            if (vseen.containsKey(curw[0])) {
                int value = curw[1] + vseen.get(curw[0]);
                if (minSeen != -1) {
                    if (value < minSeen) {
                        minSeen = value;
                        spot = curw[0];
                    }
                }
                else {
                    minSeen = value;
                    spot = curw[0];
                }
            }
            for (Integer edge : this.G.adj(curw[0])) {
                if (!wseen.containsKey(edge)) {
                    wqueue.enqueue(new int[] { edge, curw[1] + 1 });
                    wseen.put(edge, curw[1] + 1);
                }
            }

        }

        while (!vqueue.isEmpty()) {
            int[] curv = vqueue.dequeue();
            vseen.put(curv[0], curv[1]);
            if (wseen.containsKey(curv[0])) {
                int value = curv[1] + wseen.get(curv[0]);
                if (minSeen != -1) {
                    if (value < minSeen) {
                        minSeen = value;
                        spot = curv[0];
                    }
                }
                else {
                    minSeen = value;
                    spot = curv[0];
                }

            }
            for (Integer edge : this.G.adj(curv[0])) {
                if (!vseen.containsKey(edge)) {
                    vqueue.enqueue(new int[] { edge, curv[1] + 1 });
                    vseen.put(edge, curv[1] + 1);
                }
            }

        }

        setAncestorsAndPaths(v, w, spot, minSeen);
    }

    private void setAncestorsAndPaths(int v, int w, int ancester, int length) {
        if (!this.commonAncestors.containsKey(v)) {
            this.commonAncestors.put(v, new HashMap<Integer, Integer>());
        }
        this.commonAncestors.get(v).put(w, ancester);
        if (!this.commonAncestors.containsKey(w)) {
            this.commonAncestors.put(w, new HashMap<Integer, Integer>());
        }
        this.commonAncestors.get(w).put(v, ancester);

        if (!this.shortestPathLengths.containsKey(v)) {
            this.shortestPathLengths.put(v, new HashMap<Integer, Integer>());
        }
        this.shortestPathLengths.get(v).put(w, length);

        if (!this.shortestPathLengths.containsKey(w)) {
            this.shortestPathLengths.put(w, new HashMap<Integer, Integer>());
        }
        this.shortestPathLengths.get(w).put(v, length);

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return 0;
        }

        if (this.shortestPathLengths.containsKey(v) && this.shortestPathLengths.get(v)
                                                                               .containsKey(w)) {
            return this.shortestPathLengths.get(v).get(w);
        }

        biDFS(v, w);
        return this.shortestPathLengths.get(v).get(w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return v;
        }
        if (this.commonAncestors.containsKey(v) && this.commonAncestors.get(v).containsKey(w)) {
            return this.commonAncestors.get(v).get(w);
        }

        biDFS(v, w);
        return this.commonAncestors.get(v).get(w);


    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int minSeen = Integer.MAX_VALUE;
        for (Integer newv : v) {
            if (newv == null) {
                throw new IllegalArgumentException();
            }
            for (Integer neww : w) {
                if (neww == null) {
                    throw new IllegalArgumentException();
                }
                int curLength = length(newv, neww);
                if (curLength < minSeen && curLength != -1) {
                    minSeen = curLength;
                }
            }
        }
        if (minSeen < Integer.MAX_VALUE) {
            return minSeen;
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int minSeen = Integer.MAX_VALUE;
        int minAncestor = -1;
        for (Integer newv : v) {
            if (newv == null) {
                throw new IllegalArgumentException();
            }
            for (Integer neww : w) {
                if (neww == null) {
                    throw new IllegalArgumentException();
                }
                int curAncestor = ancestor(newv, neww);
                int curPathLength = this.shortestPathLengths.get(newv).get(neww);
                if (curPathLength < minSeen && curPathLength != -1) {
                    minAncestor = curAncestor;
                }
            }
        }
        return minAncestor;

    }

    // do unit testing of this class
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
