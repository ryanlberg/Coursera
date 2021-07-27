/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 1/6/2021
 *  Description: BaseBall elmination as descrbed at https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import java.util.ArrayList;
import java.util.HashMap;


public class BaseballElimination {
	
	private final int teamCount;
	private final Team[] teams;
	private final HashMap<String, Integer> stringToId;

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
        if (team == null || !stringToId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        FordFulkIt(team);
        return teams[stringToId.get(team)].getIsEliminated();

    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !stringToId.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        
        return teams[stringToId.get(team)].getEleminated();
    }
    
    private void FordFulkIt(String team) {
    	
    	int game_v = ((this.teamCount-1)*(this.teamCount-2))/2;
    	int nodes = this.teamCount-1 + game_v + 2;
    	
    	FlowNetwork current = new FlowNetwork(nodes);
    	
    	int teamId = this.stringToId.get(team);
    	
    	int[][] translate = new int[game_v][2];
    	int[][] edgeHelper = new int[game_v][2];
    	int[] flowedges = new int[this.teamCount-1];
    	
    	int flow = 0;
    	int cur = 0;
    	for(int i = 0; i < flowedges.length; i++) {
    		for(int j = i+1; j < flowedges.length; j++ ) {
    			edgeHelper[cur][0] = i;
    			edgeHelper[cur][1] = j;
    			cur++;
    		}
    	}
    	
    	cur = 0;
    	for(int i = 0; i < this.teams.length; i++) {
    		if(i != teamId) {
    			flowedges[flow] = i;
    			flow++;
    			for(int j = i+1; j < this.teams.length; j++) {
    				if(j != teamId) {
    					translate[cur][0] = i;
    					translate[cur][1] = j;
    					cur++;
    				}
    			}
    			
    		}
    	}
    	
    	//System.out.println("length: " + edgeHelper.length);
    	
    	//add edges from S to game vertices
    	for(int i = 0;  i < edgeHelper.length; i++) {
    		FlowEdge x = new FlowEdge(0, i+1, teams[translate[i][0]].getRemaining(teams[translate[i][1]].getId()));
    		//System.out.println(x);
    		current.addEdge(x);
    	}
    	
    	//add edges from game vertices to team vertices
    	for(int i = 0; i < edgeHelper.length; i++) {
    		FlowEdge x = new FlowEdge(i+1, 1 + edgeHelper.length + edgeHelper[i][0], Integer.MAX_VALUE);
    		FlowEdge y = new FlowEdge(i+1, 1 + edgeHelper.length + edgeHelper[i][1], Integer.MAX_VALUE);
    		//System.out.println(x);
    		//System.out.println(y);
    		current.addEdge(x);
    		current.addEdge(y);
    	}
    	
    	//add edges from team vertices to T
    	int last_edge = nodes-1;
    	for(int i = 0; i < flowedges.length; i++) {
    		int edge_cap = teams[teamId].getWins() + teams[teamId].getGamesLeft() - teams[flowedges[i]].getWins();
    		if(edge_cap < 0) {
    			edge_cap = 0;
    		}
    		current.addEdge(new FlowEdge(1 + edgeHelper.length + i, last_edge, edge_cap));
    	}
    	
    	FordFulkerson ff = new FordFulkerson(current, 0, last_edge);
    	ArrayList<String> becauseof = new ArrayList<String>();
    	
    	for(int i = 0; i < flowedges.length; i++) {
    		int curcheck = 1 + edgeHelper.length + i;
    		//System.out.println("check " + teams[teamId].getName() + ", " + curcheck);
    		if(ff.inCut(curcheck)) {	
    			teams[teamId].eliminate();
    			becauseof.add(teams[flowedges[i]].getName());
    		}
    	}
    	
    	teams[teamId].setEliminated(becauseof);
    	
    	
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
