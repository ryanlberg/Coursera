/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 1/6/2021
 *  Description: BaseBall elmination as descrbed at https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    public BaseballElimination(String filename) {

    }

    public int numberOfTeams() {

    }

    public Iterable<String> teams() {

    }

    public int wins(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
    }

    public int losses(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
    }

    public int remaining(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEliminated(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
