
public class RyTrie {
	
	private final RyTrieNode trie;
	
	public RyTrie() {
		this.trie = new RyTrieNode();
	}
	
	public void addWord(String word) {
		if (word.length() == 0 || word == null) {
			throw new IllegalArgumentException();
		}
		this.trie.add(word,  0);
	}
	
	public boolean contains(char c) {
		return this.trie.hasLetter(c);
	}
	
	public RyTrieNode getTrie() {
		return this.trie;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
