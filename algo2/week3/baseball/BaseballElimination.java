/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 1/6/2021
 *  Description: BaseBall elmination as descrbed at https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import java.util.ArrayList;
import java.util.HashMap;


public class BaseballElimination {
	
	private int teamCount;
	private FordFulkerson g;
	private Team[] teams;
	private HashMap<String, Integer> stringToId;

    public BaseballElimination(String filename) {
    	In reader = new In(filename);
    	
    	this.teamCount = reader.readInt();
    	reader.readLine();
    	this.teams = new Team[this.teamCount];
    	this.stringToId = new HashMap<>();
    	for(int i = 0; i < this.teamCount; i++) {
    		teams[i] = getTeam(reader, i);
    		stringToId.put(teams[i].getName(), i);
    	}
    	
    }
    
    private Team getTeam(In in, int id) {
    	String current = in.readLine();
    	current = current.trim();
    	String[] values = current.split("\s+");
    
    	String name = values[0].trim();
    	
    	int wins = Integer.parseInt(values[1].trim());
    	int losses = Integer.parseInt(values[2].trim());
    	int left = Integer.parseInt(values[3].trim());
    	int[] remaining = new int[this.teamCount];
    	
    	for(int i = 0; i < remaining.length; i++) {
    		remaining[i] = Integer.parseInt(values[4+i].trim());
    	}
    	return new Team(name, id, wins, losses, left, remaining);
    }

    public int numberOfTeams() {
    	return this.teamCount;
    }

    public Iterable<String> teams() {
    	ArrayList<String> ret = new ArrayList<>();
    	for(Team t : teams) {ret.add(t.getName());}
    	return ret;
    }

    public int wins(String team) {
        if (team == null || !this.stringToId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return teams[this.stringToId.get(team)].getWins();
    }

    public int losses(String team) {
        if (team == null || !this.stringToId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return teams[this.stringToId.get(team)].getLosses();
    }

    public int remaining(String team) {
        if (team == null || !this.stringToId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return teams[this.stringToId.get(team)].getGamesLeft();
    }

    public int against(String team1, String team2) {
        if (team1 == null  || !stringToId.containsKey(team1) || team2 == null || !stringToId.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return teams[this.stringToId.get(team1)].getRemaining(this.stringToId.get(team2));
    }

    public boolean isEliminated(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return false;

    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return new ArrayList<String>();
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
