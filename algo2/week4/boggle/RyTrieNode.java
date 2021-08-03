import java.util.HashMap;

public class RyTrieNode {

	private boolean isWord;
	private final HashMap<Character, RyTrieNode> contains;
	
	public RyTrieNode() {
		this.isWord = false;
		this.contains = new HashMap<>();
	}
	
	public void add(String word, int i) {
		if (i == word.length()) {
			this.isWord = true;
		} else {
			char curlet = word.charAt(i);
			if (!this.hasLetter(curlet)) {
				this.contains.put(curlet, new RyTrieNode());
			}
			this.getNode(curlet).add(word, i+1);
		}
	}
	
	public boolean hasLetter(char letter) {
		return this.contains.containsKey(letter);
	}
	
	public RyTrieNode getNode(char letter) {
		if (!this.hasLetter(letter)) {
			throw new NullPointerException();
		}
		return this.contains.get(letter);
	}
	
	public boolean getIsWord() {
		return this.isWord;
	}
	
	public int size() {
		return this.contains.size();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
