import java.util.ArrayList;
public class Team {

	private final String name;
	private final int id;
	private int wins;
	private int losses;
	private int gamesLeft;
	private int[] remaining;
	private boolean isEliminated;
	private ArrayList<String> eliminated;
	
	public Team(String name, int id, int wins, int losses, int left, int[] remaining) {
		this.name = name;
		this.id = id;
		this.wins = wins;
		this.losses = losses;
		this.gamesLeft = left;
		this.remaining = remaining;
		this.isEliminated = false;
		this.eliminated = new ArrayList<>();
	}
	
	public int getId() {
		return this.id;
	}
	
	public void eliminate() {
		this.isEliminated = true;
	}
	
	public boolean getIsEliminated() {
		return this.isEliminated;
	}
	
	public void setEliminated(ArrayList<String> elimList) {
		this.eliminated = elimList;
	}
	
	public ArrayList<String> getEleminated() {
		return this.eliminated;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getGamesLeft() {
		return this.gamesLeft;
	}
	
	public int getWins() {
		return this.wins;
	}
	
	public int getLosses() {
		return this.losses;
	}
	
	public int getRemaining(int other_id) {
		if(other_id < 0 || other_id >= remaining.length || other_id == this.id) {
			throw new IllegalArgumentException();
		}
		return this.remaining[other_id];
	}
	
	public String toString() {
		return this.name + " Wins: " + this.wins + " Losses: " + this.losses + " left: " + this.gamesLeft; 
	}
	
	
}
